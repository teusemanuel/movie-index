package br.com.moviecreator.proxies.requests;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import br.com.moviecreator.utils.LogUtils;
import br.com.moviecreator.views.app.App;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public class GsonRequest<T> extends Request<T> {

    private static final String TAG = GsonRequest.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT_MS = 60 * 2 * 1000;

    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    private final Object body;
    private final Map<String, String> params;
    private final String rawUrl;

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> params, Object body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.rawUrl = url;
        this.clazz = clazz;
        this.params = params;
        this.body = body;
        this.listener = listener;

        setRetryPolicy(new DefaultRetryPolicy(GsonRequest.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String getUrl() {
        String url = super.getUrl();

        if (getMethod() != Method.GET && getMethod() != Method.DELETE) {
            LogUtils.logDebug(TAG, String.format("[%s] %s", getMethodString(getMethod()), url));

            return url;
        }

        try {
            Map<String, String> params = getParams();

            if (params == null) {
                LogUtils.logDebug(TAG, String.format("[%s] %s", getMethodString(getMethod()), url));

                return url;
            }

            StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }

                try {
                    builder.append(String.format("%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    builder.append(String.format("%s=%s", entry.getKey(), entry.getValue().replace(" ", "+")));
                }
            }

            if (builder.length() > 0) {
                url = String.format("%s?%s", url, builder.toString());
            }
        } catch (AuthFailureError authFailureError) {
            LogUtils.logError(TAG, authFailureError);
        }

        LogUtils.logDebug(TAG, String.format("[%s] %s", getMethodString(getMethod()), url));

        return url;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = this.params != null ? this.params : super.getParams();

        if (params != null) {
            LogUtils.logDebug(TAG, params.toString());
        }

        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        if (response != null) {
            LogUtils.logDebug(TAG, response.toString());
        }

        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            LogUtils.logDebug(TAG, json);

            return Response.success(App.getApplication().getGson().fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? App.getApplication().getGson().toJson(body).getBytes() : super.getBody();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    private String getMethodString(int methodRequest) {
        switch (methodRequest) {
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            default:
                return "";
        }
    }
}
