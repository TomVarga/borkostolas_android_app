package hu.tvarga.bor.borkostolas.model;


import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.controller.JSONParser;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class RemoteDAO implements DAO {

    public RemoteDAO(){
        System.out.println("DAO object created");
    }

    ArrayList<Wine> wines = new ArrayList<Wine>();

    @Override
    public boolean addOrUpdateWineScore(Wine wine, int user_id, int score) {
        return false;
    }

    @Override
    public ArrayList<Wine> getWines() {
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
                Wine wine = JSONParser.getWineFromJSONObj(obj);
                wines.add(wine);
            }
            System.out.println("Response : " + result);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }


        return wines;
    }

}
