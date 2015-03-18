package hu.tvarga.bor.borkostolas;

import hu.tvarga.bor.borkostolas.controller.DBSyncController;
import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        Bundle extras = getIntent().getExtras();
        final int user_id = extras.getInt("user_id");
        final TextView content = (TextView) findViewById(R.id.contentTV);

        context = getBaseContext();
        LocalDAO lDAO = new LocalDAO(context);
        SQLiteDatabase db = lDAO.getDB();
        final DBSyncController dbSyncController = new DBSyncController();

        Button btnUpdateWines = (Button) findViewById(R.id.btnGetWinesFromRemoteDB);

        btnUpdateWines.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                                RemoteDAO dao;
                                dao = new RemoteDAO();
                                wines = dao.getWines();
                                if (wines.size() > 0){
                                    final boolean updateSucceeded = dbSyncController.updateLocalWineDatabase(context, wines);

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(UserPage.this, updateSucceeded ? R.string.action_wineDBUpdateSucces : R.string.action_wineDBUpdateFail, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String s = "";
                                            for (int i = 0; i < wines.size(); i++) {
                                                s = s + wines.get(i).toString() + "\n";
                                            }

                                            content.setText("Response from PHP : " + s);
                                        }
                                    });
                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(UserPage.this, R.string.error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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