package com.peekaboo.data.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.peekaboo.data.Constants;
import com.peekaboo.data.ResponseErrorHandler;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.domain.ErrorHandler;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    SSLSocketFactory sslSocketFactory() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }

    }

    @Provides
    @Singleton
    public OkHttpClient provideOkClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .sslSocketFactory(sslSocketFactory())
                .hostnameVerifier((hostname, session) -> true)
//                .addInterceptor(new AuthenticatingInterceptor(authentificator))
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public User provideUser(SharedPreferences prefs) {
        return new User(prefs);
    }

    @Provides
    @Singleton
    public SessionRepository provideRepository(Retrofit retrofit, User user) {
        return new SessionDataRepository(new RestApi(retrofit.create(PeekabooApi.class)), new MapperFactory(), user);
    }


    @Provides
    @Singleton
    public ErrorHandler provideErrorHandler(Context context) {
        return new ResponseErrorHandler(context);
    }
}
