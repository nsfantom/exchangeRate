package tm.fantom.exchangerate.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateUtils {

    public static final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter fmtDateChartFormat = DateTimeFormat.forPattern("dd.MM");

    public static boolean isExpired(long time) {
        DateTime dateTime = new DateTime(time);
        DateTime dateTimeNow = DateTime.now().minusMinutes(10);
        return dateTime.isBefore(dateTimeNow);
    }

    public static String getStartDate(){
        DateTime dateTimeNow = DateTime.now().minusDays(7);
        return dateTimeNow.toString(dateFmt);
    }

    public static String getEndDate(){
        DateTime dateTimeNow = DateTime.now();
        return dateTimeNow.toString(dateFmt);
    }

    public static long getSyncTime(){
        return DateTime.now().getMillis();
    }

    public static String getDateChart(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.toString(fmtDateChartFormat);
    }

    public static long parseDate(String date){
        return DateTime.parse(date,dateFmt).getMillis();
    }
}
