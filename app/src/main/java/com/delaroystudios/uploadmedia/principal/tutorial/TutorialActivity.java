package com.delaroystudios.uploadmedia.principal.tutorial;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.model.TutorialVideos;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;

import java.util.Vector;

public class TutorialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Vector<TutorialVideos> youtubeVideos = new Vector<TutorialVideos>();
    String name, email, colaborador_id, tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        email = dados.getString("email");
        name = dados.getString("name");
        colaborador_id = dados.getString("id");
        tipo = dados.getString("tipo");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        youtubeVideos.add( new TutorialVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/uQhCPoAUUis\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new TutorialVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Iu_WWHuKy_Y\" frameborder=\"0\" allowfullscreen></iframe>") );

        TutorialAdapter videoAdapter = new TutorialAdapter(youtubeVideos);

        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja voltar para tela inicial")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TutorialActivity.this, MainActivity_Principal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("id", colaborador_id);
                        dados.putString("tipo", tipo);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }


}