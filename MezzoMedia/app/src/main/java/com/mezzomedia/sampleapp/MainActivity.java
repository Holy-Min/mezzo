package com.mezzomedia.sampleapp;

import com.mezzomedia.common.MZUtils;
import com.mezzomedia.common.MzLog;
import com.mezzomedia.man.AdConfig;
import com.mezzomedia.man.adid.AdidClient;
import com.mezzomedia.man.adid.AdidListener;
import com.mezzomedia.man.adid.GetAdidTask;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    StringBuilder builder = new StringBuilder("[ INFO ]" + "\n");
    private ViewGroup bannerArea;
    private RelativeLayout movieArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerArea = (RelativeLayout)findViewById(R.id.bannerArea);
        movieArea = (RelativeLayout)findViewById(R.id.movieArea);
        setTitle();
        btnSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Navimanager.getInstance().onDestoryBanner(MainActivity.this);
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
	private void setTitle() {
        String appName = "appname";
        try {
            appName = (String) getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        builder.append("appName : 		" + appName + "\n");
        builder.append("Version : 			" + AdConfig.SDK_VERSION + "\n");
        builder.append("Release Date : 	" + AdConfig.SDK_RELEASEDATE + "\n");
        builder.append("Release version : 	" + android.os.Build.VERSION.RELEASE);
        ((TextView) findViewById(R.id.version)).setText(builder.toString());

        final GetAdidTask task = new GetAdidTask(getApplicationContext(), new AdidListener() {
            @Override
            public void onAdid(AdidClient.AdInfo adInfo) {
                String adid = "";
                if (adInfo != null) {
                    adid = adInfo.getId();
                }
                try {
                    ((TextView) findViewById(R.id.adid)).setText("adid : " + adid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute();
    }

    private void btnSetting(){
        bannerSetting();
        interSetting();
        endingSetting();
        movieSetting();
        movieCheckbox();
    }
 
    private void bannerSetting() {

        LinearLayout btn_banner1 = (LinearLayout)findViewById(R.id.banner);
        findViewById(R.id.start_banner).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Navimanager.getInstance().onDestoryBanner(MainActivity.this);
                int pub = 100;
                int media = 200;
                int sec = 300;
                Navimanager.getInstance().requestBanner(MainActivity.this, pub, media, sec, bannerArea);
            }
        });
        findViewById(R.id.stop_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navimanager.getInstance().onDestoryBanner(MainActivity.this);
            }
        });
        btn_banner1.setVisibility(View.VISIBLE);
    }

    int interPosition = 0;
    int inlayoutStylePosition = 0;

    private void interSetting() {
        // 전면
        final Spinner s = (Spinner) findViewById(R.id.interspinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                interPosition = position;
                switch (interPosition) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "전체 전면.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "팝업 전면.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Spinner s0 = (Spinner) findViewById(R.id.inlayoutspinner);
        final RelativeLayout interArea = (RelativeLayout)findViewById(R.id.interArea);
        s0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                inlayoutStylePosition = position;
                switch (inlayoutStylePosition) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "아웃레이아웃.", Toast.LENGTH_SHORT).show();
                        s.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "인레이아웃.", Toast.LENGTH_SHORT).show();
                        s.setVisibility(View.GONE);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        LinearLayout btn_interstital1 = (LinearLayout)findViewById(R.id.inter1);
        Button start_inter = (Button)findViewById(R.id.start_inter);
        start_inter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int pub = 100;
                int media = 200;
                int sec = 300;
                if(inlayoutStylePosition == 0){
                    String isPopup = (interPosition == 0)?AdConfig.NOT_USED:AdConfig.USED;
                    Navimanager.getInstance().requestInter(MainActivity.this, pub, media, sec, isPopup);
                }else{
                    Navimanager.getInstance().requestInlayoutInter(MainActivity.this, pub, media, sec, interArea);
                }
            }
        });
        btn_interstital1.setVisibility(View.VISIBLE);
    }


    private void endingSetting() {
        // 종료
        LinearLayout btn_ending1 = (LinearLayout) findViewById(R.id.ending1);
        Button start_ending = (Button)findViewById(R.id.start_ending);
        start_ending.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int pub = 100;
                int media = 200;
                int sec = 300;
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                int w = MZUtils.nearSize(160, width);
                int h = (int) (1.5 * w);

                if (h > height) {
                    h = MZUtils.nearSize(240, height);
                    w = (int) (h / 1.5);
                }
                Navimanager.getInstance().requestEnding(MainActivity.this, pub, media, sec, w, h);
            }
        });
        btn_ending1.setVisibility(View.VISIBLE);
    }

    private String autoPlayValue = AdConfig.USED;
    private String autoReplayValue = AdConfig.NOT_USED;
    private String clickFullAreaValue = AdConfig.NOT_USED;
    private String mutedValue = AdConfig.USED;
    private String soundBtnShowValue = AdConfig.USED;
    private String clickBtnShowValue = AdConfig.USED;
    private String skipBtnShowValue = AdConfig.USED;
    private String closeShowValue = AdConfig.NOT_USED;

    private void movieCheckbox(){
        final CheckBox autoPlay = (CheckBox) findViewById(R.id.autoPlay) ;
        final CheckBox autoReplay = (CheckBox) findViewById(R.id.autoReplay) ;
        final CheckBox clickFullArea = (CheckBox) findViewById(R.id.clickFullArea) ;
        final CheckBox muted = (CheckBox) findViewById(R.id.muted) ;
        final CheckBox soundBtnShow = (CheckBox) findViewById(R.id.soundBtnShow) ;
        final CheckBox clickBtnShow = (CheckBox) findViewById(R.id.clickBtnShow) ;
        final CheckBox skipBtnShow = (CheckBox) findViewById(R.id.skipBtn) ;
        final CheckBox closeShow = (CheckBox) findViewById(R.id.closeShow) ;

        autoPlay.setChecked(AdConfig.USED.equals(autoPlayValue));
        autoReplay.setChecked(AdConfig.USED.equals(autoReplayValue));
        clickFullArea.setChecked(AdConfig.USED.equals(clickFullAreaValue));
        muted.setChecked(AdConfig.USED.equals(mutedValue));
        soundBtnShow.setChecked(AdConfig.USED.equals(soundBtnShowValue));
        clickBtnShow.setChecked(AdConfig.USED.equals(clickBtnShowValue));
        skipBtnShow.setChecked(AdConfig.USED.equals(skipBtnShowValue));
        closeShow.setChecked(AdConfig.USED.equals(closeShowValue));

        closeShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeShow.isChecked()){
                    closeShowValue = AdConfig.USED;
                }else{
                    closeShowValue = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoPlay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoPlay.isChecked()){
                    autoPlayValue = AdConfig.USED;
                }else{
                    autoPlayValue = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoReplay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoReplay.isChecked()){
                    autoReplayValue = AdConfig.USED;
                }else{
                    autoReplayValue = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickFullArea.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickFullArea.isChecked()){
                    clickFullAreaValue = AdConfig.USED;
                }else{
                    clickFullAreaValue = AdConfig.NOT_USED;
                }
            }
        }) ;

        muted.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muted.isChecked()){
                    mutedValue = AdConfig.USED;
                }else{
                    mutedValue = AdConfig.NOT_USED;
                }
            }
        }) ;

        soundBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundBtnShow.isChecked()){
                    soundBtnShowValue = AdConfig.USED;
                }else{
                    soundBtnShowValue = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickBtnShow.isChecked()){
                    clickBtnShowValue = AdConfig.USED;
                }else{
                    clickBtnShowValue = AdConfig.NOT_USED;
                }
            }
        }) ;


        skipBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(skipBtnShow.isChecked()){
                    skipBtnShowValue = AdConfig.USED;
                }else{
                    skipBtnShowValue = AdConfig.NOT_USED;
                }
            }
        }) ;
