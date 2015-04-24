package hu.tvarga.bor.borkostolas;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import hu.tvarga.bor.borkostolas.controller.NetworkChecker;

public class ForgotPassword extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        WebView myWebView = (WebView) findViewById(R.id.webViewForgotPassword);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (NetworkChecker.haveNetworkConnection(getBaseContext())) {
            myWebView.loadUrl("http://bor.tvarga.hu/androidPasswordReset.php");
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ForgotPassword.this, R.string.error_noNetworkAccess, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}