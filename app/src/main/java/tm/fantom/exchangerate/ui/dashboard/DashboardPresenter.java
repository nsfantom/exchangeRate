package tm.fantom.exchangerate.ui.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import tm.fantom.exchangerate.api.model.LatestResponse;
import tm.fantom.exchangerate.ui.base.BaseApiPresenter;
import tm.fantom.exchangerate.ui.base.BaseContract;


public class DashboardPresenter extends BaseApiPresenter implements DashboardContract.Presenter {

    private DashboardContract.View view;

    @Override
    public void attach(DashboardContract.View view) {
        this.view = view;
        view.restoreToggle(sharedStorage.isDarkMode());
    }

    @Override
    protected BaseContract.View getView() {
        return view;
    }

    @Override
    public void subscribe() {
        view.restorePosition();

//
//        if (TextUtils.isEmpty(authToken.getGuestSessionId()) || DateUtils.isExpired(authToken.getExpiresAt())) {
//            getCompositeDisposable().add(simpleApi.getAuthenticationGuestSessionNew()
//                    .compose(applyMaybeAsync())
//                    .subscribe(r -> {
//                        if (r.success()) {
//                            sharedStorage.saveAuthToken(AuthToken.fromNetwork(r));
//                            getMovies();
//                        }
//                    }, this::parseErrorSilent)
//            );
//        } else {
//            getMovies();
//        }
//        simpleApi.getAuthenticationGuestSessionNew();
        getRates();
    }

    private void getRates() {
        getCompositeDisposable().add(simpleApi.getLatest()
                .compose(applyMaybeBackground())
                .map(LatestResponse::getRates)
                .map(this::parseRates)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rates -> view.showRates(rates), this::parseError)
        );
    }

    private List<RateModel> parseRates(Map<String, Double> mapRates) {
        List<RateModel> movies = new ArrayList<>();
        for (Map.Entry<String, Double> entry: mapRates.entrySet()){
            if("USD".equals(entry.getKey())) continue;
            movies.add(new RateModel(entry.getKey(),String.format(Locale.ENGLISH,"%1$,.2f", entry.getValue())));
        }
        return movies;
    }

    @Override
    public void forceRefresh() {
        subscribe();
    }

    @Override
    public void onRateClick(String name) {
//        sharedStorage.getMovieIdSubject().onNext(new MovieId(id).setMovieName(name));
        view.showDetails();
    }

    @Override
    public void unsubscribe() {
        clearDisposables();
    }

}