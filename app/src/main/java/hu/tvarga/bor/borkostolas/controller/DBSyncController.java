package hu.tvarga.bor.borkostolas.controller;

import android.content.Context;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.User;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class DBSyncController {

    public static int getMaxWineId(ArrayList<Score> scores){
        int max = 0;
        for (int i=0; i<scores.size(); i++){
            int wineId = scores.get(i).getWine_id();
            if ( max < wineId) max = wineId;
        }
        return max;
    }

    public boolean updateLocalWineDatabase(Context context, List<Wine> wines){
        LocalDAO lDAO = new LocalDAO(context);

        for (int i = 0; i < wines.size(); i++) {
            lDAO.addWine(wines.get(i));
        }

        return true;
    }

    public boolean syncScores(Context context, ArrayList<Score> remoteScores, User user) {
        LocalDAO lDAO = new LocalDAO(context);
        RemoteDAO rDAO = new RemoteDAO();
        ArrayList<Score> localScores = lDAO.getScores(user.getUser_id());

        // update local DB
        if (remoteScores.size() > 0) {
            for (int i = 0; i < remoteScores.size(); i++) {
                Score remoteScore = remoteScores.get(i);
                lDAO.addOrUpdateScore(remoteScore);
            }
        }
        System.out.println("Local ScoreDB in sync with remote");

        // update remote DB
        int max = (getMaxWineId(localScores) > getMaxWineId(remoteScores)) ? getMaxWineId(localScores) : getMaxWineId(remoteScores);
        JSONArray jsArray = new JSONArray();
        jsArray.put(user.getUser_name());

        HashMap<Integer, Score> localScoreMap = new HashMap<>();
        for ( Score s : localScores ){
            localScoreMap.put(s.getWine_id(), s);
        }
        HashMap<Integer, Score> remoteScoreMap = new HashMap<>();
        for ( Score s : remoteScores ){
            remoteScoreMap.put(s.getWine_id(), s);
        }

        for ( int i = 1; i <= max; i++ ){
            Score localScore = localScoreMap.get(i);
            Score remoteScore = remoteScoreMap.get(i);
            if ( localScoreMap.containsKey(i) && remoteScoreMap.containsKey(i)){
                Double nScore = localScore.getScore();
                if ( localScore.getTimestamp().after(remoteScore.getTimestamp())){
                    jsArray.put(( nScore > 0) ? nScore : (Object) null);
                } else if (localScore.getTimestamp() == remoteScore.getTimestamp()){
                    jsArray.put(( nScore > 0) ? nScore : (Object) null);
                } else{
                    nScore = remoteScore.getScore();
                    jsArray.put(( nScore > 0) ? nScore : (Object) null);
                }
            } else if (localScoreMap.containsKey(i) && !remoteScoreMap.containsKey(i)){
                Double nScore = localScore.getScore();
                jsArray.put(( nScore > 0) ? nScore : (Object) null);
            } else if (!localScoreMap.containsKey(i) && remoteScoreMap.containsKey(i)){
                // this should not happen and in this case we should update the local DB with the key
                Double nScore = remoteScore.getScore();
                jsArray.put(( nScore > 0) ? nScore : (Object) null);
            } else if (!localScoreMap.containsKey(i) && !remoteScoreMap.containsKey(i)){
                jsArray.put((Object) null);
            }
        }
        rDAO.addOrUpdateScore(jsArray, user);
        System.out.println("Remote ScoreDB in sync with locale");

        return true;
    }
}
