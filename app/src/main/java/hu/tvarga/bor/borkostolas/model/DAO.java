package hu.tvarga.bor.borkostolas.model;

import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public interface DAO {
    public ArrayList<Wine> getWines();

    public ArrayList<Score> getScores(int user_id);

    public boolean addOrUpdateScore(Score score);
}
