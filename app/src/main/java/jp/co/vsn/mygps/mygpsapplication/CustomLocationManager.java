package jp.co.vsn.mygps.mygpsapplication;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

public class CustomLocationManager implements LocationListener {
    private LocationManager mLocationManager;
    private LocationCallback mLocationCallback;
    private Handler mHandler = new Handler();

    // private static final int NETWORK_TIMEOUT = 5000;

    public CustomLocationManager(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
    }

    public void getNowLocationData(int delayMillis, LocationCallback locationCallback) {
        this.mLocationCallback = locationCallback;
        mHandler.postDelayed(gpsTimeOutRun, delayMillis);
        startLocation(LocationManager.GPS_PROVIDER);
    }

    // /* 設定項目 */
    // private static final int mAccuracy = Criteria.ACCURACY_FINE;// 位置情報取得の精度
    // private static final int mBearingAccuracy = Criteria.ACCURACY_LOW;// 方位精度
    // private static final int mHorizontalAccuracy = Criteria.ACCURACY_HIGH;// 緯度経度の取得
    // private static final int mVerticalAccuracy = Criteria.NO_REQUIREMENT;// 高度の取得
    // private static final int mPowerLevel = Criteria.NO_REQUIREMENT;// 電力消費レベルの設定
    // private static final int mSpeedAccuracy = Criteria.NO_REQUIREMENT;// 速度の精度
    // private static final boolean isAltitude = false;// 高度の取得の有無
    // private static final boolean isBearing = false;// 方位を取得の有無
    // private static final boolean isCostAllowed = false;// 位置情報取得に関して金銭的なコストの許可
    // private static final boolean isSpeed = false;// 速度を出すかどうか
    //
    // /* 最適なプロバイダーの選択 */
    // private String getBestProvider(boolean enabledOnly) {
    //
    // Criteria criteria = new Criteria();
    //
    // criteria.setAccuracy(mAccuracy);
    // criteria.setBearingAccuracy(mBearingAccuracy);
    // criteria.setAltitudeRequired(isAltitude);
    // criteria.setBearingRequired(isBearing);
    // criteria.setCostAllowed(isCostAllowed);
    // criteria.setHorizontalAccuracy(mHorizontalAccuracy);
    // criteria.setPowerRequirement(mPowerLevel);
    // criteria.setSpeedAccuracy(mSpeedAccuracy);
    // criteria.setSpeedRequired(isSpeed);
    // criteria.setVerticalAccuracy(mVerticalAccuracy);
    //
    // return mLocationManager.getBestProvider(criteria, enabledOnly);
    //
    // }

    private Runnable gpsTimeOutRun = new Runnable() {

        @Override
        public void run() {
            removeUpdate();
            // 不正防止の為、Wifiでの取得を行わない
            // mHandler.postDelayed(networkTimeOutRun, NETWORK_TIMEOUT);
            // startLocation(LocationManager.NETWORK_PROVIDER);
            if (mLocationCallback != null) {
                mLocationCallback.onTimeout();
            }
        }
    };

    private Runnable networkTimeOutRun = new Runnable() {

        @Override
        public void run() {
            removeUpdate();
            if (mLocationCallback != null) {
                mLocationCallback.onTimeout();
            }
        }
    };

    public boolean checkGpsMode() {
        boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    public void startLocation(String provider) {
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    public void removeUpdate() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mHandler.removeCallbacks(gpsTimeOutRun);
        mHandler.removeCallbacks(networkTimeOutRun);
        if (this.mLocationCallback != null) {
            this.mLocationCallback.onComplete(location);
        }
        removeUpdate();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public static interface LocationCallback {
        public void onComplete(Location location);

        public void onTimeout();
    }
}
