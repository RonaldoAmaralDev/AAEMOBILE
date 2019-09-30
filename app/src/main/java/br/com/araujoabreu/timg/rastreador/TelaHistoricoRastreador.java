package br.com.araujoabreu.timg.rastreador;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import br.com.araujoabreu.timg.adapter.CentroLucroAdapter;
import br.com.araujoabreu.timg.adapter.HistoricoAdapter;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.helper.PermissoesSMS;
import br.com.araujoabreu.timg.model.CL;
import br.com.araujoabreu.timg.model.Historico;
import br.com.araujoabreu.timg.model.MyDividerItemDecoration;

public class TelaHistoricoRastreador extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private String name, token, colaborador_id, email;
    private List<Historico> historicos;
    private HistoricoAdapter mAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    BancoGeral bancoGeral;
    private ArrayAdapter adapter;
    private Spinner spinner_vehicle_filter_2, spinner_hour_filter_2, spinner_type_filter_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_historico_rastreador);


        bancoGeral = new BancoGeral(this);

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


        //Spinner com o dispaly placas
        //Por enquanto fixo, depois ira puxar no banco

        spinner_vehicle_filter_2 = (Spinner) findViewById(R.id.spinner_vehicle_filter_2);
        String[] placas = {"Todas as placas", "QOB-5726 - GOL 1.0 2019", "QPV-3232 - SAVEIRO 2019", "PQR-3298 - SAVEIRO 2019", "VPA-4562 - ONIX 2019", "LOK-2353 - SAVEIRO 2019"};
        ArrayAdapter adapterPlacas = new ArrayAdapter<String>(this, R.layout.spinner_item, placas);
        adapterPlacas.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_vehicle_filter_2.setAdapter(adapterPlacas);
        spinner_vehicle_filter_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(TelaHistoricoRastreador.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner com horarios

        spinner_hour_filter_2 = (Spinner) findViewById(R.id.spinner_hour_filter_2);
        String[] horarios = {"Todos", "6 Horas", "12 Horas", "24 Horas", "48 Horas", "Mês"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, horarios);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_hour_filter_2.setAdapter(adapter);
        spinner_hour_filter_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(TelaHistoricoRastreador.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Spinner com situações

        spinner_type_filter_2 = (Spinner) findViewById(R.id.spinner_type_filter_2);
        String[] situacoes = {"Todos", "Excesso de velocidade", "Dentro do Horário de Trabalho", "Fora do Horário de Trabalho", "Finais de Semana", "Motor Ligado Sem Movimento"};
        ArrayAdapter adapterSituacoes = new ArrayAdapter<String>(this, R.layout.spinner_item, situacoes);
        adapterSituacoes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_type_filter_2.setAdapter(adapterSituacoes);
        spinner_type_filter_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        RecyclerView recyclerView = findViewById(R.id.recycler_viewhistorico);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        mAdapter = new HistoricoAdapter(this, bancoGeral.buscaHistorico());
        recyclerView.setAdapter(mAdapter);



        // white background notification bar
        whiteNotificationBar(recyclerView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.history_menu, menu);

        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_ticketlog) {
            Intent intent = new Intent(TelaHistoricoRastreador.this, TelaPrincipalRastreador.class);
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

            Intent intent = new Intent(TelaHistoricoRastreador.this, TelaMapaRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);


        }

        if (id == R.id.navigation_historico) {

            TelaHistoricoRastreador.super.onRestart();
            Intent intent = new Intent(TelaHistoricoRastreador.this, TelaHistoricoRastreador.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }

        if (id == R.id.navigation_hidrometro) {

            Intent intent = new Intent(TelaHistoricoRastreador.this, TelaHodometroRastreador.class);
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }



}


