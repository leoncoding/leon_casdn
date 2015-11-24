package suzhou.dataup.cn.myapplication.adputer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import suzhou.dataup.cn.myapplication.R;
import suzhou.dataup.cn.myapplication.bean.BlogTagBean;
import suzhou.dataup.cn.myapplication.utiles.ToastUtils;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MyCardViewAdapter extends RecyclerView.Adapter<MyCardViewAdapter.ViewHolder>{

    private List<BlogTagBean> list;

    private Context mContext;

    public MyCardViewAdapter( Context context , List<BlogTagBean> list)
    {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        // 给ViewHolder设置布局文件
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_view, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        BlogTagBean p = list.get(position);
        holder.mTextView.setText(p.getName().toString());
        holder.bind(list.get(position));
    }


    @Override
    public int getItemCount()
    {
        // 返回数据总数
        return list == null ? 0 : list.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView;

        public CheckBox checkBox;

        public ViewHolder(View v )
        {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            checkBox = (CheckBox) v.findViewById(R.id.my_checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        ToastUtils.longShow(((BlogTagBean)checkBox.getTag()).getName());
                        listener.checked(((BlogTagBean)checkBox.getTag()).getName());
                    }
                }
            });
        }

        public void bind(BlogTagBean bean){
            checkBox.setTag(bean);
        }
    }
    static CheckBoxListener listener;
    public void setListener(CheckBoxListener listener){
        this.listener = listener;
    }

    public interface CheckBoxListener{
        public void checked(String name);
    }
}
