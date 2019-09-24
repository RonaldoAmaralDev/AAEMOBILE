package br.com.araujoabreu.timg.helper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.araujoabreu.timg.rastreador.TelaMapaRastreador;


public class PermissoesSMS {

    public static void Request_SEND_SMS(TelaMapaRastreador act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.SEND_SMS},code);
    }

    public static void Request_READ_SMS(TelaMapaRastreador act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_SMS},code);
    }


    public static boolean validarPermissoes(String[] permissoes, TelaMapaRastreador activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> listaPermissoes = new ArrayList<>();

            /*Percorre as permissões passadas,
            verificando uma a uma
            * se já tem a permissao liberada */
            for ( String permissao : permissoes ){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if ( !temPermissao ) listaPermissoes.add(permissao);
            }

            /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
            if ( listaPermissoes.isEmpty() ) return true;
            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray( novasPermissoes );

            //Solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode );

        }

        return true;

    }
}


