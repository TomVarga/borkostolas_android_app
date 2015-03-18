package hu.tvarga.bor.borkostolas.controller;

import org.json.JSONObject;

import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class JSONParser {

    public static Wine getWineFromJSONObj(JSONObject obj){
        Wine wine;
        try {
            int wine_id = obj.getInt("wine_id");
            String wine_name = obj.getString("wine_name");
            String wine_winery = obj.getString("wine_winery");
            String wine_location = obj.getString("wine_location");
            String wine_composition = obj.getString("wine_composition");
            // these can be null so we check first
            int wine_year = -1, wine_price = -1;
            if (!obj.isNull("Wine_year")) wine_year = obj.getInt("wine_year");
            if (!obj.isNull("wine_price")) wine_price = obj.getInt("wine_price");
            wine = new Wine(wine_id, wine_name, wine_winery, wine_location, wine_year, wine_composition, wine_price);
            return wine;
        }catch(Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return null;
    }
}