package br.com.araujoabreu.timg.equipamento.qrcode;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import br.com.araujoabreu.timg.BuildConfig;
import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.equipamento.GerarQRCODE;
import br.com.araujoabreu.timg.banco.BancoGeral;
import br.com.araujoabreu.timg.visitas.AbrirCorretiva;
import br.com.araujoabreu.timg.activity.MainActivity_Principal;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

public class ScannerQRCODE extends AppCompatActivity {

    private String[] permissoes = new String[]{"android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};
    public String email, modelo, tiposervico, numeroOS, name, colaborador_id, tipo, tipoequipamento, equipamentoDescricao, equipamentoID, local_id, centrolucro_id, numeroserie, btu, localdescricao, bairro, cidade, descricao, dataAbertura, dataExecucao, centrolucro_descricao, numerocl, quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes, quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes, fabricante, fornecedor;
    BancoGeral myBDGeral;
    private File pdfFile;
    private static final String TAG = "PdfCreatorActivity";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_scanner_qrcode);

        myBDGeral = new BancoGeral(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(100, 100);

        leitor();
    }

   protected void onActivityResult(int i, int i2, Intent intent) {
        IntentResult parseActivityResult = IntentIntegrator.parseActivityResult(i, i2, intent);
       if (parseActivityResult == null) {
           super.onActivityResult(i, i2, intent);
        } else if (parseActivityResult.getContents() != null) {
            alert(parseActivityResult.getContents());
        } else {
            alert("Scan Cancelado");
        }
    }


    private void alert(String codigo) {

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String email= pref.getString("email", "");
        String colaborador_id = pref.getString("id", "" );
        String token = pref.getString("token", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(ScannerQRCODE.this);
        builder.setTitle("O que você deseja: ");
        String[] opcao = {"RESUMO EQUIPAMENTO", "GERAR QRCODE", "ABRIR CORRETIVA"};
        builder.setItems(opcao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        try {
                            Cursor abrirCorretiva = myBDGeral.verificaEquipamentoTAG(codigo);
                            while (abrirCorretiva.moveToNext()) {
                                equipamentoID = abrirCorretiva.getString(0);
                                local_id = abrirCorretiva.getString(4);
                                centrolucro_id = abrirCorretiva.getString(3);
                                modelo = abrirCorretiva.getString(5);
                                numeroserie = abrirCorretiva.getString(7);
                                btu = abrirCorretiva.getString(8);
                                fornecedor = abrirCorretiva.getString(11);
                                fabricante = abrirCorretiva.getString(9);
                                tipoequipamento = abrirCorretiva.getString(10);

                                Cursor dataLocal = myBDGeral.verificaLocal(local_id);
                                while (dataLocal.moveToNext()) {
                                    localdescricao = dataLocal.getString(3);
                                    bairro = dataLocal.getString(4);
                                    cidade = dataLocal.getString(5);
                                }
                                dataLocal.close();

                                Cursor dataContrato = myBDGeral.verificaCL(centrolucro_id);
                                while (dataContrato.moveToNext()) {
                                    numerocl = dataContrato.getString(1);
                                    centrolucro_descricao = dataContrato.getString(2);
                                }
                                dataContrato.close();

                                quantPrevCriadas = String.valueOf(myBDGeral.dbCountPrevCriadas(equipamentoID));
                                quantPrevExecutadas = String.valueOf(myBDGeral.dbCountPrevExecutadas(equipamentoID));
                                quantPrevPendentes = String.valueOf(myBDGeral.dbCountPrevPendentes(equipamentoID));
                                quantCorretivaCriadas = String.valueOf(myBDGeral.dbCountCorretivaCriadas(equipamentoID));
                                quantCorretivaExecutada = String.valueOf(myBDGeral.dbCountCorretivaExecutadas(equipamentoID));
                                quantCorretivaPendentes = String.valueOf(myBDGeral.dbCountCorretivaPendentes(equipamentoID));

                            }
                            abrirCorretiva.close();

                          //Gerar PDF
                            gerarRelatorioPDF(equipamentoID, localdescricao, bairro, cidade, descricao, codigo, centrolucro_descricao, numerocl, modelo, numeroOS, dataAbertura, tiposervico, dataExecucao, quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes, quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes, fabricante, tipoequipamento, fornecedor);

                        } catch (FileNotFoundException e) {
                           e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    // Se marcar Gerar QRCODE
                    case 1:
                        Cursor data = myBDGeral.getTag(codigo);
                        if (!data.moveToNext()) {
                            Toast.makeText(getApplicationContext(), "Não encontrado equipamento TAG: " + codigo, Toast.LENGTH_SHORT).show();
                            Intent intentVoltar = new Intent(ScannerQRCODE.this, MainActivity_Principal.class);
                            Bundle dados = new Bundle();
                            dados.putString("name", name);
                            dados.putString("email", email);
                            dados.putString("id", colaborador_id);
                            dados.putString("token", token);
                            intentVoltar.putExtras(dados);
                            startActivity(intentVoltar);
                        }
                        else  {
                            equipamentoID = data.getString(0);
                            equipamentoDescricao = data.getString(2);

                            Intent intent = new Intent(ScannerQRCODE.this, GerarQRCODE.class);
                            Bundle dados = new Bundle();
                            dados.putString("tag", codigo);
                            dados.putString("equipamento_id", equipamentoID);
                            dados.putString("descricao", equipamentoDescricao);
                            intent.putExtras(dados);
                            startActivity(intent);
                    }
                        break;
                    // Se marcar Abrir Corretiva
                    case 2:
                        Cursor abrirCorretiva = myBDGeral.verificaEquipamentoTAG(codigo);
                        while (abrirCorretiva.moveToNext()) {

                        String equipamentoID = abrirCorretiva.getString(0);
                        local_id = abrirCorretiva.getString(4);
                        centrolucro_id = abrirCorretiva.getString(3);
                        Intent intent = new Intent(ScannerQRCODE.this, AbrirCorretiva.class);
                        Bundle dados = new Bundle();
                        dados.putString("equipamento_id", equipamentoID);
                        dados.putString("local_id", local_id);
                        dados.putString("centrolucro_id", centrolucro_id);
                        intent.putExtras(dados);
                        startActivity(intent);
                        abrirCorretiva.close();
                        }
                        break;
                }
            }
        });

