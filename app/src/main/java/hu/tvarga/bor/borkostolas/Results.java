package hu.tvarga.bor.borkostolas;

import android.app.ProgressDialog;
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
import android.widget.Toast;

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
import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.User;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import static hu.tvarga.bor.borkostolas.R.string.error_noNetworkAccess;


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
        if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
            myWebView.loadUrl("http://bor.tvarga.hu/services/androidCalculator.php?user_id=" + user.getUser_id());
        }else{
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Results.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                }
            });
        }
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
