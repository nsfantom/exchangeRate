package tm.fantom.exchangerate.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import tm.fantom.exchangerate.repo.model.RateModel;

@Database(
        entities = {
                RateModel.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RatesDao ratesDao();
}


