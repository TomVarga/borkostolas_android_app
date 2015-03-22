package hu.tvarga.bor.borkostolas.model.bean;


import java.util.Date;

import static hu.tvarga.bor.borkostolas.controller.JSONParser.dateToString;

public class Score {
    private int user_id;
    private int wine_id;
    private double score;
    private Date timestamp;


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getWine_id() {
        return wine_id;
    }

    public void setWine_id(int wine_id) {
        this.wine_id = wine_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Score(int user_id, int wine_id, double score, Date timestamp) {
        this.user_id = user_id;
        this.wine_id = wine_id;
        this.score = score;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Score{" +
                "user_id=" + user_id +
                ", wine_id=" + wine_id +
                ", score=" + score +
                ", timestamp=" + dateToString(timestamp) +
                '}';
    }

    public String toString2() {
        return "new Score(" + user_id + wine_id + score + "\"" + dateToString(timestamp) + "\"";
    }
}
