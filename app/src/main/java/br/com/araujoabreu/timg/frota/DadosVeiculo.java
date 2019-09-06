package br.com.araujoabreu.timg.frota;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
/**
 * Created by Belal on 1/23/2018.
 */

public class DadosVeiculo extends Fragment {

    private String placa, responsavel, modelo, fabricante, descricao, contrato, cidade;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle dados = intent.getExtras();
        placa = dados.getString("placa");
        responsavel = dados.getString("colaborador");
        modelo = dados.getString("modelo");
        fabricante = dados.getString("fabricante");
        descricao = dados.getString("descricao");
        contrato = dados.getString("contrato");
        cidade = dados.getString("cidade");

        View rootView = inflater.inflate(R.layout.fragment_dadosveiculo, container, false);

        TextView txt_placa = (TextView) rootView.findViewById(R.id.txt_placaVeiculo);
        TextView txt_responsavel = (TextView) rootView.findViewById(R.id.txt_responsavel_veiculo);
        TextView txtModelo = (TextView) rootView.findViewById(R.id.txt_modeloveiculo);
        TextView txtFabricante = (TextView) rootView.findViewById(R.id.txt_fabricanteveiculo);
        TextView txtContrato = (TextView) rootView.findViewById(R.id.txt_contratofrota);
        TextView txtCidade = (TextView) rootView.findViewById(R.id.txt_cidadeveiculo);
        txt_placa.setText(placa);
        txt_responsavel.setText(responsavel);
        txtModelo.setText(modelo);
        txtFabricante.setText(fabricante);
        txtContrato.setText(contrato);
        txtCidade.setText(cidade);

        return rootView;
    }

}