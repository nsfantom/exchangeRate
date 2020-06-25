package tm.fantom.exchangerate.di.component;

import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import tm.fantom.exchangerate.di.module.ApiModule;
import tm.fantom.exchangerate.repo.SharedStorage;
import tm.fantom.exchangerate.ui.MainActivity;
import tm.fantom.exchangerate.ui.base.BaseApiPresenter;


@Singleton
@Component(modules = {ApiModule.class})
public interface AppComponent {

    SharedStorage sharedStorage();

    Retrofit retrofit();

    Resources resources();

    void inject(BaseApiPresenter baseApiPresenter);

    void inject(MainActivity mainActivity);
}
