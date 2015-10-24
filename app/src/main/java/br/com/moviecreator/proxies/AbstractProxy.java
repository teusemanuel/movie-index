package br.com.moviecreator.proxies;

import android.app.Activity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;

import br.com.moviecreator.proxies.listeners.ProxyListener;
import br.com.moviecreator.proxies.requests.GsonRequest;
import br.com.moviecreator.utils.LogUtils;
import br.com.moviecreator.views.app.App;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public abstract class AbstractProxy {
    private WeakReference<Activity> activityRef;
    private String tag;

    protected AbstractProxy(Activity activity) {
        this.activityRef = new WeakReference<>(activity);
    }

    public void destroy() {
        this.activityRef.clear();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    void createAndScheduleGsonRequest(String tag, int method, String url, Class clazz, Object body, Map<String, String> params, final ProxyListener proxyListener, View view) {
        Activity activity = activityRef.get();

        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    proxyListener.onStart();
                }
            });
        }

        App.getApplication().addToRequestQueue(this, view, new GsonRequest<>(method, url, clazz, params, body, new Response.Listener<Object>() {

            @Override
            public void onResponse(final Object response) {
                Activity activity = activityRef.get();

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                proxyListener.onSuccess(response);
                                proxyListener.onComplete();
                            } catch (Exception e) {
                                LogUtils.logError("AbstractProxy", e);
                            }
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Activity activity = activityRef.get();

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                proxyListener.onFailure(error);
                                proxyListener.onComplete();
                            } catch (Exception e) {
                                LogUtils.logError("AbstractProxy", e);
                            }
                        }
                    });
                }
            }
        }), tag);
    }

}
