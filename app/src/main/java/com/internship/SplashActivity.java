package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;

    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageview = findViewById(R.id.splash_image);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(2000);
        imageview.startAnimation(animation);

//        ScaleAnimation scaleAnimation = new ScaleAnimation(
//                0.5f, // From X scale factor
//                1.0f, // To X scale factor
//                0.5f, // From Y scale factor
//                1.0f, // To Y scale factor
//                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X
//                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y
//        );

        // Set the animation duration in milliseconds
//        scaleAnimation.setDuration(3000); // 1 second

//        onAnimationEnd(scaleAnimation);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString(ConstantSp.REMEMBER, "").equalsIgnoreCase("")) {
                    new CommonMethod(SplashActivity.this, MainActivity.class);
                }
                else {
                    new CommonMethod(SplashActivity.this, DashboardActivity.class);
                }
            }
        }, 1500);
    }

//    public void onAnimationEnd(Animation scaleAnimation) {
//        if (sp.getString(ConstantSp.REMEMBER, "").equalsIgnoreCase("")) {
//            new CommonMethod(SplashActivity.this, MainActivity.class);
//        } else {
//            new CommonMethod(SplashActivity.this, HomeActivity.class);
//        }
//    }

}