package com.zhotkit.justin.zhotkit;

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
import com.zhotkit.justin.zhotkit.adapter.ListRowAdapter;
import com.zhotkit.justin.zhotkit.utils.ApiUtils;
import com.zhotkit.justin.zhotkit.utils.CustomizeToast;
import com.zhotkit.justin.zhotkit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentZhihulists extends BaseFragment {

    private Calendar calendar;

    public FragmentZhihulists() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, +24);

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_zhihu);
        adapter = new ListRowAdapter(getActivity(), mTitles, mUrls);

        reloadJsonContent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Intent intent = new Intent(getActivity(), ArticleActivity.class);
                 intent.putExtra("arctleId", mIDs.get(i));
                 intent.putExtra("type", "zhihu");
                 startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void loadJsonContent() {
        String url = ApiUtils.URL_ZHIHU_PREFIX + "before/" + (new SimpleDateFormat("yyyyMMdd")
                .format(calendar.getTime()));
        ConnectivityManager conManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        Context appContext = getActivity().getApplicationContext();
        if ( netInfo != null && netInfo.isAvailable() ) {
            httpGetZhihuJson(url, appContext, new AsyncHttpClient());
        } else {
            CustomizeToast.toast(getActivity().getApplicationContext(), R.string.err_link_network);
            loadLocalJson(appContext);
        }
    }

    private void reloadJsonContent() {
        resetAdapter();
        loadJsonContent();
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


    private void httpGetZhihuJson(String url, final Context context, AsyncHttpClient client ) {
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
