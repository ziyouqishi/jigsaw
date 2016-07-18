package tools;

import com.zhimei.jiasaw.PuzzleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张佳亮 on 2016/7/16.
 */

public class GameUtil {
    public static ArrayList<ItemBean> mItemBeans=new ArrayList<>();
    public static ItemBean mBlackItemBean;


    /**
     * 生成随机的Item。
     */
    public static void getPuzzleGenerator(){
        int index=0;
        for(int i=0;i<mItemBeans.size();i++){
            index=(int)(Math.random()*PuzzleActivity.TYPE*PuzzleActivity.TYPE);
            swapItems(mItemBeans.get(index),GameUtil.mBlackItemBean);
        }

        List<Integer>data=new ArrayList<>();
        for(int i=0;i<mItemBeans.size();i++){
            data.add(mItemBeans.get(i).getmBitmapId());
        }

        if(canSolve(data)){
            return;
        }else {
            getPuzzleGenerator();
        }
    }

    /**
     * 交换空格与点击Item的位置
     * @param from 交换图
     * @param blank 空白图
     */
    public static void swapItems(ItemBean from,ItemBean blank){
        ItemBean tempItemBean=new ItemBean();
        /**
         * 交换bitmapId
         */
        tempItemBean.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(tempItemBean.getmBitmapId());
        /**
         * 交换bitmap
         */
        tempItemBean.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(tempItemBean.getmBitmap());

        GameUtil.mBlackItemBean=from;

    }

    /**
     * 该数据是否有解
     * @param data
     * @return
     */
    public static boolean canSolve(List<Integer> data){
        int blankId=GameUtil.mBlackItemBean.getmItemId();
        if(data.size()%2==1){
            return getInversions(data)%2==0;
        }else{
            if(((blankId-1)/ PuzzleActivity.TYPE)%2==1){
                return getInversions(data)%2==0;
            }else {
                return getInversions(data)%2==1;
            }
        }

    }


    /**
     * 计算倒置和算法
     * @param data 拼图数组数据
     * @return 该序列的倒置和
     */
    public static int getInversions(List<Integer>data){
        int inversions=0;
        int inversionCount=0;

        for(int i=0;i<data.size();i++){
            for(int j=i+1;j<data.size();j++){
                int index=data.get(i);
                if(data.get(j)!=0&&data.get(j)<index){
                    inversionCount++;
                }
            }

            inversions+=inversionCount;
            inversionCount=0;
        }
        return inversions;
    }


    /**
     * 判断点击的Item是否可以移动
     * @param position
     * @return
     */
    public static boolean isMoveable(int position){
        int type=PuzzleActivity.TYPE;
        int blankId=GameUtil.mBlackItemBean.getmItemId()-1;
        if(Math.abs(blankId-position)==type){
            return  true;
        }

        if((blankId/type==position/type)&&Math.abs(blankId-position)==1){
            return  true;
        }

        return false;
    }

    /**
     * 判断是否拼图成功
     * @return
     */
    public static boolean isSuccess(){
        for(ItemBean tempBean:GameUtil.mItemBeans){
            if(tempBean.getmBitmapId()!=0&&(tempBean.getmItemId())==tempBean.getmBitmapId()){
                continue;
            }else if(tempBean.getmBitmapId()==0 && tempBean.getmItemId()==PuzzleActivity.TYPE *PuzzleActivity.TYPE){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }
}
