package com.zhimei.jiasaw;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.util.Timer;
import java.util.TimerTask;

import adapter.Adapter2;
import tools.GameUtil;
import tools.ImagesUtil;
import tools.PictureDeal;

public class PuzzleActivity extends AppCompatActivity {
    public static Bitmap mLastBitmap;
    public static int TYPE=2;
    private Bitmap oriBitmap;
    private int selectedId;

    private GridView gridView;
    private Adapter2 adapter;
    private PictureDeal picturedeal=new PictureDeal();
    private TextView step,time;
    private  int count=0;
    private Timer mTimer;
    private TimerTask timerTask;
    private boolean isFirst=true;
    private boolean isSuccess=false;
    private ImageView imageView;
    private RippleView back,resetting,origin;
    private SoundPool soundPool;
    private int soundId;

    private Handler handler=new Handler(){
        int timeCount=0;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                timeCount++;
                time.setText(timeCount+" 秒");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        getData();
        initView();
        playSound();

    }
    void getData(){
        Intent intent=getIntent();
        TYPE=intent.getIntExtra("mType",2);
        String path;
        path=intent.getStringExtra("mPicPath");
        if(path!=null){
            oriBitmap= BitmapFactory.decodeFile(path);
        }else {
            selectedId=intent.getIntExtra("picSelectedID",R.drawable.three);
           // oriBitmap=BitmapFactory.decodeResource(getResources(),selectedId);
            oriBitmap= picturedeal.readBitmap(this,selectedId);

        }

        new ImagesUtil().createBitmaps(TYPE,oriBitmap,this);
        GameUtil.getPuzzleGenerator();
    }

    void initView(){
        imageView=(ImageView) findViewById(R.id.originImage);
        gridView=(GridView) findViewById(R.id.main_detail);
        step=(TextView)findViewById(R.id.step);
        time=(TextView)findViewById(R.id.time) ;
        gridView.setNumColumns(TYPE);

        gridView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("liang", "run: "+gridView.getHeight());
                adapter=new Adapter2(PuzzleActivity.this, GameUtil.mItemBeans,gridView.getHeight(),TYPE);
                gridView.setAdapter(adapter);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 开始计时
                 */
                if(isFirst){
                    startTime();
                    isFirst=false;
                }
                /**
                 * 判断点击的图片是否可以移动，
                 * 每次移动后判断是否拼图成功。
                 */
                if(GameUtil.isMoveable(position)){
                    GameUtil.swapItems(GameUtil.mItemBeans.get(position),GameUtil.mBlackItemBean);
                    adapter.notifyDataSetChanged();
                    count++;
                    step.setText(count+"");
                    if(GameUtil.isSuccess()){
                       GameUtil.mItemBeans.get(TYPE*TYPE-1).setmBitmap(PuzzleActivity.mLastBitmap);
                        Toast.makeText(PuzzleActivity.this, "拼图成功", Toast.LENGTH_SHORT).show();
                        gridView.setFocusable(false);
                        gridView.setEnabled(false);
                       if(mTimer!=null){
                           mTimer.cancel();
                       }

                        isSuccess=true;
                    }
                }
            }
        });



        back=(RippleView)findViewById(R.id.back);
        resetting=(RippleView)findViewById(R.id.reseting) ;
        origin=(RippleView)findViewById(R.id.origin);
        back.setRippleDuration(100);
        resetting.setRippleDuration(300);
        origin.setRippleDuration(300);
        back.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                PuzzleActivity.this.finish();
            }
        });

        resetting.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if(gridView.getVisibility()==View.VISIBLE){
                    if(!isSuccess){
                        GameUtil.getPuzzleGenerator();
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    imageView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }

            }
        });

        origin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                showImage();
            }
        });


    }

    void showImage(){
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(oriBitmap);
        gridView.setVisibility(View.GONE);
        ObjectAnimator animator1=ObjectAnimator.ofFloat(imageView,"scaleX",0f,1f);
        ObjectAnimator animator2=ObjectAnimator.ofFloat(imageView,"scaleY",0f,1f);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(imageView,"alpha",0f,1f);

        AnimatorSet set=new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator1,animator2,animator3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameUtil.mItemBeans.clear();
        if(mTimer!=null){
            mTimer.cancel();
        }

       /* if(soundPool!=null){
            soundPool.release();
            soundPool = null;
        }*/
    }

    void startTime(){
        mTimer=new Timer(true);
        timerTask=new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        };

        mTimer.schedule(timerTask,0,1000);

    }


    /**
     * 播放音效
     */
    void playSound(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(this, R.raw.succeed, 1);
        soundPool.play(soundId, 2.0f, 2.0f, 1, 0, 1.0f);
    }
}
