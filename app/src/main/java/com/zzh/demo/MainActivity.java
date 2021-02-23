package com.zzh.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.recommendstop.RecommendStopSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 轨迹运行demo展示
 */
public class MainActivity extends AppCompatActivity implements BaiduMap.OnMapStatusChangeListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Polyline mPolyline;
    private Marker mMoveMarker;
    private Handler mHandler;

    private List<Marker> markerList = new ArrayList<>();
    private List<LatLng> startEndPoiList = new ArrayList<>();
    private List<LatLng> lastStartPoiList = new ArrayList<>();

    private LatLng[] startEndPoiLatlngs = new LatLng[2];
    private LatLng[] startLatlngs = new LatLng[1];
    private boolean isFirst = true;

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
//    private static final int TIME_INTERVAL = 80;
//    private static final double DISTANCE = 0.00002;
    private BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png");
    private BitmapDescriptor mBitmapCar = BitmapDescriptorFactory.fromResource(R.drawable.car);


    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 500;
    private static final double DISTANCE = 0.0001;
    private LinkedList<String> mTopicList = new LinkedList<>();
    private ArrayList<CarPosInfo> mCarPosInfoList = new ArrayList<>();
    private Marker[] markerArr;
    private int[] mTopicArrQos = null;
    private Marker marker11;
    OverlayOptions markerOptions11;
    LatLng startLatLng11;
    LatLng endLatLng11;
    int postion;
    boolean isFirstLoc = true;
    PolylineOptions polylineOptions11;
    private Polyline mPolyline11;
    CarPosInfo carPosInfo;
    List<LatLng> polylines = new ArrayList<>();
    LatLng mCenter;
    ImageView iv_center;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_center = findViewById(R.id.iv_center);
        connectMqtt();

        mHandler = new Handler(Looper.getMainLooper());
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(this, savedInstanceState);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
//        MapStatus.Builder builder = new MapStatus.Builder();
//        // builder.target(new LatLng(40.056865, 116.307766));
//        builder.target(new LatLng(31.844146,117.143432)); // 31.84408, 117.143266
//        builder.zoom(19.0f);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

//        MapStatus.Builder builder = new MapStatus.Builder();
//        // builder.target(new LatLng(40.056865, 116.307766));
//        startLatLng11 = new LatLng(31.844146,117.143432);
//        builder.target(startLatLng11); // 31.84408, 117.143266
//        builder.zoom(19.0f);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        // 设置初始中心点为公司附近 31.844146, 117.143432
//        final LatLng center = new LatLng(36.92204734706147, 118.85991819050368);
//        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 18);
//        mBaiduMap.setMapStatus(mapStatusUpdate);
//        mBaiduMap.setOnMapStatusChangeListener(this);
//        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                Log.i("zzz1", "onMapLoaded--");
//            }
//        });
        drawPolyLine();


        // 模拟数据
//        String resultStr = Utils.readAssert(this, "car_test.txt");
//        Map<String, Object> resultMap = StringUtils.transJsonToMap(resultStr);
//        String data = (String) resultMap.get("data");
//        Log.i("zzz1", "data-- " + data);
//        if (!StringUtils.isStrEmpty(data)) {
//            TypeToken<List<CarInfoBean>> typeToken
//                    = new TypeToken<List<CarInfoBean>>() {
//            };
//            List<CarInfoBean> itemList = (List<CarInfoBean>) StringUtils.convertMapToList(data, typeToken);
//            Log.i("zzz1", "itemList.size " + itemList.size());
//
//            if (itemList != null && itemList.size() > 0){
//                moveLooper(itemList);
//            }
//        }
//        getData();

    }


    private void drawPolyLine() {
        List<LatLng> polylines = new ArrayList<>();
        for (int index = 0; index < latlngs.length; index++) {
            polylines.add(latlngs[index]);
        }
        polylines.add(latlngs[0]);

//        polylineOptions = new PolylineOptions().points(polyLines).width(0).color(Color.TRANSPARENT);
        // 绘制纹理PolyLine
//        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).customTexture(mGreenTexture)
//                .dottedLine(true);
        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.TRANSPARENT)
                .dottedLine(true);
        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

        // 添加小车marker
