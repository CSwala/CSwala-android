package com.cswala.cswala.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cswala.cswala.R;

public class WebActivity extends AppCompatActivity {

    private WebView myWebView;
    private Toolbar toolbar;
    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        myWebView=findViewById(R.id.webView);

        toolbar = findViewById(R.id.webViewToolbar);
        setSupportActionBar(toolbar);

        WebSettings webSettings=myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String WebUrl=getIntent().getStringExtra("URL");
        myWebView.loadUrl(WebUrl);
        myWebView.setWebViewClient(new WebViewClient());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.desktopView:
                myWebView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
                myWebView.reload();
                break;

            case R.id.zoomControls:
                myWebView.getSettings().setBuiltInZoomControls(true);
                myWebView.getSettings().setDisplayZoomControls(true);
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}