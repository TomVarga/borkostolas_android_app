package hu.tvarga.bor.borkostolas.model.bean;

/**
 * Created by Calebzor on 3/20/2015.
 */
public class ScoredWine extends Wine {
    private double wine_score;
    private int user_id;

    public double getWine_score() {
        return wine_score;
    }

    public void setWine_score(double wine_score) {
        this.wine_score = wine_score;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public ScoredWine(int wine_id, String wine_name, String wine_winery, String wine_location, int wine_year, String wine_composition, int wine_price, double wine_score, int user_id) {
        super(wine_id, wine_name, wine_winery, wine_location, wine_year, wine_composition, wine_price);
        this.wine_score = wine_score;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return super.toString() + "ScoredWine{" +
                "wine_score=" + wine_score +
                ", user_id=" + user_id +
                '}';
    }
}
