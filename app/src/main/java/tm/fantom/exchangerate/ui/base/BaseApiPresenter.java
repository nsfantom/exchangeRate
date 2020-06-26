package tm.fantom.exchangerate.ui.base;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.StringRes;

import javax.inject.Inject;

import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import tm.fantom.exchangerate.RateApp;
import tm.fantom.exchangerate.api.SimpleApi;
import tm.fantom.exchangerate.db.AppDatabase;
import tm.fantom.exchangerate.repo.Repository;
import tm.fantom.exchangerate.repo.SharedStorage;

public abstract class BaseApiPresenter {

    @Inject
    protected SimpleApi simpleApi;

    @Inject
    protected SharedStorage sharedStorage;

    @Inject
    protected AppDatabase appDatabase;

    @Inject
    protected Resources resources;

    @Inject
    protected Context appContext;

    protected CompositeDisposable disposables;

    protected abstract BaseContract.View getView();

    protected void clearDisposables() {
        if (null != disposables && !disposables.isDisposed()) {
            disposables.dispose();
            disposables = null;
        }
    }

    protected CompositeDisposable getCompositeDisposable() {
        if (disposables == null || disposables.isDisposed()) {
            disposables = new CompositeDisposable();
        }
        return disposables;
    }

    protected void parseError(Throwable throwable) {
        notifyError(throwable.getLocalizedMessage());
    }

    protected void parseErrorSilent(Throwable throwable) {
        Timber.e(throwable, "Error: %s", throwable.getMessage());
    }

    private void notifyError(String msg) {
        if (getView() != null) {
            getView().showError(msg);
        }
    }

    private void notifyError(@StringRes int msg) {
        if (getView() != null) {
            getView().showError(msg);
        }
    }

    public void injectDependency(final Context context) {
        RateApp.get(context).getAppComponent().inject(this);
    }

    protected <T> MaybeTransformer<T, T> applyMaybeAsync() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> ObservableTransformer<T, T> applyObservableAsync() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> MaybeTransformer<T, T> applyMaybeBackground() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }


}
