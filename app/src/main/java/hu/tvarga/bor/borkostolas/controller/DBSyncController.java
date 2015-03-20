package hu.tvarga.bor.borkostolas.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class DBSyncController {

    public boolean updateLocalWineDatabase(Context context, List<Wine> wines){
        LocalDAO lDAO = new LocalDAO(context);

        for (int i = 0; i < wines.size(); i++) {
            lDAO.addWine(wines.get(i));
        }

        return true;
    }

    public boolean syncScores(Context context, ArrayList<Score> remoteScores, int user_id) {
        LocalDAO lDAO = new LocalDAO(context);
        RemoteDAO rDAO = new RemoteDAO();
        ArrayList<Score> localScores = lDAO.getScores(user_id);

        // update local DB
        for ( int i = 0; i < remoteScores.size(); i++){
            Score remoteScore = remoteScores.get(i);
//            if (localScores.size() > 0){
//                for ( int j = 0; j < localScores.size(); j++){
//                    Score localScore = localScores.get(j);
//                    if ( remoteScore.getUser_id() == localScore.getUser_id() && remoteScore.getWine_id() == localScore.getWine_id() && remoteScore.getScore() != localScore.getScore() && localScore.getTimestamp().after(remoteScore.getTimestamp())){
//                        rDAO.addOrUpdateScore(localScore);
//                        break;
//                        // TODO come back to this once we can update local score
//                    }
//                }
//            }
            lDAO.addOrUpdateScore(remoteScore);
        }

        return true;
    }
}