//        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
//                position(polylines.get(0)).rotate((float) getAngle(0));
//        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    }

    private void getData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String resultStr = Utils.readAssert(MainActivity.this, "car_test.txt");
                Map<String, Object> resultMap = StringUtils.transJsonToMap(resultStr);
                String data = (String) resultMap.get("data");
                Log.i("zzz1", "data-- " + data);
                if (!StringUtils.isStrEmpty(data)) {
                    TypeToken<List<CarInfoBean>> typeToken
                            = new TypeToken<List<CarInfoBean>>() {
                    };
                    List<CarInfoBean> itemList = (List<CarInfoBean>) StringUtils.convertMapToList(data, typeToken);
                    Log.i("zzz1", "itemList.size " + itemList.size());

                    if (itemList != null && itemList.size() > 0) {
                        moveLooper(itemList);
                    }
                }
            }
        }, 5000);
//        }
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex, Polyline mVirtureRoad) {
        if (mVirtureRoad != null) {
            if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
                throw new RuntimeException("index out of bonds");
            }
            LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
            LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
            return getAngle(startPoint, endPoint);
        } else {
            return 0;
        }
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        } else if (slope == 0.0) {
            if (toPoint.longitude > fromPoint.longitude) {
                return -90;
            } else {
                return 90;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {
        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmapCar.recycle();
        mGreenTexture.recycle();
        mBaiduMap.clear();
        mMapView.onDestroy();
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE || slope == 0.0) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * 1 / slope) / Math.sqrt(1 + 1 / (slope * slope)));
    }

    /**
     * 计算y方向每次移动的距离
     */
    private double getYMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE || slope == 0.0) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    public void moveLooper(final Marker marker, final LatLng[] latlngs, final int pos) {
        new Thread() {
            public void run() {
//                while (true) {
                Log.i("zzz1", "单个小车move");
                Log.i("zzz1", "latlngs.length " + latlngs.length);
                for (int i = 0; i < latlngs.length - 1; i++) {
                    Log.i("zzz1", "zzzz");
                    final LatLng startPoint = latlngs[i];
                    Log.i("zzz1", "sp" + startPoint.latitude + "--" + startPoint.longitude);
                    final LatLng endPoint = latlngs[i + 1];
                    Log.i("zzz1", "ep" + endPoint.latitude + "--" + endPoint.longitude);
                    marker.setPosition(startPoint);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // refresh marker's rotate
                            if (mMapView == null) {
                                return;
                            }
                            marker.setRotate((float) getAngle(startPoint, endPoint));
                        }
                    });
                    double slope = getSlope(startPoint, endPoint);
                    // 是不是正向的标示
                    boolean isYReverse = (startPoint.latitude > endPoint.latitude);
                    boolean isXReverse = (startPoint.longitude > endPoint.longitude);
                    double intercept = getInterception(slope, startPoint);
                    double xMoveDistance = isXReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);
                    double yMoveDistance = isYReverse ? getYMoveDistance(slope) : -1 * getYMoveDistance(slope);

                    for (double j = startPoint.latitude, k = startPoint.longitude;
                         !((j > endPoint.latitude) ^ isYReverse) && !((k > endPoint.longitude) ^ isXReverse); ) {
                        LatLng latLng = null;

                        if (slope == Double.MAX_VALUE) {
                            latLng = new LatLng(j, k);
                            j = j - yMoveDistance;
                        } else if (slope == 0.0) {
                            latLng = new LatLng(j, k - xMoveDistance);
                            k = k - xMoveDistance;
                        } else {
                            latLng = new LatLng(j, (j - intercept) / slope);
                            j = j - yMoveDistance;
                        }

                        final LatLng finalLatLng = latLng;
                        if (finalLatLng.latitude == 0 && finalLatLng.longitude == 0) {
                            continue;
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mMapView == null) {
                                    return;
                                }
                                marker.setPosition(finalLatLng);
                                // 上一次的end存为下一次的start
                                lastStartPoiList.set(pos, finalLatLng);
                                // 设置 Marker 覆盖物的位置坐标,并同步更新与Marker关联的InfoWindow的位置坐标.
                                marker.setPositionWithInfoWindow(finalLatLng);
                            }
                        });
                        try {
                            Thread.sleep(TIME_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
//            }

        }.start();
    }

    public void moveLooper(final List<CarInfoBean> carInfoBeanList) {
        new Thread() {
            public void run() {
//                while (isFirst) {
                if (isFirst) {
                    for (int i = 0; i < carInfoBeanList.size(); i++) {
                        // 添加小车marker
                        String latitude = carInfoBeanList.get(i).getLatitude().trim();
                        String longitude = carInfoBeanList.get(i).getLongitude().trim();

                        double latitudeValue = Double.parseDouble(latitude);
                        double longitudeValue = Double.parseDouble(longitude);
                        LatLng latLng0 = new LatLng(latitudeValue, longitudeValue);
                        Log.i("zzz1", "latitude-- list " + latLng0.latitude);
                        Log.i("zzz1", "longitude-- list " + latLng0.longitude);
                        lastStartPoiList.add(latLng0);

                        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
                                position(latLng0).rotate((float) getAngle(0));
                        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
                        markerList.add(marker);
                        Log.i("zzz1", "moveLooper-- first");
//                            startEndPoiList.add(latLng0);
                    }
                    isFirst = false;
                } else {
                    Log.i("zzz1", "moveLooper-- second");
                    for (int i = 0; i < carInfoBeanList.size(); i++) {
                        // 添加小车marker
                        String latitude = carInfoBeanList.get(i).getLatitude().trim();
                        String longitude = carInfoBeanList.get(i).getLongitude().trim();
                        double latitudeValue = Double.parseDouble(latitude);
                        double longitudeValue = Double.parseDouble(longitude);
                        LatLng latLngEnd = new LatLng(latitudeValue, longitudeValue);
//                            startEndPoiList.add(latLngEnd);
                        // 移动小车
                        startEndPoiLatlngs[0] = lastStartPoiList.get(i);
                        startEndPoiLatlngs[1] = latLngEnd;
                        moveLooper(markerList.get(i), startEndPoiLatlngs, i);
                    }
                }
            }
//            }

        }.start();
    }


    private static final LatLng[] latlngs = new LatLng[]{
            new LatLng(31.844146, 117.143432), //31.844146, 117.143432
            new LatLng(31.844092, 117.143648), // 31.844092, 117.143648
            new LatLng(31.844076, 117.143809), // 31.844076, 117.143809
            new LatLng(31.844061, 117.143195), // 31.844061, 117.143195
            new LatLng(31.844038, 117.143477), // 31.844038, 117.143477
            new LatLng(31.844023, 117.14557), // 31.844023, 117.14557
            new LatLng(31.843992, 117.145929), // 31.843992, 117.145929

            new LatLng(31.8439, 117.146104), // 31.8439, 117.146104
            new LatLng(31.844141, 117.146131), // 31.844141, 117.146131
            new LatLng(31.844456, 117.146131), // 31.844456, 117.146131
            new LatLng(31.844686, 117.146104), // 31.844686, 117.146104
            new LatLng(31.845054, 117.146113), // 31.845054, 117.146113
            new LatLng(31.845039, 117.14614), //  31.845039, 117.14614
            new LatLng(31.845591, 117.146131), //  31.845591, 117.146131
            new LatLng(31.845874, 117.146203), //  31.845874, 117.146203
            new LatLng(31.846296, 117.146104), //  31.846296, 117.146104
    };


    // 连接并接收服务器推送消息
    private void connectMqtt() {
        // 读取订阅toplist
        initTopicList();

        String serverURI = "tcp://10.5.4.27:1883";
        String clientId = "client-yuchao-20150910-2020091001";
        String username = "{'routerIdentification':'8722@3400@201909221526909'}";
        String password = "wlw123456";
//        String topic = "津C3Y0X9";
        int qos = 1;

        try {
            final MqttClient client = new MqttClient(serverURI, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            options.setMqttVersion(4);
            options.setAutomaticReconnect(true);
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
//                    log.info("connectComplete {} {}", reconnect, serverURI);
//                    LogUtil.i("connectComplete", "reconnect " + reconnect + "serverURI " + serverURI);
                    Log.i("zzz1", "connectComplete " + "reconnect " + reconnect + "serverURI " + serverURI);
                }

                //                @SneakyThrows
                @Override
                public void connectionLost(Throwable cause) {
//                    log.info("this method is called when the connection to the server is lost");
//                    LogUtil.i("connectionLost", "this method is called when the connection to the server is lost ");
                    Log.i("zzz1", "connectionLost");

                    try {
                        Thread.sleep(500);
                        Log.i("zzz1", "reconnect");
                        client.reconnect();
                    } catch (Exception e) {

                    }
                }

                @Override

                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    String message = new String(mqttMessage.getPayload(), Charset.defaultCharset());
//                    log.info(message);
//                    LogUtil.i("messageArrived", "" + message);
                    Log.i("zzz1", "messageArrived " + message);
                    // {"carno":"津C3Y0X9",
                    // "latitude":"30.611424591212",
                    // "location":"安徽省黄山市屯溪区Y051奕途玉雕工作室附近1米",
                    // "longitude":"114.35221311345","status":"行驶","time":"2020-09-24 14:17:25"}
                    if (!StringUtils.isStrEmpty(message)) {
                        Map<String, Object> resultMap = StringUtils.transJsonToMap(message);
                        String carno = (String) resultMap.get("carno");
                        String latitude = (String) resultMap.get("latitude");
                        String longitude = (String) resultMap.get("longitude");
                        double latitudeValue = Double.parseDouble(latitude);
                        double longitudeValue = Double.parseDouble(longitude);
                        final LatLng latLngEnd = new LatLng(latitudeValue, longitudeValue);
                        Log.i("zzz1", "latitude recev " + latLngEnd.latitude);
                        Log.i("zzz1", "longitude recev " + latLngEnd.longitude);
                        Log.i("zzz1", "carno recev " + carno);
                        for (int i = 0; i < mCarPosInfoList.size(); i++) {
                            Log.i("zzz1", "zzz --- ");
                            if (carno.equals(mCarPosInfoList.get(i).getCarno())) {
                                postion = i;
                                Log.i("zzz1", "postion --- " + postion);
                                break;
                            } else {
                            }
                        }
                        carPosInfo = mCarPosInfoList.get(postion);
                        boolean isFirst = carPosInfo.isFirst();
                        Log.i("zzz1", "isFirst--" + isFirst);
                        final List<LatLng> polylines = carPosInfo.getPolylines();

                        if (isFirst) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }

                                    startLatLng11 = latLngEnd;
                                    // 不是第一次定位中心点 && 第一回调定位
                                    if (!isFirstLoc) {
                                        double dis = DistanceUtil.getDistance(startLatLng11, mCenter);
                                        Log.i("zzz1", "Distance " + dis);
                                        if (dis > 0 && dis < 1000) {
                                            polylines.clear();
                                            polylines.add(startLatLng11);
                                            polylines.add(latLngEnd);
                                            polylineOptions11 = new PolylineOptions().points(polylines).width(10).color(Color.TRANSPARENT)
                                                    .dottedLine(true);
                                            mPolyline11 = (Polyline) mBaiduMap.addOverlay(polylineOptions11);

                                            markerOptions11 = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
                                                    position(startLatLng11).rotate((float) getAngle(0, mPolyline11));
                                            Marker marker11 = (Marker) mBaiduMap.addOverlay(markerOptions11);
                                            marker11.setPosition(startLatLng11);
                                            markerArr[postion] = marker11;
                                        } else {
                                            return;
                                        }
                                    } else { // 第一次定位中心点
                                        polylines.clear();
                                        polylines.add(startLatLng11);
                                        polylines.add(latLngEnd);
                                        polylineOptions11 = new PolylineOptions().points(polylines).width(10).color(Color.TRANSPARENT)
                                                .dottedLine(true);
                                        mPolyline11 = (Polyline) mBaiduMap.addOverlay(polylineOptions11);

                                        markerOptions11 = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
                                                position(startLatLng11).rotate((float) getAngle(0, mPolyline11));
                                        Marker marker11 = (Marker) mBaiduMap.addOverlay(markerOptions11);
                                        marker11.setPosition(startLatLng11);
                                        markerArr[postion] = marker11;
                                    }


                                    if (isFirstLoc) {
                                        MapStatus.Builder builder = new MapStatus.Builder();
                                        // builder.target(new LatLng(40.056865, 116.307766));
//                                        startLatLng11 = new LatLng(31.844146,117.143432);
                                        builder.target(startLatLng11); // 31.84408, 117.143266
                                        builder.zoom(16.0f); // 19.0
                                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                        // 第一次center
                                        iv_center.setVisibility(View.VISIBLE);
                                        mCenter = startLatLng11;
//                                        final LatLng center = new LatLng(36.92204734706147, 118.85991819050368);
                                        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(startLatLng11, 16.0f);
                                        mBaiduMap.setMapStatus(mapStatusUpdate);
                                        mBaiduMap.setOnMapStatusChangeListener(MainActivity.this);
                                        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
                                            @Override
                                            public void onMapLoaded() {
                                                Log.i("zzz1", "onMapLoaded--");
                                            }
                                        });
                                        isFirstLoc = false;
                                    }

                                    carPosInfo = mCarPosInfoList.get(postion);
                                    carPosInfo.setFirst(false);
                                    carPosInfo.setStartLatlng(startLatLng11);
                                    polylines.remove(0);
                                }
                            });
                        } else {
                            Log.i("zzz1", "polylines " + polylines.size());
                            if (polylines.size() > 7) {
                                polylineOptions11 = new PolylineOptions().points(polylines).width(10).color(Color.TRANSPARENT)
                                        .dottedLine(true);
                                mPolyline11 = (Polyline) mBaiduMap.addOverlay(polylineOptions11);
                                // 移动对应的车
                                moveLooper1(postion, markerArr[postion], polylines);
                            } else {
                                polylines.add(latLngEnd);
                            }
                        }

                    }
                }

                @Override

                public void deliveryComplete(IMqttDeliveryToken token) {
//                    log.info("Called when delivery for a message has been completed");
//                    LogUtil.i("deliveryComplete", "" + token);
                    Log.i("zzz1", "deliveryComplete " + token);
                }
            });

            try {
                client.connect(options);
                Log.i("zzz1", "start connect");
                String[] topicArray = mTopicList.toArray(new String[mTopicList.size()]);
                client.subscribe(topicArray, mTopicArrQos);
            } catch (Exception ex) {
//                log.error("Connection Cause Exception {}", ex.toString());
//                LogUtil.i("Connection Cause Exception {}", "" + ex.toString());
                Log.i("zzz1", "connect Exception" + ex.toString());
            }
        } catch (Exception e) {
            Log.i("zzz1", "Exception1  " + e.toString());
        }

    }

    public void moveLooper1(final int postion, final Marker marker, final List<LatLng> polylines) {
        new Thread() {
            public void run() {
//                while (true) {

                for (int i = 0; i < polylines.size() - 1; i++) {
                    count = i;
                    final LatLng startPoint = polylines.get(i);
                    final LatLng endPoint = polylines.get(i + 1);
                    Log.i("zzz1", "move startPoint" + startPoint.latitude + "--" + startPoint.longitude);
                    Log.i("zzz1", "move endPoint" + endPoint.latitude + "--" + endPoint.longitude);
                    marker.setPosition(startPoint);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // refresh marker's rotate
                            if (mMapView == null) {
                                return;
                            }
                            marker.setRotate((float) getAngle(startPoint, endPoint));
                        }
                    });
                    double slope = getSlope(startPoint, endPoint);
                    // 是不是正向的标示
                    boolean isYReverse = (startPoint.latitude > endPoint.latitude);
                    boolean isXReverse = (startPoint.longitude > endPoint.longitude);
                    double intercept = getInterception(slope, startPoint);
                    double xMoveDistance = isXReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);
                    double yMoveDistance = isYReverse ? getYMoveDistance(slope) : -1 * getYMoveDistance(slope);

                    for (double j = startPoint.latitude, k = startPoint.longitude;
                         !((j > endPoint.latitude) ^ isYReverse) && !((k > endPoint.longitude) ^ isXReverse); ) {
                        LatLng latLng = null;

                        if (slope == Double.MAX_VALUE) {
                            latLng = new LatLng(j, k);
                            j = j - yMoveDistance;
                        } else if (slope == 0.0) {
                            latLng = new LatLng(j, k - xMoveDistance);
                            k = k - xMoveDistance;
                        } else {
                            latLng = new LatLng(j, (j - intercept) / slope);
                            j = j - yMoveDistance;
                        }

                        final LatLng finalLatLng = latLng;
                        if (finalLatLng.latitude == 0 && finalLatLng.longitude == 0) {
                            continue;
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mMapView == null) {
                                    return;
                                }
                                marker.setPosition(finalLatLng);
                                if (count == polylines.size() - 2) {
                                    CarPosInfo carPosInfo = mCarPosInfoList.get(postion);
                                    carPosInfo.setStartLatlng(finalLatLng);
                                    carPosInfo.getPolylines().clear();
                                    carPosInfo.getPolylines().add(finalLatLng);
                                    count = 0;
                                }
                                // 设置 Marker 覆盖物的位置坐标,并同步更新与Marker关联的InfoWindow的位置坐标.
//                                marker.setPositionWithInfoWindow(finalLatLng);
                            }
                        });
                        try {
                            Thread.sleep(TIME_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
//            }

        }.start();
    }

    private void initTopicList() {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("carno.txt"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
//                Log.i("zzz1", "" + line );
//                Log.i("zzz1", "" + line.equals("渝B109PO"));
                mTopicList.add(line);
            }

            Log.i("zzz1", "mTopicList.size " + mTopicList.size());
            mTopicArrQos = new int[mTopicList.size()];
            markerArr = new Marker[mTopicList.size()];
            for (int i = 0; i < mTopicList.size(); i++) {
                CarPosInfo carPosInfo = new CarPosInfo();
                carPosInfo.setCarno(mTopicList.get(i));
                carPosInfo.setFirst(true);
                mCarPosInfoList.add(carPosInfo);
                mTopicArrQos[i] = 1;
            }
//            Log.i("zzz1", "mTopicArrQos[2500] " + mTopicArrQos[2500]);

        } catch (Exception e) {

        }
    }


    private void drawPolyLine11() {
        List<LatLng> polylines = new ArrayList<>();
        for (int index = 0; index < latlngs.length; index++) {
            polylines.add(latlngs[index]);
        }
        polylines.add(latlngs[0]);

//        polylineOptions = new PolylineOptions().points(polyLines).width(0).color(Color.TRANSPARENT);
        // 绘制纹理PolyLine
//        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).customTexture(mGreenTexture)
//                .dottedLine(true);
        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).color(Color.TRANSPARENT)
                .dottedLine(true);
        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

        // 添加小车marker
//        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
//                position(polylines.get(0)).rotate((float) getAngle(0));
//        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    }


    @Override
    public void onMapStatusChangeStart(MapStatus status) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus status, int reason) {

    }

    @Override
    public void onMapStatusChange(MapStatus status) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus status) {
        final LatLng center = status.target;
        Log.i("zzz1", "center " + center.latitude + " " + center.longitude);
        double disCenterMove = DistanceUtil.getDistance(mCenter, center);
        Log.i("zzz1", "disCenterMove " + disCenterMove);
        mCenter = center;
        if (disCenterMove > 1500) {
            Log.i("zzz1", "清除 ");
            CarPosInfo carPosInfo = null;
            for (int i = 0; i < markerArr.length; i++) {
                carPosInfo = mCarPosInfoList.get(i);
                LatLng carStartPos = carPosInfo.getStartLatlng();
                double dis = DistanceUtil.getDistance(carStartPos, center);
                if (dis > 0 && dis > 1000) {
                    markerArr[i].remove();
                    carPosInfo.setFirst(true);
                    carPosInfo.getPolylines().clear();
                }
            }
        }
    }

}