        AlertDialog dialogTipoSolicitacao = builder.create();
        dialogTipoSolicitacao.show();

    }

   public void leitor() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Insira Codigo Barras ou QR-CODE");
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
   }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(R.string.app_name)
                .setMessage("Deseja voltar ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);
                    String name = pref.getString("name", "");
                    String email= pref.getString("email", "");
                    String colaborador_id = pref.getString("id", "" );
                    String token = pref.getString("token", "");

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ScannerQRCODE.this, MainActivity_Principal.class);
                        Bundle dados = new Bundle();
                        dados.putString("name", name);
                        dados.putString("email", email);
                        dados.putString("id", colaborador_id);
                        dados.putString("token", token);
                        intent.putExtras(dados);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void gerarRelatorioPDF(String id, String localdescricao, String bairro, String cidade, String descricaoequipamento, String tag, String centrolucro_descricao, String numerocl, String modelo, String numeroOS, String dataAbertura, String tiposervico, String dataExecucao, String quantPrevCriadas, String quantPrevExecutadas, String quantPrevPendentes, String quantCorretivaCriadas, String quantCorretivaExecutada, String quantCorretivaPendentes, String fabricante, String tipoequipamento, String fornecedor) throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(ScannerQRCODE.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            createPdf(id, localdescricao, bairro, cidade, descricaoequipamento, tag, centrolucro_descricao, numerocl, modelo, numeroserie, btu, numeroOS, dataAbertura, tiposervico, dataExecucao, quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes, quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes, fabricante, tipoequipamento, fornecedor);
        }
    }


    private void createPdf(String id, String localdescricao, String bairro, String cidade, String descricaoequipamento, String tag, String centrolucro_descricao, String numerocl, String modelo, String numeroserie, String btu, String numeroOS, String dataAbertura, String tiposervico, String dataExecucao, String quantPrevCriadas, String quantPrevExecutadas, String quantPrevPendentes, String quantCorretivaCriadas, String quantCorretivaExecutada, String quantCorretivaPendentes, String fabricante, String tipoequipamento, String fornecedor) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "HELPER")+ "/EQUIPAMENTO/");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Criado diretorio para Relatorios Fotograficos");
        }
        try
        {


            pdfFile = new File(docsFolder.getAbsolutePath(), id + "_rf_equipamento.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document();
            document.addAuthor("AAE MOBILE");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("AAE MOBILE");
            document.addTitle("Relatório Equipamento - " + tag);
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, output);

            StringBuilder htmlString = new StringBuilder();
            htmlString.append(new String("<html><body>"));
            htmlString.append(new String("<head>"));
            htmlString.append(new String("<title>Relatório Equipamento - " + tag + "</title>"));
            htmlString.append(new String("</head>"));

            // Header
            htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/qrcode.jpg'></img></td>"));
            htmlString.append(new String("<td width='40%' align='center' valign='middle'><h1 class='arial'><span style='text-align: center;'><strong>Relatório Equipamento</strong></span></h1></td>"));
            htmlString.append(new String("<td width='10%' align='center' valign='middle'><img src='https://helper.aplusweb.com.br/assets/imgs/logo.png'></img></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            // Fim Header


            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));

            // Dados Equipamento
            htmlString.append(new String("<table width='100%' border='1' cellpadding='3' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Contrato: " + centrolucro_descricao+ "</span></td>"));
            htmlString.append(new String("<td width='51%'><span class='arial'>Local: " + localdescricao + " , " + bairro + " , " + cidade + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Data Ativação:  </span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Modelo: " + modelo + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Número Serie: " + numeroserie + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>TAG: " + tag +  "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Nota Fiscal:  </span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Garantia: </span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Tipo Equipamento: " + tipoequipamento + " </span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>BTU: " + btu + " </span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Fornecedor: " + fornecedor + "</span></td>"));
            htmlString.append(new String("<td width='49%'><span class='arial'>Fabricante: " + fabricante + "</span></td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));
            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));


            //Historico Preventiva Equipamento

            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Preventiva</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th align='center' valign='middle'>Criadas</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Realizadas</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Pendentes</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantPrevCriadas + " </td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantPrevExecutadas + " </td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantPrevPendentes + "</td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));


            //Historico Corretiva Equipamento
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Corretiva</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th align='center' valign='middle'>Criadas</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Realizadas</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Pendentes</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantCorretivaCriadas + " </td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantCorretivaExecutada + " </td>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'> " + quantCorretivaPendentes + " </td>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));


            //Historico VisitasLocal
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Historico VisitasLocal</span></h3></caption>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<th align='center' valign='middle'>OS</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Tipo</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Status</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Data Abertura</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Data Execução</th>"));
            htmlString.append(new String("<th align='center' valign='middle'>Executante</th>"));
            htmlString.append(new String("</tr>"));
            htmlString.append(new String("<tr>"));
            htmlString.append(new String("<td width='35%' align='center' valign='middle'>"+numeroOS+"</td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'>"+tiposervico+"</td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'>Aberta</td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'>"+dataAbertura+"</td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'>"+dataExecucao+"</td>"));
            htmlString.append(new String("<td width='45%' align='center' valign='middle'></td>"));


            htmlString.append(new String("</tr>"));
            htmlString.append(new String("</thbody>"));
            htmlString.append(new String("</table>"));

            htmlString.append(new String("<p></p>"));
            htmlString.append(new String("<br></br>"));
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

        Intent target = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        target.setDataAndType(uri,"application/pdf");
        target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Intent intent = Intent.createChooser(target, "Como deseja abrir PDF");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Caso não possua arquivo leitor PDF irá solicitar instalação

            Toast.makeText(ScannerQRCODE.this, "Você não possui leitor arquivo PDF", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.adobe.reader&hl=pt_BR"));
            // i.setData(Uri.parse("market://details?id=com.adobe.reader&hl=en"));
            startActivity(i);
        }
    }




}
