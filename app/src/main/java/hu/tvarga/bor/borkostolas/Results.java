package hu.tvarga.bor.borkostolas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hu.tvarga.bor.borkostolas.model.bean.User;


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
