package com.hypebeast.sdk.application.popbee;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.webkit.WebResourceResponse;

import com.hypebeast.sdk.Constants;
import com.hypebeast.sdk.Util.CacheManager;
import com.hypebeast.sdk.Util.UrlCache;
import com.hypebeast.sdk.application.ApplicationBase;
import com.hypebeast.sdk.clients.PBEditorialClient;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bolts.CancellationToken;
import bolts.CancellationTokenSource;
import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

import static com.hypebeast.sdk.Constants.*;

/**
 * Created by hesk on 4/2/16.
 */
public class PopbeeMainApp extends ApplicationBase {

    private static final String ACCESS_FILE_URL = "http://popbee.com/wp-content/themes/popbee-v6/app/main.css";
    public static final String local_css_file_name = "pb.css";

    private PBEditorialClient client;
    public static PopbeeMainApp instance;
    private com.hypebeast.sdk.application.popbee.sync mListener;

    public static PopbeeMainApp with(Application app, sync mListener) {
        if (instance == null) {
            instance = new PopbeeMainApp(app, mListener);
            instance.init();
        } else {
            instance.addInterface(mListener);
            instance.init();
        }
        return instance;
    }


    public static PopbeeMainApp getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("please init a new instance. or go to the slash screen again");
        }
        return instance;
    }


    public PopbeeMainApp(Application app, sync mListener) {
        super(app);
        client = PBEditorialClient.getInstance(app);
        //client.setLanguageBase(HBEditorialClient.BASE_EN);
        //mOverheadRequest = client.createOverHead();
        //request_login = client.createAuthenticationHBX();
        addInterface(mListener);
    }

    private void addInterface(sync listenerSync) {
        mListener = listenerSync;
    }

    protected void init() {
        super.init();
        CancellationTokenSource cts = new CancellationTokenSource();
        getIntAsync(cts.getToken()).continueWithTask(new Continuation<String, Task<Void>>() {
            @Override
            public Task<Void> then(Task<String> task) throws Exception {
                ArrayList<Task<Void>> tasks = new ArrayList<Task<Void>>();
                tasks.add(setCssFile());
                return Task.whenAll(tasks);
            }
        }).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> ignored) throws Exception {
                // Every comment was deleted.
                mListener.syncDone(PopbeeMainApp.this, "done");
                return null;
            }
        });

    }

    @Override
    protected void removeAllData() {
        CacheManager.trimCache(app);
        saveInfo(PREFERENCE_FOUNDATION_FILE_CONTENT, "");
        saveInfo(PREFERENCE_BRAND_LIST, "");
        saveInfo(PREFERENCE_FOUNDATION_REGISTRATION, "");
        client.removeAllCache();
    }


    /*
    blocking tasking in here
     */
    public Task<Void> setCssFile() {
        TaskCompletionSource<Void> successful = new TaskCompletionSource<>();
        StringWriter writer = new StringWriter();
        final String root = Environment.getExternalStorageDirectory().toString() + File.separator;
        final String path_final = root + APP_FOLDER_NAME;
        final File myDir = new File(path_final);
        UrlCache mUrlCache = new UrlCache(app, myDir);
        mUrlCache.register(ACCESS_FILE_URL, local_css_file_name, "text/css", "UTF-8", 5 * UrlCache.ONE_DAY);
        //  SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(app);
        //  cssLoader = new LoadCacheCssN(mUrlCache, PreferenceManager.getDefaultSharedPreferences(app));
        saveInfo(APP_CSS_FILE_PATH, path_final);
        if (loadRef(APP_CSS_FILE_PATH).equalsIgnoreCase(EMPTY_FIELD)) {
            WebResourceResponse loadedcontent = mUrlCache.load(ACCESS_FILE_URL);
            if (loadedcontent != null) {
                try {
                    IOUtils.copy(loadedcontent.getData(), writer, "UTF-8");
                    saveInfo(PREFERENCE_CSS_FILE_CONTENT, writer.toString());
                    saveInfo(APP_CSS_FILE_PATH, path_final);
                } catch (IOException e) {
                    successful.setError(e);
                }
            }
        }

        successful.setResult(null);
        return successful.getTask();
    }

    public Task<String> getIntAsync(final CancellationToken ct) {
        // Create a new Task
        final TaskCompletionSource<String> tcs = new TaskCompletionSource<>();
        new Thread() {
            @Override
            public void run() {
                // Check if cancelled at start
                if (ct.isCancellationRequested()) {
                    tcs.setCancelled();
                    return;
                }

                int result = 0;
                while (result < 100) {
                    // Poll isCancellationRequested in a loop
                    if (ct.isCancellationRequested()) {
                        tcs.setCancelled();
                        return;
                    }
                    result++;
                }
                tcs.setResult("done");
            }
        }.start();

        return tcs.getTask();
    }

}
