package hu.tvarga.bor.borkostolas;

import android.app.Activity;

import android.os.Bundle;
import android.widget.TextView;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import java.util.ArrayList;
import java.util.List;

public class UserPage extends Activity {
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);

        Bundle extras = getIntent().getExtras();
        final int user_id = extras.getInt("user_id");
        final TextView content = (TextView) findViewById(R.id.contentTV);

        new Thread(new Runnable() {
            public void run() {
                try{

                    httpclient=new DefaultHttpClient();
                    httppost= new HttpPost("http://bor.tvarga.hu/getWineScoresForUser.php"); // make sure the url is correct.
                    //add your data
                    nameValuePairs = new ArrayList<>(2);
                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                    nameValuePairs.add(new BasicNameValuePair("user_id", user_id+""));  // $Edittext_value = $_POST['Edittext_value'];
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //Execute HTTP Post Request
                    response=httpclient.execute(httppost);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    content.setText("Response from PHP : " + response);


                }catch(Exception e){
                    System.out.println("Exception : " + e.getMessage());
                }
            }
        }).start();
    }
}