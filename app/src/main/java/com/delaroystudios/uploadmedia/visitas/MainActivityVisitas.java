package com.delaroystudios.uploadmedia.visitas;

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

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.adapter.VisitaAdapter;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.operacao.local.MyDividerItemDecoration;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;

public class MainActivityVisitas extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivityVisitas.class.getSimpleName();

    private VisitaAdapter mAdapter;
    private SearchView searchView;
    private String name, email, colaborador_id, tipo;
    BancoGeral myDBGeral;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_visitas);

        Toolbar toolbar = findViewById(R.id.toolbarvisita);
        setSupportActionBar(toolbar);
        //Pegar localização atual colaborador

        myDBGeral = new BancoGeral(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        name = dados.getString("name");
        email = dados.getString("email");
        colaborador_id = dados.getString("colaborador_id");
        tipo = dados.getString("tipo");

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("VISITAS");

        RecyclerView recyclerView = findViewById(R.id.recycler_viewvisita);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));


        //getting bottom navigation view and attaching the listener
       BottomNavigationView navigation = findViewById(R.id.nav_viewVisita);
       navigation.setOnNavigationItemSelectedListener(this);

        //Trocar titulos
        navigation.getMenu().findItem(R.id.navigation_abertas).setTitle("ABERTAS("+String.valueOf(myDBGeral.dbCountAbertas()+")"));
        navigation.getMenu().findItem(R.id.navigation_pendentes).setTitle("PENDENTES("+String.valueOf(myDBGeral.dbCountEmEspera()+")"));
        navigation.getMenu().findItem(R.id.navigation_encerradas).setTitle("ENCERRADAS("+String.valueOf(myDBGeral.dbCountEncerradas()+")"));


        mAdapter = new VisitaAdapter(this, myDBGeral.buscaOSColaborador(colaborador_id));
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

            mAdapter.swapCursor(myDBGeral.buscaVisitasAbertas("aberta"));
            mAdapter.notifyDataSetChanged();
            return false;

        }
        if(id == R.id.navigation_pendentes) {

            mAdapter.swapCursor(myDBGeral.buscaVisitasEmEspera("em espera"));
            mAdapter.notifyDataSetChanged();
            return false;

        }

        if (id == R.id.navigation_encerradas) {

        //    mAdapter.swapCursor(myDBGeral.buscaVisitasSincronizadas("sincronizadas"));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }


        if(id == android.R.id.home) {
            SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
            String name = pref.getString("name", "");
            String email= pref.getString("email", "");
            String colaborador_id = pref.getString("id", "" );
            String tipo = pref.getString("tipo", "");

            Intent intent = new Intent(MainActivityVisitas.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("tipo", tipo);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(MainActivityVisitas.this, MainActivity_Principal.class);
        Bundle dados = new Bundle();
        dados.putString("name", name);
        dados.putString("email", email);
        dados.putString("id", colaborador_id);
        dados.putString("tipo", tipo);
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