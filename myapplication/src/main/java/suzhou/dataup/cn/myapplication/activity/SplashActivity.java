package suzhou.dataup.cn.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import suzhou.dataup.cn.myapplication.MainActivity;
import suzhou.dataup.cn.myapplication.R;
import suzhou.dataup.cn.myapplication.adputer.MyCardViewAdapter;
import suzhou.dataup.cn.myapplication.bean.BlogTagBean;
import suzhou.dataup.cn.myapplication.callback.MyHttpCallBcak;
import suzhou.dataup.cn.myapplication.config.SysConfig;
import suzhou.dataup.cn.myapplication.mangers.OkHttpClientManager;

public class SplashActivity extends Activity implements MyCardViewAdapter.CheckBoxListener {

    String json ;
    private List<BlogTagBean> list = new ArrayList<BlogTagBean>();
    private ArrayList<String> taglist = new ArrayList<String>();

    private RecyclerView mRecylerView;
    Button bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
        initView();
    }


    private void initView(){
        mRecylerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // 创建一个线性布局管理器
       // final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecylerView.setLayoutManager(mLayoutManager);//设置线性的管理器！
        // 设置ItemAnimator
        mRecylerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        mRecylerView.setHasFixedSize(true);
        MyCardViewAdapter mAdapter = new MyCardViewAdapter(this,list);
        mAdapter.setListener(this);
        mRecylerView.setAdapter(mAdapter);
        bn =  (Button)findViewById(R.id.complete);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("list",taglist);
                startActivity(intent);
            }
        });
    }

    private void initData(){

        String s =getFromAssets("tags.json");

        try {
            JSONObject object = new JSONObject(s);
            JSONArray array = object.optJSONArray("tag");
            for(int i =0;i<array.length();i++){
                JSONObject jsonObject = array.optJSONObject(i);
                BlogTagBean bean = new Gson().fromJson(jsonObject.toString(),BlogTagBean.class);
                list.add(bean);
                Log.e("splash",bean.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getFromAssets(String fileName){
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[]  buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");//你的文件的编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void checked(String name) {
        taglist.add(name);
    }
}

