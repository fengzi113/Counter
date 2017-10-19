package com.example.android.counter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {


    int A = 0;
    int B = 0;
    int flag = 0;
    private SoundPool soundPool;

    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.score_A);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.score_B);
        scoreView.setText(String.valueOf(score));
    }

    //addone
    public void addOneA(View view) {
        A = A + 1;
        displayForTeamA(A);
    }

    public void addOneB(View view) {
        B = B + 1;
        displayForTeamB(B);
    }

    //addthree
    public void addThreeA(View view) {
        A = A + 3;
        displayForTeamA(A);
    }

    public void addThreeB(View view) {
        B = B + 3;
        displayForTeamB(B);
    }

    //negativeone
    public void negativeOneA(View view) {
        if (A == 0) {
            return;
        }
        A = A - 1;
        displayForTeamA(A);
    }

    public void negativeOneB(View view) {

        if (B == 0) {
            return;
        }
        B = B - 1;
        displayForTeamB(B);
    }

    //哨声 播放
    public void whistle(View view) {
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    //reset
    public void reset(View view) {

        A = 0;
        B = 0;
        displayForTeamA(A);
        displayForTeamB(B);
        Toast.makeText(MainActivity.this, " 而 今 迈 步 从 头 越 ", Toast.LENGTH_SHORT).show();

    }

    // 防误触导致退出 ，需点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            flag = flag + 1;
        }

        if (flag == 2) {
            return super.onKeyDown(keyCode, event);//退出
        }

//        Toast.makeText(MainActivity.this, "防误触启动，再点一次退出", Toast.LENGTH_SHORT).show();
        return true;
    }

//    拍照部分 看了快一周的demo 终于看懂一点了

//    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
private String mCurrentPhotoPath;

    @TargetApi(Build.VERSION_CODES.FROYO)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
//        File albumF = getAlbumDir();
        File albumF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageF = File.createTempFile
                (imageFileName,
                        ".jpg",
                        albumF);
        return imageF;
    }

//intent拍照 开始intent
    public void dispatchTakePictureIntent(View v) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, 1);
        galleryAddPic();
    }

   //加到相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //        先加载声音 whistle.wav
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPool.load(this, R.raw.whistle, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        ！！！ 字体的设置必须置于 setContentView(R.layout.activity_main) 之后 ！！！
        //将字体文件保存在main/assets/fonts/目录下，创建Typeface对象
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/pilcrowIcons.ttf");
        //得到TextView控件对象
        TextView tv1 = (TextView) findViewById(R.id.score_A);
        TextView tv2 = (TextView) findViewById(R.id.score_B);
        //使用字体
        tv1.setTypeface(face);
        tv2.setTypeface(face);
    }
}
