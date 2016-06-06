package wolfusz.voidlauncher.essentials;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

/**
 * Created by wolf2 on 12.04.2016.
 */
@SuppressWarnings("deprecation")
public class FlashLight {
    public static Camera camera;
    public static boolean on = false;

    public static void toggle() {
        if (on == false)
            turnOn();
        else
            turnOff();
    }

    public static void turnOn() {
        if (camera == null) camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        try {
            camera.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException e) { }
        camera.startPreview();
        try {
            Thread.sleep(50);
            camera.stopPreview();
            Thread.sleep(100);
            camera.startPreview();
        } catch (Exception e) { }
        on = true;
    }

    public static void turnOff() {
        if (camera == null) return;
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        camera.release();
        camera = null;
        params = null;
        on = false;
    }
}
