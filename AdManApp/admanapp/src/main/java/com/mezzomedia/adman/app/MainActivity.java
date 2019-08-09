package com.mezzomedia.adman.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.mezzomedia.common.MZUtils;
import com.mezzomedia.common.MzLog;
import com.mezzomedia.man.AdConfig;
import com.mezzomedia.man.activity.AdActivity;
import com.mezzomedia.man.adid.AdidClient;
import com.mezzomedia.man.adid.AdidListener;
import com.mezzomedia.man.adid.GetAdidTask;
import com.mezzomedia.man.view.AdManView;

//import android.webkit.WebView;

public class MainActivity extends Activity {
    StringBuilder builder = new StringBuilder("[ INFO ]" + "\n");
    public static String mode = AdConfig.DEV_MODE;
    public static String isLocation = AdConfig.NOT_USED;
    public static String isReadPhone = AdConfig.NOT_USED;
    private ViewGroup bannerArea;
    private ViewGroup bannerFloatingArea;
    private RelativeLayout movieArea;
    private RelativeLayout movieArea2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        AdManView.init(getApplicationContext());
        bannerArea = (RelativeLayout)findViewById(R.id.bannerArea);
        bannerFloatingArea = (FrameLayout)findViewById(R.id.fruting);
        movieArea = (RelativeLayout)findViewById(R.id.movieArea);
        movieArea2 = (RelativeLayout)findViewById(R.id.movieArea2);
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

