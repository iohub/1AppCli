package com.zhotkit.justin.zhotkit.utils;


import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhotkit.justin.zhotkit.R;

public class CustomizeToast {

    public static void toast(final Context context, String msg) {
        Toast toast = Toast.makeText(context, msg,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(R.drawable.ic_launcher);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    public static void toast(final Context context, int msgCode) {
        Toast toast = Toast.makeText(context, msgCode,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(R.drawable.ic_launcher);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }
}
