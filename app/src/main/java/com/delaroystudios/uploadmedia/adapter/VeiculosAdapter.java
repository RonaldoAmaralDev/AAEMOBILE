package com.delaroystudios.uploadmedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.frota.TelaVeiculo;
import com.delaroystudios.uploadmedia.banco.BancoGeral;

public class VeiculosAdapter extends RecyclerView.Adapter<VeiculosAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    BancoGeral myBDGeral;
    String ordemservico;


    public VeiculosAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        myBDGeral = new BancoGeral(mContext);

    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView placaveiculo;
        public TextView descricaoveiculo;
        public TextView modeloveiculo;
        public TextView contratoveiculo;
        public TextView responsavel;
        public ImageView logo_veiculo;

        public GroceryViewHolder(View itemView) {
            super(itemView);


            logo_veiculo = itemView.findViewById(R.id.logo_veiculo);
            placaveiculo = itemView.findViewById(R.id.placaveiculo);
            descricaoveiculo = itemView.findViewById(R.id.descricaoveiculo);
            modeloveiculo = itemView.findViewById(R.id.modeloveiculo);
            contratoveiculo = itemView.findViewById(R.id.contratoveiculo);
            responsavel = itemView.findViewById(R.id.cidadeveiculo);
        }
    }

    @Override
    public VeiculosAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.veiculo_row_line, parent, false);
        return new VeiculosAdapter.GroceryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VeiculosAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_VEICULO));
        String placa = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_PLACA_VEICULO));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_VEICULO));
        String modelo = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_MODELO_VEICULO));
        String contrato = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROLUCRO_VEICULO));
        String fabricante = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_FABRICANTE_VEICULO));
        String responsavel = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_COLABORADOR_VEICULO));
        String cidade = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CIDADE_VEICULO));

        holder.placaveiculo.setText("Placa: " + placa);
        holder.descricaoveiculo.setText("Descrição: " + descricao);
        holder.modeloveiculo.setText("Modelo: " + modelo);
        holder.contratoveiculo.setText("Contrato: " + contrato);
        holder.responsavel.setText("Responsável: " + responsavel);
        holder.logo_veiculo.setImageResource(R.drawable.frota_saveiro);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8




            Intent intent = new Intent(mContext, TelaVeiculo.class);
            Bundle dados = new Bundle();
            dados.putString("id", id);
            dados.putString("placa", placa);
            dados.putString("descricao", descricao);
            dados.putString("modelo", modelo);
            dados.putString("contrato", contrato);
            dados.putString("colaborador", responsavel);
            dados.putString("fabricante", fabricante);
            dados.putString("cidade", cidade);
            intent.putExtras(dados);
            mContext.startActivity(intent);

        });
    }






    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        //try using this
        if(mCursor==null)
            return 0;
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
