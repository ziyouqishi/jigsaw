package tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.zhimei.jiasaw.PuzzleActivity;
import com.zhimei.jiasaw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张佳亮 on 2016/7/16.
 */

public class ImagesUtil {
    public ItemBean itemBean;

    public void createBitmaps(int type, Bitmap picSelected, Context context){
        Bitmap bitmap=null;
        List<Bitmap>bitmapItems=new ArrayList<>();

        int itemWidth=picSelected.getWidth()/type;
        int itemHeight=picSelected.getHeight()/type;

        for(int i=1;i<=type;i++){
            for(int j=1;j<=type;j++){
                bitmap=Bitmap.createBitmap(picSelected,(j-1)*itemWidth,(i-1)*itemHeight,itemWidth,itemHeight);
                bitmapItems.add(bitmap);
                itemBean=new ItemBean((i-1)*type+j,(i-1)*type+j,bitmap);
                GameUtil.mItemBeans.add(itemBean);
            }
        }

        PuzzleActivity.mLastBitmap=bitmapItems.get(type*type-1);
        bitmapItems.remove(type*type-1);
        GameUtil.mItemBeans.remove(type*type-1);

        Bitmap blackBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.link);
        blackBitmap=resizeBitmap(itemWidth,itemHeight,blackBitmap);

        blackBitmap=Bitmap.createBitmap(blackBitmap,0,0,itemWidth,itemHeight);

        bitmapItems.add(blackBitmap);
        GameUtil.mItemBeans.add(new ItemBean(type*type,0,blackBitmap));
        GameUtil.mBlackItemBean=GameUtil.mItemBeans.get(type*type-1);



    }

    /**
     * 处理图片 放大缩小到合适的位置
     * @param newWidth 缩放后的width
     * @param newHeight 缩放后的height
     * @param bitmap
     * @return
     */


    public Bitmap resizeBitmap(float newWidth,float newHeight,Bitmap bitmap){
        Matrix matrix=new Matrix();

        matrix.postScale(newWidth/bitmap.getWidth(),newHeight/bitmap.getHeight());

        Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return newBitmap;
    }





}
