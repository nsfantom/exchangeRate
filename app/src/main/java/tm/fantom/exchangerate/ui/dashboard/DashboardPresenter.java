package tm.fantom.exchangerate.ui.dashboard;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tm.fantom.exchangerate.R;
import tm.fantom.exchangerate.api.model.LatestResponse;
import tm.fantom.exchangerate.api.model.RateValue;
import tm.fantom.exchangerate.repo.model.RateModel;
import tm.fantom.exchangerate.repo.model.RateSymbol;
import tm.fantom.exchangerate.ui.base.BaseApiPresenter;
import tm.fantom.exchangerate.ui.base.BaseContract;
import tm.fantom.exchangerate.util.DateUtils;


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
        getCompositeDisposable().add(appDatabase.ratesDao().getLatest()
                .compose(applyObservableAsync())
                .subscribe(rateModels -> view.showRates(rateModels), this::parseError)
        );
        getRates();
    }

    private void getRates() {
        if (sharedStorage.isLastSyncExpired()) {
            getCompositeDisposable().add(simpleApi.getLatest()
                    .compose(applyMaybeBackground())
                    .map(LatestResponse::getRates)
                    .map(this::parseRates)
                    .subscribe(rates -> {
                        appDatabase.ratesDao().insert(rates);
                        sharedStorage.saveLastSync();
                    }, this::parseErrorSilent)
            );
        }
    }

    private List<RateModel> parseRates(Map<String, Double> mapRates) {
        List<RateModel> movies = new ArrayList<>();
        for (Map.Entry<String, Double> entry : mapRates.entrySet()) {
            if ("USD".equals(entry.getKey())) continue;
            movies.add(new RateModel(entry.getKey(), String.format(Locale.ENGLISH, "%1$,.2f", entry.getValue())));
        }
        return movies;
    }

    @Override
    public void forceRefresh() {
        subscribe();
    }

    @Override
    public void onRateClick(String name) {
        getCompositeDisposable().add(simpleApi.getHistory(DateUtils.getStartDate(), DateUtils.getEndDate(), name)
                .compose(applyMaybeAsync())
                .subscribe(r -> {
                    if (r.isEmptyHistory()) {
                        view.showError(R.string.error_no_data);
                    } else {
                        sharedStorage.getRateSymbolBehaviorSubject().onNext(new RateSymbol(name).setData(parseData(r.getRates())));
                        view.showDetails();
                    }
                }, this::parseError)
        );
    }

    private List<Entry> parseData(Map<String, RateValue> rates) {
        List<Entry> data = new ArrayList<>();
        for (Map.Entry<String, RateValue> rate : rates.entrySet()) {
            data.add(new Entry(DateUtils.parseDate(rate.getKey()), rate.getValue().getValue()));
        }
        Collections.sort(data, (o1, o2) -> Float.compare(o1.getX(), o2.getX()));
        return data;
    }

    @Override
    public void unsubscribe() {
        clearDisposables();
    }

}