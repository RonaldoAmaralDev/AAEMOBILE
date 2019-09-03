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
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.visitas.Locais;


public class CentroLucroAdapter extends RecyclerView.Adapter<CentroLucroAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public CentroLucroAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView centrolucro;
        public TextView descricao_contrato;
        public ImageView logo_contrato;


        public GroceryViewHolder(View itemView) {
            super(itemView);

            logo_contrato = itemView.findViewById(R.id.logo_contrato);
            centrolucro = itemView.findViewById(R.id.contrato_cl);
            descricao_contrato = itemView.findViewById(R.id.contrato_descricao);

        }
    }

    @Override
    public CentroLucroAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.contrato_row_item, parent, false);
        return new CentroLucroAdapter.GroceryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CentroLucroAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String centrolucro = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROLUCRO_CL));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_CL));
        int id = mCursor.getInt(mCursor.getColumnIndex(BancoGeral.COL_ID_CL));

        String drawablename = "logo_" + centrolucro;
        int drawableId = mContext.getResources().getIdentifier(drawablename, "drawable", mContext.getPackageName());

        if(id == 2) {
            holder.logo_contrato.setImageResource(R.drawable.logo);
        } else {
            holder.logo_contrato.setImageResource(drawableId);
        }

        String id_contrato = Integer.toString(id);
        holder.centrolucro.setText("Centro Lucro: " + centrolucro);
        holder.descricao_contrato.setText("Contrato: " + descricao);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8

            Intent intent = new Intent(mContext, Locais.class);
            Bundle dados = new Bundle();
            dados.putString("centrolucro_id", id_contrato);
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
