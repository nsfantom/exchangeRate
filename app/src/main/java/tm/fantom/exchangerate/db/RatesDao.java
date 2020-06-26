package tm.fantom.exchangerate.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;
import tm.fantom.exchangerate.repo.model.RateModel;

@Dao
public interface RatesDao {

    @Query("SELECT * FROM ratemodel")
    Observable<List<RateModel>> getLatest();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RateModel> item);
}
