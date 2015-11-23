package suzhou.dataup.cn.myapplication.mangers;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import suzhou.dataup.cn.myapplication.callback.MyHttpCallBcak;

/**
 * 作者：liujingyuan on 2015/10/25 20:07
 * 邮箱：906514731@qq.com
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Handler mDelivery;


    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    //封装get请求
    public static void get(String uri, final MyHttpCallBcak mMyHttpCallBcak) {
        OkHttpClientManager instance = OkHttpClientManager.getInstance();
        //创建okHttpClient对象
        //创建一个Request
        final Request request = new Request.Builder()
                .url(uri)
                .build();
        //new call
        final Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mMyHttpCallBcak.onFailure(request, e);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                mMyHttpCallBcak.onResponse(response);
            }
        });
    }

    //封装post请求
    public static void post(String uri, HashMap mHashMap, final MyHttpCallBcak mMyHttpCallBcak) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Iterator iter = mHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            builder.add(key + "", val + "");
        }
        //创建一个Request
        final Request request = new Request.Builder()
                .url(uri)
                .post(builder.build())
                .build();
        //new call
        final Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                //直接运行在主线程！
                mMyHttpCallBcak.onFailure(request, e);
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                mMyHttpCallBcak.onResponse(response);
            }
        });
    }

}