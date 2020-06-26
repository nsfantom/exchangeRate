package tm.fantom.exchangerate.ui.detail;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import tm.fantom.exchangerate.ui.base.BaseContract;

public interface DetailContract {

    interface View extends BaseContract.View {

        void showTitle(String name);

        void showData(List<Entry> data);

    }

    interface Presenter extends BaseContract.Presenter<DetailContract.View> {

        void subscribe();

        void unsubscribe();

    }
}