package tm.fantom.exchangerate.api.model;

public class RateValue {
    private float value;

    public RateValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public RateValue setValue(float value) {
        this.value = value;
        return this;
    }
}
