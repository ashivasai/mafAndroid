package com.zambient.maf;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
    BounceInterpolator bounceInterpolator;
    ImageView img_animation;
    LinearLayout playGround;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_animation = (ImageView) findViewById(R.id.image);
        playGround = (LinearLayout)findViewById(R.id.playground);
        bounceInterpolator = new BounceInterpolator();
        img_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareObjectAnimator(bounceInterpolator);
            }
        });
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);


    }

    private void prepareObjectAnimator(TimeInterpolator timeInterpolator){
        //float w = (float)playGround.getWidth();
        float h = (float)playGround.getHeight();
        float propertyStart = 0f;
        float propertyEnd = -(h/2 - (float)img_animation.getHeight()/2);
        String propertyName = "translationY";
        ObjectAnimator objectAnimator
                = ObjectAnimator.ofFloat(img_animation, propertyName, propertyStart, propertyEnd);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setInterpolator(timeInterpolator);
        objectAnimator.start();
    }
}