    @Override
    protected void onPause() {
        Navimanager.getInstance().onDestoryBanner(MainActivity.this);
        super.onPause();
    }

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
        domainController();
        bannerSetting();
        interSetting();
        endingSetting();
        movieSetting();
        movieCheckbox();
        movieCheckbox2();
        movieCheckbox3();

    }

    private void domainController() {
        Spinner s = (Spinner)findViewById(R.id.test_spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                String log = "테스트모드";
                if (position == 0) {//test
                    mode = AdConfig.DEV_MODE;
                    log = "테스트모드";
                } else if (position == 1) {//real
                    mode = AdConfig.REAL_MODE;
                    log = "상용모드";
                }
                Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Spinner position = (Spinner)findViewById(R.id.islocation);
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                String log = "위치 정보 미사용";
                if (position == 0) {//test
                    isLocation = AdConfig.NOT_USED;
                    log = "위치 정보 미사용";
                } else if (position == 1) {//real
                    isLocation = AdConfig.USED;
                    log = "위치 정보 사용";
                }
                Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Spinner readphone = (Spinner)findViewById(R.id.isReadphone);
        readphone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                String log = "폰 정보 미사용";
                if (position == 0) {//test
                    isReadPhone = AdConfig.NOT_USED;
                    log = "폰 정보 미사용";
                } else if (position == 1) {//real
                    isReadPhone = AdConfig.USED;
                    log = "폰 정보 사용";
                }
                Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
    int floatingIndex = 0;
    private void bannerSetting() {
//        final Spinner bannerStylespinner = (Spinner) findViewById(R.id.bannerStylespinner);
//        bannerStylespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
//                floatingIndex = position;
//                switch (floatingIndex) {
//                    case 0:
//                        Toast.makeText(getApplicationContext(), "NORMAL 배너.", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        Toast.makeText(getApplicationContext(), "floating 배너.", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });

        LinearLayout btn_banner1 = (LinearLayout)findViewById(R.id.banner);
        final Button startBanner = findViewById(R.id.start_banner);
        startBanner.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Navimanager.getInstance().onDestoryBanner(MainActivity.this);
                if (setBaseInfo(R.id.banner_pub, R.id.banner_media)) {
                    click(R.id.banner_sec, new Handler() {
                        @Override
                        public void dispatchMessage(final Message msg) {
                            try {
                                int pub = Integer.valueOf(((EditText) findViewById(R.id.banner_pub)).getText().toString());
                                int media = Integer.valueOf(((EditText) findViewById(R.id.banner_media)).getText().toString());
                                int sec = (Integer) msg.obj;
                                if(floatingIndex == 1){
                                    Navimanager.getInstance().requestBanner(startBanner,MainActivity.this, pub, media, sec, mode, AdConfig.USED, bannerFloatingArea);
                                }else if(floatingIndex == 2){
                                    Navimanager.getInstance().requestBanner(startBanner, MainActivity.this, pub, media, sec, mode, AdConfig.USED, bannerFloatingArea);
                                }else{
                                    Navimanager.getInstance().requestBanner(startBanner, MainActivity.this, pub, media, sec, mode, AdConfig.NOT_USED, bannerArea);
                                }

                            }catch (Exception e){
                                debug("접근방식이 올바르지 않습니다!");
                            }
                        }
                    });
                }
            }
        });
        findViewById(R.id.stop_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBanner.setEnabled(true);
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
        final Button start_inter = (Button)findViewById(R.id.start_inter);
        start_inter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (setBaseInfo(R.id.inter_pub, R.id.inter_media)) {
                    click(R.id.inter_sec, new Handler() {
                        @Override
                        public void dispatchMessage(final Message msg) {
                            try {
                                int pub = Integer.valueOf(((EditText) findViewById(R.id.inter_pub)).getText().toString());
                                int media = Integer.valueOf(((EditText) findViewById(R.id.inter_media)).getText().toString());
                                int sec = (Integer) msg.obj;
                                if(inlayoutStylePosition == 0){
                                    String isPopup = (interPosition == 0)?AdConfig.NOT_USED:AdConfig.USED;
                                    Navimanager.getInstance().requestInter(start_inter,MainActivity.this, pub, media, sec, mode, isPopup);
                                }else{
                                    Navimanager.getInstance().requestInlayoutInter(start_inter, MainActivity.this, pub, media, sec, mode, interArea);
                                }

                            }catch (Exception e){
                                debug("접근방식이 올바르지 않습니다!");
                            }
                        }
                    });
                }
            }
        });
        btn_interstital1.setVisibility(View.VISIBLE);
    }


    private void endingSetting() {
        // 종료
        LinearLayout btn_ending1 = (LinearLayout) findViewById(R.id.ending1);
        final Button start_ending = (Button)findViewById(R.id.start_ending);
        start_ending.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (setBaseInfo(R.id.ending_pub, R.id.ending_media)) {
                    click(R.id.ending_sec, new Handler() {
                        @Override
                        public void dispatchMessage(final Message msg) {
                            try {
                                int sec = (Integer) msg.obj;
                                int pub = Integer.valueOf(((EditText) findViewById(R.id.ending_pub)).getText().toString());
                                int media = Integer.valueOf(((EditText) findViewById(R.id.ending_media)).getText().toString());
                                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                                int width = dm.widthPixels;
                                int height = dm.heightPixels;
                                int w = MZUtils.nearSize(160, width);
                                int h = (int) (1.5 * w);

                                if (h > height) {
                                    h = MZUtils.nearSize(240, height);
                                    w = (int) (h / 1.5);
                                }
                                Navimanager.getInstance().requestEnding(start_ending, MainActivity.this, pub, media, sec, mode, w, h);
                            }catch (Exception e){
                                debug("접근방식이 올바르지 않습니다!");
                            }
                        }
                    });
                }
            }
        });//(final Context c, final int p, final int m, final int s, final String mode, final int w, final int h) {
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

    private String autoPlayValue2 = AdConfig.USED;
    private String autoReplayValue2 = AdConfig.NOT_USED;
    private String clickFullAreaValue2 = AdConfig.NOT_USED;
    private String mutedValue2 = AdConfig.USED;
    private String soundBtnShowValue2 = AdConfig.USED;
    private String clickBtnShowValue2 = AdConfig.USED;
    private String skipBtnShowValue2 = AdConfig.USED;
    private String closeShowValue2 = AdConfig.NOT_USED;

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
                int w=width;
                int h=height;
                String type = "NORMAL";
                switch (position) {
                    case 0:
                        w= RelativeLayout.LayoutParams.MATCH_PARENT;
                        h = (int) height/2;
                        type = "NORMAL";
                        break;
                    case 1:
                        w = (int) width/2;
                        h = (int) width;
                        type = "VERTICAL";
                        break;
                    case 2:
                        w = (int) width/2;
                        h = (int) width/2;
                        type = "SQUARE";
                        break;
                }
                String toast = "type : "+type+" w : "+w+" h : "+h;
                layoutParams.width = w;
                layoutParams.height = h;
                MzLog.d(toast);
                movieArea.setLayoutParams(layoutParams);
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void movieCheckbox2(){
        final CheckBox autoPlay = (CheckBox) findViewById(R.id.autoPlay2) ;
        final CheckBox autoReplay = (CheckBox) findViewById(R.id.autoReplay2) ;
        final CheckBox clickFullArea = (CheckBox) findViewById(R.id.clickFullArea2) ;
        final CheckBox muted = (CheckBox) findViewById(R.id.muted2) ;
        final CheckBox soundBtnShow = (CheckBox) findViewById(R.id.soundBtnShow2) ;
        final CheckBox clickBtnShow = (CheckBox) findViewById(R.id.clickBtnShow2) ;
        final CheckBox skipBtnShow = (CheckBox) findViewById(R.id.skipBtn2) ;
        final CheckBox closeShow = (CheckBox) findViewById(R.id.closeShow2) ;

        autoPlay.setChecked(AdConfig.USED.equals(autoPlayValue2));
        autoReplay.setChecked(AdConfig.USED.equals(autoReplayValue2));
        clickFullArea.setChecked(AdConfig.USED.equals(clickFullAreaValue2));
        muted.setChecked(AdConfig.USED.equals(mutedValue2));
        soundBtnShow.setChecked(AdConfig.USED.equals(soundBtnShowValue2));
        clickBtnShow.setChecked(AdConfig.USED.equals(clickBtnShowValue2));
        skipBtnShow.setChecked(AdConfig.USED.equals(skipBtnShowValue2));
        closeShow.setChecked(AdConfig.USED.equals(closeShowValue2));

        closeShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeShow.isChecked()){
                    closeShowValue2 = AdConfig.USED;
                }else{
                    closeShowValue2 = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoPlay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoPlay.isChecked()){
                    autoPlayValue2 = AdConfig.USED;
                }else{
                    autoPlayValue2 = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoReplay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoReplay.isChecked()){
                    autoReplayValue2 = AdConfig.USED;
                }else{
                    autoReplayValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickFullArea.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickFullArea.isChecked()){
                    clickFullAreaValue2 = AdConfig.USED;
                }else{
                    clickFullAreaValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;

        muted.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muted.isChecked()){
                    mutedValue2 = AdConfig.USED;
                }else{
                    mutedValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;

        soundBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundBtnShow.isChecked()){
                    soundBtnShowValue2 = AdConfig.USED;
                }else{
                    soundBtnShowValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickBtnShow.isChecked()){
                    clickBtnShowValue2 = AdConfig.USED;
                }else{
                    clickBtnShowValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;


        skipBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(skipBtnShow.isChecked()){
                    skipBtnShowValue2 = AdConfig.USED;
                }else{
                    skipBtnShowValue2 = AdConfig.NOT_USED;
                }
            }
        }) ;
