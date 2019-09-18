package br.com.araujoabreu.timg.chat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.chat.config.ConfiguracaoFirebase;
import br.com.araujoabreu.timg.chat.fragment.ContatosFragment;
import br.com.araujoabreu.timg.chat.fragment.ConversasFragment;
import br.com.araujoabreu.timg.equipamento.qrcode.LoadingScanner;
import br.com.araujoabreu.timg.visitas.Contratos;

public class TelaPrincipalChat extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    BancoGeral myBDGeral;
    private String name, colaborador_id, token, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainchat);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        myBDGeral = new BancoGeral(this);

      //  Intent intent = getIntent();
      //  Bundle dados = intent.getExtras();
      //  email = dados.getString("email");
     //   name = dados.getString("name");
      //  colaborador_id = dados.getString("id");
     //   token = dados.getString("token");

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle(name );
        setSupportActionBar( toolbar );


        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", ConversasFragment.class)
                .add("Contatos", ContatosFragment.class)
                .create()
        );
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter( adapter );

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager( viewPager );

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewTelaPrincipal);
        navigation.setOnNavigationItemSelectedListener(this);

        //Aparecer todos os Icones e Titulos
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        //Trocar titulos
        navigation.getMenu().findItem(R.id.navigation_mapa).setTitle("MAPA");
        navigation.getMenu().findItem(R.id.navigation_visitas).setTitle("VISITA(" + String.valueOf(myBDGeral.dbCountAbertas() + ")"));
        navigation.getMenu().findItem(R.id.navigation_frota).setTitle("FROTA");
        navigation.getMenu().findItem(R.id.navigation_chat).setTitle("CHAT");
        navigation.getMenu().findItem(R.id.navigation_syncVisita).setTitle("ENVIAR(" + String.valueOf(myBDGeral.dbCountEncerradas() + ")"));


        //Configuração do search view
        searchView = findViewById(R.id.materialSearchPrincipal);
        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();

            }
        });

        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("evento", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("evento", "onQueryTextChange");

                // Verifica se esta pesquisando Conversas ou Contatos
                // a partir da tab que esta ativa
                switch ( viewPager.getCurrentItem() ){
                    case 0:
                        ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);
                        if( newText != null && !newText.isEmpty() ){
                            conversasFragment.pesquisarConversas( newText.toLowerCase() );
                        }else {
                            conversasFragment.recarregarConversas();
                        }
                        break;
                    case 1 :
                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(1);
                        if( newText != null && !newText.isEmpty() ){
                            contatosFragment.pesquisarContatos( newText.toLowerCase() );
                        }else {
                            contatosFragment.recarregarContatos();
                        }
                        break;
                }



                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menuSair :
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracoes :
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        //Os 4 primeiros são o navegation bottom (De baixo)
        if(id == R.id.navigation_mapa) {
            Intent intent = new Intent(TelaPrincipalChat.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            finish();
        }
        if(id == R.id.navigation_visitas) {

            Intent intent = new Intent(TelaPrincipalChat.this, Contratos.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("colaborador_id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);

        }

        if (id == R.id.navigation_frota) {

            Intent intent = new Intent(TelaPrincipalChat.this, TelaPrincipalChat.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }

        if (id == R.id.navigation_chat) {
            Intent intent = new Intent(TelaPrincipalChat.this, TelaPrincipalChat.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
            // Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.navigation_syncVisita) {

            Intent intent = new Intent(TelaPrincipalChat.this, MainActivity_Principal.class);
            Bundle dados = new Bundle();
            dados.putString("name", name);
            dados.putString("email", email);
            dados.putString("id", colaborador_id);
            dados.putString("token", token);
            intent.putExtras(dados);
            startActivity(intent);
        }
        //Fim Itens Menu
        return true;
    }

    public void deslogarUsuario(){

        try {
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent(TelaPrincipalChat.this, ConfiguracoesChat.class);
        startActivity( intent );
    }

}
