package hu.tvarga.bor.borkostolas;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hu.tvarga.bor.borkostolas.controller.NetworkChecker;
import hu.tvarga.bor.borkostolas.model.bean.User;

import static hu.tvarga.bor.borkostolas.R.string.error_noNetworkAccess;

public class Main extends Activity {

    User user;
    Context context;
    Button b, r, f;
    EditText et,pass;
    TextView tv;
    HttpPost httppost;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getBaseContext();

        b = (Button)findViewById(R.id.Login);
        r = (Button)findViewById(R.id.Register);
        f = (Button)findViewById(R.id.ForgotPassword);
        et = (EditText)findViewById(R.id.username);
        pass= (EditText)findViewById(R.id.password);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                    dialog = ProgressDialog.show(Main.this, "", getResources().getString(R.string.action_userValidation), true);
                    new Thread(new Runnable() {
                        public void run() {
                            login();
                        }
                    }).start();
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Main.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        r.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                    new Thread(new Runnable() {
                        public void run() {
                            register();
                        }
                    }).start();
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Main.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
                    new Thread(new Runnable() {
                        public void run() {
                            forgotPassword();
                        }
                    }).start();
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Main.this, error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void login(){
        try{
            user = new User();
            user.setUser_name(et.getText().toString().trim());
            user.setUser_password(pass.getText().toString().trim());
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://bor.tvarga.hu/login3/androidLogin.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("login","true"));
            nameValuePairs.add(new BasicNameValuePair("user_name",user.getUser_name()));
            nameValuePairs.add(new BasicNameValuePair("user_password",user.getUser_password()));
            nameValuePairs.add(new BasicNameValuePair("user_rememberme", null));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    //                    tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });

            if(response.equalsIgnoreCase("Login Failed")){
                showAlert();
            }else{
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Main.this,R.string.action_successfulLogin, Toast.LENGTH_SHORT).show();
                    }
                });

                JSONObject obj = new JSONObject(response);
                int user_id = obj.getInt("user_id");
                user.setUser_id(user_id);
                user.setLoggedIn(true);

                user.setPrefs(context);

                Intent intent = new Intent(Main.this, UserPage.class);
                startActivity(intent);
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    void register(){
        Intent intent = new Intent(Main.this, Register.class);
        startActivity(intent);
    }

    void forgotPassword(){
        Intent intent = new Intent(Main.this, ForgotPassword.class);
        startActivity(intent);
    }
    public void showAlert(){
        Main.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setTitle(R.string.error_error);
                builder.setMessage(R.string.error_loginError)
                        .setCancelable(false)
                        .setPositiveButton(R.string.action_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        et = (EditText)findViewById(R.id.username);
        pass= (EditText)findViewById(R.id.password);
        et.setText("");
        pass.setText("");
    }
    @Override
    public void onBackPressed() {
        return;
    }

}
