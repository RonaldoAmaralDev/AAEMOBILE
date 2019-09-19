package br.com.araujoabreu.timg.visitas;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.banco.DatabaseHelper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Atividade_Antes extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DatabaseHelper myDb;
    BancoGeral myDBGeral;
    ImageView imageView, imageView2, imageView3;
    TextView txtLatitude, txtLongitude, txtObservacao, txtAtividade;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int CAMERA_PIC_REQUEST2 = 1111;
    private static final int CAMERA_PIC_REQUEST3 = 1111;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_LOCATION = 1;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public static final int MEDIA_TYPE_IMAGE = 1;
    private String  os_id, foto1a, foto2a, foto3a, foto1d, foto2d, foto3d, checklist_id;
    private String medicao1, update_Status, data, inicio, fim;
    private String id_centrolucro, latitude, frequencia_id, longitude, equipamento_id, atividade, id_Atividade, tiposervico, local_id, dataplanejamento;
    private int index;
    public static final int REQ_CODE_SPEAK = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_TAKEPICTURE = {Manifest.permission.CAMERA};
    private static String[] PERMISSIONS_READ_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private Uri currentURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 480)
        {
            setContentView(R.layout.tela_activityantestablet);

        } else if (config.smallestScreenWidthDp == 720)

        {
            setContentView(R.layout.tela_activityantestablet);
        }
        else {
            setContentView(R.layout.tela_atividadeantes);
        }


        myDb = new DatabaseHelper(this);
        myDBGeral = new BancoGeral(this);

        imageView = (ImageView) findViewById(R.id.foto1a);
        imageView2 = (ImageView) findViewById(R.id.foto2a);
        imageView3 = (ImageView) findViewById(R.id.foto3a);
        txtObservacao = (TextView) findViewById(R.id.txtObservacaoAntes);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtAtividade = (TextView) findViewById(R.id.txtAtividade);

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Antes");

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        os_id = dados.getString("os_id");
        equipamento_id = dados.getString("equipamento_id");
        checklist_id = dados.getString("checklist_id");
        id_Atividade = dados.getString("id_Atividade");
        id_centrolucro = dados.getString("id_centrolucro");
        local_id = dados.getString("local_id");
        frequencia_id = dados.getString("frequencia_id");
        tiposervico = dados.getString("tiposervico");
        dataplanejamento = dados.getString("dataplanejamento");
        atividade = dados.getString("atividade");

        //Coloca a atividade
        txtAtividade.setText(atividade);

        File imgFile = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1A" + ".jpg");
        File imgFile2 = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_"+ id_Atividade + "_" + "2A" + ".jpg");
        File imgFile3 = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_" + id_Atividade  + "_" + "3A" + ".jpg");

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
        if(imgFile2.exists()){
            Bitmap myBitmap2 = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
            imageView2.setImageBitmap(myBitmap2);
        }
        if(imgFile3.exists()){
            Bitmap myBitmap3 = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
            imageView3.setImageBitmap(myBitmap3);
        }
        Cursor data =  myDb.getOS(os_id, id_Atividade);
        if (data.moveToNext()) {
            latitude = data.getString(14);
            longitude = data.getString(15);
            String observacao = data.getString(12);

            txtLatitude.setText(latitude);
            txtLongitude.setText(longitude);
            txtObservacao.setText(observacao);
        }


        //Pegar Localização Colaborador
        callConection();

        txtObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Atividade_Antes.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Você deseja: ")
                        .setPositiveButton("Falar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                lerVoz();
                            }
                        })
                        .setNegativeButton("Digitar Observação", null)
                        .show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Atividade_Antes.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Você Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CreateImage1();
                            }

                        })
                        .setNegativeButton("Remover Foto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int witch) {
                                imageView.setImageBitmap(null);
                            }
                        })
                        .show();
            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Atividade_Antes.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Você Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CreateImage2();
                            }

                        })
                        .setNegativeButton("Remover Foto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int witch) {
                                imageView2.setImageBitmap(null);
                            }
                        })
                        .show();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Atividade_Antes.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Você Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CreateImage3();
                            }

                        })
                        .setNegativeButton("Remover Foto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int witch) {
                                imageView3.setImageBitmap(null);
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_atividadesantes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email = pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        //Se clicar em continuar visita
        if (id == R.id.action_confirmar) {
            if (imageView.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "Você deve tirar pelo menos a 1ª Foto !", Toast.LENGTH_LONG).show();
            } else {
                myDb.updateAtividade(os_id,
                        checklist_id,
                        id_Atividade,
                        currentTime,
                        txtObservacao.getText().toString(),
                        txtLatitude.getText().toString(),
                        txtLongitude.getText().toString(),
                        "C",
                        "no"
                );
                Intent intent = new Intent(Atividade_Antes.this, Atividade_Depois.class);
                Bundle dados = new Bundle();
                dados.putString("os_id", os_id);
                dados.putString("checklist_id", checklist_id);
                dados.putString("equipamento_id", equipamento_id);
                dados.putString("atividade", atividade);
                dados.putString("latitude", txtLatitude.getText().toString());
                dados.putString("longitude", txtLongitude.getText().toString());
                dados.putString("id_Atividade", id_Atividade);
                dados.putString("id_centrolucro", id_centrolucro);
                dados.putString("local_id", local_id);
                dados.putString("frequencia_id", frequencia_id);
                dados.putString("tiposervico", tiposervico);
                dados.putString("dataplanejamento", dataplanejamento);
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("idColaborador", colaborador_id);
                dados.putString("token", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
            return true;
        }
        // Se clicar em Não se Aplica
        else if (id == R.id.action_naoseaplica) {
            //Verifica se é Corretiva, se for não pode por NA
            int tiposervicoINT = Integer.parseInt(tiposervico);
            if(tiposervicoINT == 2) {
                Toast.makeText(getApplicationContext(), "Você está em uma visita corretiva. ", Toast.LENGTH_LONG).show();
            } else {
                boolean isInserted = myDb.insertNA(
                        os_id,
                        checklist_id,
                        id_Atividade,
                        foto1a = "",
                        foto2a = "",
                        foto3a = "",
                        foto1d = "",
                        foto2d = "",
                        foto3d = "",
                        currentTime,
                        currentTime,
                        "NA",
                        "NA",
                        txtLatitude.getText().toString(),
                        txtLongitude.getText().toString(),
                        "NA",
                        currentTime,
                        update_Status = "no");
                if (isInserted == true) {

                    //Encerra Atividade
                    myDBGeral.updateStatusAtividade(
                            id_Atividade,
                            checklist_id
                    );

                    Intent intent = new Intent(Atividade_Antes.this, MainActivityAtividades.class);
                    Bundle dados = new Bundle();
                    dados.putString("os_id", os_id);
                    dados.putString("checklist_id", checklist_id);
                    dados.putString("equipamento_id", equipamento_id);
                    dados.putString("local_id", local_id);
                    dados.putString("frequencia_id", frequencia_id);
                    dados.putString("tiposervico", tiposervico);
                    dados.putString("dataplanejamento", dataplanejamento);
                    dados.putString("centrocusto_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("colaborador_id", colaborador_id);
                    dados.putString("tipo", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
            }
            return true;
        }
        //Caso clique para inserir Medição
        else if (id == R.id.action_medicao) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.app_name);
            alert.setIcon(R.drawable.logo);
            alert.setCancelable(false);
            alert.setMessage("Informe a medição: ");
            final EditText input = new EditText(this);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            alert.setView(input);
            alert.setPositiveButton("Gravar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                   medicao1 = input.getText().toString();

                   //Gravar Medição 1
                   myDb.updateMedicao1(
                           os_id,
                           checklist_id,
                           id_Atividade,
                           medicao1
                   );

                }
            });
            alert.show();
        return true;
        }
        //Se pressionar para voltar atividade
        if(id == android.R.id.home) {
            //Volta para MainActivityAtividades

            Intent intent = new Intent(Atividade_Antes.this, MainActivityAtividades.class);
            Bundle dados = new Bundle();
            dados.putString("os_id", os_id);
            dados.putString("checklist_id", checklist_id);
            dados.putString("equipamento_id", equipamento_id);
            dados.putString("local_id", local_id);
            dados.putString("tiposervico", tiposervico);
            dados.putString("frequencia_id", frequencia_id);
            dados.putString("dataplanejamento", dataplanejamento);
            dados.putString("centrocusto_id", id_centrolucro);
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("colaborador_id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PIC_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Atividade_Antes.this,  "Não foi possivel salvar foto", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }


    @Override
    public void onBackPressed() {

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        Intent intent = new Intent(Atividade_Antes.this, MainActivityAtividades.class);
        Bundle dados = new Bundle();
        dados.putString("os_id", os_id);
        dados.putString("checklist_id", checklist_id);
        dados.putString("equipamento_id", equipamento_id);
        dados.putString("local_id", local_id);
        dados.putString("tiposervico", tiposervico);
        dados.putString("frequencia_id", frequencia_id);
        dados.putString("dataplanejamento", dataplanejamento);
        dados.putString("centrocusto_id", id_centrolucro);
        dados.putString("name", name);
        dados.putString("email", email);
        dados.putString("colaborador_id", colaborador_id);
        dados.putString("token", token);
        intent.putExtras(dados);
        startActivity(intent);
    }

    public void CreateImage1() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent, 1);
    }

    public void CreateImage2() {

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent2, 2);
    }

    public void CreateImage3() {

        Intent intent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent3.putExtra(MediaStore.EXTRA_OUTPUT, currentURI);
        startActivityForResult(intent3, 3);
    }

    public void lerVoz() {

        Intent intent4 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent4.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent4.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent4.putExtra(RecognizerIntent.EXTRA_PROMPT, "Favor descrever observação. Ex: Abertura Loja 10:00. ");
        try {
            startActivityForResult(intent4, REQ_CODE_SPEAK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "O seu telefone não suporta a utilização do microfone. ", Toast.LENGTH_LONG).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(thumbnail);
                    boolean isInserted = myDb.insertFoto1(
                            os_id,
                            checklist_id,
                            id_Atividade,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1A" + ".jpg",
                            "no");
                    // Se salvar a 1ª Foto e gravar na tabela
                    if(isInserted == true) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER") + File.separator;
                    File myDir = new File(root);
                    myDir.mkdirs();
                    String fname = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1A" + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(bytes.toByteArray());
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                    break;
                case 2:
                    // Ao Clicar para tirar 2 FOTO:
                    Bitmap thumbnail2 = (Bitmap) data.getExtras().get("data");
                    imageView2.setImageBitmap(thumbnail2);
                    //Se tirar foto fazer update Tabela
                    myDb.updateFotoCaminho2a(
                            os_id,
                            checklist_id,
                            id_Atividade,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "2A" + ".jpg");
                    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
                    thumbnail2.compress(Bitmap.CompressFormat.JPEG, 30, bytes2);
                    String imageFileName2 = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "2A" + ".jpg";
                    File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER") + File.separator + imageFileName2);
                    try {
                        file2.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file2);
                        fo.write(bytes2.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    // Ao Clicar para tirar 3 FOTO:
                    Bitmap thumbnail3 = (Bitmap) data.getExtras().get("data");
                    imageView3.setImageBitmap(thumbnail3);
                    //Se tirar foto fazer update Tabela
                    myDb.updateFotoCaminho3a(
                            os_id,
                            checklist_id,
                            id_Atividade,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "3A" + ".jpg");
                    ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
                    thumbnail3.compress(Bitmap.CompressFormat.JPEG, 30, bytes3);
                    String imageFileName3 = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "3A" + ".jpg";
                    File file3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER") + File.separator + imageFileName3);
                    try {
                        file3.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file3);
                        fo.write(bytes3.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case REQ_CODE_SPEAK:
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String resultado = result.get(0);
                    txtObservacao.setText(resultado);
                    break;
            }
        }
    }
    //CODE GOOGLE MAPS - LOCALIZAÇÃO
    private synchronized  void callConection() {
        mGoogleApiClient = new GoogleApiClient.Builder(Atividade_Antes.this)
                .addOnConnectionFailedListener(Atividade_Antes.this)
                .addConnectionCallbacks(Atividade_Antes.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");

        if (ContextCompat.checkSelfPermission(Atividade_Antes.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Atividade_Antes.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(l != null) {
            Log.i("LOG", "Latitude" + l.getLatitude());
            Log.i("LOG", "Longtitude" + l.getLongitude());

            txtLatitude.setText( "" + l.getLatitude());
            txtLongitude.setText("" + l.getLongitude());
        }else {
            txtLatitude.setVisibility(View.INVISIBLE);
            txtLongitude.setVisibility(View.INVISIBLE);
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

