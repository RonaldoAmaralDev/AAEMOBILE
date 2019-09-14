package br.com.araujoabreu.timg.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.araujoabreu.timg.chat.config.ConfiguracaoFirebase;
import br.com.araujoabreu.timg.chat.helper.Base64Custom;
import br.com.araujoabreu.timg.chat.helper.UsuarioFirebase;
import br.com.araujoabreu.timg.chat.model.Usuario;
import br.com.araujoabreu.timg.helper.Permissões;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.gson.JsonObject;
import br.com.araujoabreu.timg.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class TelaLogin extends AppCompatActivity {

    private EditText emailLogar, senhaLogar;
    private Button btnLogar;
    private ImageView btnSite;
    private TextView txtRecuperarSenha;
    public String email, id, name, tipo;
    private FirebaseAuth autenticacao;


    private static final int REQUEST_PERMISSIONS_CODE = 1;
    private static final String TAG = "PermissaoTAG";
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private String HOST = "http://helper.aplusweb.com.br/aplicativo";

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
        String token = pref.getString("token", "");
        if(!email.isEmpty()) {

            Intent intent = new Intent(TelaLogin.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", id);
            dados.putString("token", token);
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

                String URL = "http://helper.aplusweb.com.br/api/auth";
                // Verifica se tem campo vazio

                if(verificaConexao() == true) {
                    if (email.isEmpty()) {
                        Toast.makeText(TelaLogin.this, "Campo email está em branco.", Toast.LENGTH_LONG).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(TelaLogin.this, "Campo senha está em branco.", Toast.LENGTH_LONG).show();
                    } else {
                        Ion.with(TelaLogin.this)
                                .load(URL)
                                .setBodyParameter("email", email)
                                .setBodyParameter("password", password)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                                  // Armazenar dados no APP
                                                  String token = result.get("token").getAsString();
                                                  //Pega dados na tabela users
                                                  JsonObject rates = (JsonObject) result.get("user");
                                                  String name = rates.get("name").getAsString();
                                                  String email = rates.get("email").getAsString();

                                                  //Pega dados na tabela colaboradors
                                                  JsonObject colaboradors = (JsonObject) result.get("colaborador");
                                                  String id = colaboradors.get("id").getAsString();
                                                  String matricula = colaboradors.get("matricula").getAsString();
                                                  String celular = colaboradors.get("celular").getAsString();

                                                  SharedPreferences.Editor pref = getSharedPreferences("info", MODE_PRIVATE).edit();
                                                  pref.putString("id", id);
                                                  pref.putString("name", name);
                                                  pref.putString("email", email);
                                                  pref.putString("token", token);
                                                  pref.putString("matricula", matricula);
                                                  pref.putString("celular", celular);

                                                  // Armazena as Preferencias
                                                  pref.commit();

                                                  Usuario usuario = new Usuario();
                                                  usuario.setNome( name );
                                                  usuario.setEmail( email );
                                                  usuario.setSenha( password );

                                                  //Cadastra Usuario Firebase
                                                  cadastrarUsuario( usuario, token);

                                        } catch (Exception erro) {
                                            senhaLogar.setText("");
                                            Toast.makeText(TelaLogin.this, "Verifique sua matricula, email ou senha."  , Toast.LENGTH_LONG).show();
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

    public void cadastrarUsuario(final Usuario usuario, String token){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                   // Toast.makeText(TelaLogin.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizarNomeUsuario( usuario.getNome() );
                    Intent intent = new Intent(TelaLogin.this, MainActivity_Principal.class);
                    Bundle dados = new Bundle();
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("token", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                    finish();
                    try {
                        String identificadorUsuario = Base64Custom.codificarBase64( emailLogar.getText().toString() );
                        usuario.setId( identificadorUsuario );
                        usuario.salvar();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                         excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                         excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                         excecao = "Este conta já foi cadastrada";
                         Intent intent = new Intent(TelaLogin.this, MainActivity_Principal.class);
                         Bundle dados = new Bundle();
                         intent.putExtra("id", id);
                         intent.putExtra("name", name);
                         intent.putExtra("email", email);
                         intent.putExtra("token", token);
                         intent.putExtras(dados);
                         startActivity(intent);
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }
                  //  Toast.makeText(TelaLogin.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}