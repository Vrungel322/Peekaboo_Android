package com.peekaboo.data.di;

import android.content.Context;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.peekaboo.R;
import com.peekaboo.data.Constants;
import com.peekaboo.data.ResponseErrorHandler;
import com.peekaboo.data.mappers.AbstractMapperFactory;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.domain.AccountUser;
import com.peekaboo.domain.UserMessageMapper;
import com.peekaboo.domain.SessionRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    TextToSpeech textToSpeech;


    @Singleton
    @Provides
    @Named("domens")
    public List<String> getStringFromFile() {
        BufferedReader br = null;
        List<String> domens = new ArrayList<>();

        try {

            String sCurrentLine;
            String filename = Environment.getExternalStorageDirectory() + File.separator + "domens.txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String domen = Constants.BASE_URL + "\n" + Constants.BASE_URL_SOCKET;
                bufferedWriter.write(domen, 0, domen.length());
                bufferedWriter.close();
            }
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                domens.add(sCurrentLine);
            }
        } catch (IOException e) {
            domens.add(Constants.BASE_URL);
            domens.add(Constants.BASE_URL_SOCKET);
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return domens;
    }

    private SSLSocketFactory newSslSocketFactory(Context context) {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.keystore);

            try {
                trusted.load(in, null);
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            return sslContext.getSocketFactory();

        } catch (Exception e) {
            throw new Error(e);
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
//                .sslSocketFactory(newSslSocketFactory(context))
//                .hostnameVerifier((hostname, session) -> true)
//                .addInterceptor(new AuthenticatingInterceptor(authentificator))
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okClient, @Named("domens") List<String> domens) {
        return new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
                .baseUrl(domens.get(0))
                .client(okClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public PeekabooApi providePeekabooApi(Retrofit retrofit) {
        return retrofit.create(PeekabooApi.class);
    }

    @Provides
    @Singleton
    public RestApi provideRestApi(PeekabooApi api) {
        return new RestApi(api);
    }

    @Provides
    @Singleton
    public SessionRepository provideRepository(AccountUser user, RestApi restApi) {
        return new SessionDataRepository(restApi, new MapperFactory(), user);
    }

    @Provides
    @Singleton
    public TextToSpeech provideTextToSpeech(Context context){
        textToSpeech = new TextToSpeech(context, status -> {
            if(status == TextToSpeech.SUCCESS){
                textToSpeech.setLanguage(Locale.US);
            } else if (status == TextToSpeech.ERROR) {
                Toast.makeText(context, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            }
        });
        return textToSpeech;
    }

    @Provides
    public AbstractMapperFactory provideMapperFactory(){
        return new MapperFactory();
    }


    @Provides
    @Singleton
    public UserMessageMapper provideErrorHandler(Context context) {
        return new ResponseErrorHandler(context);
    }
}
