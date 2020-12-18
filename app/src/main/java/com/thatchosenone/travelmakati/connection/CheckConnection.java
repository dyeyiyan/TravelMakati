package com.thatchosenone.travelmakati.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {

    public static boolean isConnected(Context context)
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null)
            {
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                    if(info != null)
                        {
                            if(info.getState() == NetworkInfo.State.CONNECTED)
                            {
                                return true;
                            }
                        }
            }
            return false;
        }

}