/**
 *         <item>NORMAL</item>
 *         <item>VERTICAL</item>
 *         <item>SQUARE</item>
 */
        final Spinner screenmode2 = (Spinner) findViewById(R.id.screenmode2);
        screenmode2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) movieArea2.getLayoutParams();
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "NORMAL.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        layoutParams.height = (int) height/2;
                        movieArea2.setLayoutParams(layoutParams);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "VERTICAL.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = (int) width/2;
                        layoutParams.height = (int) width;
                        movieArea2.setLayoutParams(layoutParams);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "SQUARE.", Toast.LENGTH_SHORT).show();
                        layoutParams.width = (int) width/2;
                        layoutParams.height = (int) width/2;
                        movieArea2.setLayoutParams(layoutParams);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }



    private void movieCheckbox3(){
        final CheckBox autoPlay = (CheckBox) findViewById(R.id.autoPlayactivity) ;
        final CheckBox autoReplay = (CheckBox) findViewById(R.id.autoReplayactivity) ;
        final CheckBox clickFullArea = (CheckBox) findViewById(R.id.clickFullAreaactivity) ;
        final CheckBox muted = (CheckBox) findViewById(R.id.mutedactivity) ;
        final CheckBox soundBtnShow = (CheckBox) findViewById(R.id.soundBtnShowactivity) ;
        final CheckBox clickBtnShow = (CheckBox) findViewById(R.id.clickBtnShowactivity) ;
        final CheckBox skipBtnShow = (CheckBox) findViewById(R.id.skipBtnactivity) ;
        final CheckBox closeShow = (CheckBox) findViewById(R.id.closeShowactivity) ;

        autoPlay.setChecked(AdConfig.USED.equals(autoPlayValueactivity));
        autoReplay.setChecked(AdConfig.USED.equals(autoReplayValueactivity));
        clickFullArea.setChecked(AdConfig.USED.equals(clickFullAreaValueactivity));
        muted.setChecked(AdConfig.USED.equals(mutedValueactivity));
        soundBtnShow.setChecked(AdConfig.USED.equals(soundBtnShowValueactivity));
        clickBtnShow.setChecked(AdConfig.USED.equals(clickBtnShowValueactivity));
        skipBtnShow.setChecked(AdConfig.USED.equals(skipBtnShowValueactivity));
        closeShow.setChecked(AdConfig.USED.equals(closeShowValueactivity));

        closeShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeShow.isChecked()){
                    closeShowValueactivity = AdConfig.USED;
                }else{
                    closeShowValueactivity = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoPlay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoPlay.isChecked()){
                    autoPlayValueactivity = AdConfig.USED;
                }else{
                    autoPlayValueactivity = AdConfig.NOT_USED;
                }

            }
        }) ;

        autoReplay.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoReplay.isChecked()){
                    autoReplayValueactivity = AdConfig.USED;
                }else{
                    autoReplayValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickFullArea.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickFullArea.isChecked()){
                    clickFullAreaValueactivity = AdConfig.USED;
                }else{
                    clickFullAreaValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;

        muted.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muted.isChecked()){
                    mutedValueactivity = AdConfig.USED;
                }else{
                    mutedValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;

        soundBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundBtnShow.isChecked()){
                    soundBtnShowValueactivity = AdConfig.USED;
                }else{
                    soundBtnShowValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;

        clickBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickBtnShow.isChecked()){
                    clickBtnShowValueactivity = AdConfig.USED;
                }else{
                    clickBtnShowValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;


        skipBtnShow.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(skipBtnShow.isChecked()){
                    skipBtnShowValueactivity = AdConfig.USED;
                }else{
                    skipBtnShowValueactivity = AdConfig.NOT_USED;
                }
            }
        }) ;
    }

    public static float convertDpToPixel(float dp, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);

        return px;

    }
    private String autoPlayValueactivity = AdConfig.USED;
    private String autoReplayValueactivity = AdConfig.NOT_USED;
    private String clickFullAreaValueactivity = AdConfig.NOT_USED;
    private String mutedValueactivity = AdConfig.USED;
    private String soundBtnShowValueactivity = AdConfig.USED;
    private String clickBtnShowValueactivity = AdConfig.USED;
    private String skipBtnShowValueactivity = AdConfig.USED;
    private String closeShowValueactivity = AdConfig.NOT_USED;
    private void movieSetting() {
        LinearLayout movie3 = (LinearLayout) findViewById(R.id.movieactivityarea);
        final Button start_movie_activity = (Button)findViewById(R.id.start_movieactivity);
        start_movie_activity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("p",((EditText) findViewById(R.id.movie_pubactivity)).getText().toString());
                intent.putExtra("m",((EditText) findViewById(R.id.movie_mediaactivity)).getText().toString());
                intent.putExtra("s",((EditText) findViewById(R.id.movie_secactivity)).getText().toString());
                intent.putExtra("autoplay",autoPlayValueactivity);
                intent.putExtra("autoreplay",autoReplayValueactivity);
                intent.putExtra("clickfullarea",clickFullAreaValueactivity);
                intent.putExtra("muted",mutedValueactivity);
                intent.putExtra("soundbtnshow",soundBtnShowValueactivity);
                intent.putExtra("clickbtnshow",clickBtnShowValueactivity);
                intent.putExtra("skipbtnshow",skipBtnShowValueactivity);
                intent.putExtra("closeshow",closeShowValueactivity);

                startActivity(intent);
            }
        });
        movie3.setVisibility(View.VISIBLE);


        // 영상
        LinearLayout movie1 = (LinearLayout) findViewById(R.id.movie1);
        final Button start_movie = (Button)findViewById(R.id.start_movie);
        start_movie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (setBaseInfo(R.id.movie_pub, R.id.movie_media)) {
                    click(R.id.movie_sec, new Handler() {
                        @Override
                        public void dispatchMessage(final Message msg) {
                            try {
                                int sec = (Integer) msg.obj;
                                int pub = Integer.valueOf(((EditText) findViewById(R.id.movie_pub)).getText().toString());
                                int media = Integer.valueOf(((EditText) findViewById(R.id.movie_media)).getText().toString());
                                int areaWidth = movieArea.getWidth();
                                int areaHeight = movieArea.getHeight();
                                debug("areaWidth : "+areaWidth);
                                debug("areaHeight : "+areaHeight);
                                //(final Context c, final int p, final int m, final int s, final String mode, final RelativeLayout bannerArea, final int w, final int h) {
                                Navimanager.getInstance().requestMovie(start_movie, MainActivity.this, pub, media, sec, mode, movieArea, areaWidth, areaHeight, autoPlayValue, autoReplayValue, clickFullAreaValue, mutedValue, soundBtnShowValue, clickBtnShowValue, skipBtnShowValue, closeShowValue);
                            }catch (Exception e){
                                debug("접근방식이 올바르지 않습니다!");
                            }
                        }
                    });
                }
            }
        });//(final Context c, final int p, final int m, final int s, final String mode, final int w, final int h) {
        movie1.setVisibility(View.VISIBLE);


        LinearLayout movie2 = (LinearLayout) findViewById(R.id.movie2);
        final Button start_movie2 = (Button)findViewById(R.id.start_movie2);
        start_movie2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (setBaseInfo(R.id.movie_pub2, R.id.movie_media2)) {
                    click(R.id.movie_sec2, new Handler() {
                        @Override
                        public void dispatchMessage(final Message msg) {
                            try {
                                int sec = (Integer) msg.obj;
                                int pub = Integer.valueOf(((EditText) findViewById(R.id.movie_pub2)).getText().toString());
                                int media = Integer.valueOf(((EditText) findViewById(R.id.movie_media2)).getText().toString());
                                int areaWidth = movieArea2.getWidth();
                                int areaHeight = movieArea2.getHeight();
                                debug("areaWidth : "+areaWidth);
                                debug("areaHeight : "+areaHeight);
                                //(final Context c, final int p, final int m, final int s, final String mode, final RelativeLayout bannerArea, final int w, final int h) {
                                Navimanager.getInstance().requestMovie(start_movie2,MainActivity.this, pub, media, sec, mode, movieArea2, areaWidth, areaHeight, autoPlayValue2, autoReplayValue2, clickFullAreaValue2, mutedValue2, soundBtnShowValue2, clickBtnShowValue2, skipBtnShowValue2, closeShowValue2);
                            }catch (Exception e){
                                debug("접근방식이 올바르지 않습니다!");
                            }
                        }
                    });
                }
            }
        });//(final Context c, final int p, final int m, final int s, final String mode, final int w, final int h) {
        movie2.setVisibility(View.VISIBLE);
    }

    private boolean setBaseInfo(int pubid, int mediaid) {
        if (((EditText) findViewById(pubid)).getText().toString().trim().length() > 0) {
            if (((EditText) findViewById(mediaid)).getText().toString().trim().length() > 0) {
                return true;
            } else {
                debug("MEDIA 입력!");
            }
        } else {
            debug("PUB 입력!");
        }
        return false;
    }

    private void click(int sectionId, Handler handler) {
        String secStr = ((EditText) findViewById(sectionId)).getText().toString();
        try {
            if (secStr.trim().length() > 0) {
                int sec = Integer.valueOf(secStr);
                Message msg = new Message();
                msg.obj = sec;
                handler.dispatchMessage(msg);
            } else {
                debug("SECTION 입력!");
            }
        } catch (Exception e) {
            debug("SECTION 입력이 올바르지 않습니다!");
        }

    }

    private void debug(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        MzLog.d(msg);
    }
}
