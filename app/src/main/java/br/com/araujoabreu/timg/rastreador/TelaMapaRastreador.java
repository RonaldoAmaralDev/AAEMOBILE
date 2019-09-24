package br.com.araujoabreu.timg.rastreador;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.dev.localizacao.MostrarColaborador;
import br.com.araujoabreu.timg.helper.PermissoesSMS;

public class TelaMapaRastreador extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    BancoGeral myDBGeral;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private ImageView mGps, ic_tipoMapa;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private String email, name, token,colaborador_id;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    public static final String URL="http://helper.aplusweb.com.br/aplicativo/localizacaoVeiculo.php";
    public JsonArrayRequest request, requestAtividades ;
    public RequestQueue requestQueue, requestQueueAtividades;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String[] permissoes = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa_rastreador);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MAPA RASTREAMENTO");

        PermissoesSMS.validarPermissoes(permissoes, this, 1);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#8F152A")));


        mGps = (ImageView) findViewById(R.id.ic_gps);
        ic_tipoMapa = (ImageView) findViewById(R.id.ic_camada);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

        //Não abrir o teclado automatico
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        myDBGeral = new BancoGeral(this);
        getLocationPermission();

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewTelaRastreamento);
        navigation.setOnNavigationItemSelectedListener(this);

        //Aparecer todos os Icones e Titulos
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        ic_tipoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(TelaMapaRastreador.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Selecione o tipo de mapa: ")
                        .setPositiveButton("Mapa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            }
                        })
                        .setNegativeButton("Satelite", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                            }
                        })
                        .setNeutralButton("Hibrido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_veiculo, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(TelaMapaRastreador.this, TelaPrincipalRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_syncVeiculo) {

            //Enviar SMS
            try{
             //   sendSMS();
                //Como enviou o SMS com sucesso, ele irá ler o SMS de resposta
                lerSMS();
            }
            catch (Exception e){
                Toast.makeText(TelaMapaRastreador.this, "SMS Falhou ao ser enviado, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void sendSMS() {
        //Numero Teste: 31989882253 (RASTREADOR 001)
        //Mensagem Para enviar localização = smslink123456
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("(31)989882253", null, "OI", null, null);
        Toast.makeText(getApplicationContext(), "SMS Enviado com sucesso !", Toast.LENGTH_LONG).show();
        return;
    }

    public void lerSMS() {

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
        //Pego a ultimo SMS recebido, no caso o TOP 1
        cursor.moveToFirst();

        //Tratar o SMS recebido, pegando as variaveis
        Toast.makeText(getApplicationContext(), cursor.getString(12), Toast.LENGTH_LONG).show();


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);
        String dataehora = data + " - " + currentTime;

        String mensagem = cursor.getString(12);
        String latitude = mensagem.substring(mensagem.indexOf("lat:") + 4, mensagem.indexOf("long:", mensagem.indexOf("lat:")));
        String longitude = mensagem.substring(mensagem.indexOf("long:") + 5, mensagem.indexOf("speed:", mensagem.indexOf("long:")));
        String velocidade = mensagem.substring(mensagem.indexOf("speed:") + 7, mensagem.indexOf("T:", mensagem.indexOf("speed:")));
        String imei = mensagem.substring(mensagem.indexOf("IMEI:") + 5, mensagem.indexOf("http:", mensagem.indexOf("IMEI:")));
        Toast.makeText(getApplicationContext(), "Latitude Teste: " + latitude + " longitude: " + longitude + " velocidade: " + velocidade + " Data: " + dataehora  + " IMEI: " + imei, Toast.LENGTH_LONG).show();


    }

      @SuppressWarnings("StatementWithEmptyBody")
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_ticketlog) {
            Intent intent = new Intent(TelaMapaRastreador.this, TelaPrincipalRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            finish();
        }
        if (id == R.id.navigation_mapa) {

            TelaMapaRastreador.super.onRestart();
            Intent intent = new Intent(TelaMapaRastreador.this, TelaMapaRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);

        }
        if (id == R.id.navigation_historico) {

            Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

        }

        if (id == R.id.navigation_hidrometro) {

            Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            requestAtividades = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    JSONObject jsonObject = null;
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            jsonObject = response.getJSONObject(i);
                                            String veiculo_id = jsonObject.getString("veiculo_id");
                                            String colaborador_id = jsonObject.getString("colaborador_id");
                                            String latitude = jsonObject.getString("latitude");
                                            String longitude = jsonObject.getString("longitude");
                                            String velocidade = jsonObject.getString("velocidade");
                                            String dataehora = jsonObject.getString("dataehora");

                                            mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(Double.parseDouble(latitude) , Double.parseDouble(longitude)))
                                                    .title("Veiculo: " + veiculo_id)
                                                    .snippet("Data e Hora: " + dataehora)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rastreamento_car))
                                            );

                                            //Fazer um arrayList pra armazenar as variaveis
                                            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                                    .add(new LatLng(Double.parseDouble(latitude) , Double.parseDouble(longitude)))
                                                    .add(new LatLng(-19.916164, -43.975792))
                                                    .width(9f)
                                                    .color(Color.RED)
                                            );

                                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                                    DEFAULT_ZOOM,
                                                    "Minha Localização");
                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            requestQueueAtividades = Volley.newRequestQueue(TelaMapaRastreador.this);
                            int socketTimeout2 = 20000;
                            RetryPolicy policy3 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            requestAtividades.setRetryPolicy(policy3);
                            requestQueueAtividades.add(requestAtividades);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(TelaMapaRastreador.this, "Não foi possivel pegar a localização atual. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));


        if(!title.equals("Localização Atual")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(TelaMapaRastreador.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}

