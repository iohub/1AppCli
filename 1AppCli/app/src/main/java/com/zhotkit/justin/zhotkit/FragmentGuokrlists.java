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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class FragmentGuokrlists extends BaseFragment {

    private long timestamp;
    private long offset = 0;

    public FragmentGuokrlists() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        timestamp = (new Date()).getTime();
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_zhihu);
        adapter = new ListRowAdapter(getActivity(), mTitles, mUrls);

        reloadJsonContent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("arctleId", mIDs.get(i));
                intent.putExtra("type", "guokr");
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        return rootView;
    }

    public void loadGuokrJsonContent() {
        String url = ApiUtils.URL_GUOKR_PREFIX + "&limit=" + String.valueOf(ApiUtils.GUOKR_PAGE_LIMIT)
                + "&offset=" + String.valueOf(offset) + "&_=" + String.valueOf(timestamp);
        ConnectivityManager conManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        Context appContext = getActivity().getApplicationContext();
        if (netInfo != null && netInfo.isAvailable()) {
            httpGetGuokrJson(url, appContext, new AsyncHttpClient());
        } else {
            CustomizeToast.toast(getActivity().getApplicationContext(), R.string.err_link_network);
        }
    }

    private void reloadJsonContent() {
        resetAdapter();
        loadGuokrJsonContent();
    }

    @Override
    public void loadJsonContent() {
        loadGuokrJsonContent();
    }

    private boolean  decodeJsonArray(JSONArray response) {
        if ( response == null ) return false;
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                mIDs.add( obj.getString("id") );
                mTitles.add( obj.getString("title"));
                mUrls.add( obj.getString("small_image"));
            } catch (JSONException e) {
                Log.e(getTag(), e.toString());
                return false;
            }
        }
        return true;
    }

    private void httpGetGuokrJson(String url, final Context context, AsyncHttpClient client ) {
        client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        offset += ApiUtils.GUOKR_PAGE_LIMIT;
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
                            if ( decodeJsonArray(obj.getJSONArray("result")) ) {
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
