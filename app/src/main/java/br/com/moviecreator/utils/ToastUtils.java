package br.com.moviecreator.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public final class ToastUtils {

    private ToastUtils() {
    }

    public static void toastInfo(Context context, String message, int length, String tag) {
        Toast.makeText(context, message, length).show();

        LogUtils.logInfo(tag, message);
    }
}