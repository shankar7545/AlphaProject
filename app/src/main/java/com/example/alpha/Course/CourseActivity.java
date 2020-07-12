package com.example.alpha.Course;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.alpha.Activity.HelpActivity;
import com.example.alpha.R;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class CourseActivity extends AppCompatActivity {


    public Toolbar usetoolbar;
    ProgressBar progressBar, progressBarCircle;
    WebView webView;
    ProgressDialog progressDialog;
    DatabaseReference websitelinkdb;
    String websitedata = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //Website link declaration here


        progressBar = findViewById(R.id.progressbarLinear);
        progressBarCircle = findViewById(R.id.progressbarCircle);
        webView = findViewById(R.id.webview2);
        progressDialog = new ProgressDialog(CourseActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();

        initToolbar();

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setBackgroundColor(Color.WHITE);
            webView.getSettings().getCacheMode();
            webView.setWebViewClient(new ourViewClient());
            webView.setWebChromeClient(new WebChromeClient() {
                                           public void onProgressChanged(WebView view, int progress) {
                                               progressBar.setProgress(progress);
                                               if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                                                   progressBar.setVisibility(ProgressBar.GONE);
                                                   progressBarCircle.setVisibility(ProgressBar.GONE);
                                               }
                                               if (progress == 100) {
                                                   progressBar.setVisibility(ProgressBar.GONE);
                                                   progressBarCircle.setVisibility(ProgressBar.GONE);

                                                   //progressDialog.dismiss();
                                               }

                                           }
                                       }

            );

            final String data = getIntent().getDataString();
            if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
                webView.loadUrl(data);
            } else {

                webView.loadUrl("https://www.github.com/shankar7545/jtsAdmin/tree/master/app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fexample%2Fjtsadmin%2FCompanyJobs.java");


            }


        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        statusBarColor();

    }

    private void statusBarColor() {

        Window window = CourseActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(CourseActivity.this, R.color.blue_grey_900));
    }


    public class ourViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView v, String url) {
            if (url.contains(websitedata)) {
                v.loadUrl(url);
                CookieManager.getInstance().setAcceptCookie(true);

            } else {
                Uri uri = Uri.parse(url);
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Choose browser"));
            }
            return true;
        }


        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue_grey_900));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(CourseActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Close Now?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

}
