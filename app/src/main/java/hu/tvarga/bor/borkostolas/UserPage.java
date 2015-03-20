package hu.tvarga.bor.borkostolas;

import hu.tvarga.bor.borkostolas.controller.DBSyncController;
import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import java.util.ArrayList;
import java.util.List;

public class UserPage extends Activity {
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ArrayList<Wine> wines;
    Context context;

    public void createTableRows(List<Wine> wines){
//        TableLayout winesTable = (TableLayout) findViewById(R.id.winesTable);

        for (int i = 0; i < wines.size(); i++) {

            TableRow row = new TableRow(this);
//
//            LinearLayout left = new LinearLayout(this);
//            left.setOrientation(LinearLayout.HORIZONTAL);
//            left.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.7f));
//
//            Button button = new Button(this);
//            button.setGravity(Gravity.CENTER);
//            button.setText(wines.get(i).getWine_name());
////            button.setTag(1, wines.get(i).getWine_id());
////            button.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
////            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
//
//            left.addView(button);
//
//            LinearLayout right = new LinearLayout(this);
//            right.setOrientation(LinearLayout.HORIZONTAL);
//            right.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.3f));
//            right.setGravity(Gravity.RIGHT);
//
//            TextView scoreTV = new TextView(this);
////            scoreTV.setText(wines.get(i).getWine_price() + "");
//            scoreTV.setText("100");
//            scoreTV.setGravity(Gravity.CENTER);
////            scoreTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
//
//            right.addView(scoreTV);
//
//            row.addView(left);
//            row.addView(right);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
            row.setLayoutParams(lp);
            Button button = new Button(this);
            TextView score = new TextView(this);
            score.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.3f));
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.7f));
            button.setText(wines.get(i).getWine_name());
            score.setText("10");
            row.addView(score);
            row.addView(button);
//            winesTable.addView(row,i);
//            TableRow wineRow = (TableRow) findViewById(R.id.wineRow);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        Bundle extras = getIntent().getExtras();
        final int user_id = extras.getInt("user_id");
        final TextView content = (TextView) findViewById(R.id.contentTV);

        context = getBaseContext();

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


        LocalDAO lDAO = new LocalDAO(context);
        wines = lDAO.getWines();
        System.out.println("wine size: " + wines.size());
        if (wines.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String s = "";
                    for (int i = 0; i < wines.size(); i++) {
                        s = s + wines.get(i).toString() + "\n";
                    }

                    content.setText("Local wines : " + s);
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    createTableRows(wines);
                }
            });
        }

//        String[] winesArray = new String[wines.size()];
//        for (int i=0; i < wines.size(); i++){
//            winesArray[i] = wines.get(i).getWine_name();
//        }


//        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"a", "b", "c"});

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, winesArray);
        WinesAdapter adapter = new WinesAdapter(this, wines);
        ListView winesList = (ListView) findViewById(R.id.winesList);
        winesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        winesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position: "+position);
                System.out.println("win in position: "+wines.get(position).getWine_name());
                TextView detailsWineNameTV = (TextView) findViewById(R.id.detailsWineName);
                detailsWineNameTV.setText(wines.get(position).getWine_name());
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(UserPage.this, "clicked list item", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        winesList.setAdapter(adapter);






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









//        content.setText("poop");
    }


    public class WinesAdapter extends ArrayAdapter<Wine> {

        public WinesAdapter(Context context, ArrayList<Wine> wines) {
            super(context, 0, wines);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Wine wine = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.winerow, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.wineRowName);
            TextView tvScore = (TextView) convertView.findViewById(R.id.wineRowScore);

            tvName.setText(wine.getWine_name());

//            convertView.setTag(1, wine.getWine_id());

            return convertView;
        }

    }
}