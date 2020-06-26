package tm.fantom.exchangerate.repo.model;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public final class RateSymbol {

    private String name;
    private List<Entry> data;


    public RateSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Entry> getData() {
        return data;
    }

    public RateSymbol setData(List<Entry> data) {
        this.data = data;
        return this;
    }
}
