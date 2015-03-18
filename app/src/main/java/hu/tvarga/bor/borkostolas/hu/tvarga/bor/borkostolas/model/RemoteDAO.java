package hu.tvarga.bor.borkostolas.hu.tvarga.bor.borkostolas.model;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class RemoteDAO implements DAO {

    public RemoteDAO(){
        System.out.println("DAO object created");
    }

    List<Wine> wines = new ArrayList<Wine>();

    @Override
    public boolean addOrUpdateWineScore(Wine wine, int user_id, int score) {
        return false;
    }

    @Override
    public List<Wine> getWines() {
        HttpPost httppost;
        HttpClient httpclient;

        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://bor.tvarga.hu/getWineData.php"); // make sure the url is correct.
            httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String result = httpclient.execute(httppost, responseHandler);
            JSONArray jsonArray = new JSONArray(result);
            for (int i=0; i < jsonArray.length(); i++ ){
                JSONObject obj = (JSONObject) jsonArray.get(i);
                int wine_id = obj.getInt("wine_id");
                String wine_name = obj.getString("wine_name");
                String wine_winery = obj.getString("wine_winery");
                String wine_location = obj.getString("wine_location");
                int wine_year = obj.getInt("wine_year");
                String wine_composition = obj.getString("wine_composition");
                int wine_price = obj.getInt("wine_price");
                Wine wine = new Wine(wine_id, wine_name, wine_winery, wine_location, wine_year, wine_composition, wine_price);
                wines.add(wine);
            }
            System.out.println("Response : " + result);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }


        return wines;
    }

}
