package tm.fantom.exchangerate.repo.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public final class RateModel {

    @PrimaryKey @NonNull
    private String name = "";
    private String rate;

    public RateModel(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
