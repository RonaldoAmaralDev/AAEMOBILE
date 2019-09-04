package com.delaroystudios.uploadmedia.visitas;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.adapter.AtividadeAdapter;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
import com.delaroystudios.uploadmedia.operacao.AssinaturaColaborador;
import com.delaroystudios.uploadmedia.model.MyDividerItemDecoration;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;


public class MainActivityAtividades extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivityAtividades.class.getSimpleName();

    private AtividadeAdapter mAdapter;
    private SearchView searchView;
    private String os_id, checklist_id, equipamento_id, local_id, name, email, colaborador_id, token, tiposervico, dataplanejamento, centrocusto_id;
    BancoGeral myDBGeral;
    DatabaseHelper myDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_atividades);

        Toolbar toolbar = findViewById(R.id.toolbaratividades);
        setSupportActionBar(toolbar);

        myDBGeral = new BancoGeral(this);
        myDb = new DatabaseHelper(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        os_id = dados.getString("os_id");
        checklist_id = dados.getString("checklist_id");
        equipamento_id = dados.getString("equipamento_id");
        local_id = dados.getString("local_id");
        tiposervico = dados.getString("tiposervico");
        dataplanejamento = dados.getString("dataplanejamento");
        centrocusto_id = dados.getString("centrocusto_id");
        name = dados.getString("name");
        email = dados.getString("email");
        colaborador_id = dados.getString("idColaborador");
        token = dados.getString("token");

        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ATIVIDADES");

        RecyclerView recyclerView = findViewById(R.id.recycler_viewatividades);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewAtividades);
        navigation.setOnNavigationItemSelectedListener(this);


        mAdapter = new AtividadeAdapter(this, myDBGeral.buscaAtividadesAbertas(checklist_id));
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

          mAdapter.swapCursor(myDBGeral.buscaAtividade("aberta", checklist_id));
          mAdapter.notifyDataSetChanged();
          return false;

        }
        if(id == R.id.navigation_encerradas) {

            mAdapter.swapCursor(myDBGeral.buscaAtividade("encerrada", checklist_id));
            mAdapter.notifyDataSetChanged();
            return false;
        }


        //Fim Itens Menu
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_atividades, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.swapCursor(myDBGeral.filtroAtividade(query, checklist_id));
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mAdapter.swapCursor(myDBGeral.filtroAtividade(query, checklist_id));
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

        // Se ele clicar para finalizar Visita

        if (id == R.id.action_confirmar) {

            //Verifica se existe ainda atividades para realizar

            if(myDBGeral.dbCountAtividadesAbertas(checklist_id) == 0) {
                Intent intentFinalizarVisita = new Intent(MainActivityAtividades.this, AssinaturaColaborador.class);
                Bundle dadosVisita = new Bundle();
                dadosVisita.putString("os_id", os_id);
                dadosVisita.putString("equipamento_id", equipamento_id);
                dadosVisita.putString("local_id", local_id);
                dadosVisita.putString("checklist", checklist_id);
                dadosVisita.putString("tiposervico", tiposervico);
                dadosVisita.putString("dataplanejamento", dataplanejamento);
                dadosVisita.putString("id_centrolucro", centrocusto_id);
                dadosVisita.putString("name", name);
                dadosVisita.putString("email", email);
                dadosVisita.putString("idColaborador", colaborador_id);
                dadosVisita.putString("token", token);
                intentFinalizarVisita.putExtras(dadosVisita);
                startActivity(intentFinalizarVisita);
            } else {
                Toast.makeText(getApplicationContext(), "Você ainda tem " + myDBGeral.dbCountAtividadesAbertas(checklist_id) + " atividades para realizar", Toast.LENGTH_LONG).show();
            }

            return true;
        }


        if(id == android.R.id.home) {

            Intent intent = new Intent(MainActivityAtividades.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
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

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        new AlertDialog.Builder(MainActivityAtividades.this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Você deseja: ")
                .setPositiveButton("Cancelar Visita", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deleteOS(os_id);

                        myDBGeral.updateStatusAtividadeAberta(
                                checklist_id);


                        Intent intent = new Intent(MainActivityAtividades.this, VisitasLocal.class);
                        Bundle dados = new Bundle();
                        dados.putString("equipamento_id", equipamento_id);
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("colaborador_id", colaborador_id);
                        dados.putString("token", token);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }

                })
                .setNeutralButton("Modo Espera", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
                        String name = pref.getString("name", "");
                        String email= pref.getString("email", "");
                        String idColaborador = pref.getString("id", "" );
                        String token = pref.getString("token", "");

                        myDBGeral.updateModoEspera(os_id);

                        myDBGeral.updateStatusAtividadeAberta(
                                checklist_id);

                        Intent intent = new Intent(MainActivityAtividades.this, VisitasLocal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("colaborador_id", idColaborador);
                        dados.putString("token", token);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Fechar Tela", null)
                .show();
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
