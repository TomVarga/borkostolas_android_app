package hu.tvarga.bor.borkostolas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

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
import hu.tvarga.bor.borkostolas.model.bean.User;
import hu.tvarga.bor.borkostolas.model.bean.Wine;


public class Results extends ActionBarActivity {
    User user;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        context = getBaseContext();
        user = new User();
        user.populateFromPrefs(context);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://bor.tvarga.hu/services/androidCalculator.php?user_id="+user.getUser_id());

                new Thread(new Runnable() {
                    public void run() {

                        HttpPost httppost;
                        HttpClient httpclient;
                        List<NameValuePair> nameValuePairs;
                        try {
                            httpclient = new DefaultHttpClient();
                            httppost = new HttpPost("http://bor.tvarga.hu/services/androidCalculator.php"); // make sure the url is correct.
                            nameValuePairs = new ArrayList<>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", user.getUser_id() + ""));
//                            nameValuePairs.add(new BasicNameValuePair("user_name", user.getUser_name() + ""));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            String result = httpclient.execute(httppost, responseHandler);
//                JSONArray jsonArray = new JSONArray(result);
//                for (int i=0; i < jsonArray.length(); i++ ){
//                    JSONObject obj = (JSONObject) jsonArray.get(i);
//                    Score score = JSONParser.getScoreFromJSONObj(obj);
//                    scores.add(score);
//                }
//                            System.out.println("Response : " + result);
                        } catch (Exception e) {
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                }).start();


    }

    public boolean onCreateOptionsMenu(Menu menu){
        if (user.isLoggedIn()) menu.removeItem(R.id.action_login);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (user.isLoggedIn()) menu.removeItem(R.id.action_login);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        if (item.getItemId() == R.id.action_ertekel) {
            intent = new Intent(getApplicationContext(), UserPage.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_login) {
            intent = new Intent(getApplicationContext(), Main.class);
            user.setLoggedIn(false);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_logout) {
            intent = new Intent(getApplicationContext(), Main.class);
            user.setLoggedIn(false);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_results){
            intent = new Intent(getApplicationContext(), Results.class);
            startActivity(intent);
        }
        invalidateOptionsMenu(); // forces update
        return true;
    }
}
