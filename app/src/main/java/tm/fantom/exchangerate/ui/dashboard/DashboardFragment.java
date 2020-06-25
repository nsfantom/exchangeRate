package tm.fantom.exchangerate.ui.dashboard;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import tm.fantom.exchangerate.R;
import tm.fantom.exchangerate.RateApp;
import tm.fantom.exchangerate.databinding.FragmentDashboardBinding;
import tm.fantom.exchangerate.ui.Fragments;
import tm.fantom.exchangerate.ui.VHOffsetItemDecoration;
import tm.fantom.exchangerate.ui.base.BaseFragment;


public class DashboardFragment extends BaseFragment implements DashboardContract.View {
    private FragmentDashboardBinding layout;
    private DashboardPresenter presenter;
    private MovieSearchAdapter adapter;
    private int scrollPosition;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new DashboardPresenter();
        adapter = new MovieSearchAdapter();
        RateApp.get(context).getAppComponent().inject(presenter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentDashboardBinding.inflate(inflater);
        return attachToBaseView(inflater, container, layout.getRoot());
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attach(this);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        presenter.unsubscribe();
        scrollPosition = layout.rvItems.getScrollY();
        super.onPause();
    }

    private void initViews() {
        layout.rvItems.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        layout.rvItems.setLayoutManager(llm);

        adapter.setClickListener(n -> presenter.onRateClick(n));
        layout.rvItems.addItemDecoration(
                new VHOffsetItemDecoration(
                        llm.getOrientation(),
                        getResources().getDimensionPixelOffset(R.dimen.default_margin),
                        getResources().getDimensionPixelOffset(R.dimen.default_margin),
                        false
                )
        );
        layout.swipeToRefresh.setEnabled(true);
        layout.swipeToRefresh.setColorSchemeResources(R.color.control_color_state);
        layout.swipeToRefresh.setOnRefreshListener(() -> {
            layout.swipeToRefresh.setRefreshing(true);
            presenter.forceRefresh();
        });
    }

    @Override
    public void restoreToggle(boolean enabled) {
        layout.toggleButton.setChecked(enabled);
        layout.toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> getParentActivity().setThemeMode(isChecked));
    }

    @Override
    public void showRates(List<RateModel> rates) {
        hideProgress();
        adapter.setItems(rates);
        layout.rvItems.scrollToPosition(0);
    }

    @Override
    public void restorePosition() {
        layout.rvItems.setScrollY(scrollPosition);
    }

    @Override
    public void showDetails() {
        getParentActivity().navigateTo(Fragments.DETAILS);
    }

    @Override
    public void showError(String message) {
        hideProgress();
        if (!TextUtils.isEmpty(message)) showToast(message, Toast.LENGTH_SHORT);
    }

    @Override
    public void showError(int text) {
        showError(getString(text));
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        if (layout.swipeToRefresh.isRefreshing()) layout.swipeToRefresh.setRefreshing(false);
    }
}
