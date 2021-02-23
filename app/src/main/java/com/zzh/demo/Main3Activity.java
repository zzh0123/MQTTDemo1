package com.zzh.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class Main3Activity extends AppCompatActivity {
    private VrPanoramaView  mVrPanoramaView;
    private VrPanoramaView.Options paNormalOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initVrPaNormalView();
//        Toast.makeText(this, "更新了", Toast.LENGTH_LONG).show();
        new LoadPanoramaImageTask().execute();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mVrPanoramaView.pauseRendering();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVrPanoramaView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        // Destroy the widget and free memory.
        super.onDestroy();
        mVrPanoramaView.shutdown();
    }


    //初始化VR图片
    private void initVrPaNormalView() {
        mVrPanoramaView = (VrPanoramaView) findViewById(R.id.mVrPanoramaView);
        paNormalOptions = new VrPanoramaView.Options();
        paNormalOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//        mVrPanoramaView.setFullscreenButtonEnabled (false); //隐藏全屏模式按钮
        mVrPanoramaView.setInfoButtonEnabled(false); //设置隐藏最左边信息的按钮
        mVrPanoramaView.setStereoModeButtonEnabled(false); //设置隐藏立体模型的按钮
        mVrPanoramaView.setEventListener(new ActivityEventListener()); //设置监听
        //加载本地的图片源 andes imggugong  imgbug test1
//        mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.test1), paNormalOptions);
        //设置网络图片源
//        panoWidgetView.loadImageFromByteArray();
    }

    private class ActivityEventListener extends VrPanoramaEventListener {
        @Override
        public void onLoadSuccess() {//图片加载成功
        }


        @Override
        public void onLoadError(String errorMessage) {//图片加载失败
        }

        @Override
        public void onClick() {//当我们点击了VrPanoramaView 时候触发            super.onClick();
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            //改变显示模式时候出发（全屏模式和纸板模式）
            super.onDisplayModeChanged(newDisplayMode);
        }
    }
    private class LoadPanoramaImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                //加载assets目录下的全景图片
                AssetManager assetManager = getAssets();
                //加载本地的图片源 andes imggugong  imgbug test1
                InputStream open = assetManager.open("andes.jpg");
                return BitmapFactory.decodeStream(open);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            VrPanoramaView.Options options = new VrPanoramaView.Options();
            //图片类型为立体图像
            options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//            Log.i("zzz1", "onPostExecute");
            mVrPanoramaView.loadImageFromBitmap(bitmap, options);
        }
    }
}

