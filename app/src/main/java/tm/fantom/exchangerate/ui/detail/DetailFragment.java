package tm.fantom.exchangerate.ui.detail;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import tm.fantom.exchangerate.R;
import tm.fantom.exchangerate.RateApp;
import tm.fantom.exchangerate.databinding.FragmentDetailBinding;
import tm.fantom.exchangerate.ui.base.BaseFragment;
import tm.fantom.exchangerate.util.DisplayUtil;

public class DetailFragment extends BaseFragment implements DetailContract.View {
    private FragmentDetailBinding layout;
    private DetailPresenter presenter;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new DetailPresenter();
        RateApp.get(context).getAppComponent().inject(presenter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentDetailBinding.inflate(inflater);
        return attachToBaseView(inflater, container, layout.getRoot());
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attach(this);
        initViews();
        initClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        presenter.unsubscribe();
        super.onPause();
    }

    private void initViews() {
        applyChartStyle(layout.chartRate);
    }

    private void initClickListeners() {
        layout.ivBack.setOnClickListener(v -> getParentActivity().onBackPressed());
    }

    @SuppressLint("ResourceType")
    private void applyChartStyle(LineChart lineChart) {
//        lineChart.setBackgroundColor(getResources().getColor(R.attr.themeItemBackground));
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDragXEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        XAxis xAxis;

        {
            xAxis = lineChart.getXAxis();

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAxisLineWidth(1);
            xAxis.setGranularity(24 * 60 * 60 * 1000f);
            xAxis.setTextSize(12);
            xAxis.setTextColor(ContextCompat.getColor(context, R.color.colorChartValue));
            MyXAxisValueFormatter custom = new MyXAxisValueFormatter();
            xAxis.setValueFormatter(custom);

            xAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.colorChartAxis));
            xAxis.enableGridDashedLine(DisplayUtil.dpToPx(context, 2), DisplayUtil.dpToPx(context, 4), 0f);
            xAxis.setDrawGridLinesBehindData(false);

        }

        YAxis yAxis;

        {
            yAxis = lineChart.getAxisLeft();
            yAxis.setTextSize(12);
            yAxis.setTextColor(ContextCompat.getColor(context, R.color.colorChartValue));
            yAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.colorChartAxis));
            yAxis.setLabelCount(3, true);
            yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            yAxis.setAxisLineWidth(1);
            yAxis.setDrawLabels(true);

            lineChart.getAxisRight().setEnabled(false);

            yAxis.enableGridDashedLine(DisplayUtil.dpToPx(context, 2), DisplayUtil.dpToPx(context, 4), 0f);
            yAxis.setDrawGridLinesBehindData(false);
        }
    }


    @Override
    public void showTitle(String name) {
        layout.tvTitle.setText(name);
    }

    @Override
    public void showData(List<Entry> data) {
        hideProgress();
        LineDataSet valueDataSet;

        if (layout.chartRate.getData() != null &&
                layout.chartRate.getData().getDataSetCount() > 0) {
            valueDataSet = (LineDataSet) layout.chartRate.getData().getDataSetByIndex(0);
            valueDataSet.setValues(data);

            valueDataSet.notifyDataSetChanged();
            layout.chartRate.getData().notifyDataChanged();
            layout.chartRate.notifyDataSetChanged();
        } else {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = setupDataSet(data);
            dataSets.add(lineDataSet);

            lineDataSet.setFillFormatter((dataSet, dataProvider) -> layout.chartRate.getAxisLeft().getAxisMinimum());

            LineData lineData = new LineData(dataSets);

            layout.chartRate.setData(lineData);
        }
        layout.chartRate.animateX(500);
        layout.chartRate.postInvalidate();
    }

    private LineDataSet setupDataSet(List<Entry> data) {
        LineDataSet lineDataSet;

        lineDataSet = new LineDataSet(data, "");
        lineDataSet.setLabel(null);

        lineDataSet.setDrawIcons(false);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);

        lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorChartLine));
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleRadius(5f);

        lineDataSet.setDrawCircleHole(false);

        lineDataSet.setFormSize(15.f);

        lineDataSet.setValueTextSize(9f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);

        lineDataSet.enableDashedHighlightLine(DisplayUtil.dpToPx(context, 5), DisplayUtil.dpToPx(context, 2), 0f);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setHighlightLineWidth(1f);

        lineDataSet.setDrawFilled(false);

        return lineDataSet;
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
}
