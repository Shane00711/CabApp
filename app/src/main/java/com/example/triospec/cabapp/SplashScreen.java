package com.example.triospec.cabapp;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.graphics.PixelFormat;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * Created by Cortex Hub on 12/20/2015.
 */
public class SplashScreen extends Activity {

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    //private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();

/**
        new Handler().postDelayed(new Runnable(){

            public void run(){
                Intent i = new Intent(SplashScreen.this, Login_Driver.class);
                startActivity(i);

                finish();

            }
        }, SPLASH_TIME_OUT);
**/
    }
    Thread splashTread;

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.splash);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashScreen.this,
                            Login_Driver.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();
    }
}
