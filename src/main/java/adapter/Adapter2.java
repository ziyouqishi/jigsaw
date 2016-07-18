package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhimei.jiasaw.R;

import java.util.ArrayList;

import tools.ItemBean;

/**
 * Created by 张佳亮 on 2016/7/16.
 */

public class Adapter2 extends BaseAdapter {
    private Context context;
    private ArrayList<ItemBean> data;
    private LayoutInflater inflater;
    private int parantViewHeight;
    private int type;
    public Adapter2(Context context,ArrayList<ItemBean> data2,int height,int type) {
        super();
        this.context=context;
        this.data=data2;
        this.parantViewHeight=height;
        this.type=type;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View covertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(covertView==null){
            holder=new ViewHolder();
            covertView=inflater.inflate(R.layout.item_pintu,null);
            holder.imageView=(ImageView)covertView.findViewById(R.id.image);
            covertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)covertView.getTag();
        }
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                parantViewHeight/type-10));

        holder.imageView.setImageBitmap(data.get(position).getmBitmap());
        return covertView;
    }


   public final class ViewHolder{
        public ImageView imageView;

    }
}
