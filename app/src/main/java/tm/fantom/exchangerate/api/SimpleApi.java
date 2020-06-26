package tm.fantom.exchangerate.api;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tm.fantom.exchangerate.api.model.HistoryResponse;
import tm.fantom.exchangerate.api.model.LatestResponse;


public interface SimpleApi {

    @GET("latest?base=USD")
    Maybe<LatestResponse> getLatest();

    //https://api.exchangeratesapi.io/history?start_at=2019-11-27&end_at=2019-12-03&base=USD&symbols=CAD

    @GET("history?base=USD")
    Maybe<HistoryResponse> getHistory(@Query("start_at") String startAt, @Query("end_at") String endAt, @Query("symbols") String symbol);
}
