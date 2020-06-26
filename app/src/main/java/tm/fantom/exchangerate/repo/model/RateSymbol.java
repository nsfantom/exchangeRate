package tm.fantom.exchangerate.repo.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import tm.fantom.exchangerate.api.model.HistoryResponse;

public final class RateSymbol {

    private String name;
    private List<Entry> data;
    private HistoryResponse historyResponse;

    public RateSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HistoryResponse getHistoryResponse() {
        return historyResponse;
    }

    public RateSymbol setHistoryResponse(HistoryResponse historyResponse) {
        this.historyResponse = historyResponse;
        return this;
    }

    public List<Entry> getData() {
        return data;
    }

    public RateSymbol setData(List<Entry> data) {
        this.data = data;
        return this;
    }
}
