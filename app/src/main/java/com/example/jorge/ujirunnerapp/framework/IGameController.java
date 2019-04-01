package com.example.jorge.ujirunnerapp.framework;

import android.graphics.Bitmap;

import com.example.jorge.ujirunnerapp.framework.IGameController;

import java.util.List;

/**
 * Created by jvilar on 29/03/16.
 * Modified by jcamen on 15/01/17.
 */
public interface IGameController {
    void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents);
    Bitmap onDrawingRequested();
}
