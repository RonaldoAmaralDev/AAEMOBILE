package br.com.araujoabreu.timg.dev.localizacao.frota.locais.local;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.banco.BancoGeral;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;




    public ContactsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView descricao;
        public TextView bairro;
        public TextView cidade;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name);
            descricao = itemView.findViewById(R.id.phone);
            bairro = itemView.findViewById(R.id.bairrolocal);
            cidade = itemView.findViewById(R.id.cidadelocal);
        }
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.local_row_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_LOCAL));
        String name = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CODIGO_LOCAL));
        String descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_LOCAL));
        String bairro = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_BAIRRO_LOCAL));
        String cidade = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CIDADE_LOCAL));
        String latitude = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LATITUDE_LOCAL));
        String longitude = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LONGITUDE_LOCAL));

        holder.nameText.setText("Codigo: " + name);
        holder.descricao.setText("Descrição: " + descricao);
        holder.bairro.setText("Bairro: " + bairro);
        holder.cidade.setText("Cidade: " + cidade);

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8
            Intent intent = new Intent(mContext, HomeActivity.class);
            Bundle dados = new Bundle();
            dados.putString("id", id);
            dados.putString("latitude", latitude);
            dados.putString("longitude", longitude);
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