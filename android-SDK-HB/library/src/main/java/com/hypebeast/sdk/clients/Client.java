package com.hypebeast.sdk.clients;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.hypebeast.sdk.R;
import com.hypebeast.sdk.api.exception.ErrorHandlerResponseCode;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by hesk on 2/7/15.
 */
public abstract class Client {
    public final static String LOG_TAG = "hbsdk.log";
    protected Gson gsonsetup;
    protected final ErrorHandler handlerError = new ErrorHandlerResponseCode();
    protected Context context;
    protected boolean cache_supported;
    protected Cache mCache;
    /**
     * Rest adapter
     */
    protected RestAdapter mAdapter;

    protected abstract void registerAdapter();

    protected abstract String get_USER_AGENT();

    protected abstract void jsonCreate();

    protected void createInterfaces() {
    }

    protected RequestInterceptor getIn() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", get_USER_AGENT());
                request.addHeader("Accept", "application/json");
            }
        };
    }


    public Client(Context context) {
        cache_supported = false;
        this.context = context;
        jsonCreate();
        registerAdapter();
        createInterfaces();
    }


    @Deprecated
    public Client() {
        cache_supported = false;
        jsonCreate();
        registerAdapter();
        createInterfaces();
    }


    protected void buildCompletCacheRestAdapter(
            String base_end_point,
            Context context,
            @Nullable RestAdapter.LogLevel how_to_log) {

        final RestAdapter.Builder restbuilder = new RestAdapter.Builder()
                .setEndpoint(base_end_point)
                .setLogLevel(how_to_log == null ? RestAdapter.LogLevel.HEADERS : how_to_log)
                .setErrorHandler(handlerError)
                .setRequestInterceptor(getIn())
                .setConverter(new GsonConverter(gsonsetup));

        try {
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            mCache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            // Create OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setCache(mCache);
            okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);

           /* okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "auth-token")
                            .method(original.method(), original.body())
                            .build();
                    Response response = chain.proceed(request);
                    // Customize or return the response
                    return response;
                }
            });
            */
            mAdapter = restbuilder
                    .setClient(new OkClient(okHttpClient))
                    .build();
            cache_supported = true;
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
            //no cache file system is supported
            mAdapter = restbuilder.build();
            cache_supported = false;
        }
    }

    public void removeAllCache() {
        try {
            Iterator<String> it = mCache.urls();
            while (it.hasNext()) {
                String next = it.next();
                it.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFromCache(String urlString) {
        try {
            Iterator<String> it = mCache.urls();
            while (it.hasNext()) {
                String next = it.next();
                if (next.contains(urlString)) {
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
