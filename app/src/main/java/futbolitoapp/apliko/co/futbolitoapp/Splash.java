package futbolitoapp.apliko.co.futbolitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private long timeSplash = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
                Splash.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, timeSplash);
    }
}