/**
 *         <item>NORMAL</item>
 *         <item>VERTICAL</item>
 *         <item>SQUARE</item>
 */
        final Spinner screenmode = (Spinner) findViewById(R.id.screenmode);
        screenmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) movieArea.getLayoutParams();
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "NORMAL.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        layoutParams.height = (int) height/2;
                        movieArea.setLayoutParams(layoutParams);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "VERTICAL.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = (int) width/2;
                        layoutParams.height = (int) width;
                        movieArea.setLayoutParams(layoutParams);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "SQUARE.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = (int) width/2;
                        layoutParams.height = (int) width/2;
                        movieArea.setLayoutParams(layoutParams);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public static float convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return px;

    }

    private void movieSetting() {
        // 영상
        LinearLayout movie1 = (LinearLayout) findViewById(R.id.movie1);
        Button start_movie = (Button)findViewById(R.id.start_movie);
        start_movie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int pub = 100;
                int media = 200;
                int sec = 300;
                int areaWidth = movieArea.getWidth();
                int areaHeight = movieArea.getHeight();
                debug("areaWidth : "+areaWidth);
                debug("areaHeight : "+areaHeight);
                Navimanager.getInstance().requestMovie(MainActivity.this, pub, media, sec, movieArea, areaWidth, areaHeight, autoPlayValue, autoReplayValue, clickFullAreaValue, mutedValue, soundBtnShowValue, clickBtnShowValue, skipBtnShowValue, closeShowValue);
            }
        });
        movie1.setVisibility(View.VISIBLE);
    }
    
    private void debug(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        MzLog.d(msg);
    }
}
