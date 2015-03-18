package hu.tvarga.bor.borkostolas.model.bean;

public class Wine {

    private int wine_id;
    private String wine_name;
    private String wine_winery;
    private String wine_location;
    private int wine_year;
    private String wine_composition;
    private int wine_price;

    public void setWine_year(int wine_year) {
        this.wine_year = wine_year;
    }

    public String getWine_location() {
        return wine_location;
    }

    public void setWine_location(String wine_location) {
        this.wine_location = wine_location;
    }

    public String getWine_winery() {
        return wine_winery;
    }

    public void setWine_winery(String wine_winery) {
        this.wine_winery = wine_winery;
    }

    public int getWine_year() {
        return wine_year;
    }

    public String getWine_name() {
        return wine_name;
    }

    public void setWine_name(String wine_name) {
        this.wine_name = wine_name;
    }

    public int getWine_id() {
        return wine_id;
    }

    public void setWine_id(int wine_id) {
        this.wine_id = wine_id;
    }

    public String getWine_composition() {
        return wine_composition;
    }

    public void setWine_composition(String wine_composition) {
        this.wine_composition = wine_composition;
    }

    public int getWine_price() {
        return wine_price;
    }

    public void setWine_price(int wine_price) {
        this.wine_price = wine_price;
    }

    public Wine(int wine_id, String wine_name, String wine_winery, String wine_location, int wine_year, String wine_composition, int wine_price) {
        this.wine_id = wine_id;
        this.wine_name = wine_name;
        this.wine_winery = wine_winery;
        this.wine_location = wine_location;
        this.wine_composition = wine_composition;
        if (wine_year != -1) this.wine_year = wine_year;
        if (wine_price != -1) this.wine_price = wine_price;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "wine_id=" + wine_id +
                ", wine_name='" + wine_name + '\'' +
                ", wine_winery='" + wine_winery + '\'' +
                ", wine_location='" + wine_location + '\'' +
                ", wine_year=" + wine_year +
                ", wine_composition='" + wine_composition + '\'' +
                ", wine_price=" + wine_price +
                '}';
    }
}
