package tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 张佳亮 on 2016/7/13.
 */

public class ScreenUtil {

    public static final int WIDTH=0;
    public static final int HEIGHT=1;

    /**
     * 获取屏幕相关参数
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return  metrics;
    }

    /**
     *
     * @param context
     * @return 屏幕density
     */

    public static float getDeviceDensity(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 通过传入的参数类型得到屏幕的宽度或者高度
     * @param context
     * @param type
     * @return
     */

    public int getScreenParams(Context context,int type){
        WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width=outMetrics.widthPixels;
        int height=outMetrics.heightPixels;
        if(type==ScreenUtil.HEIGHT){
            return height;
        }else if(type==ScreenUtil.WIDTH){
            return  width;
        }else{
            return  0;
        }

    }
}
