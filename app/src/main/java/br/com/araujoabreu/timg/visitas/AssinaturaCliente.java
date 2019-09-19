package br.com.araujoabreu.timg.visitas;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.banco.DatabaseHelper;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import br.com.araujoabreu.timg.R;


public class AssinaturaCliente extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private String name, email, idColaborador, token, os_id, frequencia_id, equipamento_id, local_id, tiposervico, checklist, dataplanejamento, id_centrolucro;
    private TextView txtCliente;
    BancoGeral myDBGeral;
    DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_assinaturacliente);

        myDB = new DatabaseHelper(this);


        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ass. Cliente");

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        os_id = dados.getString("os_id");
        equipamento_id = dados.getString("equipamento_id");
        dataplanejamento = dados.getString("dataplanejamento");
        local_id = dados.getString("local_id");
        checklist = dados.getString("checklist");
        tiposervico = dados.getString("tiposervico");
        id_centrolucro = dados.getString("id_centrolucro");
        frequencia_id = dados.getString("frequencia_id");
        name = dados.getString("name");
        email = dados.getString("email");
        idColaborador = dados.getString("idColaborador");
        token = dados.getString("token");

        myDBGeral = new BancoGeral(this);

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


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.drawable.logo);
        alert.setMessage("Cliente, favor fornecer identificação: ");
        final EditText input = new EditText(this);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int whichButton) {

                myDB.updateAssinaturaCliente(
                        os_id,
                        "/assets/os/" + os_id + "_assinaturaCliente.jpg"
                );

                if(input.equals("")) {
                    Toast.makeText(getApplicationContext(), "Favor justificar falta matricula", Toast.LENGTH_LONG).show();
                } else
                {

                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.setNeutralButton("Não se Aplica", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDBGeral.updateEncerradaStatus(os_id);

                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(dt);

                myDB.updateAssinaturaCliente(
                        os_id,
                        "/assets/imgs/semfoto.jpg"
                );

                //Finalizar Contagem HH da OS
                myDBGeral.updateHHFim(
                        os_id,
                        currentTime);

                myDBGeral.updateStatusAtividadeAberta(
                        checklist);

                //Fazer verificação se for uma visita preventiva, ele irá gerar para o mes de frequencia

                int frequencia_idINT = Integer.parseInt(frequencia_id);

                //Se for mensal (30 dias)
                if (frequencia_idINT == 1) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_MONTH, 30); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Diario (1 Dia)
                else if(frequencia_idINT == 2) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_MONTH, 1); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Anual
                else if(frequencia_idINT == 3) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 365); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                else if(frequencia_idINT == 4) {
                    //Se for bimestral (60 dias)

                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 60); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Semestral (6 Meses = 182 Dias)
                else if(frequencia_idINT == 5) {

                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 182); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Trimestral (90 dias)
                else if(frequencia_idINT == 6) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 90); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for NA (Provavelmente sem periodicidade)
                else if(frequencia_idINT == 7 ) {

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for SEMANAL (7 Dias)
                else if(frequencia_idINT == 8) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_WEEK, 7); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se não for nenhum desses
                else {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 30); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Fim IF

            }
        });


        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_assinatura, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdfBR = new java.text.SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");


        //Se clicar em continuar visita
        if (id == R.id.action_confirmar) {
            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
            if (addJpgSignatureToGallery(signatureBitmap)) {

                myDBGeral.updateEncerradaStatus(os_id);

                java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(dt);

                myDB.updateAssinaturaCliente(
                        os_id,
                        "/assets/os/" + os_id + "_assinaturaCliente.jpg"
                );

                //Finalizar Contagem HH da OS
                myDBGeral.updateHHFim(
                        os_id,
                        currentTime);

                myDBGeral.updateStatusAtividadeAberta(
                        checklist);

                //Fazer verificação se for uma visita preventiva, ele irá gerar para o mes de frequencia

                int frequencia_idINT = Integer.parseInt(frequencia_id);

                //Se for mensal (30 dias)
                if (frequencia_idINT == 1) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_MONTH, 30); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Diario (1 Dia)
                else if(frequencia_idINT == 2) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_MONTH, 1); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Anual
                else if(frequencia_idINT == 3) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 365); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                else if(frequencia_idINT == 4) {
                    //Se for bimestral (60 dias)

                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 60); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Semestral (6 Meses = 182 Dias)
                else if(frequencia_idINT == 5) {

                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 182); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for Trimestral (90 dias)
                else if(frequencia_idINT == 6) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 90); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for NA (Provavelmente sem periodicidade)
                else if(frequencia_idINT == 7 ) {

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se for SEMANAL (7 Dias)
                else if(frequencia_idINT == 8) {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_WEEK, 7); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Se não for nenhum desses
                else {
                    Calendar cal = Calendar.getInstance(); //
                    cal.setTime(new Date()); //
                    cal.add(Calendar.DAY_OF_YEAR, 30); // Adicionar Tempo Estimado
                    cal.getTime(); //
                    SimpleDateFormat datafim = new SimpleDateFormat("yyyy-MM-dd");
                    String proximaData = datafim.format(cal.getTime());

                    myDBGeral.updateDataProximaVisita(
                            os_id,
                            proximaData);

                    Toast.makeText(getApplicationContext(), "Visita: " + os_id + " , finalizada com sucesso !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AssinaturaCliente.this, VisitasLocal.class);
                    Bundle dados = new Bundle();
                    dados.putString("local_id", local_id);
                    dados.putString("centrolucro_id", id_centrolucro);
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", idColaborador);
                    dados.putString("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
                //Fim IF

                } else {
                    Toast.makeText(AssinaturaCliente.this,  "Não foi possivel salvar assinatura !", Toast.LENGTH_SHORT).show();
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

            Intent intent = new Intent(this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", idColaborador);
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
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AssinaturaCliente.this,  "Não foi possivel salvar Assinatura no celular", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.

        String fname = os_id + "_assinaturaCliente.jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")
                +File.separator + fname);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory não salvo !");
        }
        return file;
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
            String fname = os_id + "_assinaturaCliente.jpg";
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
        AssinaturaCliente.this.sendBroadcast(mediaScanIntent);
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

                        myDBGeral.updateStatusAtividadeAberta(
                                checklist);

                        Intent intent = new Intent(AssinaturaCliente.this, MainActivity_Principal.class);
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