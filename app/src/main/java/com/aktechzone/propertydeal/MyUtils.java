package com.aktechzone.propertydeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

public class MyUtils {
    static AlertDialog.Builder builder;

    @SuppressLint("InlinedApi")
    public static AlertDialog.Builder showAlertDialog(Context context,DialogInterface.OnClickListener dialoginterface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.myDialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Delete Item")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", dialoginterface)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return builder;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showSnackBar(Context context) {
        String message = "NO INTERNET CONNECTION";
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar
                .make(((Activity) context).findViewById(R.id.snackBarView), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
