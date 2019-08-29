package com.delaroystudios.uploadmedia.rota;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.google.android.gms.maps.model.LatLng;

public class TrajetoLocal extends AppCompatActivity {

    public String email, name, colaborador_id, tipo, latitude, longitude, latitude_local, longitude_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajeto_local);

        // Deixa Tela Cheia
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("colaborador_id");
        tipo = dados.getString("tipo");
        latitude_local = dados.getString("latitude_local");
        longitude_local = dados.getString("longitude_local");

        SharedPreferences salvarLocalizacao = getSharedPreferences("salvarLocalizacao", MODE_PRIVATE);
        latitude = salvarLocalizacao.getString("latitude", "");
        longitude= salvarLocalizacao.getString("longitude", "");

        WebView myWebView = (WebView) findViewById(R.id.mapaRota);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Traçando a melhor rota, aguarde alguns segundos... ");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 3000);

                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });

        myWebView.loadUrl("https://www.google.com/maps/dir/" + latitude + "," + longitude + "/" + latitude_local + "," + longitude_local+"/data=!3m1!4b1");
    }


    //Se pressionar botão voltar
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja voltar para tela inicial")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TrajetoLocal.this, MainActivity_Principal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("id", colaborador_id);
                        dados.putString("tipo", tipo);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

}