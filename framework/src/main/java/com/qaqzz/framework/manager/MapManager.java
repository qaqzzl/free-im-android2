package com.qaqzz.framework.manager;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.qaqzz.framework.utils.LogUtils;

/**
 * FileName: MapManager
 * Founder: LiuGuiLin
 * Profile: 地图管理类
 */
public class MapManager {

    private static volatile MapManager mInstance = null;

    private GeocodeSearch geocodeSearch;

    private OnAddress2PoiGeocodeListener address2poi;
    private OnPoi2AddressGeocodeListener poi2address;

    private MapManager() {

    }

    public static MapManager getInstance() {
        if (mInstance == null) {
            synchronized (MapManager.class) {
                if (mInstance == null) {
                    mInstance = new MapManager();
                }
            }
        }
        return mInstance;
    }

    public void initMap(Context mContext) {
        geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.setOnGeocodeSearchListener(searchListener);
    }

    private GeocodeSearch.OnGeocodeSearchListener searchListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (regeocodeResult != null) {
                    if (poi2address != null) {
                        poi2address.poi2address(regeocodeResult.getRegeocodeAddress()
                                .getFormatAddress());
                    }
                }
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (geocodeResult != null) {
                    if (address2poi != null) {
                        if (geocodeResult.getGeocodeAddressList() != null &&
                                geocodeResult.getGeocodeAddressList().size() > 0) {
                            GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                            address2poi.address2poi(
                                    address.getLatLonPoint().getLatitude(),
                                    address.getLatLonPoint().getLongitude(),
                                    address.getFormatAddress()
                            );
                        }
                    }
                }
            }
        }
    };

    /**
     * 地址转经纬度
     *
     * @param address
     */
    public MapManager address2poi(String address, OnAddress2PoiGeocodeListener listener) {
        this.address2poi = listener;
        GeocodeQuery query = new GeocodeQuery(address, "");
        geocodeSearch.getFromLocationNameAsyn(query);
        return mInstance;
    }

    /**
     * 经纬度转地址
     *
     * @param la
     * @param lo
     */
    public MapManager poi2address(double la, double lo,OnPoi2AddressGeocodeListener listener) {
        this.poi2address = listener;
        RegeocodeQuery query = new RegeocodeQuery(
                new LatLonPoint(la, lo), 3000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        return mInstance;
    }

    public interface OnPoi2AddressGeocodeListener {
        void poi2address(String address);
    }

    public interface OnAddress2PoiGeocodeListener {
        void address2poi(double la, double lo, String address);
    }

    /**
     * 获取静态地图Url
     *
     * @param la
     * @param lo
     * @return
     */
    public String getMapUrl(double la, double lo) {
        String url = "https://restapi.amap.com/v3/staticmap?location=" + lo + "," + la +
                "&zoom=17&scale=2&size=150*150&markers=mid,,A:" + lo + ","
                + la + "&key=" + "389bc08b815e3146bfd1e45fd7f47fc5";
        LogUtils.i("url:" + url);
        return url;
    }
}