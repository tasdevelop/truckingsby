package com.transporindo.truckingsby;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView view;
    private ProgressDialog progressDialog;
    private BroadcastReceiver mNetworkReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {

            view = (WebView) this.findViewById(R.id.webview);

            view.setWebViewClient(new MyBrowser());
            view.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= 19) {
                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }

            view.loadUrl("about:blank");
            view.clearCache(true);
            view.getSettings().setSupportMultipleWindows(true);
            view.loadUrl("http://36.81.248.22/ci-android-sby/");
        }



    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            progressDialog.show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressDialog.isShowing() ) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(MainActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onDestroy() {
        view.clearHistory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (view.isFocused() && view.canGoBack()) {
            view.goBack();
        } else {

            super.onBackPressed();
        }
    }
}
