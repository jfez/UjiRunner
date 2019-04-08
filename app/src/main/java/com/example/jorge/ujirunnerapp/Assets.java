package com.example.jorge.ujirunnerapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Assets {

    public static Bitmap[] bgLayers;

    public static void createAssets(Context context, int playerWidth, int stageHeight,
                                    int parallaxWidth) {
        Resources resources = context.getResources();
        if (bgLayers != null)
            for (Bitmap bitmap: bgLayers)
                bitmap.recycle();
        int[] bgLayersResources = {
                R.drawable.ground,
                R.drawable.foreground,
                R.drawable.decor_middle,
                R.drawable.decor_bg,
                R.drawable.sky
        };
        bgLayers = new Bitmap[bgLayersResources.length];
        for (int i = 0; i < bgLayers.length; i++) {
            bgLayers[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    resources, bgLayersResources[i]), parallaxWidth, stageHeight, true);
        }
    }

}
