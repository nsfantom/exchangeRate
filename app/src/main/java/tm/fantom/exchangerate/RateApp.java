package tm.fantom.exchangerate;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;

import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;
import tm.fantom.exchangerate.di.component.AppComponent;
//import tm.fantom.exchangerate.di.component.DaggerAppComponent;
import tm.fantom.exchangerate.di.component.DaggerAppComponent;
import tm.fantom.exchangerate.di.module.ApiModule;


public final class RateApp extends Application {
//    static {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//    }

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        initDagger();
        Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .setDiskCacheEnabled(true)
                .build());
        RxJavaPlugins.setErrorHandler(e -> Timber.e(e, "unhandled exception: %s", e.getMessage()));
    }
    public static RateApp get(Context context) {
        return (RateApp) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initDagger() {
        appComponent = DaggerAppComponent
                .builder()
                .apiModule(new ApiModule(this))
                .build();
    }

}
