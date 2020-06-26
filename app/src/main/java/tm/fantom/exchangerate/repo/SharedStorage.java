package tm.fantom.exchangerate.repo;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.subjects.BehaviorSubject;
import tm.fantom.exchangerate.repo.model.RateSymbol;
import tm.fantom.exchangerate.util.DateUtils;


public final class SharedStorage {
    private static final String THEME_DARK = "dark_mode";
    private static final String LAST_SYNC = "last_sync";

    private SharedPreferences sharedPreferences;
    private Context context;
    private Gson gson;

    private BehaviorSubject<RateSymbol> rateSymbolBehaviorSubject = BehaviorSubject.create();

    public BehaviorSubject<RateSymbol> getRateSymbolBehaviorSubject() {
        return rateSymbolBehaviorSubject;
    }

    public SharedStorage(Context context) {
        this.context = context;
        gson = new GsonBuilder()
//                .registerTypeAdapter(DateTime.class, new DateTimeConverter())
                .create();
    }

    public boolean saveDarkMode(boolean darkMode) {
        if (darkMode)
            return getPrefs().edit().putBoolean(THEME_DARK, true).commit();
        else
            return getPrefs().edit().remove(THEME_DARK).commit();
    }

    public boolean isDarkMode() {
        return getPrefs().getBoolean(THEME_DARK, false);
    }

    private SharedPreferences getPrefs() {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("tmdb", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void saveLastSync() {
        getPrefs().edit().putLong(LAST_SYNC, DateUtils.getSyncTime()).apply();
    }

    public boolean isLastSyncExpired() {
        return DateUtils.isExpired(getPrefs().getLong(LAST_SYNC, 0));
    }
}
