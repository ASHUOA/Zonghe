package com.example.yuekao.bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fanyishuo on 2017/7/26.
 */

public class NewWork {
    public static boolean isConnnent(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info!=null&&info.isConnected());
    }
}
