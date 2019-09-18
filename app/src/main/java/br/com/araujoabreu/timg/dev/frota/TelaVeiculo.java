package br.com.araujoabreu.timg.dev.frota;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.com.araujoabreu.timg.R;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class TelaVeiculo extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String placa, responsavel, modelo, fabricante, descricao, contrato, cidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_veiculo);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        placa = dados.getString("placa");
        responsavel = dados.getString("colaborador");
        modelo = dados.getString("modelo");
        fabricante = dados.getString("fabricante");
        descricao = dados.getString("descricao");
        contrato = dados.getString("contrato");
        cidade = dados.getString("cidade");



        // Dados do Veiculo : + placa
        getSupportActionBar().setTitle("Dados Veiculo: " + placa);

        //loading the default fragment
        loadFragment(new DadosVeiculo());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_viewVeiculo);
        navigation.setOnNavigationItemSelectedListener(this);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
        //    case R.id.navigation_home:
        //        fragment = new DadosVeiculo();
        //        break;

         //   case R.id.navigation_dashboard:
        //   //     fragment = new DashboardFragment();
        //        break;

        //    case R.id.navigation_notifications:
        //       fragment = new MapFragment();
        //        break;

        }

        return loadFragment(fragment);
    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}