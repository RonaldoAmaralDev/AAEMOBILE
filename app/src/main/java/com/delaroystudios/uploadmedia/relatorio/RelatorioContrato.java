package com.delaroystudios.uploadmedia.relatorio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
import com.delaroystudios.uploadmedia.banco.DatabaseHelper;
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

public class RelatorioContrato extends AppCompatActivity {

    DatabaseHelper myBDOperacao;
    BancoGeral myBDGeral;
    String os_id, linhaOS, dataAbertura, dataEncerramento, tiposolicitacao, tiposolicitacao_descricao, tiposervico, tiposervico_descricao, dataehora ,name, email, colaborador_id, tipo, numerocl, centrolucro_descricao, contrato_id, quantLocais, quantEquipamentos, observacaoantes;
    private File pdfFile;
    private static final String TAG = "RelatorioFotografico";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_contrato);


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

        try {
            createPdfWrapper(contrato_id, numerocl, centrolucro_descricao, dataehora, os_id, tiposolicitacao, tiposolicitacao_descricao, tiposervico, tiposervico_descricao, name, dataAbertura);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private void createPdfWrapper(String contrato_id, String numerocl, String centrolucro_descricao, String dataehora, String os_id, String tiposolicitacao, String tiposolicitacao_descricao, String tiposervico, String tiposervico_descricao, String name, String dataAbertura) throws FileNotFoundException,DocumentException{
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            createPdf(contrato_id, numerocl, centrolucro_descricao, dataehora, os_id, tiposolicitacao, tiposolicitacao_descricao, tiposervico, tiposervico_descricao, name, dataAbertura);
        }
    }

    private void createPdf(String contrato_id, String numerocl, String centrolucro_descricao, String dataehora, String os_id, String tiposolicitacao, String tiposolicitacao_descricao, String tiposervico, String tiposervico_descricao, String name, String dataAbertura) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/enviados/");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Criado diretorio para Relatorios Fotograficos");
        }
        File relatorio = new File("/sdcard/PicturesHELPER/enviados/rf_contrato.pdf");
        if(relatorio.exists()) {
            relatorio.delete();
        }
        try
        {
            pdfFile = new File(docsFolder.getAbsolutePath(),"rf_contrato.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document();
            document.addAuthor("AAE MOBILE");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("AAE MOBILE");
            document.addTitle("Relatório Fotográfico - Resumo Contrato");
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, output);


            StringBuilder htmlString = new StringBuilder();
            htmlString.append(new String("<html><body>"));
            htmlString.append(new String("<head>"));
            htmlString.append(new String("<title>Relatório Fotográfico - Resumo Contrato</title>"));
            htmlString.append(new String("</head>"));

            // Header
            htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/" + numerocl + "/logo.jpg'></img></td>"));
            htmlString.append(new String("<td width='40%' align='center' valign='middle'><h2 class='arial'><span style='text-align: center;'><strong>Resumo Contrato</strong></span></h2></td>"));
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
            htmlString.append(new String("<td width='49%'><span class='arial; style='font-size: 14px;'>Contrato: " + centrolucro_descricao+ "</span></td>"));
            htmlString.append(new String("<td width='51%'><span class='arial; style='font-size: 14px;'>Ultima Atualização: " + dataehora + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            // Fim Dados Visita


            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));


            //Locais e Equipamentos direcionados para o colaborador
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='2'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Responsabilidades</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th align='center' valign='middle'>Locais</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Equipamentos</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='50%' align='center' valign='middle'>"+String.valueOf(myBDGeral.dbCountLocaisCL(contrato_id)) + " </td>"));
            htmlString.append(new String("<td width='50%' align='center' valign='middle'>"+String.valueOf(myBDGeral.dbCountEquipamentoCL(contrato_id)) + " </td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            //Fim Locais e Equipamentos Direcionados para o colaborador

            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));

            //Horas Trabalhadas
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='3'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Horas Trabalhadas</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th align='center' valign='middle'>Disponível</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>HH Real</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>HH Pendente</th>"));
            htmlString.append(new String("</tr>"));

            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'>250</td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'>150</td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'>100</td>"));
            htmlString.append(new String("</tr>"));

            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            //Fim Horas Trabalhadas

            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));

            //Resumo P/ Tipo Solicitação
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='5'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Tipo Solicitação</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th width='45%' align='center' valign='middle'>Tipo</th>"));
            htmlString.append(new String("<th width='35%' align='center' valign='middle'>Criadas</th>"));
            htmlString.append(new String("<th width='35%'align='center' valign='middle'>Abertas</th>"));
            htmlString.append(new String("<th width='35%'align='center' valign='middle'>Executadas</th>"));
            htmlString.append(new String("<th width='35%'align='center' valign='middle'>Pendentes</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));


            Cursor dataTipoSolicitacao = myBDGeral.getTipoSolicitacao();
            while (dataTipoSolicitacao.moveToNext()) { //Read every row
                tiposolicitacao = dataTipoSolicitacao.getString(0);
                tiposolicitacao_descricao = dataTipoSolicitacao.getString(1);

                htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='5'> "));
                htmlString.append(new String("<thbody>"));
                htmlString.append(new String("<tr>"));
                htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>" + tiposolicitacao_descricao + "</span></td>"));
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoSolicitacaoCriadas(tiposolicitacao)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoSolicitacaoAbertas(tiposolicitacao)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoSolicitacaoEncerradas(tiposolicitacao)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + (myBDGeral.dbCountTipoSolicitacaoAbertas(tiposolicitacao) -  myBDGeral.dbCountTipoSolicitacaoEncerradas(tiposolicitacao))+ "</span></td>"));
                htmlString.append(new String("</tr>"));

                htmlString.append(new String("</thbody>"));
                htmlString.append(new String("</table>"));
            }
            //Fim Resumo P/ Tipo Solicitação


            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));

            //Resumo Tipo Serviço
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='5'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Oficina</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th width='45%' align='center' valign='middle'>Oficina</th>"));
            htmlString.append(new String("<th width='35%' align='center' valign='middle'>Criadas</th>"));
            htmlString.append(new String("<th width='35% 'align='center' valign='middle'>Abertas</th>"));
            htmlString.append(new String("<th width='35% 'align='center' valign='middle'>Executadas</th>"));
            htmlString.append(new String("<th width='35% 'align='center' valign='middle'>Pendentes</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));


            Cursor dataOficina = myBDGeral.getTipoServico();
            while (dataOficina.moveToNext()) { //Read every row
                tiposervico = dataOficina.getString(0);
                tiposervico_descricao = dataOficina.getString(1);

                htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='5'> "));
                htmlString.append(new String("<thbody>"));
                htmlString.append(new String("<tr>"));
                htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>" + tiposervico_descricao + "</span></td>"));
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoServicoCriadas(tiposervico)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoServicoAbertas(tiposervico)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + myBDGeral.dbCountTipoServicoEncerradas(tiposervico)) + "</span></td>");
                htmlString.append(new String("<td width='35%' align='center' valign='middle'><span style='font-size: 14px;'>" + (myBDGeral.dbCountTipoServicoAbertas(tiposervico) -  myBDGeral.dbCountTipoSolicitacaoEncerradas(tiposolicitacao))+ "</span></td>"));
                htmlString.append(new String("</tr>"));
                htmlString.append(new String("</thbody>"));
                htmlString.append(new String("</table>"));
            }
                //Fim Resumo Tipo Serviço


                htmlString.append(new String("<p></p>"));
                htmlString.append(new String("<br></br>"));




            //Historico Inspeções
             htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='6'> "));
             htmlString.append(new String("<thbody>"));
             htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Histórico</span></h3></caption>"));
             htmlString.append(new String("<tr>"));
             htmlString.append(new String("<th width='25%' align='center' valign='middle'>OS</th>"));
             htmlString.append(new String("<th width='45%'align='center' valign='middle'>Executante</th>"));
             htmlString.append(new String("<th width='45%'align='center' valign='middle'>Data Abertura</th>"));
             htmlString.append(new String("<th width='45%'align='center' valign='middle'>Data Enceramento</th>"));
             htmlString.append(new String("<th width='45%'align='center' valign='middle'>Tipo Serviço</th>"));
             htmlString.append(new String("<th width='45%'align='center' valign='middle'>Observação</th>"));
             htmlString.append(new String("</tr>"));
             htmlString.append(new String("</thbody>"));
             htmlString.append(new String("</table>"));

            Cursor dataOS = myBDGeral.buscaOSContrato(contrato_id);
            while (dataOS.moveToNext()) { //Read every row
                os_id = dataOS.getString(0);
                dataAbertura = dataOS.getString(6);
                String tiposolicitacao_idOS = dataOS.getString(10);

                Cursor dataTipoSolicitacaoOS = myBDGeral.buscaTipoSolicitacaoOS(tiposolicitacao_idOS);
                while (dataTipoSolicitacaoOS.moveToNext()) { //Read every row
                   String tiposolicitacaoOS = dataTipoSolicitacaoOS.getString(0);
                   String tiposolicitacao_descricaoOS = dataTipoSolicitacaoOS.getString(1);

            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='5'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='25%' align='center' valign='middle'><span style='font-size: 14px;'>" + os_id + "</span></td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>"+name+"</span></td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>"+dataAbertura+"</span></td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'></span></td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'>"+tiposolicitacao_descricaoOS+"</span></td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'><span style='font-size: 14px;'></span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            }
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

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/enviados/" + "rf_contrato.pdf");
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
        Intent intent = new Intent(RelatorioContrato.this, MainActivity_Principal.class);
        Bundle dados = new Bundle();
        dados.putString("name", name);
        dados.putString("email", email);
        dados.putString("id", colaborador_id);
        dados.putString("tipo", tipo);
        intent.putExtras(dados);
        startActivity(intent);
    }


}
