package com.aktechzone.propertydeal.MyProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
    public static ProgressDialog showDialog(Context context, String message){
        ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage(message);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.show();
        return mDialog;
    }
}
