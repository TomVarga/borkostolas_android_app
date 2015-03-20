package hu.tvarga.bor.borkostolas.model;

import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.bean.Wine;

public interface DAO {
    public boolean addOrUpdateWineScore(Wine wine, int user_id, int score);

    public ArrayList<Wine> getWines();
}
