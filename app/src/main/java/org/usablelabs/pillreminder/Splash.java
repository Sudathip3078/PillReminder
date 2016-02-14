package org.usablelabs.pillreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends BaseActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(Splash.this, BaseActivity.class);
                Splash.this.startActivity(splashIntent);
                Splash.this.finish();
            }

        }, SPLASH_TIME_OUT);
    }
}
