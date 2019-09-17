package br.com.araujoabreu.timg.visitas;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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


import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.adapter.VisitaAdapter;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.model.MyDividerItemDecoration;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;

import java.util.Calendar;

public class VisitasEquipamento extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = VisitasEquipamento.class.getSimpleName();

    private VisitaAdapter mAdapter;
    private SearchView searchView;
    private String name, email, colaborador_id, token, equipamento_id, local_id, centrolucro_id;
    BancoGeral myDBGeral;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_equipamento);

        Toolbar toolbar = findViewById(R.id.toolbarvisita);
        setSupportActionBar(toolbar);

        myDBGeral = new BancoGeral(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        equipamento_id = dados.getString("equipamento_id");
        centrolucro_id = dados.getString("centrolucro_id");
        local_id = dados.getString("local_id");

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        colaborador_id = pref.getString("id", "");
        token = pref.getString("token", "");

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("VISITAS");

        RecyclerView recyclerView = findViewById(R.id.recycler_viewvisita);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData = new java.text.SimpleDateFormat("yyyy/MM/dd");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);



        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewVisita);
        navigation.setOnNavigationItemSelectedListener(this);


        //Trocar titulos
        navigation.getMenu().findItem(R.id.navigation_abertas).setTitle("ABERTAS("+String.valueOf(myDBGeral.dbCountAbertasLocaleEquipamento(equipamento_id)+")"));
        navigation.getMenu().findItem(R.id.navigation_pendentes).setTitle("PENDENTES("+String.valueOf(myDBGeral.dbCountEmEsperaeEquipamento(equipamento_id)+")"));
        navigation.getMenu().findItem(R.id.navigation_encerradas).setTitle("ENCERRADAS("+String.valueOf(myDBGeral.dbCountEncerradasEquipamento(local_id, equipamento_id)+")"));


        mAdapter = new VisitaAdapter(this, myDBGeral.buscaOS(equipamento_id, colaborador_id));
        recyclerView.setAdapter(mAdapter);


        // white background notification bar
        whiteNotificationBar(recyclerView);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Os 3 primeiros são o navegation bottom (De baixo)
        if(id == R.id.navigation_abertas) {

            mAdapter.swapCursor(myDBGeral.buscaVisitasAbertasEquipamento("aberta", equipamento_id));
            mAdapter.notifyDataSetChanged();
            return false;

        }
        if(id == R.id.navigation_pendentes) {

            mAdapter.swapCursor(myDBGeral.buscaVisitasEmEsperaEquipamento("em espera", equipamento_id));
            mAdapter.notifyDataSetChanged();
            return false;

        }

        if (id == R.id.navigation_encerradas) {

            //    mAdapter.swapCursor(myDBGeral.buscaVisitasEncerradasEquipamento("sincronizadas"));
            //    mAdapter.notifyDataSetChanged();
            return false;

        }
        //Fim Itens Menu
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_visita, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.swapCursor(myDBGeral.filtroOS(query, colaborador_id));
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mAdapter.swapCursor(myDBGeral.filtroOS(query, colaborador_id));
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdfData = new java.text.SimpleDateFormat("dd");
        String data = sdfData.format(dt);

        if( id == R.id.navegation_hoje) {

            mAdapter.swapCursor(myDBGeral.filtroOSData(data, equipamento_id));
            mAdapter.notifyDataSetChanged();

            return  true;
        }
        if(id == R.id.navegation_amanha) {

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            String formatted = sdfData.format(c.getTime());

            // Toast.makeText(getApplicationContext(), "Data: " + formatted, Toast.LENGTH_LONG).show();

            mAdapter.swapCursor(myDBGeral.filtroOSEquipamentoData(formatted, equipamento_id));
            mAdapter.notifyDataSetChanged();

            return true;
        }
        if(id == R.id.navegation_mes) {

            java.text.SimpleDateFormat sdfDataMes = new java.text.SimpleDateFormat("MM");
            String mes = sdfDataMes.format(dt);

            mAdapter.swapCursor(myDBGeral.filtroOSEquipamentoData(mes, equipamento_id));
            mAdapter.notifyDataSetChanged();

            return true;
        }

        if(id == R.id.navegation_ano) {

            java.text.SimpleDateFormat sdfDataMes = new java.text.SimpleDateFormat("yyyy");
            String ano = sdfDataMes.format(dt);

            mAdapter.swapCursor(myDBGeral.filtroOSEquipamentoData(ano, equipamento_id));
            mAdapter.notifyDataSetChanged();

            return true;
        }

        if(id == android.R.id.home) {
            Intent intent = new Intent(VisitasEquipamento.this, Equipamentos.class);
            Bundle dados = new Bundle();
            dados.putString("centrolucro_id", centrolucro_id);
            dados.putString("local_id", local_id);
            dados.putString("equipamento_id", equipamento_id);
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(VisitasEquipamento.this, MainActivity_Principal.class);
        Bundle dados = new Bundle();
        dados.putString("name", name);
        dados.putString("email", email);
        dados.putString("id", colaborador_id);
        dados.putString("token", token);
        intent.putExtras(dados);
        startActivity(intent);
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
