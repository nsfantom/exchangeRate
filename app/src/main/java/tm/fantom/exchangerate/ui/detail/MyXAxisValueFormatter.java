package tm.fantom.exchangerate.ui.detail;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.DateTime;

import tm.fantom.exchangerate.util.DateUtils;

public class MyXAxisValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        try {
            return DateUtils.getDateChart(new DateTime((long) value));
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return DateUtils.getDateChart(new DateTime((long) value));
    }

}