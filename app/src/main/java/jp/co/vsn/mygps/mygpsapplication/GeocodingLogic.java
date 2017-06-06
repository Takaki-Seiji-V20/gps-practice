package jp.co.vsn.mygps.mygpsapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodingLogic {
    private static final String GEO_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=false&language=ja";
    private String mLat;
    private String mLon;
    private GeoCallback mGeoCallback;

    public void startGeocode(String lat, String lon, GeoCallback callback) {
        this.mLat = lat;
        this.mLon = lon;
        this.mGeoCallback = callback;
        new Thread(runGeoApi).start();
    }

    private String createUrl(String lat, String lon) {
        return String.format(GEO_URL, lat, lon);
    }

    private Runnable runGeoApi = new Runnable() {

        @Override
        public void run() {
            String geoUrl = createUrl(mLat, mLon);
            try {
                String response = HttpRequestManager.sendGetData(geoUrl);
                String parseAdminArea = parseAdminArea(response);
                mGeoCallback.onComplete(parseAdminArea);
            } catch (Exception e) {
                mGeoCallback.onError();
            }
        }
    };

    public String parseAdminArea(String json) throws JSONException {
        try {
            JSONObject rootJObject = new JSONObject(json);
            JSONArray jsonArray = rootJObject.getJSONArray("results");
            if (jsonArray.length() < 1) {
                new JSONException("no data");
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray2 = jsonObject.getJSONArray("address_components");
            int length = jsonArray2.length();
            if (length < 1) {
                new JSONException("no data");
            }
            JSONObject jsonObject2;
            String adminAreaName = null;
            for (int i = 0; i < length; i++) {
                jsonObject2 = jsonArray2.getJSONObject(i);
                JSONArray object = jsonObject2.getJSONArray("types");
                if (object != null && object.length() > 0) {
                    String type = object.get(0).toString();
                    if (type.equals("administrative_area_level_1")) {
                        adminAreaName = jsonObject2.getString("long_name");
                    }
                }
            }
            return adminAreaName;
        } catch (JSONException e) {
            throw e;
        }
    }

    public static interface GeoCallback {
        public void onComplete(String adminAreaName);

        public void onError();
    }

}
