package hu.tvarga.bor.borkostolas.controller;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class JSONParser {
    static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Wine getWineFromJSONObj(JSONObject obj){
        Wine wine;
        try {
            int wine_id = obj.getInt("wine_id");
            String wine_name = obj.getString("wine_name");
            String wine_winery = obj.getString("wine_winery");
            String wine_location = obj.getString("wine_location");
            String wine_composition = obj.getString("wine_composition");
            // these can be null so we check first
            int wine_year = -1, wine_price = -1;
            if (!obj.isNull("wine_year")) wine_year = obj.getInt("wine_year");
            if (!obj.isNull("wine_price")) wine_price = obj.getInt("wine_price");
            wine = new Wine(wine_id, wine_name, wine_winery, wine_location, wine_year, wine_composition, wine_price);
            return wine;
        }catch(Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return null;
    }

    public static Score getScoreFromJSONObj(JSONObject obj){
        Score score;
        try {
            int user_id = obj.getInt("user_id");
            int wine_id = obj.getInt("wine_id");
            double nScore = (!obj.isNull("score")) ? obj.getDouble("score") : -1;
            String sTimestamp = obj.getString("timestamp");
            java.util.Date timestamp = stringToDate(sTimestamp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();
            // HACK: this is because remote MYSQL is running GMT
            cal.add(Calendar.HOUR_OF_DAY, (int) TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));
            java.util.Date adjustedTimestamp = cal.getTime();
            score = new Score(user_id, wine_id, nScore, adjustedTimestamp);
            return score;
        }catch(Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return null;
    }

    public static java.util.Date stringToDate(String string) throws ParseException {
        java.util.Date timestamp = timestampFormat.parse(string);
        return timestamp;
    }

    public static String dateToString(java.util.Date date){
        String timestmapString = timestampFormat.format(date);
        return timestmapString;
    }
}
