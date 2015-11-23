package suzhou.dataup.cn.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import suzhou.dataup.cn.myapplication.R;
import suzhou.dataup.cn.myapplication.base.BaseActivity;
import suzhou.dataup.cn.myapplication.constance.ConstanceData;
import suzhou.dataup.cn.myapplication.utiles.LogUtil;

//详情的界面
public class CountActivity extends BaseActivity {

    @InjectView(R.id.toolbars)
    Toolbar mToolbars;
    @InjectView(R.id.webview)
    WebView mWebview;
    @InjectView(R.id.myProgressBar)
    ProgressBar mMyProgressBar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public CountActivity() {
        super(R.layout.activity_count);
    }

    @Override
    protected void initHead() {
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        mToolbars.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        //  webSettings.setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; zh-tw) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16");
        mWebview.requestFocusFromTouch();
    }
    @Override
    protected void initContent() {
        String count = getIntent().getStringExtra(ConstanceData.COUNT_URI);
        String[] split = count.split(",");
        //当添加视察的动画的时候不要用toobal设置标题了！
        mCollapsingToolbar.setTitle(split[1]);
        mWebview.loadUrl(split[0]);
    }
    @Override
    protected void initLocation() {
        //监听返回键
        mToolbars.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                } else {
                    CountActivity.this.onBackPressed();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebview.getClass().getMethod("onPause").invoke(mWebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initLogic() {
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                showToast(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                mMyProgressBar.setMax(100);
                if (progress == 100) {
                    mMyProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == mMyProgressBar.getVisibility()) {
                        mMyProgressBar.setVisibility(View.VISIBLE);
                    }
                    mMyProgressBar.setProgress(progress);
                }
                LogUtil.e("加载了" + progress);
            }
        });
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CountActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {

            mWebview.goBack();
            return true;
        } else {

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);

    }

}

