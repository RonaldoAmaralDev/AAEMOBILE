package br.com.araujoabreu.timg.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.model.Historico;
import br.com.araujoabreu.timg.visitas.Locais;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public HistoricoAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView dataehora;
        public TextView status;
        public TextView velocidade;
        public TextView endereco;
        public ImageView imageView;


        public GroceryViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logo_historico);
            dataehora = itemView.findViewById(R.id.txtdataehoraHistorico);
            status = itemView.findViewById(R.id.txtstatusHistorico);
            velocidade = itemView.findViewById(R.id.txtvelocidadeHistorico);
            endereco = itemView.findViewById(R.id.txtenderecoHistorico);

        }
    }

    @Override
    public HistoricoAdapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.historico_row_item, parent, false);
        return new HistoricoAdapter.GroceryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(HistoricoAdapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String dataehora = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DATAEHORA_LOCALIZACAO));
        String velocidade = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_VELOCIDADE_LOCALIZACAO));
        String latitude = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LATITUDE_LOCALIZACAO));
        String longitude = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LONGITUDE_LOCALIZACAO));


        // Transformar latitude e longitude em endereço
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            holder.endereco.setText(address);
        } catch (Exception e) {
            holder.endereco.setText("Sem captura da localização");
        }

        //Calendario Icon
        holder.imageView.setImageResource(R.drawable.logo_historico);
        //Data e Hora
        holder.dataehora.setText(dataehora);
        //Status Ignição
        holder.status.setText("Veiculo Ligado");
        //Velocidade
        holder.velocidade.setText(velocidade + " Km/h");

        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8


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
