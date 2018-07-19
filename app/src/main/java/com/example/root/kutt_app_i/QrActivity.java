package com.example.root.kutt_app_i;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.security.Policy;

public class QrActivity extends AppCompatActivity {

    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    ImageView close_button,flash;
    public static  final int PERMISSION_REQUEST=200;
    ObjectAnimator animator;
    View scannerLayout;
    View scannerBar;
    private boolean isFlashOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        close_button = findViewById(R.id.close_window);
        flash = findViewById(R.id.flash);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        scannerLayout = findViewById(R.id.scannerLayout);
        scannerBar = findViewById(R.id.scannerBar);

        animator = null;

        ViewTreeObserver vto = scannerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                scannerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    scannerLayout.getViewTreeObserver().
                            removeGlobalOnLayoutListener(this);

                } else {
                    scannerLayout.getViewTreeObserver().
                            removeOnGlobalLayoutListener(this);
                }

                float destination = (float)(scannerLayout.getY() +
                        scannerLayout.getHeight());

                animator = ObjectAnimator.ofFloat(scannerBar, "translationY",
                        scannerLayout.getY(),
                        destination);

                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(3000);
                animator.start();

            }
        });

        cameraView = findViewById(R.id.cameraView);

        cameraView.setZOrderMediaOverlay(true);
        holder=cameraView.getHolder();
        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if(!barcode.isOperational())
        {
            Toast.makeText(getApplicationContext(),"sorry",Toast.LENGTH_LONG).show();
            this.finish();
        }

        cameraSource = new CameraSource.Builder(this,barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setRequestedFps(24)
                .setRequestedPreviewSize(1902,1024)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if(ContextCompat.checkSelfPermission(QrActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

                        cameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() >0){
                   /* Intent intent = new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0));
                    setResult(RESULT_OK,intent);*/

                    Intent intent = new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0));
                    setResult(RESULT_OK,intent);
                    cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    finish();


                }
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    cameraSource.release();
                }catch (Exception e){}

                finish();
            }
        });
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashOn = !isFlashOn;
                if(isFlashOn) {
                    cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_flash_off_24px));
                }else {
                    cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_flash_on_24px));
                }

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        try {
            cameraSource.release();
        }catch (Exception e){}
    }

}
