package br.com.moviecreator.utils;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public final class LogUtils {

    private LogUtils() {
    }

    public static void logInfo(String tag, String message) {
//        Log.i(tag, message);
    }

    public static void logDebug(String tag, String message) {
//        Log.d(tag, message);
    }

    public static void logWarn(String tag, String message) {
//        Log.w(tag, message);
    }

    public static void logError(String tag, String message) {
//        Log.e(tag, message);
    }

    public static void logError(String tag, Exception exception) {
//        LogUtils.logError(tag, exception.getLocalizedMessage());
    }
}
