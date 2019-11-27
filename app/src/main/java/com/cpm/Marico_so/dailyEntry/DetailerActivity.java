package com.cpm.Marico_so.dailyEntry;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cpm.Marico_so.getterSetter.JourneyPlan;
import com.cpm.Marico_so.getterSetter.MenuMaster;
import com.cpm.Marico_so.R;
import com.cpm.Marico_so.utilities.CommonString;

public class DetailerActivity extends AppCompatActivity {

    WebView webview_detailer;
    ImageView img_main;
    String visit_date;
    private SharedPreferences preferences;
    JourneyPlan jcpGetset;
    MenuMaster menuMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detailer);
        webview_detailer = findViewById(R.id.webview_detailer);
        img_main = findViewById(R.id.img_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null && getIntent().getSerializableExtra(CommonString.KEY_MENU_ID) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
            menuMaster = (MenuMaster) getIntent().getSerializableExtra(CommonString.KEY_MENU_ID);

        }

        String url ="http://maricoso.parinaam.in/Detailer/detailer.html";
        webview_detailer.getSettings().setJavaScriptEnabled(true);
        webview_detailer.loadUrl(url);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            // NavUtils.navigateUpFromSameTask(this);
            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  view.loadUrl(url);
            view.loadUrl("http://maricoso.parinaam.in/Detailer/detailer.html");
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (checkNetIsAvailable()) {
                img_main.setVisibility(View.GONE);
                webview_detailer.setVisibility(View.VISIBLE);
            } else {
                img_main.setVisibility(View.VISIBLE);
                webview_detailer.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
            view.clearCache(true);
        }
    }
    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
