package suzhou.dataup.cn.myapplication.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import suzhou.dataup.cn.myapplication.R;
import suzhou.dataup.cn.myapplication.adputer.Myadputer;
import suzhou.dataup.cn.myapplication.base.BaseFragment;
import suzhou.dataup.cn.myapplication.bean.HomeResoutBean;
import suzhou.dataup.cn.myapplication.callback.LodeMoreCallBack;
import suzhou.dataup.cn.myapplication.callback.MyHttpCallBcak;
import suzhou.dataup.cn.myapplication.constance.CountUri;
import suzhou.dataup.cn.myapplication.listener.RecyclerViewOnScroll;
import suzhou.dataup.cn.myapplication.mangers.OkHttpClientManager;
import suzhou.dataup.cn.myapplication.utiles.LogUtil;
import suzhou.dataup.cn.myapplication.utiles.SwipContainerUtiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WealFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WealFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 福利的界面
 */
public class WealFragment extends BaseFragment implements LodeMoreCallBack {
    int lastVisibleItem = 0;
    int index = 1;
    int temp = 0;
    @InjectView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    Myadputer mMyadputer;
    List<HomeResoutBean.ResultsEntity> mResultsEntityList = new ArrayList<>();
    GridLayoutManager mLayoutManager;//创建一个瀑布流的布局
    boolean isFirstLoda = true;
    @InjectView(R.id.load_more_pb)
    ProgressBar mLoadMorePb;
    @InjectView(R.id.load_more_tv)
    TextView mLoadMoreTv;
    @InjectView(R.id.footer_linearlayout)
    LinearLayout mFooterLinearlayout;

    public WealFragment() {
        super(R.layout.fragment_weal);
    }

    @Override
    protected void initHead() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initContent() {
        // 创建一个线性布局管理器
//        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //这里可以指定他的方式
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);//设置线性的管理器！
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
        mMyadputer = new Myadputer(mResultsEntityList, options_base, mLayoutUtil);
        //设置刷新时的不同的颜色！
        mSwipeContainer.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //google官方的下拉刷新！
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastVisibleItem = 0;
                isFirstLoda = true;
                mResultsEntityList.clear();
                index = 1;
                getData(index);
            }
        });
        //监听recyclerView的上滑动的位置来进行积蓄的加载更多的数据
        recyclerView.addOnScrollListener(new RecyclerViewOnScroll(mMyadputer, this));
    }

    @Override
    protected void initLocation() {
        //利用反射进行设置自动刷新！
        SwipContainerUtiles.setRefreshing(mSwipeContainer, true, true);
    }


    @Override
    protected void initLogic() {
    }

    @Override
    protected void isShow() {

    }

    @Override
    protected void isGone() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("xioahuile");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //获取福利的数据
    private void getData(int index) {
        OkHttpClientManager.get(CountUri.BASE_URI + "/福利/10/" + index + "", new MyHttpCallBcak() {
            @Override
            public void onFailure(Request request, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeContainer.setRefreshing(false);//刷新完毕!
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    if (response != null) {
                        HomeResoutBean homeResoutBean = mGson.fromJson(response.body().string(), HomeResoutBean.class);
                        List<HomeResoutBean.ResultsEntity> results = homeResoutBean.results;
                        for (HomeResoutBean.ResultsEntity result : results) {
                            mResultsEntityList.add(result);
                        }
                        if (homeResoutBean.results.size() == 0) {
                            lastVisibleItem = 0;
                            return;
                        }
                        if (isFirstLoda) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mFooterLinearlayout.setVisibility(View.GONE);
                                    mSwipeContainer.setRefreshing(false);//刷新完毕!

                                    recyclerView.setAdapter(mMyadputer);
                                    //目前不知道什么原因倒置刷新之后头部下移。设置刷新完毕之后直接移动到首个postion
                                    recyclerView.scrollToPosition(0);
                                    isFirstLoda = false;
                                    LogUtil.e("xiala刷新了！" + recyclerView.getChildCount());
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mFooterLinearlayout.setVisibility(View.GONE);
                                    mSwipeContainer.setRefreshing(false);//刷新完毕!
                                    mMyadputer.notifyDataSetChanged();
                                    LogUtil.e("dibu刷新了！");
                                }
                            });

                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "获取服务器数据失败。。。", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    //监听加载更多
    @Override
    public void LodeMore() {
        //请求数据
        index++;
        getData(index);
        mFooterLinearlayout.setVisibility(View.VISIBLE);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
