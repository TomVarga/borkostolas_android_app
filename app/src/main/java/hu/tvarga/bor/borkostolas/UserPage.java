package hu.tvarga.bor.borkostolas;

import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import java.util.List;

public class UserPage extends Activity {
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    List<Wine> wines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        Bundle extras = getIntent().getExtras();
        final int user_id = extras.getInt("user_id");
        final TextView content = (TextView) findViewById(R.id.contentTV);


        LocalDAO lDAO = new LocalDAO(getBaseContext());
        SQLiteDatabase db = lDAO.getDB();

        Button btnUpdateWines = (Button) findViewById(R.id.btnGetWinesFromRemoteDB);

        btnUpdateWines.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            RemoteDAO dao;
                            dao = new RemoteDAO();
                            wines = dao.getWines();



                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String s = "";
                                    for (int i = 0; i < wines.size(); i++){
                                        s = s + wines.get(i).toString() + "\n";
                                    }
                                    content.setText(NetworkChecker.haveNetworkConnection(getBaseContext()) ? "van" : "nincs");
//                                    content.setText("Response from PHP : " + s);
                                }
                            });
                        }catch(Exception e){
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                }).start();
            }
        });




//                    httpclient=new DefaultHttpClient();
//                    httppost= new HttpPost("http://bor.tvarga.hu/getWineScoresForUser.php"); // make sure the url is correct.
//                    //add your data
//                    nameValuePairs = new ArrayList<>(2);
//                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
//                    nameValuePairs.add(new BasicNameValuePair("user_id", user_id+""));  // $Edittext_value = $_POST['Edittext_value'];
//                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                    //Execute HTTP Post Request
//                    response=httpclient.execute(httppost);
//                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
//                    final String response = httpclient.execute(httppost, responseHandler);
//                    System.out.println("Response : " + response);









        content.setText("poop");
    }
}