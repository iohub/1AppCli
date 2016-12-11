package com.zhotkit.justin.zhotkit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zhotkit.justin.zhotkit.adapter.DailylistRowAdapter;
import com.zhotkit.justin.zhotkit.utils.ApiUtils;
import com.zhotkit.justin.zhotkit.utils.CustomizeToast;
import com.zhotkit.justin.zhotkit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentDailylists extends Fragment {
    private DailylistRowAdapter adapter;
    private ListView listView;
    private ArrayList<String> mTitles = new ArrayList<String>();
    private ArrayList<String> mIDs = new ArrayList<String>();
    private ArrayList<String> mUrls = new ArrayList<String>();
    private Calendar calendar;

    public FragmentDailylists() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        View rootView = inflater.inflate(R.layout.fragment_dailylists, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_zhihu);
        adapter = new DailylistRowAdapter(getActivity(), mTitles, mUrls);

        reloadJsonContent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Intent intent = new Intent(getActivity(), ArticleActivity.class);
                 intent.putExtra("arctleId", mIDs.get(i));
                 startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        return rootView;
    }

    public void loadMoreJsonContent() {
        String url = ApiUtils.URL_ZHIHU_PREFIX + "before/" + (new SimpleDateFormat("yyyyMMdd")
                .format(calendar.getTime()));
        ConnectivityManager conManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        Context appContext = getActivity().getApplicationContext();
        if ( netInfo != null && netInfo.isAvailable() ) {
            loadHttpJson(url, appContext, new AsyncHttpClient());
        } else {
            CustomizeToast.toast(getActivity().getApplicationContext(), R.string.err_link_network);
            loadLocalJson(appContext);
        }
    }

    public void reloadJsonContent() {
        resetAdapter();
        ConnectivityManager conManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        Context appContext = getActivity().getApplicationContext();
        if ( netInfo != null && netInfo.isAvailable() ) {
            loadHttpJson(ApiUtils.URL_ZHIHU_LATEST, appContext, new AsyncHttpClient());
        } else {
            CustomizeToast.toast(getActivity().getApplicationContext(), R.string.err_link_network);
            loadLocalJson(appContext);
        }
    }

    private void loadLocalJson(final Context context) {
        String text = JsonUtils.loadFile2String(context, ApiUtils.JSON_CACHE_ZHIHU);
        if ( text == null || text.isEmpty() ) return;
        JSONObject obj = null;
        try {
            obj = new JSONObject(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if ( decodeJsonArray(obj.getJSONArray("stories")) ) {
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean  decodeJsonArray(JSONArray response) {
        if ( response == null ) return false;
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                mIDs.add( obj.getString("id") );
                mTitles.add( obj.getString("title"));
                mUrls.add( obj.getJSONArray("images").getString(0));
            } catch (JSONException e) {
                Log.e(getTag(), e.toString());
                return false;
            }
        }
        return true;
    }

    private void resetAdapter() {
        mIDs.clear();
        mTitles.clear();
        mUrls.clear();
    }


    private void loadHttpJson(String url, final Context context, AsyncHttpClient client ) {
        client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        calendar.add(Calendar.HOUR, -24);
                        String str = null;
                        try {
                            str = new String(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JsonUtils.JSONSTR_DAILY_LATEST = new String(str);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if ( decodeJsonArray(obj.getJSONArray("stories")) ) {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errResponse, Throwable e) {
                        CustomizeToast.toast(context, R.string.err_request_json);
                        Log.e(getTag(), e.toString());

                    }
                    @Override
                    public void onFinish() {
                        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                        fab.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

}
