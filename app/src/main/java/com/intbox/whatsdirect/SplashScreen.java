package com.intbox.whatsdirect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Splash s = new Splash();
        s.start();
    }

    class Splash extends Thread{
        @Override
        public void run() {
            try{
                sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            SplashScreen.this.finish();
        }
    }
}
