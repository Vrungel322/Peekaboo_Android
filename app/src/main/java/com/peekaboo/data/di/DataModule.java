package com.peekaboo.data.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.peekaboo.data.Constants;
import com.peekaboo.data.mappers.MapperFactory;
import com.peekaboo.data.repositories.SessionDataRepository;
import com.peekaboo.data.rest.PeekabooApi;
import com.peekaboo.data.rest.RestApi;
import com.peekaboo.domain.SessionRepository;
import com.peekaboo.domain.User;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
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
    public SharedPreferences providePreferences(Context context) {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }
}
