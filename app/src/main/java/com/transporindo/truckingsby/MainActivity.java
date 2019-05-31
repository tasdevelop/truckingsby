package com.transporindo.truckingsby;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView view;
    private ProgressDialog progressDialog;
    private BroadcastReceiver mNetworkReceiver;
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        logo = (ImageView) findViewById(R.id.logo);
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {

            view = (WebView) this.findViewById(R.id.webview);

            view.setWebViewClient(new MyBrowser());
            view.getSettings().setJavaScriptEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // chromium, enable hardware acceleration
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                // older android version, disable hardware acceleration
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            view.loadUrl("about:blank");
            view.clearCache(true);
            view.getSettings().setSupportMultipleWindows(true);
            view.loadUrl("http://36.81.248.22:8888/ci-android-sby/");
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
            logo.setVisibility(View.GONE);
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
    public void onBackPressed() {
        if (view.isFocused() && view.canGoBack()) {
            view.goBack();
        } else {

            super.onBackPressed();
        }
    }
}
