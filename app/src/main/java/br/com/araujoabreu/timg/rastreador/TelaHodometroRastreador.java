package br.com.araujoabreu.timg.rastreador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import br.com.araujoabreu.timg.R;

public class TelaHodometroRastreador extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String name, token, colaborador_id, email;
    private Spinner spinner_vehicle1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_hodometro_rastreador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#8F152A")));

        //Não abrir o teclado automatico
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando a Km dos veiculos... ");
        progressDialog.setIcon(R.drawable.logo);
        progressDialog.show();

        if (progressDialog.isShowing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 3000);

        }


        //Spinner com o dispaly placas
        //Por enquanto fixo, depois ira puxar no banco

        spinner_vehicle1 = (Spinner) findViewById(R.id.spinner_vehicle1);
        String[] placas = {"QUR-3617 - STRADA 2019", "QOB-5726 - GOL 1.0 2019", "QPV-3232 - SAVEIRO 2019", "PQR-3298 - SAVEIRO 2019", "VPA-4562 - ONIX 2019", "LOK-2353 - SAVEIRO 2019"};
        ArrayAdapter adapterPlacas = new ArrayAdapter<String>(this, R.layout.spinner_item, placas);
        adapterPlacas.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_vehicle1.setAdapter(adapterPlacas);
        spinner_vehicle1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(TelaHistoricoRastreador.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewTelaRastreamento);
        navigation.setOnNavigationItemSelectedListener(this);

        //Aparecer todos os Icones e Titulos
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.vehicle_menu, menu);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_ticketlog) {
            Intent intent = new Intent(TelaHodometroRastreador.this, TelaPrincipalRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            finish();
        }
        if (id == R.id.navigation_mapa) {

            Intent intent = new Intent(TelaHodometroRastreador.this, TelaMapaRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);


        }

        if (id == R.id.navigation_historico) {

            Intent intent = new Intent(TelaHodometroRastreador.this, TelaHistoricoRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }

        if (id == R.id.navigation_hidrometro) {

            TelaHodometroRastreador.super.onRestart();
            Intent intent = new Intent(TelaHodometroRastreador.this, TelaHodometroRastreador.class);
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
