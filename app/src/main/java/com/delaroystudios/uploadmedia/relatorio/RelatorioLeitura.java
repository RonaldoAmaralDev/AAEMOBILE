package com.delaroystudios.uploadmedia.relatorio;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
import com.delaroystudios.uploadmedia.operacao.RelatorioVisita;
import com.delaroystudios.uploadmedia.principal.MainActivity_Principal;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RelatorioLeitura extends AppCompatActivity {

    DatabaseHelper myBDOperacao;
    BancoGeral myBDGeral;
    String os_id, dataehora, dataplanejamento,name, email, colaborador_id, tipo, numerocl, centrolucro_descricao, contrato_id, tiposervico_descricao, status, dataAbertura, dataExecucao, leitura_agua1, leitura_agua2, leitura_energia1, leitura_energia2;
    private File pdfFile;
    private static final String TAG = "RelatorioFotografico";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_leitura);


        myBDOperacao = new DatabaseHelper(this);
        myBDGeral = new BancoGeral(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        name = dados.getString("name");
        email = dados.getString("email");
        colaborador_id = dados.getString("id");
        tipo = dados.getString("tipo");
        centrolucro_descricao = dados.getString("centrolucro_descricao");


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData =
                new java.text.SimpleDateFormat("dd/MM/yyyy");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);
        // Setar Data Execução
        dataehora = data + " - " + currentTime;


        //Busca Dados do Contrato
        Cursor dataContrato = myBDGeral.buscaContrato(centrolucro_descricao);
        while (dataContrato.moveToNext()) {
            contrato_id = dataContrato.getString(0);
            numerocl = dataContrato.getString(1);

        }

        //Busca Dados do Contrato
        Cursor dataMedicoes = myBDOperacao.buscaMedicoes();
        while (dataMedicoes.moveToNext()) {
            os_id = dataMedicoes.getString(1);
            leitura_agua1 = dataMedicoes.getString(2);
            leitura_agua2 = dataMedicoes.getString(3);
            leitura_energia1 = dataMedicoes.getString(4);
            leitura_energia2 = dataMedicoes.getString(5);
            status = dataMedicoes.getString(7);
            dataExecucao =  dataMedicoes.getString(8);
        }


        try {
            createPdfWrapper(numerocl, centrolucro_descricao, name, dataehora, os_id,leitura_agua1, leitura_agua2, leitura_energia1, leitura_energia2, status, dataExecucao);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

        private void createPdfWrapper(String numerocl, String centrolucro_descricao, String name, String dataehora, String os_id, String leitura_agua1, String leitura_agua2, String leitura_energia1, String leitura_energia2, String status, String dataExecucao) throws FileNotFoundException,DocumentException{
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
        return;
        }else {
        createPdf(numerocl, centrolucro_descricao, name, dataehora,os_id,leitura_agua1, leitura_agua2, leitura_energia1, leitura_energia2, status, dataExecucao );
        }
        }

        private void createPdf(String numerocl, String centrolucro_descricao, String name, String dataehora, String os_id, String leitura_agua1, String leitura_agua2, String leitura_energia1, String leitura_energia2, String status, String dataExecucao) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/enviados/");
        if (!docsFolder.exists()) {
        docsFolder.mkdir();
        Log.i(TAG, "Criado diretorio para Relatorios Fotograficos");
        }
        File relatorio = new File("/sdcard/PicturesHELPER/enviados/rf_leitura.pdf");
        if(relatorio.exists()) {
            relatorio.delete();
        }
        try
        {
        pdfFile = new File(docsFolder.getAbsolutePath(),"rf_leitura.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        document.addAuthor("AAE MOBILE");
        document.addCreationDate();
        document.addProducer();
        document.addCreator("AAE MOBILE");
        document.addTitle("Relatório Fotográfico - Leitura Água e Luz");
        document.setPageSize(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, output);

        // Verifica se existe as fotos se não coloca Sem Foto

        StringBuilder htmlString = new StringBuilder();
        htmlString.append(new String("<html><body>"));
        htmlString.append(new String("<head>"));
        htmlString.append(new String("<title>Relatório Fotográfico - Leitura Água e Luz 2</title>"));
        htmlString.append(new String("</head>"));

        // Header
        htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
        htmlString.append(new String("<thbody>"));
        htmlString.append(new String("<tr>"));
        htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/" + numerocl + "/logo.jpg'></img></td>"));
        htmlString.append(new String("<td width='40%' align='center' valign='middle'><h2 class='arial'><span style='text-align: center;'><strong>Leitura Água e Energia</strong></span></h2></td>"));
        htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/logo.png'></img></td>"));
        htmlString.append(new String("</tr>"));
        htmlString.append(new String("</thbody>"));
        htmlString.append(new String("</table>"));
        // Fim Header

        htmlString.append(new String("<p></p>"));
        htmlString.append(new String("<br></br>"));

        // Dados Visita
        htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
        htmlString.append(new String("<thbody>"));
        htmlString.append(new String("<tr>"));
        htmlString.append(new String("<td width='49%'><span class='arial'>Contrato: " + centrolucro_descricao+ "</span></td>"));
        htmlString.append(new String("<td width='51%'><span class='arial'>Ultima Atualização: " + dataehora + "</span></td>"));
        htmlString.append(new String("</tr>"));
        htmlString.append(new String("</thbody>"));
        htmlString.append(new String("</table>"));

        //Historico Inspeções
        htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='6'> "));
        htmlString.append(new String("<thbody>"));
        htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Histórico</span></h3></caption>"));
        htmlString.append(new String("<tr>"));
        htmlString.append(new String("<th align='center' valign='middle'>OS</th>"));
        htmlString.append(new String("<th align='center' valign='middle'>Data</th>"));
        htmlString.append(new String("<th align='center' valign='middle'>Copasa</th>"));
        htmlString.append(new String("<th align='center' valign='middle'>Irrigação</th>"));
        htmlString.append(new String("<th align='center' valign='middle'>Cemig</th>"));
        htmlString.append(new String("<th align='center' valign='middle'>Cemig Pilotis</th>"));
        htmlString.append(new String("</tr>"));
        htmlString.append(new String("</thbody>"));
        htmlString.append(new String("</table>"));


            Cursor dataOS = myBDGeral.buscaOSInspecao(contrato_id);
            while (dataOS.moveToNext()) { //Read every row
                os_id = dataOS.getString(0);

                htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='6'> "));
                htmlString.append(new String("<thbody>"));
                htmlString.append(new String("<tr>"));
                htmlString.append(new String("<td width='30%' align='center' valign='middle'><span style='font-size: 14px;'>" + os_id + "</span></td>"));
                htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>" + dataExecucao + "</span></td>"));
                htmlString.append(new String("<td width='40%' align='center' valign='middle'><span style='font-size: 14px;'>" + leitura_agua1 + "</span></td>"));
                htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>" + leitura_agua2 + "</span></td>"));
                htmlString.append(new String("<td width='40%' align='center' valign='middle'><span style='font-size: 14px;'>" + leitura_energia1 + "</span></td>"));
                htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>" + leitura_energia2 + "</span></td>"));
                htmlString.append(new String("</tr>"));
                htmlString.append(new String("</thbody>"));
                htmlString.append(new String("</table>"));

            }

        htmlString.append(new String("</body></html>"));

        document.open();
        InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
        document.close();
        previewPdf();

        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
        }
    private void previewPdf() {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/enviados/" + "rf_leitura.pdf");
        String mFilePath = docsFolder.getAbsolutePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file=new File(mFilePath);
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(mFilePath), "application/pdf");
            intent = Intent.createChooser(intent, "Abrir Relatorio Fotográfico");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    // Ao Pressionar no botão Voltar
        @Override
        public void onBackPressed() {
        Intent intent = new Intent(RelatorioLeitura.this, MainActivity_Principal.class);
        Bundle dados = new Bundle();
        dados.putString("name", name);
        dados.putString("email", email);
        dados.putString("id", colaborador_id);
        dados.putString("tipo", tipo);
        intent.putExtras(dados);
        startActivity(intent);
    }


}
