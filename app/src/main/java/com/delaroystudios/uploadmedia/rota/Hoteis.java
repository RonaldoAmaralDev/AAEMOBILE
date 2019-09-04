package com.delaroystudios.uploadmedia.rota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.equipamento.qrcode.LoadingScanner;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.delaroystudios.uploadmedia.visitas.VisitasLocal;

import java.text.SimpleDateFormat;

public class Hoteis extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String email, name, colaborador_id, token, latitude, longitude;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteis);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

        webView = (WebView) findViewById(R.id.mapaHoteis);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procurando os hoteis mais proximos e com os melhores preços... ");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
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

        SharedPreferences salvarLocalizacao = getSharedPreferences("salvarLocalizacao", MODE_PRIVATE);
        latitude = salvarLocalizacao.getString("latitude", "");
        longitude= salvarLocalizacao.getString("longitude", "");


        //Pega data atual
        String data = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());


        webView.loadUrl("https://www.google.com.br/maps/search/hot%C3%A9is/@"+latitude+","+longitude+",14z/data=!4m6!2m5!5m3!5m1!1s"+data+"!10e1!6e3");


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewTelaPrincipal);
        navigation.setOnNavigationItemSelectedListener(this);

        //Aparecer todos os Icones e Titulos
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Os 4 primeiros são o navegation bottom (De baixo)
        if (id == R.id.navigation_mapa) {
            Intent intent = new Intent(this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }
        if (id == R.id.navigation_visitas) {

            Intent intent = new Intent(this, VisitasLocal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("colaborador_id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);

        }

        if (id == R.id.navigation_scanner) {

            Intent intent = new Intent(this, LoadingScanner.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }
        return true;
    }



}
