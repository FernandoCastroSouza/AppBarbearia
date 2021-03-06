package br.com.quantati.AppBarbearia.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Fernando on 12/06/2017.
 */

public class Util {

    public static boolean isConnected(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
