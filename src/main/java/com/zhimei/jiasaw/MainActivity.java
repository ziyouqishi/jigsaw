package com.zhimei.jiasaw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import adapter.GridViewAdapter;
import tools.GameUtil;
import tools.PictureDeal;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_IMAGE=100;
    private static final String IMAGE_TYPE="image/*";
    private String imagePath;
    private static final int RESULT_CAMERA=200;
    private File output;
    private PopupWindow popupWindow;
    private TextView difficulty;
    private GridView gridView;
    private ArrayList<Bitmap> picList;
    private PictureDeal pictureDeal=new PictureDeal();
    private int mTYpe=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void popupShow(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        View popView = inflater.inflate(R.layout.popup_view, null,false);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 0, 0);

        final View easy=popView.findViewById(R.id.select_type_1);
        final View medium=popView.findViewById(R.id.select_type_2);
        final View hard=popView.findViewById(R.id.select_type_3);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupwindow(easy);
                difficulty.setText("2X2");
                mTYpe=2;
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupwindow(medium);
                difficulty.setText("3X3");
                mTYpe=3;
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupwindow(hard);
                difficulty.setText("4X4");
                mTYpe=4;
            }
        });


    }

    void initView(){
        picList=new ArrayList<>();
        difficulty=(TextView)findViewById(R.id.select_type);
        gridView=(GridView)findViewById(R.id.pic_list);

        final int []ResPicId=new int[]{R.drawable.haha,R.drawable.twelve,R.drawable.three,R.drawable.four,
        R.drawable.five,R.drawable.six,R.drawable.senven,R.drawable.eight,R.drawable.nien,R.drawable.ten,
        R.drawable.eleven,R.drawable.twelve,R.drawable.thirteen,R.drawable.fourteen,R.drawable.fifiteen,R.drawable.camara};

        Bitmap[] bitmaps=new Bitmap[ResPicId.length];

        for(int i=0;i<bitmaps.length;i++){
           // bitmaps[i]= BitmapFactory.decodeResource(getResources(),ResPicId[i]);
            bitmaps[i]=pictureDeal.readBitmap(this,ResPicId[i]);
            picList.add(bitmaps[i]);

        }

        GridViewAdapter gridViewAdapter=new GridViewAdapter(this,picList);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(GameUtil.mItemBeans.size()>0){
                    GameUtil.mItemBeans.clear();
                }
                if(position==ResPicId.length-1){
                    showDialog();
                }else{
                    Intent intent=new Intent(MainActivity.this,PuzzleActivity.class);
                    intent.putExtra("picSelectedID",ResPicId[position]);
                    intent.putExtra("mType",mTYpe);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                }
            }
        });

        difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupShow(difficulty);
            }
        });
    }

    void showDialog(){
        String string[] = {"拍照", "从相册中选取"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("选择照片");
        dialog.setItems(string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        choosePhoto();
                    default:
                        break;

                }
            }
        });
        dialog.show();
    }

    void takePhoto(){
        File file=new File(Environment.getExternalStorageDirectory(),"拼图");
        if(!file.exists()){
            file.mkdir();
        }
        output=new File(file,System.currentTimeMillis()+".jpg");
        imagePath=output.getPath();
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri=Uri.fromFile(output);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent,RESULT_CAMERA);

    }

    void choosePhoto(){
        Intent intent=new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE);
        startActivityForResult(intent,RESULT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==RESULT_IMAGE&&data!=null){
                Cursor cursor=this.getContentResolver().query(data.getData(),null,null,null,null);
                cursor.moveToFirst();
                String imagePath=cursor.getString(cursor.getColumnIndex("_data"));

                Intent intent=new Intent(MainActivity.this,PuzzleActivity.class);
                intent.putExtra("mPicPath",imagePath);
                intent.putExtra("mType",mTYpe);
                cursor.close();
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }else if(requestCode==RESULT_CAMERA){
                Intent intent=new Intent(MainActivity.this,PuzzleActivity.class);
                intent.putExtra("mPicPath",imagePath);
                intent.putExtra("mType",mTYpe);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }
    }


    /**
     * 点击popupwindow后，让点击的item显示背景色，显示一小段时间，再dismiss掉点击popupwindow后
     *
     * 如果直接在item的点击事件里写背景显示和dismiss，则只会dismiss掉popupwindow，不会显示背景颜色。
     *
     * @param view
     */
    void dismissPopupwindow(final View view) {

        new AsyncTask(){
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                popupWindow.dismiss();
                popupWindow=null;

            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                view.setBackgroundColor(0xFFFDE7);

            }
        }.execute();


    }


    public void test(View view){
        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
    }
}
