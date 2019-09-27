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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import br.com.araujoabreu.timg.adapter.CentroLucroAdapter;
import br.com.araujoabreu.timg.adapter.HistoricoAdapter;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.model.CL;
import br.com.araujoabreu.timg.model.Historico;
import br.com.araujoabreu.timg.model.MyDividerItemDecoration;

public class TelaHistoricoRastreador extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {


    private String name, token, colaborador_id, email;
    private List<Historico> historicos;
    private HistoricoAdapter mAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    BancoGeral bancoGeral;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_historico_rastreador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#8F152A")));

        bancoGeral = new BancoGeral(this);


        //Não abrir o teclado automatico
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

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
        mAdapter = new HistoricoAdapter(this, bancoGeral.getHistorico());
        recyclerView.setAdapter(mAdapter);



        // white background notification bar
        whiteNotificationBar(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_veiculo, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(TelaHistoricoRastreador.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_syncVeiculo) {

            Toast.makeText(getApplicationContext(), "Desenvolvimento.", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
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

            //   Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

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

            Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

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

