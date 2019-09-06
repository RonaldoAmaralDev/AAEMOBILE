package br.com.araujoabreu.timg.principal.localizacao.frota.locais.local;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static br.com.araujoabreu.timg.principal.localizacao.frota.locais.local.MapUtils.getBearing;
import static com.google.android.gms.maps.model.JointType.ROUND;


/**
 * A demonstration about car movement on google map
 by @Shihab Uddin
 TO RUN -> GIVE YOUR GOOGLE API KEY to >  google_maps_api.xml file
 -> GIVE YOUR SERVER URL TO FETCH LOCATION UPDATE
 */

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long DELAY = 4500;
    private static final long ANIMATION_TIME_PER_ROUTE = 3000;
    String polyLine = "q`epCakwfP_@EMvBEv@iSmBq@GeGg@}C]mBS{@KTiDRyCiBS";
    GoogleMap googleMap;
    private PolylineOptions polylineOptions;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_LOCATION = 1;
    private Polyline greyPolyLine;
    private SupportMapFragment mapFragment;
    private Handler handler;
    private Marker carMarker;
    private int index;
    private int next;
    private LatLng startPosition;
    private LatLng endPosition;
    private float v;
    Button button2;
    List<LatLng> polyLineList;
    private double lat, lng;
    double latitude = 23.7877649;
    double longitude = 90.4007049;
    private String TAG = "EscritorioRota";

    // Give your Server URL here >> where you get car location update
    public static final String URL_DRIVER_LOCATION_ON_RIDE = "*******";
    private boolean isFirstPosition = true;
    private Double startLatitude;
    private Double startLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_frota);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrotaEscritorio);
        mapFragment.getMapAsync(this);

        callConection();

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler = new Handler();

    }

    public void staticPolyLine() {

        googleMap.clear();

        polyLineList = MapUtils.decodePoly(polyLine);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polyLineList) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        googleMap.animateCamera(mCameraUpdate);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(9);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(ROUND);
        polylineOptions.addAll(polyLineList);
        greyPolyLine = googleMap.addPolyline(polylineOptions);

        startCarAnimation(latitude, longitude);

    }

    Runnable staticCarRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "staticCarRunnable handler called...");
            if (index < (polyLineList.size() - 1)) {
                index++;
                next = index + 1;
            } else {
                index = -1;
                next = 1;
                stopRepeatingTask();
                return;
            }

            if (index < (polyLineList.size() - 1)) {
//                startPosition = polyLineList.get(index);
                startPosition = carMarker.getPosition();
                endPosition = polyLineList.get(next);
            }

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

