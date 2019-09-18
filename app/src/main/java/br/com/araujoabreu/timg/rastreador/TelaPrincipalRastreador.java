package br.com.araujoabreu.timg.rastreador;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import br.com.araujoabreu.timg.visitas.Contratos;

public class TelaPrincipalRastreador extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String name, token, colaborador_id, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_rastreador);

        //  // toolbar fancy stuff
              getSupportActionBar().setDisplayHomeAsUpEnabled(true);
              getSupportActionBar().setTitle("MEU CARTÃO");

        //Não abrir o teclado automatico
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

        CardForm cardForm = (CardForm) findViewById(R.id.card_form);

        TextView txtValor = (TextView) findViewById(R.id.payment_amount);
        TextView txtNome = (TextView) findViewById(R.id.card_name);
        TextView txtCartão = (TextView) findViewById(R.id.card_number);
        TextView txtVencimento = (TextView) findViewById(R.id.expiry_date);
        TextView txtCodigo = (TextView) findViewById(R.id.cvc);
        TextView descritivo = (TextView) findViewById(R.id.payment_amount_holder);
        Button btnRecarregar = (Button) findViewById(R.id.btn_pay);

        //Valor do Cartão
        txtValor.setText("R$ 150,39");
        descritivo.setText("Saldo Atual:");

        //Nome no Cartão
        txtNome.setText(name);
        txtNome.setVisibility(View.INVISIBLE);

        //Numero do Cartão
        txtCartão.setText("4824 2530 3629 7205");
        txtCartão.setVisibility(View.INVISIBLE);

        txtVencimento.setText("08/25");
        txtVencimento.setVisibility(View.INVISIBLE);

        txtCodigo.setText("429");
        txtCodigo.setVisibility(View.INVISIBLE);

        btnRecarregar.setText("SOLICITAR RECARGA");
        btnRecarregar.setTextColor(R.color.white);
     //   btnRecarregar.setBackgroundColor(R.color.testcolorblue);


        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                //Your code here!! use card.getXXX() for get any card property
                //for instance card.getName();
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

        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            Intent intent = new Intent(TelaPrincipalRastreador.this, MainActivity_Principal.class);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_ticketlog) {
            TelaPrincipalRastreador.super.onRestart();
            Intent intent = new Intent(TelaPrincipalRastreador.this, TelaPrincipalRastreador.class);
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

            Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

        }

        if (id == R.id.navigation_historico) {

            Toast.makeText(getApplicationContext(), "Em Desenvolvimento.", Toast.LENGTH_LONG).show();

        }
        return true;
    }


}
