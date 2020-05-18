package souravapp.tts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    Button nxtpg;
    TextView intro;
    AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdview =(AdView)findViewById(R.id.ad);
        AdRequest adRequest= new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        intro =(TextView)findViewById(R.id.intro);
        Animation animation =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movetxt);
        intro.startAnimation(animation);
        nxtpg=(Button)findViewById(R.id.next_page);
        nxtpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this,TTS.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        if(mAdview!=null){
            mAdview.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mAdview!=null){
            mAdview.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mAdview!=null){
            mAdview.destroy();
        }
        super.onDestroy();
    }
}
