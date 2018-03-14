package a1.latitudeandlongitude;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import a1.test.R;

public class Splash extends AppCompatActivity {
    private static int spl=1500;
    LinearLayout up,down;
    Animation uptodown,downtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        up=(LinearLayout)findViewById(R.id.up);
        down=(LinearLayout)findViewById(R.id.down);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        up.setAnimation(uptodown);
        downtoup=AnimationUtils.loadAnimation(this,R.anim.downtoup);
        down.setAnimation(downtoup);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main=new Intent(Splash.this,Dashboard.class);
                startActivity(main);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        },spl);
    }
}
