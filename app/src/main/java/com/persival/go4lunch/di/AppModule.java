package com.persival.go4lunch.di;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.persival.go4lunch.data.places.GooglePlacesApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private static final String GOOGLE_PLACES_API_BASE_URL = "https://maps.googleapis.com/";

    private AppModule() {
        // This class is not meant to be instantiated
    }

    @Singleton
    @Provides
    public static FusedLocationProviderClient provideFusedLocationProviderClient(Application application) {
        return LocationServices.getFusedLocationProviderClient(application);
    }

    @Singleton
    @Provides
    public static GooglePlacesApi provideGooglePlacesApi(Application application) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .cache(new Cache(application.getCacheDir(), 1_024 * 1_024))
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit.create(GooglePlacesApi.class);
    }

    @Singleton
    @Provides
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    public static FirebaseFirestore provideFirestoreDb() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public static Resources provideResources(@NonNull Application application) {
        return application.getResources();
    }

}
