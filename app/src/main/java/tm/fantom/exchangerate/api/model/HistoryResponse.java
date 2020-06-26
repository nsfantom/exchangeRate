package tm.fantom.exchangerate.api.model;

import androidx.core.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class HistoryResponse {
    @SerializedName("rates")
    @Expose
    private Map<String, RateValue> rates = new HashMap<>();

    public Map<String, RateValue> getRates() {
        return rates;
    }

    public boolean isEmptyHistory() {
        return rates.isEmpty();
    }
}
