package com.delaroystudios.uploadmedia.operacao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class RelatorioVisita extends AppCompatActivity {

    String os_id, equipamento_id, local_id, dataplanejamento, executante, email, colaborador_id, token, id_centrolucro, localdescricao, numerocl, centrolucro_descricao, codigolocal, codigoequipamento, bairro, cidade, tiposervico_descricao, tag, latitude, longitude, dataexecucao, checklist, observacaoantes, observacaodepois, situacao, medicao1, medicao2, id_Atividade;
    BancoGeral myBDGeral;
    DatabaseHelper myBDOperacao;
    private File pdfFile;
    private static final String TAG = "RelatorioFotografico";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_visita);

        myBDGeral = new BancoGeral(this);
        myBDOperacao = new DatabaseHelper(this);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        os_id = dados.getString("os_id");
        equipamento_id = dados.getString("equipamento_id");
        local_id = dados.getString("local_id");
        checklist = dados.getString("checklist");
        dataplanejamento = dados.getString("dataplanejamento");
        String tiposervico = dados.getString("tiposervico");
        executante = dados.getString("name");
        email = dados.getString("email");
        colaborador_id = dados.getString("id");
        token = dados.getString("token");
        id_centrolucro = dados.getString("id_centrolucro");

        int tiposervico_int = Integer.parseInt(tiposervico);

        if(tiposervico_int == 1) {
            tiposervico_descricao = "Preventiva";
        } else if (tiposervico_int == 2) {
            tiposervico_descricao = "Corretiva";
        } else if(tiposervico_int == 3) {
            tiposervico_descricao = "Orçamento";
        } else if(tiposervico_int == 4) {
            tiposervico_descricao = "Preditiva";
        } else if(tiposervico_int == 5) {
            tiposervico_descricao = "Limpeza";
        }


        Cursor dataContrato = myBDGeral.verificaCL(id_centrolucro);
        while (dataContrato.moveToNext()) {
            numerocl = dataContrato.getString(1);
            centrolucro_descricao = dataContrato.getString(2);
        }

        Cursor dataLocal = myBDGeral.qrCode(local_id);
        while (dataLocal.moveToNext()) {
            codigolocal = dataLocal.getString(1);
            localdescricao = dataLocal.getString(3);
            bairro = dataLocal.getString(4);
            cidade = dataLocal.getString(5);
            latitude = dataLocal.getString(6);
            longitude = dataLocal.getString(7);

        }

        Cursor dataEquipamento= myBDGeral.verificaEquipamento(equipamento_id);
        while (dataEquipamento.moveToNext()) {
            codigoequipamento = dataEquipamento.getString(1);
            tag = dataEquipamento.getString(6);
        }

        Cursor dataVisita = myBDOperacao.getVisitas(os_id);
        while (dataVisita.moveToNext()) {
            dataexecucao = dataVisita.getString(19);
            observacaoantes = dataVisita.getString(12);
            observacaodepois = dataVisita.getString(13);
            situacao = dataVisita.getString(16);
            medicao1 = dataVisita.getString(17);
            medicao2 = dataVisita.getString(18);
        }


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat sdfData =
                new java.text.SimpleDateFormat("dd/MM/yyyy");
        String currentTime = sdf.format(dt);
        String data = sdfData.format(dt);
        // Setar Data Execução
        dataexecucao = data + " - " + currentTime;
        // Mudar Ordem na Data Programação


                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setIcon(R.drawable.logo);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Visita: " + os_id + "\nTipo Serviço: "+ tiposervico_descricao + "\nData: " + data + "\nEste processo pode demorar alguns segundos.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "GERAR RELATORIO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    createPdfWrapper(os_id, localdescricao, numerocl, centrolucro_descricao, codigoequipamento, bairro, cidade, tiposervico_descricao, tag, latitude, longitude, executante, dataplanejamento, dataexecucao, observacaoantes, observacaodepois, situacao, medicao1, medicao2);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.show();

    }

            private void createPdfWrapper(String os_id, String local, String numerocl, String centrolucro_descricao, String codigoequipamento, String bairro, String cidade, String tiposervico_descricao, String tag, String latitude, String longitude, String executantes, String dataplanejamento, String dataexecucao, String observacaoantes, String observacaodepois, String situacao, String medicao1, String medicao2) throws FileNotFoundException,DocumentException{
                int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else {
                    createPdf(os_id, local, numerocl, centrolucro_descricao, codigolocal, codigoequipamento, bairro, cidade, tiposervico_descricao, tag, latitude, longitude, executantes, dataplanejamento, dataexecucao, observacaoantes, observacaodepois, situacao, medicao1, medicao2);

                }
            }


    private void createPdf(String os_id, String localdescricao, String numerocl, String centrolucro_descricao, String codigolocal, String codigoequipamento, String bairro, String cidade, String tiposervico_descricao, String tag, String latitude, String longitude, String executante, String dataplanejamento, String dataexecucao, String observacaoantes, String observacaodepois, String situacao, String medicao1, String medicao2) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/RF/");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Criado diretorio para Relatorios Fotograficos");
        }
        try
        {
            pdfFile = new File(docsFolder.getAbsolutePath(),"rf_" + os_id + ".pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document();
            document.addAuthor("AAE MOBILE");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("AAE MOBILE");
            document.addTitle("Relatório Fotográfico - " + os_id);
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, output);

            // Verifica se existe as fotos se não coloca Sem Foto

            StringBuilder htmlString = new StringBuilder();
            htmlString.append(new String("<html><body>"));
            htmlString.append(new String("<head>"));
            htmlString.append(new String("<title>Relatório Fotografico - " + os_id + "</title>"));
            htmlString.append(new String("</head>"));

            // Header
            htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/" + numerocl + "/logo.jpg'></img></td>"));
            htmlString.append(new String("<td width='40%' align='center' valign='middle'><h1 class='arial'><span style='text-align: center;'><strong>Relatório Fotográfico</strong></span></h1></td>"));
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
            htmlString.append(new String("<td width='51%'><span class='arial'>Equipamento: " + codigoequipamento + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Local: " + codigolocal + " - " + localdescricao + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Região: " + bairro + " - " + cidade + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Tipo Serviço: " + tiposervico_descricao + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>TAG Equipamento: " + tag +  "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Localização: " + latitude + " , " + longitude + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Executante(s): " + executante +  "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Data Programação: " + dataplanejamento + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Data Execução: " + dataexecucao +  "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));


            Cursor dataAtividade = myBDGeral.verificaAtividades(checklist);
            while (dataAtividade.moveToNext()) { //Read every row
                id_Atividade = dataAtividade.getString(0);
                for (int i= 1; i <= 1; i++) { //Read every column
                    Cursor dataSetor = myBDGeral.buscaAtividades(id_Atividade, checklist);
                    while (dataSetor.moveToNext()) {
                        // Atividade
                        String atividade = dataSetor.getString(2);

                        htmlString.append(new String("<p> <span class='arial'>"+ atividade + "</span></p>"));

                        // Antes Observação e Foto
                        htmlString.append(new String("<p class='arial'>Observação Antes:</p>"));
                        htmlString.append(new String("<p>"));
                        htmlString.append(new String("<textarea>" + observacaoantes + "</textarea>"));
                        htmlString.append(new String("</p>"));
                        htmlString.append(new String("<p class='arial'>Medição: " + medicao1 + " - " + " Situação: " + situacao + "</p>"));
                        htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
                        htmlString.append(new String("<thbody>"));
                        htmlString.append(new String("<tr>"));
                        File foto1a = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "1A" + ".jpg");
                        File foto2a = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "2A" + ".jpg");
                        File foto3a = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "3A" + ".jpg");
                        if(foto1a.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_1A.jpg' height='130' width='210'></img></td>"));
                        }
                       else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        if(foto2a.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_2A.jpg' height='130' width='210'></img></td>"));
                        }
                        else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        if(foto3a.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_3A.jpg' height='130' width='210'></img></td>"));
                        }
                        else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        htmlString.append(new String("</tr>"));
                        htmlString.append(new String("</thbody>"));
                        htmlString.append(new String("</table>"));
                        // Depois Fotos e Observação
                        htmlString.append(new String("<p class='arial'>Observação Depois:</p>"));
                        htmlString.append(new String("<p>"));
                        htmlString.append(new String("<textarea> " + observacaodepois + "</textarea>"));
                        htmlString.append(new String("</p>"));
                        htmlString.append(new String("<p class='arial'>Medição: " + medicao2 + "</p>"));

                        htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
                        htmlString.append(new String("<thbody>"));
                        htmlString.append(new String("<tr>"));
                        File foto1d = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "1D" + ".jpg");
                        File foto2d = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "2D" + ".jpg");
                        File foto3d = new  File("/sdcard/PicturesHELPER/" + os_id + "_" + checklist + "_" + id_Atividade + "_" + "3D" + ".jpg");
                        if(foto1d.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_1D.jpg' height='130' width='210'></img></td>"));
                        }
                        else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        if(foto2d.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_2D.jpg' height='130' width='210'></img></td>"));
                        }
                        else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        if(foto3d.exists()) {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_"+checklist+"_"+id_Atividade+"_3D.jpg' height='130' width='210'></img></td>"));
                        }
                        else {
                            htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
                        }
                        htmlString.append(new String("</tr>"));
                        htmlString.append(new String("</thbody>"));
                        htmlString.append(new String("</table>"));

                    }
                }
            }

            // Assinatura Colaborador e Cliente
            htmlString.append(new String("<hr></hr>"));
            htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/" + os_id + "_assColaborador.jpg' height='130' width='210'></img></td>"));
            File assinaturaCliente = new  File("/sdcard/PicturesHELPER/" + os_id + "_assCliente.jpg");
            if(assinaturaCliente.exists()) {
                htmlString.append(new String(" <td align='center' valign='middle'><img src='/sdcard/PicturesHELPER/"+os_id +"_assCliente.jpg' height='130' width='210'></img></td>"));
            } else {
                htmlString.append(new String(" <td align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/semfoto.jpg' height='130' width='210'></img></td>"));
            }
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
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

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/RF/" + "rf_" + os_id + ".pdf");
        String mFilePath = docsFolder.getAbsolutePath().toString();
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
        Intent intent = new Intent(RelatorioVisita.this, MainActivity_Principal.class);
        Bundle dados = new Bundle();
        dados.putString("name", executante);
        dados.putString("email", email);
        dados.putString("id", colaborador_id);
        dados.putString("token", token);
        intent.putExtras(dados);
        startActivity(intent);
    }

}
