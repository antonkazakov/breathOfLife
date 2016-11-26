package kazakov.com.breathoflife;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class BreathingActivity extends AppCompatActivity {

    private boolean isRunning = false;
    private boolean isAnimationRunning = false;

    ImageView imageView;

    RotateAnimation rotateAnimation;

    private int TIMEOUT = 300;

    SoundMeter soundMeter;

    TextView tvCount;
    TextView tvValue;


    int count = 0;
    double value = 0.0;
    private Handler mHandler = new Handler();

    private Runnable mPollTask = new Runnable() {
        public void run() {

            double amp = soundMeter.getAmplitude();

            if (amp>0.7){
                Log.i("AMPLITUDE VALUE", amp+"");
                value = amp;
                count++;
                tvCount.setText(count+"");
                tvValue.setText(value+"");
                rotateAnimation.setDuration((long)(1000-amp*100));
                if(!isAnimationRunning){
                    imageView.startAnimation(rotateAnimation);
                }
            }

            mHandler.postDelayed(mPollTask, TIMEOUT);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        soundMeter = new SoundMeter();

        imageView = (ImageView) findViewById(R.id.imageView);

        tvValue = (TextView)findViewById(R.id.tv_value);
        tvCount = (TextView) findViewById(R.id.tv_count);

        rotateAnimation = new RotateAnimation(30, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatMode(Animation.INFINITE);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRunning) {
            isRunning = true;
            start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundMeter.stop();
        isRunning = false;
    }


    private void start() {
        soundMeter.start();
        mHandler.postDelayed(mPollTask, TIMEOUT);
    }

}
