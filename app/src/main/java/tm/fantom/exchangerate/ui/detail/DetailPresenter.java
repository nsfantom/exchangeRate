package tm.fantom.exchangerate.ui.detail;

import tm.fantom.exchangerate.repo.model.RateSymbol;
import tm.fantom.exchangerate.ui.base.BaseApiPresenter;
import tm.fantom.exchangerate.ui.base.BaseContract;

public class DetailPresenter extends BaseApiPresenter implements DetailContract.Presenter {

    private DetailContract.View view;

    @Override
    public void attach(DetailContract.View view) {
        this.view = view;
    }

    @Override
    protected BaseContract.View getView() {
        return view;
    }

    @Override
    public void subscribe() {
        getCompositeDisposable().add(sharedStorage.getRateSymbolBehaviorSubject()
                .doOnNext(r -> view.showData(r.getData()))
                .map(RateSymbol::getName)
                .subscribe(view::showTitle));
    }

    @Override
    public void unsubscribe() {
        clearDisposables();
    }

}