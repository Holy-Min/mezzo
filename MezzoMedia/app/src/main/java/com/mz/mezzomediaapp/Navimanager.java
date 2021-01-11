package com.mz.mezzomediaapp;

import com.mezzomedia.common.MZDisplayUtil;
import com.mezzomedia.common.MzLog;
import com.mezzomedia.man.AdConfig;
import com.mezzomedia.man.AdEvent;
import com.mezzomedia.man.AdListener;
import com.mezzomedia.man.AdResponseCode;
import com.mezzomedia.man.data.AdData;
import com.mezzomedia.man.view.AdManPage;
import com.mezzomedia.man.view.AdManView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Navimanager {
    private static Navimanager instance = null;
    private AdManView bannerView = null;
    private AdManView inlayoutInter = null;

    private AdManView adManEnding = null;
    private Dialog alertDialog;

    private AdManPage adManInterActivity = null;

    public static Navimanager getInstance() {
        if (instance == null) {
            instance = new Navimanager();
        }
        return instance;
    }

    public void onDestoryBanner(final Context c) {
        if (bannerView != null) {
            bannerView.onDestroy();
        }
        bannerView = null;
    }

    public void destroyEnding() {
        if(adManEnding != null){
            adManEnding.onDestroy();
        }

        if(alertDialog != null){
            alertDialog.dismiss();
        }
        adManEnding = null;
        alertDialog = null;
    }

//    public void destroyMovie(){
//        if(movieView!=null){
//            movieView.onDestroy();
//        }
//        movieView = null;
//    }

    public void destroyInter() {
		if (inlayoutInter != null) {
            inlayoutInter.onDestroy();
            inlayoutInter = null;
		}

        if (adManInterActivity != null) {
            adManInterActivity.onDestroy();
            adManInterActivity = null;
        }
    }

    public void requestMovie(final Context c, final int p, final int m, final int s, final RelativeLayout bannerArea, final int w, final int h, String autoPlay, final String autoReplay, String clickFullArea, String muted, String soundBtnShow, String clickBtnShow, String skipBtnShow, String showClose) {
        bannerArea.removeAllViews();
        AdData adData = new AdData();
        String appName = "appname";
        try {
            appName = (String) c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int width = (int) MZDisplayUtil.convertPixelsToDp(c, w);
        int height = (int) MZDisplayUtil.convertPixelsToDp(c, h);
        adData.major("movie", AdConfig.API_MOVIE, p, m, s, "http://www.storeurl.com", c.getPackageName(), appName, width, height);
        adData.minor("0", "40", "mezzo", "geon-jin.mun@cj.net");
        adData.movie(autoPlay, autoReplay, clickFullArea, muted, soundBtnShow, clickBtnShow, skipBtnShow);
        adData.setIsCloseShow(showClose);
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        final AdManView movieView = new AdManView(c);
        movieView.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, String jsonDataString) {
                Navimanager.getInstance().onAdSuccessCode(type, status, jsonDataString,  new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                        ((Activity) c).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AdResponseCode.Status.SUCCESS.equals(status)) {
                                    if(movieView.getParent() != null){
                                        ((ViewGroup) movieView.getParent()).removeView(movieView);
                                    }
                                    movieView.addBannerView(bannerArea);
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                if(movieView!=null){
                    movieView.onDestroy();
                }
                Navimanager.getInstance().onAdFailCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                if(movieView!=null){
                    movieView.onDestroy();
                }
                Navimanager.getInstance().onAdErrorCode(type, status, failingUrl, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {
                if(AdEvent.Type.CLICK.equals(type)){
                    if(movieView!=null){
                        movieView.onDestroy();
                    }
                    bannerArea.removeAllViews();
                }else if(AdEvent.Type.CLOSE.equals(type)){
                    if(movieView!=null){
                        movieView.onDestroy();
                    }
                    bannerArea.removeAllViews();
                }else if(AdEvent.Type.COMPLETE.equals(type)){

                }else if(AdEvent.Type.IMP.equals(type)){

                }else if(AdEvent.Type.SKIP.equals(type)){
                    if(movieView!=null){
                        movieView.onDestroy();
                    }
                    bannerArea.removeAllViews();
                }else if(AdEvent.Type.START.equals(type)){

                }else if(AdEvent.Type.FIRSTQ.equals(type)){

                }else if(AdEvent.Type.MIDQ.equals(type)){

                }else if(AdEvent.Type.THIRDQ.equals(type)){

                }else if(AdEvent.Type.OBJSHOW.equals(type)){

                }else if(AdEvent.Type.OBJHIDE.equals(type)){

                }else if(AdEvent.Type.ENDED.equals(type)){
                    if(autoReplay == AdConfig.NOT_USED){
                        if(movieView!=null){
                            movieView.onDestroy();
                        }
                        bannerArea.removeAllViews();
                    }
                }

                Navimanager.getInstance().onAdEvent(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionSetting(Object v, String id) {
                showSettingsAlert(c);
            }
        });
        movieView.request(new Handler());

    }
    public void requestBanner(final Context c, final int p, final int m, final int s, final ViewGroup bannerArea) {
        bannerArea.removeAllViewsInLayout();
        AdData adData = new AdData();
        String appName = "appname";
        try {
            appName = (String) c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        adData.major("testbanner", AdConfig.API_BANNER, p, m, s, "http://www.storeurl.com", c.getPackageName(), appName, 320, 100);
        adData.minor("0", "40", "mezzo", "geonjin.mun@cj.net");
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        bannerView = new AdManView(c);
        bannerView.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, final String jsonDataString) {
                Navimanager.getInstance().onAdSuccessCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                        ((Activity) c).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AdResponseCode.Status.SUCCESS.equals(status)) {
                                    bannerView.addBannerView(bannerArea);
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                bannerView.onDestroy();
                Navimanager.getInstance().onAdFailCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                bannerView.onDestroy();
                Navimanager.getInstance().onAdErrorCode(type, status, failingUrl, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {
                if(AdEvent.Type.CLICK.equals(type)){
                    bannerView.onDestroy();
                }else if(AdEvent.Type.CLOSE.equals(type)){
                    bannerView.onDestroy();
                }else if(AdEvent.Type.IMP.equals(type)){

                }
                Navimanager.getInstance().onAdEvent(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionSetting(Object v, String id) {
                showSettingsAlert(c);
            }
        });
        bannerView.request(new Handler());

    }

    public void requestInlayoutInter(final Context c, final int p, final int m, final int s,  final RelativeLayout bannerArea) {
        bannerArea.removeAllViewsInLayout();
        AdData adData = new AdData();
        String appName = "appname";
        try {
            appName = (String) c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        adData.major("testInter", AdConfig.API_INTER, p, m, s, "http://www.storeurl.com", c.getPackageName(), appName, 320, 480);
        adData.minor("0", "40", "mezzo", "geonjin.mun@cj.net");
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        adData.setIsInLayout(AdConfig.USED);
        inlayoutInter = new AdManView(c);
        inlayoutInter.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, final String jsonDataString) {
                Navimanager.getInstance().onAdSuccessCode( type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                        ((Activity) c).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AdResponseCode.Status.SUCCESS.equals(status)) {
                                    if(inlayoutInter.getParent() != null){
                                        ((ViewGroup) inlayoutInter.getParent()).removeView(inlayoutInter);
                                    }
                                    inlayoutInter.addBannerView(bannerArea);
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
               // inlayoutInter.onDestroy();
                Navimanager.getInstance().onAdFailCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
             //   inlayoutInter.onDestroy();
                Navimanager.getInstance().onAdErrorCode(type, status, failingUrl, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {
                if(AdEvent.Type.CLICK.equals(type)){
              //      inlayoutInter.onDestroy();
                }else if(AdEvent.Type.CLOSE.equals(type)){
              //      inlayoutInter.onDestroy();
                }else if(AdEvent.Type.IMP.equals(type)){

                }
                Navimanager.getInstance().onAdEvent(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionSetting(Object v, String id) {
                showSettingsAlert(c);
            }
        });
        inlayoutInter.request(new Handler());
    }


    public void requestInter(final Context c, final int p, final int m, final int s, final String isPopup) {
        AdData adData = new AdData();
        String appName = "appname";
        try {
            appName = (String) c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        adData.major("testInter", AdConfig.API_INTER, p, m, s, "http://www.storeurl.com", c.getPackageName(), appName, 0, 0);
        adData.minor("0", "40", "mezzo", "geonjin.mun@cj.net");
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        adData.setPopup(isPopup);
        adData.setIsInLayout(AdConfig.NOT_USED);
        adManInterActivity = new AdManPage(c);
        adManInterActivity.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, String type, String status, String jsonDataString) {
                Navimanager.getInstance().onAdSuccessCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                Navimanager.getInstance().onAdFailCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                if(AdEvent.Type.CLICK.equals(type)){
                    //adManInterActivity.onDestroy();
                }else if(AdEvent.Type.CLOSE.equals(type)){
                   // adManInterActivity.onDestroy();
                }else if(AdEvent.Type.IMP.equals(type)){

                }
                Navimanager.getInstance().onAdErrorCode(type, status, failingUrl, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {
                Navimanager.getInstance().onAdEvent(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionSetting(Object v, String id) {
                showSettingsAlert(c);
            }
        });
        adManInterActivity.request(new Handler());
    }

    public void requestEnding(final Context c, final int p, final int m, final int s, final int w, final int h) {
        AdData adData = new AdData();
        String appName = "appname";
        try {
            appName = (String) c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int width = (int) MZDisplayUtil.convertPixelsToDp(c, w);
        int height = (int) MZDisplayUtil.convertPixelsToDp(c, h);
        adData.major("testInter", AdConfig.API_ENDING, p, m, s, "http://www.storeurl.com", c.getPackageName(), appName, width, height);
        adData.minor("0", "40", "mezzo", "geonjin.mun@cj.net");
        adData.isPermission(AdConfig.NOT_USED, AdConfig.NOT_USED);
        adManEnding = new AdManView(c);
        adManEnding.setData(adData, new AdListener() {
            @Override
            public void onAdSuccessCode(Object v, String id, final String type, final String status, final String jsonDataString) {
                Navimanager.getInstance().onAdSuccessCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                        ((Activity) c).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (AdResponseCode.Status.SUCCESS.equals(status)) {
                                    showEndingBanner(c, w, h);
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onAdFailCode(Object v, String id, String type, String status, String jsonDataString) {
                adManEnding.onDestroy();
                Navimanager.getInstance().onAdFailCode(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdErrorCode(Object v, String id, String type, String status, String failingUrl) {
                Navimanager.getInstance().onAdErrorCode(type, status, failingUrl, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAdEvent(Object v, String id, String type, String status, String jsonDataString) {
                if(AdEvent.Type.CLICK.equals(type)){
                   // adManEnding.onDestroy();
                }else if(AdEvent.Type.CLOSE.equals(type)){
                    adManEnding.onDestroy();
                }else if(AdEvent.Type.IMP.equals(type)){

                }
                Navimanager.getInstance().onAdEvent(type, status, jsonDataString, new Handler() {
                    @Override
                    public void dispatchMessage(Message msg) {
                        String log = String.valueOf(msg.obj);
                        Toast.makeText(c, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionSetting(Object v, String id) {
                showSettingsAlert(c);
            }
        });
        adManEnding.request(new Handler());
    }

    public void onAdSuccessCode(String type, String status, String jsonDataString, Handler handler) {
        MzLog.d("onAdSuccessCode type : " + type);
        MzLog.d("onAdSuccessCode status : " + status);
        MzLog.d("onAdSuccessCode jsonDataString : " + jsonDataString);
        Message msg = new Message();
        if(AdResponseCode.Type.HOUSE.equals(type)){
            msg.obj = "onAdSuccessCode [ " + type + " ] " + "광고 성공 : 무료";
            handler.dispatchMessage(msg);
        }else{
            msg.obj = "onAdSuccessCode [ " + type + " ] " + "광고 성공 : 유료";
            handler.dispatchMessage(msg);
        }
    }

    public void onAdFailCode(String type, String status, String jsonDataString, Handler handler) {
        MzLog.d("onAdFailCode type : " + type);
        MzLog.d("onAdFailCode status : " + status);
        MzLog.d("onAdFailCode jsonDataString : " + jsonDataString);
        Message msg = new Message();
        if(AdResponseCode.Status.NOAD.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "NOAD";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.TIMEOUT.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "TIMEOUT";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.ERROR_PARSING.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "ERROR_PARSING";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.DUPLICATIONCALL.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "DUPLICATIONCALL";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.ERROR.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "ERROR";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.ERROR_NOTSUPPORT_BROWSER.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "ERROR_NOTSUPPORT_BROWSER";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.ERROR_NOTSUPPORT_IOS.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "ERROR_NOTSUPPORT_IOS";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.ERROR_NOTSUPPORT_ANDROID.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "ERROR_NOTSUPPORT_ANDROID";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.NEEDSYNC.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "NEEDSYNC";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.DEVICE_NETWORK_ERROR.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "DEVICE_NETWORK_ERROR";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.DEVICE_RENDERING_TIMEOUT.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "DEVICE_RENDERING_TIMEOUT";
            handler.dispatchMessage(msg);
        }else if(AdResponseCode.Status.DEVICE_SETTING_ERROR.equals(status)){
            msg.obj = "onAdFailCode [ " + status + " ] " + "DEVICE_SETTING_ERROR";
            handler.dispatchMessage(msg);
        }else {
            msg.obj = "onAdFailCode [ " + status + " ] " + "etc";
            handler.dispatchMessage(msg);        	
        }
    }

    public void onAdErrorCode(String type, String status, String failingUrl, Handler handler) {
        MzLog.d("onAdErrorCode type : " + type);
        MzLog.d("onAdErrorCode status : " + status);
        MzLog.d("onAdErrorCode failingUrl : " + failingUrl);
        Message msg = new Message();
        int errorCode = Integer.valueOf(type);
        switch (errorCode) {
            case WebViewClient.ERROR_AUTHENTICATION:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_AUTHENTICATION";
                handler.dispatchMessage(msg);
                break;               // 서버에서 사용자 인증 실패
            case WebViewClient.ERROR_BAD_URL:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_BAD_URL";
                handler.dispatchMessage(msg);
                break;                           // 잘못된 URL
            case WebViewClient.ERROR_CONNECT:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_CONNECT";
                handler.dispatchMessage(msg);
                break;                          // 서버로 연결 실패
            case WebViewClient.ERROR_FAILED_SSL_HANDSHAKE:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_FAILED_SSL_HANDSHAKE";
                handler.dispatchMessage(msg);
                break;    // SSL handshake 수행 실패
            case WebViewClient.ERROR_FILE:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_FILE";
                handler.dispatchMessage(msg);
                break;                                  // 일반 파일 오류
            case WebViewClient.ERROR_FILE_NOT_FOUND:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_FILE_NOT_FOUND";
                handler.dispatchMessage(msg);
                break;               // 파일을 찾을 수 없습니다
            case WebViewClient.ERROR_HOST_LOOKUP:
                msg.obj = "onAdErrorCode " + errorCode + " ] " + "ERROR_HOST_LOOKUP";
                handler.dispatchMessage(msg);
                break;           // 서버 또는 프록시 호스트 이름 조회 실패
            case WebViewClient.ERROR_IO:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_IO";
                handler.dispatchMessage(msg);
                break;                              // 서버에서 읽거나 서버로 쓰기 실패
            case WebViewClient.ERROR_PROXY_AUTHENTICATION:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_PROXY_AUTHENTICATION";
                handler.dispatchMessage(msg);
                break;   // 프록시에서 사용자 인증 실패
            case WebViewClient.ERROR_REDIRECT_LOOP:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_REDIRECT_LOOP";
                handler.dispatchMessage(msg);
                break;               // 너무 많은 리디렉션
            case WebViewClient.ERROR_TIMEOUT:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_TIMEOUT";
                handler.dispatchMessage(msg);
                break;                          // 연결 시간 초과
            case WebViewClient.ERROR_TOO_MANY_REQUESTS:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_TOO_MANY_REQUESTS";
                handler.dispatchMessage(msg);
                break;     // 페이지 로드중 너무 많은 요청 발생
            case WebViewClient.ERROR_UNKNOWN:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_UNKNOWN";
                handler.dispatchMessage(msg);
                break;                        // 일반 오류
            case WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_UNSUPPORTED_AUTH_SCHEME";
                handler.dispatchMessage(msg);
                break; // 지원되지 않는 인증 체계
            case WebViewClient.ERROR_UNSUPPORTED_SCHEME:
                msg.obj = "onAdErrorCode[ " + errorCode + " ] " + "ERROR_UNSUPPORTED_SCHEME";
                handler.dispatchMessage(msg);
                break;          // URI가 지원되지 않는 방식
        }
    }

    public void onAdEvent(String type, String status, String jsonDataString, Handler handler) {
        MzLog.d("onAdEvent type : " + type);
        MzLog.d("onAdEvent status : " + status);
        MzLog.d("onAdEvent jsonDataString : " + jsonDataString);
        Message msg = new Message();
        if(AdEvent.Type.CLICK.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "CLICK";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.CLOSE.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "CLOSE";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.COMPLETE.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "COMPLETE";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.IMP.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "IMP";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.SKIP.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "SKIP";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.START.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "START";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.FIRSTQ.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "FIRSTQ";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.MIDQ.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "MIDQ";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.THIRDQ.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "THIRDQ";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.OBJSHOW.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "OBJSHOW";
            handler.dispatchMessage(msg);
        }else if(AdEvent.Type.OBJHIDE.equals(type)){
            msg.obj = "onAdEvent[ " + type + " ] " + "OBJHIDE";
            handler.dispatchMessage(msg);
        }
    }

    public void showSettingsAlert(final Context c) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅은 매체에서 구현필요.\n 매체구현단");
        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                c.startActivity(intent);
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void showEndingBanner(final Context c, final int w, final int h){
        AlertDialog.Builder alert = new AlertDialog.Builder(c);
        alertDialog = alert.create();
        Button button = new Button(c);
        button.setTextColor(Color.parseColor("#FFA500"));
        button.setBackgroundColor(Color.BLACK);
        button.setText("취소");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 1;
        button.setLayoutParams(buttonParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyEnding();
            }
        });

        Button button2 = new Button(c);
        button2.setText("종료");
        button2.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams buttonParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams2.weight = 1;
        button2.setLayoutParams(buttonParams2);
        button2.setTextColor(Color.parseColor("#FFA500"));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyEnding();
                ((Activity)c).finish();
            }
        });
        button2.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        LinearLayout buttonLayout = new LinearLayout(c);
        LinearLayout.LayoutParams buttonLayoutparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, button2.getMeasuredHeight());
        buttonLayout.setLayoutParams(buttonLayoutparam);
        buttonLayoutparam.topMargin = 20;
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        buttonLayout.addView(button);
        buttonLayout.addView(button2);
        TextView textView = new TextView(c);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setTextColor(Color.parseColor("#FFA500"));
        textView.setMarqueeRepeatLimit(-1);
        textView.setSelected(true);
        textView.setClickable(false);
        ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textViewParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText("종료하시겠습니까?                      종료하시겠습니까?                      종료하시겠습니까?                      종료하시겠습니까?");
        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        buttonLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        LinearLayout root = new LinearLayout(c);
        int realH = h+buttonLayout.getMeasuredHeight()+textView.getMeasuredHeight();
        int realW = w;
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(realW,realH);
        root.setGravity(Gravity.CENTER);
        root.setLayoutParams(rootParams);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout body = new LinearLayout(c);
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(w,h);
        body.setLayoutParams(bodyParams);

        body.addView(adManEnding);
        root.addView(body);
        root.addView(textView);
        root.addView(buttonLayout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.dimAmount = 0.5f;
        lp.width = realW;
        lp.height = realH;
        Window window = alertDialog.getWindow();
        alertDialog.show();
        window.setAttributes(lp);
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.setContentView(root);
    }
}
