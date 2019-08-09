package com.mezzomedia.adman.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.utils.MockView;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mezzomedia.common.MzLog;
import com.mezzomedia.man.AdConfig;
import com.mezzomedia.man.view.AdManView;

public class MovieActivity extends Activity {
    private RelativeLayout movieArea;
    private String autoPlayValue = AdConfig.USED;
    private String autoReplayValue = AdConfig.NOT_USED;
    private String clickFullAreaValue = AdConfig.NOT_USED;
    private String mutedValue = AdConfig.USED;
    private String soundBtnShowValue = AdConfig.USED;
    private String clickBtnShowValue = AdConfig.USED;
    private String skipBtnShowValue = AdConfig.USED;
    private String closeShowValue = AdConfig.NOT_USED;

    private AdManView adManView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieactivity);
        movieArea = (RelativeLayout)findViewById(R.id.movieactivityarea);
        int pub = Integer.valueOf(getIntent().getStringExtra("p"));
        int media = Integer.valueOf(getIntent().getStringExtra("m"));
        int sec = Integer.valueOf(getIntent().getStringExtra("s"));
        Display defaultDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int areaWidth = defaultDisplay.getWidth();
        int areaHeight = defaultDisplay.getHeight();
        if (defaultDisplay != null) {
            areaWidth = defaultDisplay.getWidth();
            areaHeight = defaultDisplay.getHeight();
        }
        debug("areaWidth : "+areaWidth);
        debug("areaHeight : "+areaHeight);
        autoPlayValue = getIntent().getStringExtra("autoplay");
        autoReplayValue = getIntent().getStringExtra("autoreplay");
        clickFullAreaValue = getIntent().getStringExtra("clickfullarea");
        mutedValue = getIntent().getStringExtra("muted");
        soundBtnShowValue = getIntent().getStringExtra("soundbtnshow");
        clickBtnShowValue = getIntent().getStringExtra("clickbtnshow");
        skipBtnShowValue = getIntent().getStringExtra("skipbtnshow");
        closeShowValue = getIntent().getStringExtra("closeshow");
        adManView = Navimanager.getInstance().requestMovie(null, MovieActivity.this, pub, media, sec, MainActivity.mode, movieArea, areaWidth, areaHeight, autoPlayValue, autoReplayValue, clickFullAreaValue, mutedValue, soundBtnShowValue, clickBtnShowValue, skipBtnShowValue, closeShowValue);
    }
    private void debug(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        MzLog.d(msg);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(adManView!=null){
            adManView.onDestroy();
        }
        if(adManView.getParent() != null){
            ((ViewGroup) adManView.getParent()).removeView(adManView);
        }
        adManView = null;
        movieArea.removeAllViews();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
