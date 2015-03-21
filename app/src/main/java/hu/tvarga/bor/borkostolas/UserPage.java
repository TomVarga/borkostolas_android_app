package hu.tvarga.bor.borkostolas;

import hu.tvarga.bor.borkostolas.controller.DBSyncController;
import hu.tvarga.bor.borkostolas.controller.DecimalDigitsInputFilter;
import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.controller.OnAdapterNeedsNotify;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.ScoredWine;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Date;
import java.util.List;

public class UserPage extends Activity {
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ArrayList<Wine> localWines;
    ArrayList<Score> localScores;
    ArrayList<Wine> remoteWines;
    ArrayList<Score> remoteScores;
    ArrayList<ScoredWine> localScoredWines;
    Context context;
    OnAdapterNeedsNotify adapterNotifyListener;

    public void updateLocalScoredWines(Context context, int user_id){
        LocalDAO lDAO = new LocalDAO(context);
        localWines = lDAO.getWines();
        localScoredWines.clear();

        if (localWines.size() > 0){
            for (int i = 0; i < localWines.size(); i++){
                Wine lWine = localWines.get(i);
                double lScore =  lDAO.getScore(user_id, lWine.getWine_id());
                double score = (lScore > 0) ? lScore : -1;

                //wine_id, wine_name, wine_winery, wine_location, wine_year, wine_composition, wine_price
                ScoredWine scoredWine = new ScoredWine(
                        lWine.getWine_id(),
                        lWine.getWine_name(),
                        lWine.getWine_winery(),
                        lWine.getWine_location(),
                        lWine.getWine_year(),
                        lWine.getWine_composition(),
                        lWine.getWine_price(),
                        score,
                        user_id
                );
                localScoredWines.add(scoredWine);
            }
        }
        System.out.println("localScoredWines updated");
    }

    public void setOnAdapterNeedsNotify(OnAdapterNeedsNotify eventListener) {
        adapterNotifyListener=eventListener;
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
                                RemoteDAO dao = new RemoteDAO();
                                remoteWines = dao.getWines();

                                if (remoteWines.size() > 0){
                                    final boolean updateSucceeded = dbSyncController.updateLocalWineDatabase(context, remoteWines);

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(UserPage.this, updateSucceeded ? R.string.action_wineDBUpdateSucces : R.string.action_wineDBUpdateFail, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String s = "";
                                            for (int i = 0; i < remoteWines.size(); i++) {
                                                s = s + remoteWines.get(i).toString() + "\n";
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

        localScoredWines = new ArrayList<>();
        updateLocalScoredWines(context, user_id);


//        System.out.println("wine size: " + localWines.size());
//        if (localWines.size() > 0) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String s = "";
//                    for (int i = 0; i < localWines.size(); i++) {
//                        s = s + localWines.get(i).toString() + "\n";
//                    }
//
//                    content.setText("Local wines : " + s);
//                }
//            });
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                    createTableRows(wines);
//                }
//            });
//        }

//        String[] winesArray = new String[wines.size()];
//        for (int i=0; i < wines.size(); i++){
//            winesArray[i] = wines.get(i).getWine_name();
//        }


//        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"a", "b", "c"});

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, winesArray);
        final WinesAdapter adapter = new WinesAdapter(this, localScoredWines);
        ListView winesList = (ListView) findViewById(R.id.winesList);
        winesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        winesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScoredWine wine = localScoredWines.get(position);
                TextView detailsWineNameTV = (TextView) findViewById(R.id.detailsWineName);
                detailsWineNameTV.setText(wine.getWine_name());
                TextView detailsWineryTV = (TextView) findViewById(R.id.detailsWineryTV);
                detailsWineryTV.setText(wine.getWine_winery());
                TextView detailsLocationTV = (TextView) findViewById(R.id.detailsLocationTV);
                detailsLocationTV.setText(wine.getWine_location());
                TextView detailsYearTV = (TextView) findViewById(R.id.detailsYearTV);
                detailsYearTV.setText(wine.getWine_year() + "");
                TextView detailsCompositionTV = (TextView) findViewById(R.id.detailsCompositionTV);
                detailsCompositionTV.setText(wine.getWine_composition());
                TextView detailsPriceTV = (TextView) findViewById(R.id.detailsPriceTV);
                detailsPriceTV.setText(wine.getWine_price() + "");
                EditText scoreET = (EditText) findViewById(R.id.detailsScoreET);
                double score = wine.getWine_score();
                scoreET.setText((score > 0) ? (score + "") : "");
                scoreET.setTag(wine);
            }
        });
        winesList.setAdapter(adapter);

        EditText scoreET = (EditText) findViewById(R.id.detailsScoreET);
//        scoreET.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 3)});
        // TODO: fix input filter
        scoreET.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                if(!hasFocus) {
                    ScoredWine wine = (ScoredWine) v.getTag();
                    String ETText = et.getText().toString();
                    double nScore = (ETText.equals("")) ? -1 : Double.parseDouble(ETText);
                    wine.setWine_score(nScore);
                    Score score = new Score(wine.getUser_id(), wine.getWine_id(), nScore, new Date());
                    LocalDAO lDAO = new LocalDAO(context);
                    lDAO.addOrUpdateScore(score);
                    adapter.notifyDataSetChanged();
                    System.out.println("focus lost on ET" + wine.toString());
                }
            }
        });
        Button btnSyncScore = (Button) findViewById(R.id.buttonSubmitScore);
        btnSyncScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                                RemoteDAO dao = new RemoteDAO();
                                remoteScores = dao.getScores(user_id);
                                if (remoteScores.size() > 0) {
                                    final boolean updateSucceeded = dbSyncController.syncScores(context, remoteScores, user_id);
                                    updateLocalScoredWines(context, user_id);
                                    adapterNotifyListener.onEvent();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(UserPage.this, updateSucceeded ? R.string.action_scoreSyncSuccess : R.string.action_scoreSyncFail, Toast.LENGTH_SHORT).show();

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


        setOnAdapterNeedsNotify(new OnAdapterNeedsNotify() {
            @Override
            public void onEvent() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        System.out.println("onEvent : OnAdapterNeedsNotify");
                        adapter.notifyDataSetChanged();
                    }
                });
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


//        content.setText("poop");
    }


    public class WinesAdapter extends ArrayAdapter<ScoredWine> {

        public WinesAdapter(Context context, ArrayList<ScoredWine> wines) {
            super(context, 0, wines);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScoredWine wine = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.winerow, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.wineRowName);
            TextView tvScore = (TextView) convertView.findViewById(R.id.wineRowScore);

            tvName.setText(wine.getWine_name());
            double score = wine.getWine_score();
            tvScore.setText((score > 0) ? (score + "") : "");

            return convertView;
        }

    }
}