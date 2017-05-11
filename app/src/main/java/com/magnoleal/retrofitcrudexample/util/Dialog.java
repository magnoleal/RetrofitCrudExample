package com.magnoleal.retrofitcrudexample.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.concurrent.Callable;

/**
 * Created by magno on 10/12/15.
 */
public class Dialog {

    public static AlertDialog alert(Context activity, String title, String msg){
        return closeActivityAlert(activity, title, msg, false);
    }

    public static AlertDialog closeActivityAlert(final Context activity, String title, String msg){
        return closeActivityAlert(activity, title, msg, true);
    }

    public static AlertDialog closeActivityAlert(final Context activity, String title, String msg, final boolean closeAct){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(msg)
                .setTitle(title);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if(closeAct && activity instanceof Activity)
                    ((Activity)activity).finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog confirm(Context context, String title, String msg, final Callable callSim, final Callable callNao){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(msg)
                .setTitle(title);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if(callSim != null){
                    try {
                        callSim.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if(callNao != null){
                    try {
                        callNao.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}