//                    Log.i(TAG, "Car Animation Started...");

                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endPosition.longitude + (1 - v)
                            * startPosition.longitude;
                    lat = v * endPosition.latitude + (1 - v)
                            * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f, 0.5f);
                    carMarker.setRotation(getBearing(startPosition, newPos));
                    googleMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition
                                    (new CameraPosition.Builder()
                                            .target(newPos)
                                            .zoom(15.5f)
                                            .build()));


                }
            });
            valueAnimator.start();
            handler.postDelayed(this, 5000);

        }
    };

    private void startCarAnimation(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        carMarker = googleMap.addMarker(new MarkerOptions().position(latLng).
                flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));


        index = -1;
        next = 1;
        handler.postDelayed(staticCarRunnable, 3000);
    }

    void stopRepeatingTask() {

        if (staticCarRunnable != null) {
            handler.removeCallbacks(staticCarRunnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Executar Ação Automatico:
        staticPolyLine();

        //  staticPolyLine();
//          dynamicPolyLine();
        //  startGettingOnlineDataFromCar();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

      //  if (id == R.id.action_settings) {
        //    return true;
      //  }

        return super.onOptionsItemSelected(item);
    }

    private void getDriverLocationUpdate() {

        StringRequest request = new StringRequest(Request.Method.POST, URL_DRIVER_LOCATION_ON_RIDE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("PartnerInfoRes::", response);
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    String ApiSuccess = jObj.getString("success");
                    if (ApiSuccess.trim().equals("true")) {

                        JSONObject jObj2 = new JSONObject(jObj.getString("data"));
                        JSONObject jObj3 = new JSONObject(jObj2.getString("driver"));

                        startLatitude = Double.valueOf(jObj3.getString("lat"));
                        startLongitude = Double.valueOf(jObj3.getString("lng"));

                        Log.d(TAG, startLatitude + "--" + startLongitude);

                        if (isFirstPosition) {
                            startPosition = new LatLng(startLatitude, startLongitude);

                            carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).
                                    flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small)));
                            carMarker.setAnchor(0.5f, 0.5f);

                            googleMap.moveCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(startPosition)
                                                    .zoom(15.5f)
                                                    .build()));

                            isFirstPosition = false;

                        } else {
                            endPosition = new LatLng(startLatitude, startLongitude);

                            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);

                            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {

                                Log.e(TAG, "NOT SAME");
                                startBikeAnimation(startPosition, endPosition);

                            } else {

                                Log.e(TAG, "SAMME");
                            }
                        }

                    }
                    if (jObj.getString("message").trim().equals("Unauthorized")) {

                        Log.e(TAG, "--- Unauthorized ---");

                    }

                } catch (Exception e) {
                    Log.d("jsonError::", e + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("driver_id", driverId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Log.d("acc::", ClientAccToken);
                //params.put("authorization", "ClientAccToken");

                return params;
            }

        };

        App.getAppInstance().addToRequestQueue(request, TAG);
    }

    private void startBikeAnimation(final LatLng start, final LatLng end) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                lng = v * end.longitude + (1 - v)
                        * start.longitude;
                lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.5f);
                carMarker.setRotation(getBearing(start, end));

                // todo : Shihab > i can delay here
                googleMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition
                                (new CameraPosition.Builder()
                                        .target(newPos)
                                        .zoom(15.5f)
                                        .build()));

                startPosition = carMarker.getPosition();

            }

        });
        valueAnimator.start();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {

                getDriverLocationUpdate();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            handler.postDelayed(mStatusChecker, DELAY);

        }
    };

    void startGettingOnlineDataFromCar() {
        handler.post(mStatusChecker);
    }

    void CreatePolyLineOnly() {

        googleMap.clear();

        polyLineList = MapUtils.decodePoly(polyLine);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polyLineList) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        googleMap.animateCamera(mCameraUpdate);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(5);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(ROUND);
        polylineOptions.addAll(polyLineList);
        greyPolyLine = googleMap.addPolyline(polylineOptions);
    }

    //CODE GOOGLE MAPS - LOCALIZAÇÃO
    private synchronized  void callConection() {
        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .addOnConnectionFailedListener(HomeActivity.this)
                .addConnectionCallbacks(HomeActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");

        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(l != null) {

            Intent intent = getIntent();
            Bundle dados = intent.getExtras();
            double latitudeAtual = l.getLatitude();
            double longitudeAtual = l.getLongitude();

            String latitudeTeste = dados.getString("latitude");
            String longitudeTeste = dados.getString("longitude");

            double latitudeTesteDouble= Double.parseDouble(latitudeTeste);
            double longitudeTesteDouble = Double.parseDouble(longitudeTeste);

            //Calcular Distancia
            Location startPoint=new Location("locationA");
            startPoint.setLatitude(latitudeAtual);
            startPoint.setLongitude(longitudeAtual);

            Location endPoint=new Location("locationA");
            endPoint.setLatitude(latitudeTesteDouble);
            endPoint.setLongitude(longitudeTesteDouble);
            double distance=startPoint.distanceTo(endPoint);
            TextView txtKmRodados = findViewById(R.id.tvKmRodados);
            String distancia = Double.toString(distance);
            txtKmRodados.setText(distancia);


            // get current locality based on lat lng
            Geocoder geocoder;
            List<Address> addresses;
            List<Address> enderecodestino;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitudeAtual, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                enderecodestino = geocoder.getFromLocation(latitudeTesteDouble, longitudeTesteDouble, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String enderecoDestino = enderecodestino.get(0).getAddressLine(0);

                TextView txtInicio = findViewById(R.id.etOriginFrota);
                TextView txtFim = findViewById(R.id.etDestinationFrota);
                TextView txtTempoGasto = findViewById(R.id.tvTempoGasto);
                TextView txtVelocidade = findViewById(R.id.tvVelocidade);
                TextView txtLitrosGastos = findViewById(R.id.tvLitros);

                txtInicio.setText("Inicio: " + address);
                txtFim.setText("Destino: " + enderecoDestino);
             //   txtVelocidade.setText(numberAsString);
             //   DecimalFormat df = new DecimalFormat("0.00");
             //   String numberAsString = df.format(l.getTime());
             //   txtTempoGasto.setText(numberAsString);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "O seu telefone não suporta a utilização da localização ", Toast.LENGTH_LONG).show();
            }
        }else {
            TextView txtInicio = findViewById(R.id.etOriginFrota);
            txtInicio.setText("Posição não encontrada.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed(" + connectionResult +")");
    }


}