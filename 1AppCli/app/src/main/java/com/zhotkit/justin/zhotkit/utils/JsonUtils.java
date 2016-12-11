package com.zhotkit.justin.zhotkit.utils;


import android.content.Context;
import android.util.Log;

import com.zhotkit.justin.zhotkit.R;

import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class JsonUtils {

    public static String JSONSTR_DAILY_LATEST = null;
    public static String JSONSTR_HOT_MOVIES = null;

    public static String loadFile2String(final Context context, String cacheFileName) {
        StringBuffer text = new StringBuffer();
        File cacheFile = new File(context.getCacheDir().getAbsolutePath(), cacheFileName);
        try {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(cacheFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                CustomizeToast.toast(context, R.string.err_io_open_file);
            }
            if ( br == null ) {
                return null;
            }
            String line = null;
            do {
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text.append(line+'\n');
            } while ( line != null );
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static boolean saveJsonCache(final Context context, String response, String cacheFileName) {
        if (response == null) {
            Log.e(null, " Param response in saveJsonCache is null" );
            return false;
        }
        PrintWriter writer = null;
        try {
            try {
                writer = new PrintWriter(context.getCacheDir().getAbsolutePath()+cacheFileName, "UTF-8");
            }catch (UnsupportedEncodingException e) {
                CustomizeToast.toast(context, R.string.err_io_file_encode);
                Log.e(null, e.toString() );
                return false;
            }
        } catch (FileNotFoundException e) {
            CustomizeToast.toast(context, R.string.err_io_open_file);
            Log.e(null, e.toString());
            return false;
        }
        if (writer == null) {
            return false;
        }
        writer.print(response);
        writer.close();
        return true;
    }

    public static boolean saveJsonCache(final Context context, JSONArray response, String cacheFileName) {
        if (response == null) {
            Log.e(null, " Param response in saveJsonCache is null" );
            return false;
        }
        PrintWriter writer = null;
        try {
            try {
                writer = new PrintWriter(context.getCacheDir().getAbsolutePath()+cacheFileName, "UTF-8");
            }catch (UnsupportedEncodingException e) {
                CustomizeToast.toast(context, R.string.err_io_file_encode);
                Log.e(null, e.toString() );
                return false;
            }
        } catch (FileNotFoundException e) {
            CustomizeToast.toast(context, R.string.err_io_open_file);
            Log.e(null, e.toString());
            return false;
        }
        if (writer == null) {
            return false;
        }
        writer.print(response.toString());
        writer.close();
        return true;
    }
}
