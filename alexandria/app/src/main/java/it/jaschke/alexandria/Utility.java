package it.jaschke.alexandria;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Heng on 3/2/16.
 */
public class Utility {

    static public boolean isNetworkAvailable(Context c){
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
    }
}
