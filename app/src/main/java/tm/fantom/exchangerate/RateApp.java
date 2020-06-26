package tm.fantom.exchangerate;

import android.app.Application;
import android.content.Context;

import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;
import tm.fantom.exchangerate.di.component.AppComponent;
import tm.fantom.exchangerate.di.component.DaggerAppComponent;
import tm.fantom.exchangerate.di.module.ApiModule;


public final class RateApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        initDagger();

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
