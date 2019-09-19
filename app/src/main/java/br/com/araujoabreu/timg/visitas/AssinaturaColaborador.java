package br.com.araujoabreu.timg.visitas;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import br.com.araujoabreu.timg.banco.DatabaseHelper;

import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;

public class AssinaturaColaborador extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private String name, email, idColaborador, token, os_id, frequencia_id, equipamento_id, local_id, tiposervico, checklist, dataplanejamento, id_centrolucro, checklist_id;
    private TextView txtColaborador;
    DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_assinatura_colaborador);

        myDB = new DatabaseHelper(this);

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ass. Colaborador");

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        os_id = dados.getString("os_id");
        equipamento_id = dados.getString("equipamento_id");
        local_id = dados.getString("local_id");
        dataplanejamento = dados.getString("dataplanejamento");
        checklist = dados.getString("checklist");
        frequencia_id = dados.getString("frequencia_id");
        tiposervico = dados.getString("tiposervico");
        id_centrolucro = dados.getString("id_centrolucro");
        name = dados.getString("name");
        email = dados.getString("email");
        idColaborador = dados.getString("idColaborador");
        token = dados.getString("token");

        txtColaborador = (TextView) findViewById(R.id.signature_pad_description);
        txtColaborador.setText(name);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_assinatura, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Se clicar em continuar visita
        if (id == R.id.action_confirmar) {
            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
            if (addJpgSignatureToGallery(signatureBitmap)) {


                myDB.updateAssinaturaColaborador(
                        os_id,
                        "/assets/os/" + os_id + "_assinaturaColaborador.jpg"
                );

                Intent intent = new Intent(AssinaturaColaborador.this, AssinaturaCliente.class);
                Bundle dados = new Bundle();
                dados.putString("equipamento_id", equipamento_id);
                dados.putString("os_id", os_id);
                dados.putString("local_id", local_id);
                dados.putString("tiposervico", tiposervico);
                dados.putString("checklist", checklist);
                dados.putString("dataplanejamento", dataplanejamento);
                dados.putString("id_centrolucro", id_centrolucro);
                dados.putString("frequencia_id", frequencia_id);
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("idColaborador", idColaborador);
                dados.putString("token", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
            return true;
        }
        // Se clicar em Apagar Assinatura
        else if (id == R.id.action_apagar) {
            mSignaturePad.clear();
            return true;
        }
        //Se clicar em voltar atividade Anterior
        if(id == android.R.id.home) {


            Intent intent = new Intent(AssinaturaColaborador.this, MainActivityAtividades.class);
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
            dados.putString("colaborador_id", idColaborador);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.

        String fname = os_id + "_assinaturaColaborador.jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")
                +File.separator + fname);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory não salvo !");
        }
        return file;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AssinaturaColaborador.this,  "Não foi possivel salvar Assinatura no celular", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+File.separator;
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = os_id + "_assinaturaColaborador.jpg";
            File file = new File(myDir, fname);
            saveBitmapToJPG(signature, file);
            scanMediaFile(file);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        AssinaturaColaborador.this.sendBroadcast(mediaScanIntent);
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
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja cancelar visita: " + os_id)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.deleteOS(os_id);
                        Intent intent = new Intent(AssinaturaColaborador.this, VisitasEquipamento.class);
                        Bundle dados = new Bundle();
                        dados.putString("equipamento_id", equipamento_id);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }
}