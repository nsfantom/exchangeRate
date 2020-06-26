package tm.fantom.exchangerate.di.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tm.fantom.exchangerate.BuildConfig;
import tm.fantom.exchangerate.api.SimpleApi;
import tm.fantom.exchangerate.api.model.RateValue;
import tm.fantom.exchangerate.api.model.RatesConverter;
import tm.fantom.exchangerate.db.AppDatabase;
import tm.fantom.exchangerate.repo.SharedStorage;

@Module
public final class ApiModule {
    private final Application application;
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 15;

    public ApiModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    SharedStorage provideSharedStorage() {
        return new SharedStorage(application);
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "rate_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    Context providesAppContext() {
        return application;
    }

    @Provides
    @Singleton
    Resources providesResources() {
        return application.getResources();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RateValue.class, new RatesConverter())
                .create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
        interceptorLog.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptorLog);

        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient())
                .build();
    }

    @Provides
    @Singleton
    SimpleApi providesApiService(Retrofit retrofit) {
        return retrofit.create(SimpleApi.class);
    }

}