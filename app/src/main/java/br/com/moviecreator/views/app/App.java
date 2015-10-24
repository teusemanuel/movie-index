package br.com.moviecreator.views.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.moviecreator.proxies.AbstractProxy;
import br.com.moviecreator.utils.LruBitmapCache;
import br.com.moviecreator.utils.ToastUtils;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public class App extends Application {

    private static final String AUTO_ALERTA_PREFERENCES = "br.com.moviecreator";

    private static App application;

    private LruBitmapCache lruBitmapCache;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Handler mAlertHandler;

    private List<AbstractProxy> proxies;
    private Gson gson;

    private Bundle addressBundle;

    public static App getApplication() {
        return App.application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        App.application = this;
    }

    public Bundle getAddressBundle() {
        return addressBundle;
    }

    public void setAddressBundle(Bundle addressBundle) {
        this.addressBundle = addressBundle;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
        }

        return gson;
    }

    private void manageData(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    public String getUUID() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        return manager.getDeviceId();
    }

    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(AUTO_ALERTA_PREFERENCES, Context.MODE_PRIVATE);
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    private LruBitmapCache getLruBitmapCache() {
        if (lruBitmapCache == null) {
            lruBitmapCache = new LruBitmapCache();
        }

        return lruBitmapCache;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue, getLruBitmapCache());
        }

        return this.imageLoader;
    }

    private List<AbstractProxy> getProxies() {
        if (proxies == null) {
            proxies = new ArrayList<>();
        }

        return proxies;
    }

    public <T> void addToRequestQueue(final AbstractProxy proxy, final View view, final Request<T> request, final String tag) {
        if (isOnline()) {
            request.setTag(tag);
            proxy.setTag(tag);

            getProxies().add(proxy);
            getRequestQueue().add(request);
        } else {
            Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Try again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToRequestQueue(proxy, view, request, tag);
                        }
                    }).show();
        }
    }

    public void cancelRequests(String tag) {
        if (tag == null) {
            return;
        }

        getRequestQueue().cancelAll(tag);

        List<AbstractProxy> proxies = new ArrayList<>();

        proxies.addAll(getProxies());

        for (AbstractProxy proxy : proxies) {
            if (proxy.getTag().equals(tag)) {
                removeProxy(proxy);
            }
        }
    }

    public void removeProxy(AbstractProxy proxy) {
        proxy.destroy();

        getProxies().remove(proxy);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


}