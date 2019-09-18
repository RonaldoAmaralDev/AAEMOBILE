package br.com.araujoabreu.timg.dev.frota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.araujoabreu.timg.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Belal on 1/23/2018.
 */

public class MapFragment extends Fragment  implements OnMapReadyCallback {



    GoogleMap mMap;
    SupportMapFragment mapFragment;
    private String placa, responsavel, modelo, fabricante, descricao, contrato, cidade;

    public MapFragment() {


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Intent intent = getActivity().getIntent();
        Bundle dados = intent.getExtras();
        placa = dados.getString("placa");
        responsavel = dados.getString("colaborador");
        modelo = dados.getString("modelo");
        fabricante = dados.getString("fabricante");
        descricao = dados.getString("descricao");
        contrato = dados.getString("contrato");
        cidade = dados.getString("cidade");



        View v=  inflater.inflate(R.layout.fragment_rastrearveiculo, container, false);
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;

        LatLng latLng= new LatLng(-19.8590959, -43.9657111);
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carbig));
        markerOptions.title("Placa: " + placa);
        markerOptions.snippet("Condutor: " + responsavel);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }
}