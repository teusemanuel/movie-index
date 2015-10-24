package br.com.moviecreator.proxies.listeners;

import com.android.volley.VolleyError;

import java.io.Serializable;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public interface ProxyListener<T> extends Serializable {
    void onStart();

    void onSuccess(T response);

    void onFailure(VolleyError error);

    void onComplete();
}
