package es.ibrands.bakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import es.ibrands.bakingapp.R;

/**
 * Created by pablomoreno on 25/01/18.
 */
public  class Utility
{
    public static boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            Log.d("network", context.getString(R.string.no_internet_connection));
            String message = context.getString(R.string.no_internet_connection);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }
}
