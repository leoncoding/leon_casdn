package suzhou.dataup.cn.myapplication.utiles;

import com.google.gson.Gson;

/**
 * Author:  liangjin.bai
 * Email: bailiangjin@gmail.com
 * Create Time: 2015/9/22 14:21
 */
public class GsonUtils {

    private volatile static GsonUtils instance;
    private Gson gson;

    public static GsonUtils getInstance() {
        if (instance == null) {
            synchronized (GsonUtils.class) {
                if (instance == null) {
                    instance = new GsonUtils();
                }
            }
        }
        return instance;
    }

    private GsonUtils() {
        this.gson = new Gson();

    }

    public <T> T toObj(String json, Class<T> T) {
        return gson.fromJson(json, T);
    }

    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public Gson getGson() {
        return gson;
    }


}
