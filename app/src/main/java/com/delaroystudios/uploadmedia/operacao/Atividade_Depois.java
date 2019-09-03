
package com.delaroystudios.uploadmedia.operacao;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
import com.delaroystudios.uploadmedia.operacao.os.MainActivityOS;
import com.delaroystudios.uploadmedia.visitas.MainActivityAtividades;
import com.delaroystudios.uploadmedia.visitas.MainActivityVisitas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Atividade_Depois extends AppCompatActivity {


    DatabaseHelper myDb;
    BancoGeral myDBGeral;
    ImageView imageView, imageView2, imageView3;
    TextView txtLatitude, txtLongitude, txtObservacao, txtAtividade;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int CAMERA_PIC_REQUEST2 = 1111;
    private static final int CAMERA_PIC_REQUEST3 = 1111;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private String os_id, foto1a, foto2a, foto3a, foto1d, foto2d, foto3d, checklist_id, id_Atividade;
    private String caminho1a, caminho2a, caminho3a, caminho1d, caminho2d, caminho3d, update_Status, data, inicio, fim;
    private String medicao2, dataplanejamento, tiposervico, local_id, id_centrolucro, latitude, longitude, equipamento_id, sigla, observacaoantes, situacao, atividade, medida1;
    private int index;
    public static final int REQ_CODE_SPEAK = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 480) {
            //Toast.makeText(this, "Igual ou maior que 480dp", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.tela_activitydepoistablet);

        } else if (config.smallestScreenWidthDp == 720) {
            //Toast.makeText(this, "Igual a 720dp", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.tela_activitydepoistablet);
        } else {
            setContentView(R.layout.tela_atividadedepois);
        }

        myDb = new DatabaseHelper(this);
        myDBGeral = new BancoGeral(this);

        imageView = (ImageView) findViewById(R.id.foto1d);
        imageView2 = (ImageView) findViewById(R.id.foto2d);
        imageView3 = (ImageView) findViewById(R.id.foto3d);
        txtObservacao = (TextView) findViewById(R.id.txtObservacaoDepois);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtAtividade = (TextView) findViewById(R.id.txtAtividadeDepois);

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Atividade Depois");


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        os_id = dados.getString("os_id");
        checklist_id = dados.getString("checklist_id");
        equipamento_id = dados.getString("equipamento_id");
        atividade = dados.getString("atividade");
        latitude = dados.getString("latitude");
        longitude = dados.getString("longitude");
        id_Atividade = dados.getString("id_Atividade");
        tiposervico = dados.getString("tiposervico");
        local_id = dados.getString("local_id");
        id_centrolucro = dados.getString("id_centrolucro");
        dataplanejamento = dados.getString("dataplanejamento");

        txtAtividade.setText(atividade);
        txtLatitude.setText(latitude);
        txtLongitude.setText(longitude);

        File imgFile = new File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1D" + ".jpg");
        File imgFile2 = new File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "2D" + ".jpg");
        File imgFile3 = new File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "3D" + ".jpg");

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
        if (imgFile2.exists()) {
            Bitmap myBitmap2 = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
            imageView2.setImageBitmap(myBitmap2);
        }
        if (imgFile3.exists()) {
            Bitmap myBitmap3 = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
            imageView3.setImageBitmap(myBitmap3);
        }

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.text.SimpleDateFormat sdfDataEncerramento =
                new java.text.SimpleDateFormat("yyyy/MM/dd");
        String currentTime = sdf.format(dt);
        String dataencerramento = sdfDataEncerramento.format(dt);

        txtObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Atividade_Depois.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Você deseja: ")
                        .setPositiveButton("Falar", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(Atividade_Depois.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(Atividade_Depois.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(Atividade_Depois.this)
                        .setIcon(R.drawable.logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Deseja:")
                        .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener() {
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

        getMenuInflater().inflate(R.menu.menu_atividadesdepois, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        java.text.SimpleDateFormat sdfDataEncerramento =
                new java.text.SimpleDateFormat("yyyy/MM/dd");
        String dataencerramento = sdfDataEncerramento.format(dt);

        //Se clicar em continuar visita
        if (id == R.id.action_confirmar) {
            if (imageView.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "Você deve tirar pelo menos a 1ª Foto !", Toast.LENGTH_LONG).show();
            } else {
                myDb.updateOS(
                        os_id,
                        txtObservacao.getText().toString(),
                        currentTime,
                        dataencerramento,
                        "/assets/os/" + os_id + "_assinaturaColaborador.jpg",
                        "no");

                //Encerra Atividade
                myDBGeral.updateStatusAtividade(
                        id_Atividade,
                        checklist_id
                );

                SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
                String name = pref.getString("name", "");
                String email = pref.getString("email", "");
                String colaborador_id = pref.getString("id", "");
                String token = pref.getString("token", "");


                Intent intent = new Intent(Atividade_Depois.this, MainActivityAtividades.class);
                Bundle dados = new Bundle();
                dados.putString("os_id", os_id);
                dados.putString("checklist_id", checklist_id);
                dados.putString("equipamento_id", equipamento_id);
                dados.putString("local_id", local_id);
                dados.putString("tiposervico", tiposervico);
                dados.putString("dataplanejamento", dataplanejamento);
                dados.putString("centrocusto_id", id_centrolucro);
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("colaborador_id", colaborador_id);
                dados.putString("token", token);
                intent.putExtras(dados);
                startActivity(intent);
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
                    medicao2 = input.getText().toString();

                    //Gravar Medição 1
                    myDb.updateMedicao2(
                            os_id,
                            checklist_id,
                            id_Atividade,
                            medicao2
                    );

                }
            });
            alert.show();

            return true;
        }
        //Se clicar em voltar atividade Anterior
        if(id == android.R.id.home) {

            Intent intent = new Intent(Atividade_Depois.this, Atividade_Antes.class);
            Bundle dados = new Bundle();
            dados.putString("os_id", os_id);
            dados.putString("checklist_id", checklist_id);
            dados.putString("equipamento_id", equipamento_id);
            dados.putString("id_Atividade", id_Atividade);
            dados.putString("id_centrolucro", id_centrolucro);
            dados.putString("local_id", local_id);
            dados.putString("tiposervico", tiposervico);
            dados.putString("dataplanejamento", dataplanejamento);
            dados.putString("atividade", atividade);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        Intent intent = new Intent(Atividade_Depois.this, MainActivityAtividades.class);
        Bundle dados = new Bundle();
        dados.putString("os_id", os_id);
        dados.putString("checklist_id", checklist_id);
        dados.putString("equipamento_id", equipamento_id);
        dados.putString("local_id", local_id);
        dados.putString("tiposervico", tiposervico);
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
        startActivityForResult(intent, 1);
    }

    public void CreateImage2() {

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent2, 2);
    }

    public void CreateImage3() {

        Intent intent3 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent3, 3);
    }

    public void lerVoz() {

        Intent intent4 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent4.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent4.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent4.putExtra(RecognizerIntent.EXTRA_PROMPT, "Favor descrever observação. Ex: Foi gasto 2 metros de cabo de rede. ");
        try {
            startActivityForResult(intent4, REQ_CODE_SPEAK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "O seu telefone não suporta a utilização do microfone. ", Toast.LENGTH_LONG).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            switch (requestCode) {
                case 1:
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(thumbnail);
                    //Se tirar foto fazer update Tabela
                    myDb.updateFotoCaminho1d(
                            os_id,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1D" + ".jpg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, bytes);
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER") + File.separator;
                    File myDir = new File(root);
                    myDir.mkdirs();
                    String fname = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "1D" + ".jpg";
                    File file = new File(myDir, fname);
                    //Se caso foto já exista deletar e substituir
                    if (file.exists()) file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(bytes.toByteArray());
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    // Ao Clicar para tirar 2 FOTO:
                    Bitmap thumbnail2 = (Bitmap) data.getExtras().get("data");
                    imageView2.setImageBitmap(thumbnail2);
                    //Se tirar foto fazer update Tabela
                    myDb.updateFotoCaminho2d(
                            os_id,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "2D" + ".jpg");
                    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
                    thumbnail2.compress(Bitmap.CompressFormat.JPEG, 75, bytes2);
                    String imageFileName2 = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "2D" + ".jpg";
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
                    myDb.updateFotoCaminho3d(
                            os_id,
                            "/assets/os/" + os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "3D" + ".jpg");
                    ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
                    thumbnail3.compress(Bitmap.CompressFormat.JPEG, 75, bytes3);
                    String imageFileName3 = os_id + "_" + checklist_id + "_" + id_Atividade + "_" + "3D" + ".jpg";
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

}

