package com.zhotkit.justin.zhotkit;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhotkit.justin.zhotkit.utils.ApiUtils;
import com.zhotkit.justin.zhotkit.utils.CustomizeToast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class ArticleActivity extends AppCompatActivity {

    private WebView mWebView;
    private boolean NIGHT_MODE = true;
    private int zome_size = 85;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) findViewById(R.id.webview_article_content);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(zome_size);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsFunc = String.format("javascript:set_night_mode(%s)",
                        NIGHT_MODE ? "true" : "false");
                // Read drawable
                int res = NIGHT_MODE ? R.drawable.ic_sun : R.drawable.ic_night;
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(res)).getBitmap();
                // Scale it to 50 x 50
                Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 96, 40, true));
                // Set your new, scaled drawable "d"
                fab.setBackgroundDrawable(dr);
                mWebView.loadUrl(jsFunc);
                NIGHT_MODE = !NIGHT_MODE;
            }
        });

        Intent intent = getIntent();
        String arctleId = intent.getStringExtra("arctleId");
        String type = intent.getStringExtra("type");
        if (type.compareTo("zhihu") == 0) {
            loadZhihuArticleJson(this, arctleId, new AsyncHttpClient());
        } else if (type.compareTo("guokr") == 0) {
            loadGuokrArticleJson(this, arctleId, new AsyncHttpClient());
        } else {
            CustomizeToast.toast(this, R.string.err_intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :{
                finish();
                break;
            }
            case R.id.action_zoomin: {
                zome_size += 5;
                mWebView.getSettings().setTextZoom(zome_size);
                break;
            }
            case R.id.action_zoomout: {
                zome_size -= 5;
                mWebView.getSettings().setTextZoom(zome_size);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
       return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_article_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadZhihuArticleJson(final Context context, String arctleId, AsyncHttpClient client ) {
        client.get(ApiUtils.URL_ZHIHU_PREFIX + arctleId, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        String str = null;
                        try {
                            str = new String(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String bodyContent = null;
                        try {
                            bodyContent = obj.getString("body");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.e(null, bodyContent);
                        mWebView.loadDataWithBaseURL("file:///android_asset/www/",
                                ApiUtils.HTML_HEAD + bodyContent + ApiUtils.HTML_END,
                                "text/html", "UTF-8", null);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errResponse, Throwable e) {
                        CustomizeToast.toast(context, R.string.err_request_json);
                        Log.e(null, e.toString());
                    }

                    @Override
                    public void onFinish() {
                    }
                }
        );
    }


    private void loadGuokrArticleJson(final Context context, String arctleId, AsyncHttpClient client ) {
        client.get(ApiUtils.URL_GUOKR_ARTICLE + arctleId + ".json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,Header[] headers, byte[] response) {
                        String str = null;
                        try {
                            str = new String(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jobj = null;
                        try {
                            jobj = obj.getJSONObject("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String bodyContent = null;
                        try {
                            bodyContent = jobj.getString("content");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mWebView.loadDataWithBaseURL("file:///android_asset/www/",
                                ApiUtils.HTML_HEAD + ApiUtils.BODY_BEGIN + bodyContent
                                        + ApiUtils.BODY_END + ApiUtils.HTML_END,
                                "text/html", "UTF-8", null);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errResponse, Throwable e) {
                        CustomizeToast.toast(context, R.string.err_request_json);
                        Log.e(null, e.toString());
                    }
                    @Override
                    public void onFinish() {
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
