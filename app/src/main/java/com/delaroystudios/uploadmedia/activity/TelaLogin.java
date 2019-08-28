package com.delaroystudios.uploadmedia.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.delaroystudios.uploadmedia.principal.Permissões;
import com.google.gson.JsonObject;
import com.delaroystudios.uploadmedia.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class TelaLogin extends AppCompatActivity {



    private EditText emailLogar, senhaLogar;
    private Button btnLogar;
    private ImageView btnSite;
    private TextView txtRecuperarSenha;
    public String email, id, name, tipo;

    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static final String TAG = "PermissaoTAG";
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private String HOST = "http://helper.aplusweb.com.br/aplicativo";
   // private String HOST = "http://192.168.1.181/aplicativo";

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

    public void verificaDados() {

        // Caso ja tenha logado pela 1 vez, assim que abrir o APP dnv ja entra automaticamente

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String id = pref.getString("id", "" );
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String tipo = pref.getString("tipo", "");
        if(!email.isEmpty()) {

            Intent intent = new Intent(TelaLogin.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Validar permissões
        Permissões.validarPermissoes(permissoes, this, 1);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 480)
        {
            //Toast.makeText(this, "Igual ou maior que 480dp", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.tela_logintablet);

        } else if (config.smallestScreenWidthDp == 720)

        {
            //Toast.makeText(this, "Igual a 720dp", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.tela_logintablet);
        }
        else {
            setContentView(R.layout.tela_login);
        }

        // Deixa Tela Cheia
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailLogar = (EditText) findViewById(R.id.emailLogar);
        senhaLogar = (EditText) findViewById(R.id.senhaLogar);
        btnLogar = (Button) findViewById(R.id.btnLogar);
        txtRecuperarSenha = (TextView)findViewById(R.id.txtRecuperarSenha);


        verificaDados();


        txtRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://helper.aplusweb.com.br/password/reset"));
                startActivity(intent);
            }
        });

        // Botao para Logar
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailLogar.getText().toString();
                String password = senhaLogar.getText().toString();

                String URL = HOST + "/login.php";
                // Verifica se tem campo vazio

                if(verificaConexao() == true) {


                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(TelaLogin.this, "Todos os campos devem estar preenchidos !", Toast.LENGTH_LONG).show();


                    } else {

                        Ion.with(TelaLogin.this)
                                .load(URL)
                                .setBodyParameter("email_app", email)
                                .setBodyParameter("password_app", password)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {


                                        try {
                                            String RETORNO = result.get("LOGIN").getAsString();


                                            if (RETORNO.equals("ERRO")) {

                                                Toast.makeText(TelaLogin.this, "Email ou senha incorretos! ", Toast.LENGTH_LONG).show();

                                            }
                                            // Vai abrir caso dados corretos Main PRINCIPAL

                                            //"SUCESSO" --> Dados corretos
                                            else if (RETORNO.equals("SUCESSO")) {


                                                // Armazenar dados no APP
                                                String id = result.get("ID").getAsString();
                                                String matricula = result.get("MATRICULA").getAsString();
                                                String name = result.get("NAME").getAsString();
                                                String email = result.get("EMAIL").getAsString();
                                                String senha = result.get("PASSWORD").getAsString();
                                                String setor = result.get("SETOR").getAsString();
                                                String celular = result.get("CELULAR").getAsString();

                                                SharedPreferences.Editor pref = getSharedPreferences("info", MODE_PRIVATE).edit();
                                                pref.putString("id", id);
                                                pref.putString("matricula", matricula);
                                                pref.putString("name", name);
                                                pref.putString("email", email);
                                                pref.putString("senha", senha);
                                                pref.putString("tipo", setor);
                                                pref.putString("celular", celular);

                                                // Armazena as Preferencias
                                                pref.commit();

                                                Intent intent = new Intent(TelaLogin.this, MainActivity_Principal.class);
                                                Bundle dados = new Bundle();
                                                intent.putExtra("id", id);
                                                intent.putExtra("name", name);
                                                intent.putExtra("email", email);
                                                intent.putExtra("tipo", setor);
                                                intent.putExtras(dados);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(TelaLogin.this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();

                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(TelaLogin.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Sem conexão com a Internet !", Toast.LENGTH_LONG).show();
                }
            }
        });

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
}