package com.delaroystudios.uploadmedia.principal;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.activity.TelaLogin;
import com.delaroystudios.uploadmedia.chat.MainActivityChat;
import com.delaroystudios.uploadmedia.equipamento.CadastrarEquipamento;
import com.delaroystudios.uploadmedia.equipamento.CentralEquipamento;
import com.delaroystudios.uploadmedia.facial.FaceDetectRGBActivity;
import com.delaroystudios.uploadmedia.facial.PhotoDetectActivity;
import com.delaroystudios.uploadmedia.frota.MainActivityVeiculos;
import com.delaroystudios.uploadmedia.frota.Veiculos;
import com.delaroystudios.uploadmedia.model.Atividades;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.model.CL;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
import com.delaroystudios.uploadmedia.model.TipoServico;
import com.delaroystudios.uploadmedia.model.TipoSolicitacao;
import com.delaroystudios.uploadmedia.operacao.contrato.CentroLucro;
import com.delaroystudios.uploadmedia.model.Equipamento;
import com.delaroystudios.uploadmedia.operacao.equipamento.Equipamentos;
import com.delaroystudios.uploadmedia.model.Contact;
import com.delaroystudios.uploadmedia.model.OS;
import com.delaroystudios.uploadmedia.principal.localizacao.MostrarColaborador;
import com.delaroystudios.uploadmedia.equipamento.qrcode.LoadingScanner;
import com.delaroystudios.uploadmedia.principal.sync.BootReciever;
import com.delaroystudios.uploadmedia.principal.tutorial.TutorialActivity;
import com.delaroystudios.uploadmedia.relatorio.RelatorioContrato;
import com.delaroystudios.uploadmedia.relatorio.RelatorioLeitura;
import com.delaroystudios.uploadmedia.rota.Hoteis;
import com.delaroystudios.uploadmedia.rota.PostosCombustivel;
import com.delaroystudios.uploadmedia.rota.Restaurantes;
import com.delaroystudios.uploadmedia.visitas.MainActivityVisitas;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity_Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    static final String FTP_HOST= "162.241.135.115";
    static final String FTP_USER = "aplus238";
    static final String FTP_PASS  ="bAzPauQdqC8qLSV";

    public static final int PRIMARY_FOREGROUND_NOTIF_SERVICE_ID = 1001;
    private static final int REQUEST_READ = 1002;
    private static final String TAG = "MainActivity_Principal";

    DatabaseHelper controller;
    BancoGeral myBDGeral;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_LOCATION = 1;
    public JsonArrayRequest request, requestAtividades ;
    public RequestQueue requestQueue, requestQueueAtividades;
    String email, name, colaborador_id, tipo, idLocal, codigolocal, descricaolocal, latitude, longitude;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long progress = 0;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 20f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private Marker marker;
    private ImageView mGps, mCamada, mPostos, mRestaurantes, mHoteis;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    final Context context = this;
    private AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setIcon(R.drawable.logo) // Notification icon
                .setUpdateJSON("http://helper.aplusweb.com.br/aplicativo/versao.json")
                .setTitleOnUpdateAvailable("Nova Versão Disponivel !")
                .setContentOnUpdateAvailable("Nova Versão Disponivel na PlayStore, favor enviar suas visitas antes de atualizar.")
                .setTitleOnUpdateNotAvailable("Nova Versão Indisponivel")
                .setButtonDoNotShowAgain("Atualizar Depois")
                .setContentOnUpdateNotAvailable("Nova versão está indisponivel, favor tentar novamente mais tarde. ")
                .setButtonUpdate("Atualizar Agora")
                .setCancelable(false)
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=br.com.araujoabreu.timg")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=br.com.araujoabreu.timg")));
                        }
                    }
                })
                .setButtonDoNotShowAgainClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setButtonDismiss("Fechar Tela")
                .setButtonDismissClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .start();


        setContentView(R.layout.activity_main__principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Não abrir o teclado automatico
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        startService(new Intent(getBaseContext(), BootReciever.class));

        controller = new DatabaseHelper(this);
        myBDGeral = new BancoGeral(this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        tipo = dados.getString("tipo");

        //Verifica quantidade Imagens enviadas
        verificarArmazenamento();

        //Busca Endereço AutoComplete


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //Pegar localização atual colaborador
        getLocationPermission();

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        mCamada = (ImageView) findViewById(R.id.ic_camada);
        mPostos = (ImageView) findViewById(R.id.ic_postos);
        mRestaurantes = (ImageView) findViewById(R.id.ic_restaurantes);
        mHoteis = (ImageView) findViewById(R.id.ic_hoteis);


        //Busca Locais
        String[] srr = myBDGeral.buscaLocaisMapa(autoCompleteTextView.getText().toString());
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, srr);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        mCamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MainActivity_Principal.this)
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

        mPostos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Principal.this, PostosCombustivel.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("colaborador_id", colaborador_id);
                dados.putString("tipo", tipo);
                intent.putExtras(dados);
                startActivity(intent);            }
        });

        mRestaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity_Principal.this, Restaurantes.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("colaborador_id", colaborador_id);
                dados.putString("tipo", tipo);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        mHoteis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Principal.this, Hoteis.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("colaborador_id", colaborador_id);
                dados.putString("tipo", tipo);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });


        //Colocar quantidado ao lado menu navegação
        setNavItemCount(R.id.nav_programacaotodos, myBDGeral.dbCountLocais());
        setNavItemCount(R.id.nav_programacaoequipamento, myBDGeral.dbCountEquipamento());
        setNavItemCount(R.id.nav_syncVisita, myBDGeral.dbCountEncerradas());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        try {
            toggle.syncState();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewTelaPrincipal);
        navigation.setOnNavigationItemSelectedListener(this);

        //Trocar titulos
        navigation.getMenu().findItem(R.id.navigation_mapa).setTitle("MAPA");
        navigation.getMenu().findItem(R.id.navigation_visitas).setTitle("VISITAS(" + String.valueOf(myBDGeral.dbCountAbertas() + ")"));
        navigation.getMenu().findItem(R.id.navigation_scanner).setTitle("SCANNER");
        navigation.getMenu().findItem(R.id.navigation_chat).setTitle("CHAT");
        navigation.getMenu().findItem(R.id.navigation_mais).setTitle("MAIS");

        //Aparecer todos os Icones e Titulos
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null); //

        NavigationView navegationView2 = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navegationView2.getMenu();


        int categoria = Integer.parseInt(tipo);
        if (categoria == 2) {
            // Operação Manutenção
            nav_Menu.findItem(R.id.nav_atualizarVeiculo).setVisible(false);
            nav_Menu.findItem(R.id.nav_relatorio_ordemservico).setVisible(false);
            nav_Menu.findItem(R.id.nav_localizacao).setVisible(false);
            nav_Menu.findItem(R.id.nav_veiculomanutencao).setVisible(false);
            nav_Menu.findItem(R.id.nav_veiculos).setVisible(false);
            nav_Menu.findItem(R.id.nav_reconhecimentofacial).setVisible(false);
            nav_Menu.findItem(R.id.nav_cadastrarequip).setVisible(false);

        } else if (categoria == 10) {
            // Frota
            nav_Menu.findItem(R.id.nav_cadastrarequip).setVisible(false);
            nav_Menu.findItem(R.id.nav_syncTabelas).setVisible(false);
            nav_Menu.findItem(R.id.nav_syncVisita).setVisible(false);
            nav_Menu.findItem(R.id.nav_programacaoequipamento).setVisible(false);
            nav_Menu.findItem(R.id.nav_programacaotodos).setVisible(false);
            nav_Menu.findItem(R.id.nav_relatorio_leitura).setVisible(false);
            nav_Menu.findItem(R.id.nav_relatorio_contrato).setVisible(false);

        }

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
          //  mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }


    private void init(){
        Log.d(TAG, "init: initializing");


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //Se ele clicar na posição dele não realizar nada
                if (marker.getTitle().equals("Colaborador")) {
                    Toast.makeText(getApplicationContext(), "Colaborador: " + marker.getTitle(), Toast.LENGTH_LONG).show();
                return true;
                }
                //Se ele clicar em algum local
                else {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity_Principal.this);
                    View view = inflater.inflate(R.layout.dialog_localmapa, null);
                    view.animate();

                    TextView txtCodigoLocaleDescricao = view.findViewById(R.id.txtCodigoLocaleDescricao);
                    TextView txtEnderecoLocal = view.findViewById(R.id.txtEnderecoLocal);
                    TextView txtCidadeeEstado = view.findViewById(R.id.txtCidadeeEstado);
                    TextView txtDataPlanejamento = view.findViewById(R.id.txtDataPlanejamentoLocal);
                    TextView txtSLALocal = view.findViewById(R.id.txtSLALocal);

                    Button acceptButton = view.findViewById(R.id.acceptButton);
                    Button cancelButton = view.findViewById(R.id.cancelButton);

                    txtCodigoLocaleDescricao.setText(marker.getTitle());

                    //Busca dados do marker (posição)
                    Cursor dataOS = myBDGeral.verificaLocal(marker.getSnippet());
                    while (dataOS.moveToNext()) {

                        String endereco = dataOS.getString(23);
                        String cidade = dataOS.getString(5);
                        String estado = dataOS.getString(9);

                    txtEnderecoLocal.setText(endereco);
                    txtCidadeeEstado.setText(cidade + " - " +  estado);
                    txtSLALocal.setText("SLA: " + " Em Desenvolvimento");

                    //Busca dados da Visita pelo Local ID
                        Cursor dataVisita = myBDGeral.buscaVisitaPLocal(marker.getSnippet());
                        while (dataVisita.moveToNext()) {

                            String dataplanejamento = dataVisita.getString(6);

                            txtDataPlanejamento.setText(dataplanejamento);

                            dataVisita.close();
                        }

                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e(TAG, "onClick: accept button");
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e(TAG, "onClick: cancel button");
                            }
                        });

                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity_Principal.this)
                            .setView(view)
                            .create();

                    alertDialog.show();
                }
                return true;
            }
        });
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

                            latitude = Double.toString(currentLocation.getLatitude());
                            longitude = Double.toString(currentLocation.getLongitude());

                            if(latitude != null && longitude != null) {

                                SharedPreferences.Editor salvarLocalizacao = getSharedPreferences("salvarLocalizacao", MODE_PRIVATE).edit();
                                salvarLocalizacao.putString("latitude", latitude);
                                salvarLocalizacao.putString("longitude", longitude);

                                // Armazena as Preferencias
                                salvarLocalizacao.commit();
                            } else {

                                Toast.makeText(getApplicationContext(), "Não foi possivel fazer a captura da sua lozalização.", Toast.LENGTH_LONG).show();
                            }




                            //Emulador:
                            moveCamera(new LatLng(
                                            currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "Minha Localização");

                            Cursor dataOS = myBDGeral.getDataLocal();
                            while (dataOS.moveToNext()) {

                                idLocal = dataOS.getString(0);
                                codigolocal = dataOS.getString(1);
                                latitude = dataOS.getString(6);
                                longitude = dataOS.getString(7);

                                    //Fazer variavel se a visita estiver com data superior data atual ficar verde, se estiver no dia igual amarelo e vencida vermelho
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                                            .title(codigolocal)
                                            .snippet(idLocal)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                                    );

                                marker.showInfoWindow();
                                }

                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))
                .title(name);
        mMap.addMarker(options);




        hideSoftKeyboard();
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
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }

   private void setNavItemCount(@IdRes int itemId, int count) {
        NavigationView navegationView2 = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navegationView2.getMenu();

        TextView view = (TextView) navegationView2.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);

    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja sair do Aplicativo:")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }




    public void emitirNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "_channel_01";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, "notification", importance);
            mChannel.enableLights(true);

            String quantidadeOS = String.valueOf(myBDGeral.dbCoutaberta(colaborador_id));
            int quantOS = Integer.parseInt(quantidadeOS);
            String data = new SimpleDateFormat("HH:mm:ss -  dd/MM/yyyy ").format(System.currentTimeMillis());

            Notification notification = new Notification.Builder(getApplicationContext(), id)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Você possui novas visitas para realizar.")
                    .setContentText("Visitas em aberto: " + quantOS)
                    .setSubText("Ultima sync: " + data )
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);
            }
        }
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Menu Principal");

        getMenuInflater().inflate(R.menu.main_activity__principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Enviar LOG
        if (id == R.id.action_enviarLog) {

            Log.w("before","Arquivo LOG salvo !");
            // File logFile = new File( + "/log.txt" );
            try {
                Process process = Runtime.getRuntime().exec("logcat -d");
                process = Runtime.getRuntime().exec( "logcat -f " + "/storage/emulated/0/PicturesHelper/log.txt");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File file = new File("/storage/emulated/0/PicturesHelper/log.txt");
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                    startActivity(Intent.createChooser(sharingIntent, "Como deseja enviar"));
                    Toast.makeText(getApplicationContext(), "Enviado com sucesso, arquivo de LOG. ", Toast.LENGTH_LONG).show();

                } else {
                    File file = new File("/storage/emulated/0/PicturesHelper/log.txt");
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                    startActivity(Intent.createChooser(sharingIntent, "Como deseja enviar"));
                    Toast.makeText(getApplicationContext(), "Enviado com sucesso, arquivo de LOG. ", Toast.LENGTH_LONG).show();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        }

        //Deletar Todos os Dados
        if (id == R.id.action_deletar) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logo)
                    .setTitle(R.string.app_name)
                    .setMessage("Deseja apagar todos os telefone ? Verifique se não possui nenhuma visita para enviar.")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            controller.deleteDados();
                            myBDGeral.deleteDados();
                            Toast.makeText(getApplicationContext(), "Favor atulizar para trazer novos dados.", Toast.LENGTH_LONG).show();
                            MainActivity_Principal.super.onRestart();
                            Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Principal.class);
                            Bundle dados = new Bundle();
                            dados.putString("name", name);
                            dados.putString("email", email);
                            dados.putString("id", colaborador_id);
                            dados.putString("tipo", tipo);
                            intent.putExtras(dados);
                            startActivity(intent);
                            finish();                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }


        if( id == R.id.action_trocarusuario) {
            Intent voltarLogin = new Intent(MainActivity_Principal.this, TelaLogin.class);
            SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            startActivity(voltarLogin);
        }

        if(id == R.id.action_sair) {
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Os 4 primeiros são o navegation bottom (De baixo)
        if(id == R.id.navigation_mapa) {
            MainActivity_Principal.super.onRestart();
            Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
            finish();
        }
        if(id == R.id.navigation_visitas) {

            Intent intent = new Intent(MainActivity_Principal.this, MainActivityVisitas.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("colaborador_id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);

        }

        if (id == R.id.navigation_scanner) {

            Intent intent = new Intent(MainActivity_Principal.this, LoadingScanner.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
        }

        if (id == R.id.navigation_chat) {

            Intent intent = new Intent(MainActivity_Principal.this, MainActivityChat.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
        }

        if (id == R.id.nav_tutorial) {

            Intent intent = new Intent(MainActivity_Principal.this, TutorialActivity.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);

        }else if (id == R.id.nav_reconhecimentofacial) {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logo)
                    .setTitle(R.string.app_name)
                    .setMessage("Como deseja testar reconhecimento facial:")
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity_Principal.this, FaceDetectRGBActivity.class);
                            Bundle dados = new Bundle();
                            dados.putString("name", name);
                            dados.putString("email", email);
                            dados.putString("id", colaborador_id);
                            dados.putString("tipo", tipo);
                            intent.putExtras(dados);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Foto Galeria", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity_Principal.this, PhotoDetectActivity.class);
                            Bundle dados = new Bundle();
                            dados.putString("name", name);
                            dados.putString("email", email);
                            dados.putString("id", colaborador_id);
                            dados.putString("tipo", tipo);
                            intent.putExtras(dados);
                            startActivity(intent);
                        }
                    })
                    .show();

        }else if (id == R.id.nav_localizacao) {

            Intent intent = new Intent(MainActivity_Principal.this, MostrarColaborador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);

        } else if (id == R.id.nav_veiculos) {

            Intent intent = new Intent(MainActivity_Principal.this, MainActivityVeiculos.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);

        } else if (id == R.id.nav_atualizarVeiculo) {

            String URL_VEICULOS = "http://helper.aplusweb.com.br/aplicativo/buscarVeiculo.php";
            if(verificaConexao() == false ) {
                Toast.makeText(getApplicationContext(), "Sem conexão com a Internet. ", Toast.LENGTH_LONG).show();
            } else if(verificaConexao() == true) {

                requestAtividades = new JsonArrayRequest(URL_VEICULOS, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ProgressBarStatus();
                        JSONObject jsonObject = null;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                Veiculos veiculos = new Veiculos();
                                veiculos.setId(jsonObject.getString("id"));
                                veiculos.setCentrocusto(jsonObject.getString("centrolucro"));
                                veiculos.setColaborador(jsonObject.getString("colaborador"));
                                veiculos.setFabricante(jsonObject.getString("fabricante"));
                                veiculos.setCidade(jsonObject.getString("cidade"));
                                veiculos.setEstado(jsonObject.getString("estado"));
                                veiculos.setPlaca(jsonObject.getString("placa"));
                                veiculos.setPlaca_anterior(jsonObject.getString("placa_anterior"));
                                veiculos.setDescricao(jsonObject.getString("descricao"));
                                veiculos.setModelo(jsonObject.getString("modelo"));
                                veiculos.setKminicial(jsonObject.getString("kminicial"));
                                veiculos.setAtivo(jsonObject.getString("ativo"));
                                Cursor dataItem = myBDGeral.verificaVeiculo(jsonObject.getString("id"));
                                if (dataItem.moveToNext()) {
                                    myBDGeral.updateVeiculo(
                                            veiculos.getId(),
                                            veiculos.getCentrocusto(),
                                            veiculos.getColaborador(),
                                            veiculos.getFabricante(),
                                            veiculos.getCidade(),
                                            veiculos.getEstado(),
                                            veiculos.getPlaca(),
                                            veiculos.getPlaca_anterior(),
                                            veiculos.getDescricao(),
                                            veiculos.getModelo(),
                                            veiculos.getKminicial(),
                                            veiculos.getAtivo());
                                } else {
                                    myBDGeral.gravarVeiculos(
                                            veiculos.getId(),
                                            veiculos.getCentrocusto(),
                                            veiculos.getColaborador(),
                                            veiculos.getFabricante(),
                                            veiculos.getCidade(),
                                            veiculos.getEstado(),
                                            veiculos.getPlaca(),
                                            veiculos.getPlaca_anterior(),
                                            veiculos.getDescricao(),
                                            veiculos.getModelo(),
                                            veiculos.getKminicial(),
                                            veiculos.getAtivo());
                                }
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
                requestQueueAtividades = Volley.newRequestQueue(MainActivity_Principal.this);
                int socketTimeout2 = 20000;
                RetryPolicy policy3 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                requestAtividades.setRetryPolicy(policy3);
                requestQueueAtividades.add(requestAtividades);
            }

        } else if (id == R.id.nav_cadastrarequip) {

            Intent intent = new Intent(MainActivity_Principal.this, CadastrarEquipamento.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);

        } else if (id == R.id.nav_gerarqrcode) {

            Intent intent = new Intent(MainActivity_Principal.this, CentralEquipamento.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);



        } else if (id == R.id.nav_programacaotodos) {

            Intent intent = new Intent(MainActivity_Principal.this, CentroLucro.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);


        } else if(id == R.id.nav_programacaoequipamento) {

            Intent intent = new Intent(MainActivity_Principal.this, Equipamentos.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
        }
        else if (id == R.id.nav_syncVisita) {
            if (verificaConexao() == true) {

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                ArrayList<HashMap<String, String>> userList = controller.getAllUsers();
                if (userList.size() != 0) {
                    if (controller.dbSyncNO() != 0) {
                        ProgressBarStatusVisitas();
                        params.put("userJson", controller.composeJSONfromSQLite());
                        client.post("http://helper.aplusweb.com.br/aplicativo/sync2/gravarvisita.php", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println(response);
                                try {
                                    JSONArray arr = new JSONArray(response);
                                    System.out.println(arr.length());
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = (JSONObject) arr.get(i);
                                        System.out.println(obj.get("ordemservico_id"));
                                        System.out.println(obj.get("status"));
                                        controller.updateSyncStatus(obj.get("ordemservico_id").toString(), obj.get("status").toString());
                                        myBDGeral.updateSyncStatus(obj.get("ordemservico_id").toString());
                                    }
                                    verificarSePossuiMedicao();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Toast.makeText(getApplicationContext(), "Erro, favor alguardar alguns miinutos. ", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Throwable error,
                                                  String content) {
                                // TODO Auto-generated method stub
                                if (statusCode == 404) {
                                    Toast.makeText(getApplicationContext(), "Erro: 404, favor comunir ao TIMG!", Toast.LENGTH_LONG).show();
                                } else if (statusCode == 500) {
                                    Toast.makeText(getApplicationContext(), "Erro: 500, favor comunicar TIMG.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Ocorreu algum erro.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Você não possui nenhuma visita para enviar. ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Você não possui nenhuma visita para enviar. ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sem conexão com a Internet !", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_syncTabelas) {
            syncDados();
        } else if (id == R.id.nav_relatorio_contrato) {

            //Perguntar qual contrato ela deseja ver

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            //Cria a view a ser utilizada no dialog
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.spinner_dialog, null);
            //Obtém uma referencia ao Spinner
            Spinner spinner = (Spinner) view.findViewById(R.id.spinnerDialog);
            //Cria o Adapter
            List<String> labels = myBDGeral.getAllLabels();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
            //Atribui o adapter ao spinner
            spinner.setAdapter(dataAdapter);
            builder.setView(view);
            builder.setCancelable(true);

            String selecionado = spinner.getSelectedItem().toString();

            builder.setPositiveButton("GERAR PDF", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity_Principal.this, RelatorioContrato.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", tipo);
                    dados.putString("centrolucro_descricao", selecionado);
                    intent.putExtras(dados);
                    startActivity(intent);
                    return;
                }
            });
            builder.setNegativeButton("Não", null);

            builder.show();

        } else if(id == R.id.nav_relatorio_leitura) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            //Cria a view a ser utilizada no dialog
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.spinner_dialog, null);
            //Obtém uma referencia ao Spinner
            Spinner spinner = (Spinner) view.findViewById(R.id.spinnerDialog);
            //Cria o Adapter
            List<String> labels = myBDGeral.getAllLabels();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
            //Atribui o adapter ao spinner
            spinner.setAdapter(dataAdapter);
            builder.setView(view);
            builder.setCancelable(true);
            String selecionado = spinner.getSelectedItem().toString();

            builder.setPositiveButton("GERAR PDF", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity_Principal.this, RelatorioLeitura.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", tipo);
                    dados.putString("centrolucro_descricao", selecionado);
                    intent.putExtras(dados);
                    startActivity(intent);
                    return;
                }
            });
            builder.setNegativeButton("Não", null);
            builder.show();
        }

        //Fim Itens Menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void verificarSePossuiMedicao() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> medicoesList = controller.getMedicoes();
        if (medicoesList.size() != 0) {
            if (controller.dbSyncMedicao() != 0) {
                params.put("userJson", controller.gravarMedicao());
                client.post("http://helper.aplusweb.com.br/aplicativo/sync2/gravarmedicao.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = (JSONObject) arr.get(i);
                                System.out.println(obj.get("ordemservico_id"));
                                System.out.println(obj.get("status"));
                                controller.updateSyncStatusMedicao(obj.get("ordemservico_id").toString(), obj.get("status").toString());
                            }
                           criarNovaVisita();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Erro, favor alguardar alguns minutos. ", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Erro: 404, favor comunir ao TIMG!", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Erro: 500, favor comunicar TIMG.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ocorreu algum erro.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                criarNovaVisita();
            }
        }
        criarNovaVisita();
    }



    public void criarNovaVisita() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> osList = myBDGeral.getOrdemServicos();
        if (osList.size() != 0) {
            //Toast.makeText(getApplicationContext(), "Você possui: " + myBDGeral.gravarNovaOS2(), Toast.LENGTH_LONG).show();
                params.put("userJson", myBDGeral.gravarNovaOS2());
                client.post("http://helper.aplusweb.com.br/aplicativo/sync2/gerarnovavisita.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = (JSONObject) arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                myBDGeral.updateSituacaoOrdemServico(obj.get("id").toString(), obj.get("status").toString());
                            }
                            mandarFotos();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Erro, favor alguardar alguns minutos. ", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Erro: 404, favor comunir ao TIMG!", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Erro: 500, favor comunicar TIMG.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ocorreu algum erro.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
            mandarFotos();

        }
       mandarFotos();
    }



    public void mandarFotos() {
        //FTP
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST);
            ftpClient.setSoTimeout(10000);
            ftpClient.enterLocalPassiveMode();

            if (ftpClient.login(FTP_USER, FTP_PASS)) {

                //Se ele conectar com o FTP, chamar ProgressBARFotos

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                final File folder = new File("/storage/emulated/0/PicturesHELPER/");
                ftpClient.changeWorkingDirectory( "/public_html/helper/public/assets/os/");

                for (final File fileEntry : folder.listFiles()) {
                    try {
                        FileInputStream fs = new FileInputStream(fileEntry);
                        if (!fileEntry.isDirectory()) {
                            String fileName = fileEntry.getName();
                            ftpClient.storeFile(fileName, fs);
                            File from = new File("/storage/emulated/0/PicturesHELPER/" + fileName);
                            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/enviados/";
                            File myDir = new File(root);
                            myDir.mkdirs();
                            File to = new File(root + fileName);
                            from.renameTo(to);
                            fs.close();
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verificarArmazenamento() {


        if (ContextCompat.checkSelfPermission(MainActivity_Principal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity_Principal.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ);
        File f = new File("/storage/emulated/0/PicturesHELPER/enviados/");
        if(f.exists()) {
            int count = 0;
            for (File file : f.listFiles()) {
                //Verifica se variavel existe e pasta
                if (file.isFile() && file.exists()) {
                    count++;
                    if(count >= 0) {
                        file.delete();
                        System.out.println("Imagens já enviadas: " + count);
                    }
                }
            }
            System.out.println("Quantidade Imagens Enviadas: " + count);
            File fEnviar = new File("/storage/emulated/0/PicturesHELPER/");
            if(fEnviar.exists()) {
                for (File fileEnviar : fEnviar.listFiles()) {
                    int countEnviar = 0;
                    //Verifica se variavel existe e pasta
                    if (fileEnviar.isFile() && fileEnviar.exists()) {
                        countEnviar++;
                    } else {
                     //   txtQuantImagens.setText("0");
                    }
                  //  txtQuantImagens.setText(String.valueOf(countEnviar));
                    System.out.println("Quantidade Imagens Para Enviar: " + countEnviar);
                }
            } else {
                fEnviar.mkdirs();
            }
        } else {
            f.mkdirs();
        }
    }

    public void ProgressBarStatus() {

        progressBar = new ProgressDialog(MainActivity_Principal.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Sincronizando Dados, favor aguardar alguns segundos. "); // set message in progressbar dialog
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgressDrawable(MainActivity_Principal.this.getResources().getDrawable(
                R.drawable.custom_progress));
        progressBar.setProgress(0); //set min value of progress bar
        progressBar.setMax(100); // set max value of progress bar
        progressBar.show(); // display progress bar
        //reset progress bar status
        progressBarStatus = 0;
        //reset progress
        progress = 0;
        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    // process some tasks
                    progressBarStatus = doSomeTasks();
                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                // Progress completed ?!?!,
                if (progressBarStatus >= 100) {
                    // sleep 2000 milliseconds, so that you can see the 100%
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();
                    emitirNotificacao();
                    MainActivity_Principal.super.onRestart();
                    Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Principal.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", tipo);
                    intent.putExtras(dados);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public int doSomeTasks() {
        while (progress <= 2000000) {
            progress++;
            if (progress == 100000) {
                return 10;
            } else if (progress == 600000) {
                return 20;
            } else if (progress == 1400000) {
                return 50;
            } else if (progress == 1800000) {
                return 80;
            }
        }
        return 100;
    }

    public void ProgressBarStatusVisitas() {

        progressBar = new ProgressDialog(MainActivity_Principal.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Enviando as visitas, favor aguardar. "); // set message in progressbar dialog
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgressDrawable(MainActivity_Principal.this.getResources().getDrawable(
                R.drawable.custom_progress));
        progressBar.setProgress(0); //set min value of progress bar
        progressBar.setMax(100); // set max value of progress bar
        progressBar.show(); // display progress bar
        progressBarStatus = 0;
        progress = 0;
        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    // process some tasks
                    progressBarStatus = doSomeTasksVisitas();
                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(25000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(25000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();
                    MainActivity_Principal.super.onRestart();
                    Intent intent = new Intent(MainActivity_Principal.this, MainActivity_Principal.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", tipo);
                    intent.putExtras(dados);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public int doSomeTasksVisitas() {
        while (progress <= 2500000) {
            progress++;
            if (progress == 100000) {
                return 10;
            } else if (progress == 600000) {
                return 20;
            } else if (progress == 1600000) {
                return 50;
            } else if (progress == 1900000) {
                return 80;
            }
        }
        return 100;
    }


    public void syncDados() {

        if (verificaConexao() == true) {

            String URL = "http://helper.aplusweb.com.br/aplicativo/atualizarOperacao.php?colaborador_id=" + colaborador_id;
            ProgressBarStatus();

            request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    JSONObject jsonObject = null;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            CL cl = new CL();
                            Contact local = new Contact();
                            cl.setId(jsonObject.getString("idCL"));
                            cl.setCentrocusto(jsonObject.getString("centrocusto"));
                            cl.setDescricao(jsonObject.getString("descricao"));

                            Cursor data = myBDGeral.verificaCL(jsonObject.getString("idCL"));
                            if (data.moveToNext()) {
                                myBDGeral.updateCL(
                                        cl.getIdCL(),
                                        cl.getCentrocusto(),
                                        cl.getDescricao());
                            } else {
                                myBDGeral.insertCentroCusto(cl.getIdCL(), cl.getCentrocusto(), cl.getDescricao());
                            }
                            local.setId(jsonObject.getString("idLocal"));
                            local.setCodigolocal(jsonObject.getString("codigolocal"));
                            local.setCentrocusto_id(jsonObject.getString("centrocusto_idLocal"));
                            local.setDescricaolocal(jsonObject.getString("descricaolocal"));
                            local.setBairro(jsonObject.getString("bairro"));
                            local.setCidade(jsonObject.getString("cidade"));
                            local.setSigla(jsonObject.getString("sigla"));
                            local.setEstado(jsonObject.getString("estado"));
                         //   local.setLatitude(jsonObject.getString("latitude"));
                         //   local.setLongitude(jsonObject.getString("longitude"));
                            local.setTempogasto(jsonObject.getString("tempogasto"));
                            local.setRegiaoID(jsonObject.getString("regiaoID"));
                            local.setRegiaoDescricao(jsonObject.getString("regiaoDescricao"));
                            local.setPoloatendimentosID(jsonObject.getString("poloatendimentosID"));
                            local.setPoloatenidmentosDescricao(jsonObject.getString("poloatendimentosDescricao"));
                            local.setContatoID(jsonObject.getString("contatoID"));
                            local.setContatoCLID(jsonObject.getString("contatoCLID"));
                            local.setContatoNome(jsonObject.getString("contatoNome"));
                            local.setContatoEndereco(jsonObject.getString("contatoEndereco"));
                            local.setContatoLatitude(jsonObject.getString("contatoLatitude"));
                            local.setContatoLongitude(jsonObject.getString("contatoLongitude"));
                            local.setAreaconstruida(jsonObject.getString("areaconstruida"));
                            local.setAreacapina(jsonObject.getString("areacapina"));
                            local.setEnderecolocal(jsonObject.getString("enderecolocal"));

                            Cursor dataLocal = myBDGeral.verificaLocal(jsonObject.getString("idLocal"));
                            if (dataLocal.moveToNext()) {
                            } else {
                                myBDGeral.insertLocal(
                                        local.getId(),
                                        local.getCodigolocal(),
                                        local.getCentrocusto_id(),
                                        local.getDescricaolocal(),
                                        local.getBairro(),
                                        local.getCidade(),
                                        "-19.860786",
                                        "-44.0052989",
                                        local.getSigla(),
                                        local.getEstado(),
                                        local.getTempogasto(),
                                        local.getRegiaoID(),
                                        local.getRegiaoDescricao(),
                                        local.getPoloatendimentosID(),
                                        local.getPoloatenidmentosDescricao(),
                                        local.getContatoID(),
                                        local.getContatoCLID(),
                                        local.getContatoNome(),
                                        local.getContatoEndereco(),
                                        local.getContatoLatitude(),
                                        local.getContatoLongitude(),
                                        local.getAreaconstruida(),
                                        local.getAreacapina(),
                                        local.getEnderecolocal(),
                                        local.getFrequencia(),
                                        local.getRaio(),
                                        local.getSituacao());
                            }
                            myBDGeral.updateLocal(
                                    local.getId(),
                                    local.getCodigolocal(),
                                    local.getCentrocusto_id(),
                                    local.getDescricaolocal(),
                                    local.getBairro(),
                                    local.getCidade(),
                                    "-19.860786",
                                    "-44.0052989",
                                    local.getSigla(),
                                    local.getEstado(),
                                    local.getTempogasto(),
                                    local.getRegiaoID(),
                                    local.getRegiaoDescricao(),
                                    local.getPoloatendimentosID(),
                                    local.getPoloatenidmentosDescricao(),
                                    local.getContatoID(),
                                    local.getContatoCLID(),
                                    local.getContatoNome(),
                                    local.getContatoEndereco(),
                                    local.getContatoLatitude(),
                                    local.getContatoLongitude(),
                                    local.getAreaconstruida(),
                                    local.getAreacapina(),
                                    local.getEnderecolocal());

                            Equipamento equipamento = new Equipamento();
                            equipamento.setId(jsonObject.getString("idEquipamento"));
                            equipamento.setCodigoequipamento(jsonObject.getString("codigoequipamento"));
                            equipamento.setDescricaoequipamento(jsonObject.getString("descricaoequipamento"));
                            equipamento.setCentrocusto_id(jsonObject.getString("centrocusto_idEquipamento"));
                            equipamento.setLocal_id(jsonObject.getString("local_idEquipamento"));
                            equipamento.setModelo(jsonObject.getString("modelo"));
                            equipamento.setTag(jsonObject.getString("tag"));
                            equipamento.setNumeroserie(jsonObject.getString("numeroserie"));
                            equipamento.setBtu(jsonObject.getString("btu"));
                            equipamento.setFabricante(jsonObject.getString("fabricante"));
                            equipamento.setTipoequipamento(jsonObject.getString("tipoequipamento"));
                            equipamento.setFornecedor(jsonObject.getString("fornecedor"));
                            Cursor dataEquipamento = myBDGeral.verificaEquipamento(jsonObject.getString("idEquipamento"));
                            if (dataEquipamento.moveToNext()) {
                                myBDGeral.updateEquipamento(
                                        equipamento.getId(),
                                        equipamento.getCodigoequipamento(),
                                        equipamento.getDescricaoequipamento(),
                                        equipamento.getCentrocusto_id(),
                                        equipamento.getLocal_id(),
                                        equipamento.getModelo(),
                                        equipamento.getTag(),
                                        equipamento.getNumeroserie(),
                                        equipamento.getBtu(),
                                        equipamento.getFabricante(),
                                        equipamento.getTipoequipamento(),
                                        equipamento.getFornecedor());
                            } else {
                                myBDGeral.insertEquipamento(
                                        equipamento.getId(),
                                        equipamento.getCodigoequipamento(),
                                        equipamento.getDescricaoequipamento(),
                                        equipamento.getCentrocusto_id(),
                                        equipamento.getLocal_id(),
                                        equipamento.getFabricante(),
                                        equipamento.getTipoequipamento(),
                                        equipamento.getFornecedor());
                            }
                            myBDGeral.updateEquipamento(
                                    equipamento.getId(),
                                    equipamento.getCodigoequipamento(),
                                    equipamento.getDescricaoequipamento(),
                                    equipamento.getCentrocusto_id(),
                                    equipamento.getLocal_id(),
                                    equipamento.getModelo(),
                                    equipamento.getTag(),
                                    equipamento.getNumeroserie(),
                                    equipamento.getBtu(),
                                    equipamento.getFabricante(),
                                    equipamento.getTipoequipamento(),
                                    equipamento.getFornecedor());

                            OS os = new OS();
                            os.setId(jsonObject.getString("idOS"));
                            os.setTiposolicitacao(jsonObject.getString("tiposolicitacao_os"));
                            os.setTiposervico(jsonObject.getString("tiposervico_os"));
                            os.setChecklist_id(jsonObject.getString("checklist_id"));
                            os.setCentrocusto_id(jsonObject.getString("centrocusto_id"));
                            os.setLocal_id(jsonObject.getString("local_id"));
                            os.setEquipamento_id(jsonObject.getString("equipamento_id"));
                            os.setEquipe1(jsonObject.getString("equipe1"));
                            os.setDataplanejamento(jsonObject.getString("dataplanejamento"));
                            os.setDescricaopadrao(jsonObject.getString("descricaopadrao"));
                            //   os.setCodigochamado(jsonObject.getString("codigochamado"));
                            os.setFlag_os(jsonObject.getString("flag_os"));

                            Cursor dataOS = myBDGeral.verificaOS(jsonObject.getString("idOS"));
                            if (dataOS.moveToNext()) {
                                myBDGeral.updateOS(
                                        os.getId(),
                                        os.getLocal_id(),
                                        os.getCentrocusto_id(),
                                        os.getTiposolicitacao(),
                                        os.getTiposervico(),
                                        os.getEquipamento_id(),
                                        os.getChecklist_id(),
                                        os.getEquipe1(),
                                        os.getDataplanejamento(),
                                        os.getDescricaopadrao(),
                                        os.getFlag_os()
                                );

                                myBDGeral.updateSituacaoOSSistematica(
                                        os.getId(),
                                        "A");

                            } else {
                                myBDGeral.insertOS(
                                        os.getId(),
                                        os.getLocal_id(),
                                        os.getCentrocusto_id(),
                                        os.getTiposolicitacao(),
                                        os.getTiposervico(),
                                        os.getEquipamento_id(),
                                        os.getChecklist_id(),
                                        os.getEquipe1(),
                                        os.getDataplanejamento(),
                                        os.getDescricaopadrao(),
                                        "aberta",
                                        //os.getCodigochamado());
                                        "",
                                        os.getFlag_os());
                                // Ao inserir OS irá verificar se ela é preventiva para inserir situacao "A" assim depois criando nova
                                myBDGeral.updateSituacaoOSSistematica(
                                        os.getId(),
                                        "A");
                                dataOS.close();
                            }

                            TipoSolicitacao tipoSolicitacao = new TipoSolicitacao();
                            tipoSolicitacao.setId(jsonObject.getString("tiposolicitacao_id"));
                            tipoSolicitacao.setDescricao(jsonObject.getString("tiposolicitacao_descricao"));

                            Cursor dataTipoSolicitacao = myBDGeral.verificaTipoSolicitacao(jsonObject.getString("tiposolicitacao_id"));
                            if (dataTipoSolicitacao.moveToNext()) {
                                myBDGeral.updateTipoSolicitacao(
                                        tipoSolicitacao.getId(),
                                        tipoSolicitacao.getDescricao()
                                );
                            } else {
                                myBDGeral.insertTipoSolicitacao(
                                        tipoSolicitacao.getId(),
                                        tipoSolicitacao.getDescricao());

                            }

                            TipoServico tipoServico = new TipoServico();
                            tipoServico.setId(jsonObject.getString("tiposervico_id"));
                            tipoServico.setDescricao(jsonObject.getString("tiposervico_descricao"));

                            Cursor dataTipoServico = myBDGeral.verificaTipoServico(jsonObject.getString("tiposervico_id"));
                            if (dataTipoSolicitacao.moveToNext()) {
                                myBDGeral.updateTipoServico(
                                        tipoServico.getId(),
                                        tipoServico.getDescricao()
                                );
                            } else {
                                myBDGeral.insertTipoServico(
                                        tipoServico.getId(),
                                        tipoServico.getDescricao());
                            }


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
            requestQueue = Volley.newRequestQueue(MainActivity_Principal.this);
            int socketTimeout = 20000;
            RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy2);
            requestQueue.add(request);
            String URL_ATIVIDADES = "http://helper.aplusweb.com.br/aplicativo/atualizarAtividades.php?colaborador_id=" + colaborador_id;
            requestAtividades = new JsonArrayRequest(URL_ATIVIDADES, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            Atividades atividades = new Atividades();
                            atividades.setItenID(jsonObject.getString("itenID"));
                            atividades.setItenchecklist(jsonObject.getString("itenchecklist"));
                            atividades.setItenDescricao(jsonObject.getString("itenDescricao"));
                            Cursor dataItem = myBDGeral.verificaAtividadesJson(jsonObject.getString("itenID"));
                            if (dataItem.moveToNext()) {
                                myBDGeral.updateAtividades(
                                        atividades.getItenID(),
                                        atividades.getItenchecklist(),
                                        atividades.getItenDescricao());
                            } else {
                                myBDGeral.insertAtividades(
                                        atividades.getItenID(),
                                        atividades.getItenchecklist(),
                                        atividades.getItenDescricao(),
                                        "aberta");
                            }
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
            requestQueueAtividades = Volley.newRequestQueue(MainActivity_Principal.this);
            int socketTimeout2 = 20000;
            RetryPolicy policy3 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            requestAtividades.setRetryPolicy(policy3);
            requestQueueAtividades.add(requestAtividades);
        } else if (verificaConexao() == false) {
            Toast.makeText(getApplicationContext(), "Sem conexão com a internet. ", Toast.LENGTH_LONG).show();
        }
    }

}