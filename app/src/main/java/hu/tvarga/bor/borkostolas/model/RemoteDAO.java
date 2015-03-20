package hu.tvarga.bor.borkostolas.model;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hu.tvarga.bor.borkostolas.controller.JSONParser;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

public class RemoteDAO implements DAO {

    public RemoteDAO(){
        System.out.println("rDAO object created");
    }

    ArrayList<Wine> wines = new ArrayList<Wine>();
    ArrayList<Score> scores = new ArrayList<Score>();


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

    @Override
    public ArrayList<Score> getScores(int user_id){
        HttpPost httppost;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;

        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://bor.tvarga.hu/getWineScoresForUser.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("user_id", user_id+""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String result = httpclient.execute(httppost, responseHandler);
            JSONArray jsonArray = new JSONArray(result);
            for (int i=0; i < jsonArray.length(); i++ ){
                JSONObject obj = (JSONObject) jsonArray.get(i);
                Score score = JSONParser.getScoreFromJSONObj(obj);
                scores.add(score);
            }
            System.out.println("Response : " + result);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }

        return scores;
    }

    @Override
    public boolean addOrUpdateScore(Score score) {
        return false;
    }


}
