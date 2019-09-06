package br.com.araujoabreu.timg.equipamento;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.principal.MainActivity_Principal;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GerarQRCODE extends AppCompatActivity {

    EditText edtTexto;
    ImageView ivQRCode;
    String tag, descricaoequipamento, id;
    Bitmap bitmap;
    Button btnEnviarQRCODE;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_qrcode);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        tag = dados.getString("tag");
        id = dados.getString("id");
        descricaoequipamento = dados.getString("descricao");

        iniciliarComponentes();
        gerarQRCode();

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void gerarQRCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            // Definir a identificação qrcode (TAG) e tamanho
            BitMatrix bitMatrix = multiFormatWriter.encode(tag, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            //Mostrar QRCODE Tela
            ivQRCode.setImageBitmap(bitmap);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
            String nameQRCODE = tag  + ".jpg";
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER/QRCODE/") + File.separator;
            File myDir = new File(root);
            myDir.mkdirs();
            file = new File(myDir, nameQRCODE);
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    private void iniciliarComponentes() {
        edtTexto = (EditText) findViewById(R.id.edtTexto);
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
        btnEnviarQRCODE = (Button) findViewById(R.id.btnGerar);

        edtTexto.setText(descricaoequipamento + " - " + tag);

        btnEnviarQRCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = FileProvider.getUriForFile(GerarQRCODE.this, BuildConfig.APPLICATION_ID + ".provider", file.getAbsoluteFile());
                Intent share = ShareCompat.IntentBuilder.from(GerarQRCODE.this)
                        .setStream(uri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(uri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Como deseja enviar QRCODE"));
                //Deletar Imagem ao Enviar
                file.delete();
            }
        });
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja voltar para tela inicial")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
                        String name = pref.getString("name", "");
                        String email= pref.getString("email", "");
                        String colaborador_id = pref.getString("id", "" );
                        String token = pref.getString("token", "");

                        Intent intent = new Intent(GerarQRCODE.this, MainActivity_Principal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("id", colaborador_id);
                        dados.putString("token", token);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

}