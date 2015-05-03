package hu.tvarga.bor.borkostolas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import hu.tvarga.bor.borkostolas.controller.DBSyncController;
import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.controller.OnAdapterNeedsNotify;
import hu.tvarga.bor.borkostolas.controller.WinesAdapter;
import hu.tvarga.bor.borkostolas.model.LocalDAO;
import hu.tvarga.bor.borkostolas.model.RemoteDAO;
import hu.tvarga.bor.borkostolas.model.bean.Score;
import hu.tvarga.bor.borkostolas.model.bean.ScoredWine;
import hu.tvarga.bor.borkostolas.model.bean.User;
import hu.tvarga.bor.borkostolas.model.bean.Wine;

import static hu.tvarga.bor.borkostolas.R.string.*;

public class UserPage extends ActionBarActivity {
    ArrayList<Wine> localWines;
    ArrayList<Wine> remoteWines;
    ArrayList<Score> remoteScores;
    ArrayList<ScoredWine> localScoredWines;
    Context context;
    OnAdapterNeedsNotify adapterNotifyListener;
    ProgressDialog dialog = null;
    User user;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        findViewById(R.id.detailsContainer).requestFocus();

        context = getBaseContext();
//        Bundle extras = getIntent().getExtras();
        user = new User();
        user.populateFromPrefs(context);
        final int user_id = user.getUser_id();
//        final int user_id = extras.getInt("user_id");

//        final User user = new User(user_id, extras.getString("user_name"), extras.getString("user_password"));


        final DBSyncController dbSyncController = new DBSyncController();

        Button btnUpdateWines = (Button) findViewById(R.id.btnGetWinesFromRemoteDB);

        btnUpdateWines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(UserPage.this, "", getResources().getString(R.string.action_getWineDB), true);
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
                                            dialog.dismiss();
                                            Toast.makeText(UserPage.this, updateSucceeded ? action_wineDBUpdateSucces : action_wineDBUpdateFail, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(UserPage.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch(Exception e){
                            dialog.dismiss();
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                }).start();
            }
        });

        localScoredWines = new ArrayList<>();
        updateLocalScoredWines(context, user_id);

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

                Double score = wine.getWine_score();
                scoreET.setText(getFormattedScore(score));

                scoreET.setTag(wine);
                scoreET.setFocusable(true);
                scoreET.setFocusableInTouchMode(true);
            }
        });
        winesList.setAdapter(adapter);

        EditText scoreET = (EditText) findViewById(R.id.detailsScoreET);
        // so we can't even enter edit mode without the details frame displaying some data
        scoreET.setFocusable(false);
        scoreET.setFocusableInTouchMode(false);
//        scoreET.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 3)});
        scoreET.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                if(!hasFocus) {
                    ScoredWine wine = (ScoredWine) v.getTag();
                    String ETText = et.getText().toString();
                    Double nScore = (ETText.equals("")) ? -1 : Double.parseDouble(ETText);
                    wine.setWine_score(nScore);
                    Score score = new Score(wine.getUser_id(), wine.getWine_id(), nScore, new Date());
                    LocalDAO lDAO = new LocalDAO(context);
                    lDAO.addOrUpdateScore(score);
                    adapter.notifyDataSetChanged();
                    System.out.println("focus lost on ET" + wine.toString());
                }
            }
        });
        scoreET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    v.setCursorVisible(false);
                    v.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager)
                            context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                }
//                System.out.println("key pressed: " + event.getAction() + " enter " + KeyEvent.KEYCODE_ENTER);

                return false;
            }
        });
        Button btnSyncScore = (Button) findViewById(R.id.buttonSubmitScore);
        btnSyncScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(UserPage.this, "", getResources().getString(R.string.action_syncScores), true);
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                                RemoteDAO dao = new RemoteDAO();
                                remoteScores = dao.getScores(user_id);
//                                if (remoteScores.size() > 0) {
                                    final boolean updateSucceeded = dbSyncController.syncScores(context, remoteScores, user);
                                    updateLocalScoredWines(context, user_id);
                                    adapterNotifyListener.onEvent();

                                    // update the details view if we have to
                                    final EditText scoreET = (EditText) findViewById(R.id.detailsScoreET);
                                    if (scoreET.isFocusable()){
                                        LocalDAO lDAO = new LocalDAO(context);
                                        ScoredWine wine = (ScoredWine) scoreET.getTag();
                                        final Double score = lDAO.getScore(wine.getUser_id(), wine.getWine_id());

                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                scoreET.setText(getFormattedScore(score));
                                            }
                                        });
                                    }

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(UserPage.this, updateSucceeded ? action_scoreSyncSuccess : action_scoreSyncFail, Toast.LENGTH_SHORT).show();
                                        }
                                    });
//                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(UserPage.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch(Exception e){
                            dialog.dismiss();
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

    }

    public static String getFormattedScore(Double score){
        String sScore;
        if (score > 0) {
            sScore = score.toString().replaceAll("[.]*[0]+$", ""); // cut trailing zeros
        }else{
            sScore = "";
        }
        return sScore;
    }




}