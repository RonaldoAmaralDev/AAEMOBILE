package com.delaroystudios.uploadmedia.equipamento;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class CadastrarEquipamento extends AppCompatActivity {

    String email, name, colaborador_id, token, descricao, centrolucro_id, local_id, tipoequipamento, tipogas, fabricante_id, modelo, numeroSerie, btu;
    BancoGeral myBDGeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_equipamento);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");

        myBDGeral = new BancoGeral(this);

        // Deixa Tela Cheia
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        iniciarCadastramento();

    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }


    public void iniciarCadastramento() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CadastrarEquipamento.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        Spinner mSpinner = mView.findViewById(R.id.spinnerDialog);
        mBuilder.setTitle(R.string.app_name);
        mBuilder.setIcon(R.drawable.logo);
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Escolha o centro de lucro: ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastrarEquipamento.this,
        android.R.layout.simple_spinner_item,
        getResources().getStringArray(R.array.ContratosSpinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Escolha contrato"));
                if(mSpinner.getSelectedItemPosition() == 0) {
                    centrolucro_id = "2";
                } else if(mSpinner.getSelectedItemPosition() == 1) {
                    centrolucro_id = "4";
                } else {
                    centrolucro_id = "5";
                }
                dialogInterface.dismiss();

                inserirDescricao(centrolucro_id);
            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }


    public void inserirDescricao(String centrolucro_id) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.drawable.logo);
        alert.setCancelable(false);
        alert.setMessage("Descrição do equipamento: ");
        final EditText input = new EditText(this);
        input.setHintTextColor(getResources().getColor(R.color.white));
        alert.setView(input);
        alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(input.getText().length() == 0){//como o tamanho é zero é nula a resposta

                   Toast.makeText(getApplicationContext(), "Descrição Equipamento não pode ser vazia.", Toast.LENGTH_LONG).show();
                   inserirDescricao(centrolucro_id);

                }else if (input.getText().length() < 10){

                    Toast.makeText(getApplicationContext(), "Descrição Equipamento deve ter minimo 10 letras.", Toast.LENGTH_LONG).show();
                    inserirDescricao(centrolucro_id);

                } else {
                    descricao = input.getText().toString();
                    inserirLocal(centrolucro_id, descricao);

                }
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        alert.show();

    }

    public void inserirLocal(String centrolucro_id, String descricao) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CadastrarEquipamento.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        Spinner mSpinner = mView.findViewById(R.id.spinnerDialog);
        mBuilder.setTitle(R.string.app_name);
        mBuilder.setIcon(R.drawable.logo);
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Equipamento está no local: ");

        // database handler
        BancoGeral db = new BancoGeral(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getLocals(centrolucro_id);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinner.setAdapter(dataAdapter);

        mBuilder.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Escolha o local"));
                Cursor dataOS = myBDGeral.verificaLocalsSpinner(mSpinner.getSelectedItem().toString());
                while (dataOS.moveToNext()) {

                     local_id = dataOS.getString(0);
                    inserirTipoEquipamento(centrolucro_id, descricao, local_id);
                }
                dialogInterface.dismiss();

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    public void inserirTipoEquipamento(String centrolucro_id, String descricao, String local_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CadastrarEquipamento.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        Spinner mSpinner = mView.findViewById(R.id.spinnerDialog);
        mBuilder.setTitle(R.string.app_name);
        mBuilder.setIcon(R.drawable.logo);
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Escolha o tipo equipamento: ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastrarEquipamento.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.TipoEquipamentoSpinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Escolha tipo equipamento"));
                if(mSpinner.getSelectedItemPosition() == 0) {
                    tipoequipamento = "5";
                } else if(mSpinner.getSelectedItemPosition() == 1) {
                    tipoequipamento = "8";
                } else {
                    tipoequipamento = "14";
                }
                inserirTipoGas(centrolucro_id, descricao, local_id, tipoequipamento);
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();




    }

    public void inserirTipoGas(String centrolucro_id, String descricao, String local_id, String tipoequipamento) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CadastrarEquipamento.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        Spinner mSpinner = mView.findViewById(R.id.spinnerDialog);
        mBuilder.setTitle(R.string.app_name);
        mBuilder.setIcon(R.drawable.logo);
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Escolha o tipo gás: ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastrarEquipamento.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.TipoGasSpinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Escolha tipo Gás"));
                if(mSpinner.getSelectedItemPosition() == 0) {
                    tipogas = "1";
                } else if(mSpinner.getSelectedItemPosition() == 1) {
                    tipogas = "2";
                } else if(mSpinner.getSelectedItemPosition() == 2){
                    tipogas = "3";
                } else if(mSpinner.getSelectedItemPosition() == 3){
                    tipogas = "4";
                } else if(mSpinner.getSelectedItemPosition() == 4){
                    tipogas = "5";
                }else if(mSpinner.getSelectedItemPosition() == 5) {
                    tipogas = "6";
                }
                dialogInterface.dismiss();
                inserirFabricante(centrolucro_id, descricao, local_id, tipoequipamento, tipogas);
            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();


    }

    public void inserirFabricante(String centrolucro_id, String descricao, String local_id, String tipoequipamento, String tipogas) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CadastrarEquipamento.this);
        View mView = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
        Spinner mSpinner = mView.findViewById(R.id.spinnerDialog);
        mBuilder.setTitle(R.string.app_name);
        mBuilder.setIcon(R.drawable.logo);
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Escolha o fabricante do equipamento: ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastrarEquipamento.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.TipoFabricanteSpinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Escolha o Fabricante: "));
                if(mSpinner.getSelectedItemPosition() == 0) {
                    fabricante_id = "2";
                } else if(mSpinner.getSelectedItemPosition() == 1) {
                    fabricante_id = "3";
                } else if(mSpinner.getSelectedItemPosition() == 2){
                    fabricante_id = "4";
                } else if(mSpinner.getSelectedItemPosition() == 3){
                    fabricante_id = "5";
                } else if(mSpinner.getSelectedItemPosition() == 4){
                    fabricante_id = "6";
                }else if(mSpinner.getSelectedItemPosition() == 5) {
                    fabricante_id = "7";
                } else if(mSpinner.getSelectedItemPosition() == 6) {
                    fabricante_id = "8";
                } else if(mSpinner.getSelectedItemPosition() == 7){
                    fabricante_id = "9";
                } else if(mSpinner.getSelectedItemPosition() == 8){
                    fabricante_id = "10";
                } else if(mSpinner.getSelectedItemPosition() == 9){
                    fabricante_id = "11";
                }else if(mSpinner.getSelectedItemPosition() == 10) {
                    fabricante_id = "12";
                } else if(mSpinner.getSelectedItemPosition() == 11) {
                    fabricante_id = "13";
                } else if(mSpinner.getSelectedItemPosition() == 12){
                    fabricante_id = "14";
                } else if(mSpinner.getSelectedItemPosition() == 13){
                    fabricante_id = "15";
                } else if(mSpinner.getSelectedItemPosition() == 14){
                    fabricante_id = "16";
                }else if(mSpinner.getSelectedItemPosition() == 15) {
                    fabricante_id = "17";
                } else if(mSpinner.getSelectedItemPosition() == 16){
                    fabricante_id = "18";
                } else if(mSpinner.getSelectedItemPosition() == 17){
                    fabricante_id = "19";
                } else if(mSpinner.getSelectedItemPosition() == 18){
                    fabricante_id = "20";
                } else if(mSpinner.getSelectedItemPosition() == 19){
                    fabricante_id = "21";
            }
                inserirModelo(centrolucro_id, descricao, local_id, tipoequipamento, tipogas, fabricante_id);
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();


    }

    public void inserirModelo(String centrolucro_id, String descricao, String local_id, String tipoequipamento, String tipogas, String fabricante_id) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.app_name);
            alert.setIcon(R.drawable.logo);
            alert.setCancelable(false);
            alert.setMessage("Digite o modelo do Equipamento: ");
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    if(input.getText().length() == 0){//como o tamanho é zero é nula a resposta
                        Toast.makeText(getApplicationContext(), "Modelo do equipamento não pode estar vazia.", Toast.LENGTH_LONG).show();
                        inserirModelo(centrolucro_id, descricao, local_id, tipoequipamento, tipogas, fabricante_id);

                    }else if (input.getText().length() < 5){
                        Toast.makeText(getApplicationContext(), "Modelo do equipamento não pode ter menos que 5 letras.", Toast.LENGTH_LONG).show();
                        inserirModelo(centrolucro_id, descricao, local_id, tipoequipamento, tipogas, fabricante_id);

                    } else {
                        modelo = input.getText().toString();
                        inserirNumeroSerie(centrolucro_id, descricao, local_id, tipoequipamento, tipogas, fabricante_id, modelo);

                    }
                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
            });
            alert.show();

        }

        public void inserirNumeroSerie(String centrolucro_id, String descricao, String local_id, String tipoequipamento, String tipogas, String fabricante_id, String modelo) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.app_name);
            alert.setIcon(R.drawable.logo);
            alert.setCancelable(false);
            alert.setMessage("Digite o número série do equipamento:");
            final EditText input = new EditText(this);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            alert.setView(input);
            alert.setPositiveButton("Avançar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                        numeroSerie = input.getText().toString();
                        inserirBTU(centrolucro_id, descricao, local_id, tipoequipamento, tipogas, fabricante_id, modelo, numeroSerie);
                    }

            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                    Bundle dados = new Bundle();
                    dados.putString("name", name);
                    dados.putString("email", email);
                    dados.putString("id", colaborador_id);
                    dados.putString("tipo", token);
                    intent.putExtras(dados);
                    startActivity(intent);
                }
            });
            alert.show();


        }


    public void inserirBTU(String centrolucro_id, String descricao, String local_id, String tipoequipamento, String tipogas, String fabricante_id, String modelo, String numeroSerie) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.drawable.logo);
        alert.setCancelable(false);
        alert.setMessage("Digite a quantidade de BTU: ");
        final EditText input = new EditText(this);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Cadastrar Equipamento", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                btu = input.getText().toString();

                if(verificaConexao() == false) {
                    Toast.makeText(getApplicationContext(), "Erro, sem conexão com a internet. ", Toast.LENGTH_SHORT).show();
                } else {

                    String URL = "http://helper.aplusweb.com.br/aplicativo/abrirEquipamento.php";

                    Cursor dataLocal = myBDGeral.verificaCL(centrolucro_id);
                    while (dataLocal.moveToNext()) {

                        String centrolucro_codigo = dataLocal.getString(1);

                        int random = (int)(Math.random() * 0+10);
                        String codigoequipamento = centrolucro_codigo + ".1-ARCON-" + random;
                        String tagModelo = Integer.toString(random);
                        String tag = centrolucro_codigo + "-" + tagModelo;

                        Ion.with(CadastrarEquipamento.this)
                                .load(URL)
                                .setBodyParameter("codigoequipamento", codigoequipamento)
                                .setBodyParameter("descricaoequipamento", descricao)
                                .setBodyParameter("centrocusto_id", centrolucro_id)
                                .setBodyParameter("tipoequipamento_id", tipoequipamento)
                                .setBodyParameter("tipogas_id", tipogas)
                                .setBodyParameter("local_id", local_id)
                                .setBodyParameter("fabricante_id", fabricante_id)
                                .setBodyParameter("fornecedor_id", "1")
                                .setBodyParameter("modelo", modelo)
                                .setBodyParameter("numeroserie", numeroSerie)
                                .setBodyParameter("tag", tag)
                                .setBodyParameter("btu", btu)
                                .setBodyParameter("ativo", "A")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        try {
                                            String RETORNO = result.get("EQUIPAMENTO").getAsString();

                                            if (RETORNO.equals("ERRO")) {
                                                Toast.makeText(CadastrarEquipamento.this, "Ocorreu um erro !", Toast.LENGTH_LONG).show();

                                            } else if (RETORNO.equals("SUCESSO")) {
                                                Toast.makeText(CadastrarEquipamento.this, "Equipamento: " + codigoequipamento + " , foi criado com sucesso !", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                                                Bundle dados = new Bundle();
                                                dados.putString("name", name);
                                                dados.putString("email", email);
                                                dados.putString("id", colaborador_id);
                                                dados.putString("tipo", token);
                                                intent.putExtras(dados);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(CadastrarEquipamento.this, "Ocorreu um erro, aguarde alguns instantes !", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (Exception erro) {
                                            Toast.makeText(CadastrarEquipamento.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                }
                }
            }

        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(CadastrarEquipamento.this, MainActivity_Principal.class);
                Bundle dados = new Bundle();
                dados.putString("name", name);
                dados.putString("email", email);
                dados.putString("id", colaborador_id);
                dados.putString("tipo", token);
                intent.putExtras(dados);
                startActivity(intent);
            }
        });
        alert.show();
    }



    }



