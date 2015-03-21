package hu.tvarga.bor.borkostolas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import static hu.tvarga.bor.borkostolas.controller.JSONParser.dateToString;
import static hu.tvarga.bor.borkostolas.controller.JSONParser.stringToDate;

public class LocalDAO extends SQLiteOpenHelper implements DAO {

    private SQLiteDatabase db;
    private Context context;
    private static final String DATABASE_NAME = "borkost";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_WINES =
            "CREATE TABLE IF NOT EXISTS `wines` (\"wine_id\" INTEGER PRIMARY KEY NOT NULL UNIQUE , \"wine_name\" VARCHAR NOT NULL , \"wine_winery\" VARCHAR NOT NULL , \"wine_location\" VARCHAR NOT NULL , \"wine_year\" INTEGER, \"wine_composition\" VARCHAR NOT NULL , \"wine_price\" INTEGER);";
    private static final String DATABASE_CREATE_SCORES =
            "CREATE TABLE IF NOT EXISTS `scores` (\"user_id\" INTEGER, \"wine_id\" INTEGER, \"score\" DOUBLE, \"timestamp\" DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP);";

    public LocalDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        System.out.println("lDAO created");
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_WINES);
        database.execSQL(DATABASE_CREATE_SCORES);
    }

    public SQLiteDatabase getDB(){
        LocalDAO dbHelper = new LocalDAO(this.context);
        db = dbHelper.getWritableDatabase();
        return db;
    }

    private boolean isWineIdUsed(Wine wine){
        LocalDAO dbHelper = new LocalDAO(this.context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("wines", null, "wine_id=?", new String[] { wine.getWine_id() + ""}, null, null, null);
        db.close();
        return (cursor != null);
    }

    public boolean deleteWine (Wine wine){
        SQLiteDatabase db = getDB();
        int rowsDeleted = db.delete("wines", "wine_id = ? ", new String[] { wine.getWine_id() + "" });
        System.out.println("LocalDAO deleted : " + wine.toString() );
        db.close();
        return (rowsDeleted > 0);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(LocalDAO.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all oldoyees");
        onCreate(database);
    }

    public boolean addWine(Wine wine){
        SQLiteDatabase db = getDB();
        ContentValues contentValues = new ContentValues();

        contentValues.put("wine_id", wine.getWine_id());
        contentValues.put("wine_name", wine.getWine_name());
        contentValues.put("wine_winery", wine.getWine_winery());
        contentValues.put("wine_location", wine.getWine_location());
        contentValues.put("wine_composition", wine.getWine_composition());
        contentValues.put("wine_year", wine.getWine_year());
        contentValues.put("wine_price", wine.getWine_price());

        if (isWineIdUsed(wine)) deleteWine(wine);
        db.insert("wines", null, contentValues);
        System.out.println("LocalDAO added : " + wine.toString() );

        db.close();
        return true;
    }


    @Override
    public ArrayList<Wine> getWines() {
        ArrayList<Wine> wines = new ArrayList<>();

        String selectQuery = "SELECT * FROM wines";

        SQLiteDatabase db = getDB();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Wine wine = new Wine(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6))
                );
                wines.add(wine);
            } while (cursor.moveToNext());
        }
        db.close();
        return wines;
    }

    public double getScore(int user_id, int wine_id){
        String[] tableColumns = new String[] { "score" };
        String whereClause = "user_id =? AND wine_id =?";
        String[] whereArgs = new String[]{ user_id + "", wine_id + "" };

        SQLiteDatabase db = getDB();
        Cursor cursor = db.query("scores", tableColumns, whereClause, whereArgs, null, null, null);

        // cursor.moveToFirst() aka there was result
        boolean resultsFound = cursor.moveToFirst();
        double score = -1;
        if (resultsFound){
            score = Double.parseDouble(cursor.getString(0));
        }
        db.close();
        return score;
    }

    @Override
    public ArrayList<Score> getScores(int user_id) {
        ArrayList<Score> scores = new ArrayList<>();

        String whereClause = "user_id =?";
        String[] whereArgs = new String[]{ user_id + "" };

        SQLiteDatabase db = getDB();
        Cursor cursor = db.query("scores", null, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String sTimestamp = cursor.getString(3);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                java.util.Date timestamp = null;
                try {
                    timestamp = dateFormat.parse(sTimestamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Score score = new Score(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Double.parseDouble(cursor.getString(2)),
                        timestamp
                );
                scores.add(score);
            } while (cursor.moveToNext());
        }
        db.close();
        return scores;
    }

    @Override
    public boolean addOrUpdateScore(Score remoteScore) {
        String whereClause = "user_id =? AND wine_id =?";
        String[] whereArgs = new String[]{ remoteScore.getUser_id() + "", remoteScore.getWine_id() + "" };

        SQLiteDatabase db = getDB();
        Cursor cursor = db.query("scores", null, whereClause, whereArgs, null, null, null);

        // cursor.moveToFirst() aka there was result
        boolean resultsFound = cursor.moveToFirst();
        if (resultsFound){
            String sTimestamp = cursor.getString(3);
            java.util.Date timestamp = null;
            try {
                timestamp = stringToDate(sTimestamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Score localScore = new Score(
                    Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    Double.parseDouble(cursor.getString(2)),
                    timestamp
            );
            // don't update if scores are equal or the remote score was before the local one
            if (localScore.getScore() == remoteScore.getScore() || remoteScore.getTimestamp().before(localScore.getTimestamp())){
                db.close();
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", remoteScore.getUser_id());
        contentValues.put("wine_id", remoteScore.getWine_id());
        contentValues.put("score", remoteScore.getScore());
        contentValues.put("timestamp", dateToString(remoteScore.getTimestamp()));

        deleteScore(remoteScore);
        db.insert("scores", null, contentValues);
        System.out.println("LocalDAO added : " + remoteScore.toString() );

        db.close();
        return true;
    }

    private boolean deleteScore(Score score) {
        SQLiteDatabase db = getDB();
        int rowsDeleted = db.delete("scores", "user_id = ? AND wine_id=?", new String[] { score.getUser_id() + "", score.getWine_id() + "" });
        System.out.println("LocalDAO deleted : " + score.toString() );
        db.close();
        return (rowsDeleted > 0);
    }
}
