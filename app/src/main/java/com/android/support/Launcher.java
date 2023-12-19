package com.android.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Launcher extends Service {

    public static final String MUFFIN = "muffin";
    Menu menu;

    public static void startWithoutPermission(Context context) throws RuntimeException, IOException {

        loadLibFromAssets(context);

        if (context instanceof Activity) {
            //Check if context is an Activity.
            Menu menu = new Menu(context);
            menu.setWindowManagerActivity();
            menu.showMenu();
        } else {
            Toast.makeText(context, "Not an activity ?!", Toast.LENGTH_SHORT).show();
        }
    }

    //When this Class is called the code in this function will be executed
    @Override
    public void onCreate() {
        super.onCreate();

        menu = new Menu(this);
        menu.setWindowManagerWindowService();
        menu.showMenu();

        //Create a handler for this Class
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                threadStatus();
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Check if we are still in the game. If now our menu and menu button will dissapear
    private boolean isNotInGame() {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(runningAppProcessInfo);
        return runningAppProcessInfo.importance != 100;
    }

    private void threadStatus() {
        if (isNotInGame()) {
            menu.setVisibility(View.INVISIBLE);
        } else {
            menu.setVisibility(View.VISIBLE);
        }
    }

    //Destroy our View
    @Override
    public void onDestroy() {
        super.onDestroy();
        menu.onDestroy();
    }

    //Same as above so it wont crash in the background and therefore use alot of Battery life
    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    //Override our Start Command so the Service doesnt try to recreate itself when the App is closed
    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_NOT_STICKY;
    }

    /**
     * Simple way to guess file name based on ARCH, not the best but works as an example.
     *
     * @return String
     */
    public static String guessDummyName() {
        String arch = System.getProperty("os.arch");
        if (arch != null && (arch.contains("aarch64") || arch.contains("x86_64"))) {
            return "dummy-a64.png";
        } else {
            return "dummy-a32.png";
        }
    }

    /**
     * Simple example on how you could load a lib from assets, this can be done in c++ too
     *
     * @param context - 'this' parameter coming from an Activity
     * @throws RuntimeException in case some crap happens
     */
    public static void loadLibFromAssets(Context context) throws RuntimeException, IOException {

        if (context == null) {
            return;
        }

        String dummyPng = guessDummyName();

        AssetManager assetManager = context.getAssets();
        String[] list = assetManager.list(MUFFIN);
        String listToString = Arrays.toString(list);

        if (!listToString.contains(dummyPng)) {
            throw new RuntimeException("Unable to locate file");
        } else {
            OutputStream os;
            try (InputStream is = assetManager.open(MUFFIN.concat("/").concat(dummyPng))) {
                String outDir = context.getDataDir().getAbsolutePath();
                File outFile = new File(outDir, dummyPng);
                os = new FileOutputStream(outFile);
                copyFile(is, os);
                doLoad(outFile);
                os.flush();
                os.close();
            } catch (Exception exception) {
                Log.e("EXR", "loadLibFromAssets: ", exception);
                throw new RuntimeException("Crappy crap happened");
            }
        }

    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    private static void doLoad(File outFile) {
        try {
            System.load(outFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
