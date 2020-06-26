package tm.fantom.exchangerate.ui.dashboard;

import java.util.List;

import tm.fantom.exchangerate.repo.model.RateModel;
import tm.fantom.exchangerate.ui.base.BaseContract;

public interface DashboardContract {

    interface View extends BaseContract.View {

        void showRates(List<RateModel> rates);

        void restorePosition();

        void showDetails();

        void restoreToggle(boolean enabled);

    }

    interface Presenter extends BaseContract.Presenter<DashboardContract.View> {

        void subscribe();

        void forceRefresh();

        void onRateClick(String name);

        void unsubscribe();
    }
}