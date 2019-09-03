package com.delaroystudios.uploadmedia.operacao.local;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.adapter.ContactsAdapter;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.model.Contact;
import com.delaroystudios.uploadmedia.operacao.contrato.CentroLucro;

import java.util.List;

public class MainActivityLocals extends AppCompatActivity  {
    private static final String TAG = MainActivityLocals.class.getSimpleName();

    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    private String centrocusto_id, name, email, colaborador_id, token;
    private RecyclerView recyclerView;
    Cursor cursor;
    BancoGeral myDBGeral;

    // url to fetch contacts json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlocal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDBGeral = new BancoGeral(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        centrocusto_id = dados.getString("centrolucro_id");


        // toolbar fancy stuff
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Meus Locais");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        mAdapter = new ContactsAdapter(this, myDBGeral.buscaLocal(centrocusto_id));
        recyclerView.setAdapter(mAdapter);

        // white background notification bar
        whiteNotificationBar(recyclerView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.swapCursor(myDBGeral.filtro(query, centrocusto_id));
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mAdapter.swapCursor(myDBGeral.filtro(query, centrocusto_id));
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
            String token = pref.getString("token", "");

            Intent intent = new Intent(MainActivityLocals.this, CentroLucro.class);
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

        Intent intent = new Intent(MainActivityLocals.this, CentroLucro.class);
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
