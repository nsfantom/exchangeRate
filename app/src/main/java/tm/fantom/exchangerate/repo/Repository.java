package tm.fantom.exchangerate.repo;

import tm.fantom.exchangerate.api.SimpleApi;
import tm.fantom.exchangerate.db.AppDatabase;

public final class Repository {
    private SharedStorage sharedStorage;
    private SimpleApi simpleApi;
    private AppDatabase appDatabase;

    public Repository(SharedStorage sharedStorage, SimpleApi simpleApi, AppDatabase appDatabase) {
        this.sharedStorage = sharedStorage;
        this.simpleApi = simpleApi;
        this.appDatabase = appDatabase;
    }

    public boolean isDarkMode() {
        return sharedStorage.isDarkMode();
    }

    public void getLatest(OnError onError) {


//        return simpleApi.getLatest().subscribe();
    }

}
