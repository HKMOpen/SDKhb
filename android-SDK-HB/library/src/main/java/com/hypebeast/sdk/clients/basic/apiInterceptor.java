package com.hypebeast.sdk.clients.basic;

import android.os.Build;

import com.hypebeast.sdk.Util.Connectivity;
import com.hypebeast.sdk.Util.CookieHanger;

import retrofit.RequestInterceptor;

/**
 * Created by hesk on 3/3/16.
 */
public class apiInterceptor implements RequestInterceptor {
    private static final String USER_AGENT = "HypebeastStoreApp/1.0 Android " + Build.VERSION.SDK_INT;
    private CookieHanger mCookieHanger;
    private int cache_min = 1;
    private String api_version = "", countryCode = "";

    public void setCookieClient(CookieHanger cookie) {
        // synchronized (mCookieHanger) {
        mCookieHanger = cookie;
        // }
    }

    public void setAPIVersion(String text) {
        api_version = text;
    }

    public void setCountryCode(String code) {
        countryCode = code;
    }

    public void setCacheMinutes(int min) {
        cache_min = min;
    }

    // tolerate 4-weeks stale
    public static int timeByWeeks(int d) {
        int maxStale = 60 * 60 * 24 * 7 * d;
        return maxStale;
    }

    // read from cache for 1 minute
    public static int timeByMins(int m) {
        int maxStale = 60 * m;
        return maxStale;
    }

    public void removeAllParams() {
        api_version = "";
        countryCode = "";
        cache_min = 0;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept", "application/json");
        if (cache_min > 0) {
            request.addHeader("Cache-Control", "public, max-age=" + timeByMins(cache_min));
        }
        if (mCookieHanger != null) {
            if (!mCookieHanger.getRaw().isEmpty()) {
                request.addHeader("Cookie", mCookieHanger.getRaw());
            }
        }
        if (!api_version.isEmpty()) {
            request.addQueryParam("version", api_version);
        }

        if (!countryCode.isEmpty()) {
            request.addQueryParam("catalog_country", countryCode);
        }
        //request.addQueryParam("platform", "android");
    }

}
