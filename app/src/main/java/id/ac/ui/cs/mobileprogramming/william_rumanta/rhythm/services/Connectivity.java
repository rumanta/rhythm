package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

public class Connectivity {
    private static ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    public Connectivity(final Context ctx) {
        connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Toast.makeText(ctx, "Internet connection detected", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onLost(Network network) {
                Toast.makeText(ctx, "Lost internet connection", Toast.LENGTH_LONG)
                        .show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    public static boolean isConnectedToInternet() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
