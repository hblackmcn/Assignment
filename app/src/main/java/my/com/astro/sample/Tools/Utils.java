package my.com.astro.sample.Tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Date;

/**
 * Created by hossein on 1/2/17.
 */

public class Utils {


    public static String getFormattedHHMM(Date date) {
        String result="";

        int hh = date.getHours();
        int mm = date.getMinutes();

        if (hh<=9)
            result = "0" + hh;
        else
            result = "" + hh;

        result +=":";

        if (mm<=9)
            result += "0" + mm;
        else
            result += "" + mm;

        return result;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
