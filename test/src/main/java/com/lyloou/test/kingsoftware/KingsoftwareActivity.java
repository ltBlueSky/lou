/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.kingsoftware;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uscreen;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KingsoftwareActivity extends AppCompatActivity {

    ImageView mImageView;
    Activity mContext;
    private Bitmap mWallpaperBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_kingsoftware);

        initView();

        loadData();
    }

    private void setBackgroundToBitmap(final Bitmap sourceBitmap) {

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
        final int sourceWidth = sourceBitmap.getWidth();
        final int sourceHeight = sourceBitmap.getHeight();
        final int letterboxedWidth = Uscreen.getScreenWidth(mContext);
        final int letterboxedHeight = Uscreen.getScreenHeight(mContext) - Uscreen.getStatusBarHeight(mContext);

        final float resizeRatioX = (float) letterboxedWidth / sourceWidth;
        final float resizeRatioY = (float) letterboxedHeight / sourceHeight;

        final Bitmap letterboxedBitmap = Bitmap.createBitmap(letterboxedWidth, letterboxedHeight, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(letterboxedBitmap);
        canvas.drawRGB(0, 0, 0);

        final Matrix transformations = new Matrix();
        transformations.postScale(resizeRatioX, resizeRatioX);
        transformations.postTranslate(0, Uscreen.getStatusBarHeight(mContext));
        canvas.drawBitmap(sourceBitmap, transformations, null);

        try {
            wallpaperManager.setBitmap(letterboxedBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.iv_kingsoftware);
        mImageView.setOnLongClickListener(v -> {
            if (mWallpaperBitmap == null) {
                Toast.makeText(mContext, "无法设壁纸", Toast.LENGTH_SHORT).show();
            } else {
                setBackgroundToBitmap(mWallpaperBitmap);
                Toast.makeText(mContext, "已设壁纸", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
    }

    private void loadData() {
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .map(daily -> {
                    DrawableTypeRequest<String> load = Glide
                            .with(KingsoftwareActivity.this)
                            .load(daily.getFenxiang_img());
                    mWallpaperBitmap = load.asBitmap().into(mImageView.getWidth(), mImageView.getHeight()).get();
                    return mWallpaperBitmap;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> mImageView.setImageBitmap(bitmap),
                        Throwable::printStackTrace);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
