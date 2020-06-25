package tm.fantom.exchangerate.ui.dashboard;


public final class RateModel {

    private boolean isSkeleton = false;
    private String name;
    private String rate;

    public boolean isSkeleton() {
        return isSkeleton;
    }

    public RateModel setSkeleton(boolean skeleton) {
        isSkeleton = skeleton;
        return this;
    }

    public RateModel(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }
}
