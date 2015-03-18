package hu.tvarga.bor.borkostolas.hu.tvarga.bor.borkostolas.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.bean.Wine;

/**
 * Created by Calebzor on 3/18/2015.
 */
public class LocalDAO extends SQLiteOpenHelper implements DAO {

    private SQLiteDatabase db;
    private Context context;
    private static final String DATABASE_NAME = "borkost";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_WINES =
        "CREATE TABLE IF NOT EXISTS `wines` (\"wine_id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"wine_name\" VARCHAR NOT NULL , \"wine_winery\" VARCHAR NOT NULL , \"wine_location\" VARCHAR NOT NULL , \"wine_year\" INTEGER, \"wine_composition\" VARCHAR NOT NULL , \"wine_price\" INTEGER);";
    private static final String DATABASE_CREATE_SCORES =
        "CREATE TABLE IF NOT EXISTS `scores` (\"user_id\" INTEGER, \"wine_id\" INTEGER, \"score\" DOUBLE, \"timestamp\" DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP);";

    public LocalDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(LocalDAO.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }

    @Override
    public boolean addOrUpdateWineScore(Wine wine, int user_id, int score) {
        return false;
    }

    @Override
    public List<Wine> getWines() {
        return null;
    }
}
