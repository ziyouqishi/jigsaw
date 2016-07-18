package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhimei.jiasaw.R;

import java.util.ArrayList;

import tools.ScreenUtil;

/**
 * Created by 张佳亮 on 2016/7/13.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bitmap> picList;
    private LayoutInflater inflater;
    public GridViewAdapter(Context context, ArrayList<Bitmap>datas) {
        super();
        this.context=context;
        this.picList=datas;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder=null;
        int density=(int) ScreenUtil.getDeviceDensity(context);

        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_pintu,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }

       /* if(convertView==null){
            iv_pic_item=new ImageView(context);
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(80*density,100*density));
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            iv_pic_item=(ImageView)convertView;
        }*/

        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(80*density,100*density));
        holder.imageView.setBackgroundColor(Color.BLACK);
        holder.imageView.setImageBitmap(picList.get(position));
        return convertView;
    }

    public final class ViewHolder{
        public ImageView imageView;

    }
}
