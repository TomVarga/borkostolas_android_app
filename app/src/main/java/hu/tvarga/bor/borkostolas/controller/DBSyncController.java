package hu.tvarga.bor.borkostolas.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class DBSyncController {

    public boolean updateLocalWineDatabase(Context context, List<Wine> wines){
        LocalDAO lDAO = new LocalDAO(context);

        for (int i = 0; i < wines.size(); i++) {
            lDAO.addWine(wines.get(i));
        }

        return true;
    }
}
