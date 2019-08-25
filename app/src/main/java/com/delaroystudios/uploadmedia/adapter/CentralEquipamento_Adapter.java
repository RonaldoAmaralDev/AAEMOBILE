package com.delaroystudios.uploadmedia.adapter;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.uploadmedia.BuildConfig;
import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.equipamento.GerarQRCODE;
import com.delaroystudios.uploadmedia.banco.BancoGeral;
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


public class CentralEquipamento_Adapter extends RecyclerView.Adapter<CentralEquipamento_Adapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    BancoGeral myBDGeral;
    String numeroOS, tiposervico, dataAbertura, tag, descricao, id, modelo, enderecolocal, numeroserie, local_id, centrolucro_id, codigo, btu, centrolucro_descricao, numerocl, localdescricao, bairro, cidade, dataExecucao, fabricante, tipoequipamento, fornecedor;
    String quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes;
    String quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes;
    private File pdfFile;
    private static final String TAG = "PdfCreatorActivity";



    public CentralEquipamento_Adapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        myBDGeral = new BancoGeral(mContext);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView codigoequipamento;
        public TextView descricao;
        public TextView modeloequipamento;
        public TextView numeroserie;
        public TextView btuequipamento;
        public TextView fabricanteequipamento;
        public ImageView logo_equipamento;

        public GroceryViewHolder(View itemView) {
            super(itemView);


            logo_equipamento = itemView.findViewById(R.id.logo_equipamento);
            codigoequipamento = itemView.findViewById(R.id.placaveiculo);
            descricao = itemView.findViewById(R.id.descricaoveiculo);
            modeloequipamento = itemView.findViewById(R.id.modeloveiculo);
            numeroserie = itemView.findViewById(R.id.contratoveiculo);
            btuequipamento = itemView.findViewById(R.id.cidadeveiculo);
            fabricanteequipamento = itemView.findViewById(R.id.fabricanteequipamento);
        }
    }

    @Override
    public CentralEquipamento_Adapter.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.equipamento_row_line, parent, false);
        return new CentralEquipamento_Adapter.GroceryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CentralEquipamento_Adapter.GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_ID_EQUIPAMENTO));
        local_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_LOCAL_EQUIPAMENTO));
        codigo = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CODIGOEQUIPAMENTO_EQUIPAMENTO));
        centrolucro_id = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_CENTROCUSTO_EQUIPAMENTO));
        descricao = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_DESCRICAO_EQUIPAMENTO));
        modelo = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_MODELO_EQUIPAMENTO));
        numeroserie = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_NUMEROSERIE_EQUIPAMENTO));
        btu = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_BTU_EQUIPAMENTO));
        tag = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_TAG_EQUIPAMENTO));
        fabricante = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_FABRICANTE_EQUIPAMENTO));
        tipoequipamento = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_TIPOEQUIPAMENTO_EQUIPAMENTO));
        fornecedor = mCursor.getString(mCursor.getColumnIndex(BancoGeral.COL_FORNECEDOR_EQUIPAMENTO));


        holder.codigoequipamento.setText("Codigo: " + codigo);
        holder.descricao.setText("Descrição: " + descricao);
        holder.modeloequipamento.setText("Modelo: " + modelo);
        holder.numeroserie.setText("Nª Serie: " + numeroserie);
        holder.fabricanteequipamento.setText("Fabricante: " + fabricante);
        holder.btuequipamento.setText("BTU: " + btu);
        holder.logo_equipamento.setImageResource(R.drawable.arcondicionado_icon);

        Cursor dataContrato = myBDGeral.verificaCL(centrolucro_id);
        while (dataContrato.moveToNext()) {
            numerocl = dataContrato.getString(1);
            centrolucro_descricao = dataContrato.getString(2);
        }

        Cursor dataLocal = myBDGeral.verificaLocal(local_id);
        while (dataLocal.moveToNext()) {
            localdescricao = dataLocal.getString(3);
            enderecolocal = dataLocal.getString(23);
            bairro = dataLocal.getString(4);
            cidade = dataLocal.getString(5);
        }

       Cursor dataOS = myBDGeral.verificaOSEquipamento(id);
        while (dataOS.moveToNext()) {
           numeroOS = dataOS.getString(1);
           dataAbertura = dataOS.getString(6);
           tiposervico = dataOS.getString(10);
           dataExecucao = dataOS.getString(11);
       }

                quantPrevCriadas = String.valueOf(myBDGeral.dbCountPrevCriadas(id));
                quantPrevExecutadas = String.valueOf(myBDGeral.dbCountPrevExecutadas(id));
                quantPrevPendentes = String.valueOf(myBDGeral.dbCountPrevPendentes(id));
                quantCorretivaCriadas = String.valueOf(myBDGeral.dbCountCorretivaCriadas(id));
                quantCorretivaExecutada = String.valueOf(myBDGeral.dbCountCorretivaExecutadas(id));
                quantCorretivaPendentes = String.valueOf(myBDGeral.dbCountCorretivaPendentes(id));


        holder.itemView.setOnClickListener(v -> { // Linguagem Java 8

            new AlertDialog.Builder(mContext)
                    .setIcon(R.drawable.logo)
                    .setTitle(R.string.app_name)
                    .setMessage("Deseja: ")
                    .setPositiveButton("Gerar QRCODE", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(mContext, GerarQRCODE.class);
                            Bundle dados = new Bundle();
                            dados.putString("tag", tag);
                            dados.putString("equipamento_id", id);
                            dados.putString("descricao", descricao);
                            intent.putExtras(dados);
                            mContext.startActivity(intent);
                        }
                    })

                    .setNegativeButton("Gerar Relatório", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(mContext, "Favor aguardar alguns segundos. ", Toast.LENGTH_LONG).show();
                            try {
                                //Gerar PDF
                                gerarRelatorioPDF(id, localdescricao, enderecolocal, bairro, cidade, descricao, tag, centrolucro_descricao, numerocl, modelo, numeroOS, dataAbertura, tiposervico, dataExecucao, quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes, quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes, fabricante, tipoequipamento, fornecedor);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNeutralButton("Atualizar Dados", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(mContext, "Em desenvolvimento. ", Toast.LENGTH_LONG).show();

                        }
                    })


                    .show();

        });
    }

    private void gerarRelatorioPDF(String id, String localdescricao,String enderecolocal, String bairro, String cidade, String descricaoequipamento, String tag, String centrolucro_descricao, String numerocl, String modelo, String numeroOS, String dataAbertura, String tiposervico, String dataExecucao, String quantPrevCriadas, String quantPrevExecutadas, String quantPrevPendentes, String quantCorretivaCriadas, String quantCorretivaExecutada, String quantCorretivaPendentes, String fabricante, String tipoequipamento, String fornecedor) throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            createPdf(id, localdescricao, enderecolocal, bairro, cidade, descricaoequipamento, tag, centrolucro_descricao, numerocl, modelo, numeroserie, btu, numeroOS, dataAbertura, tiposervico, dataExecucao, quantPrevCriadas, quantPrevExecutadas, quantPrevPendentes, quantCorretivaCriadas, quantCorretivaExecutada, quantCorretivaPendentes, fabricante, tipoequipamento, fornecedor);
        }
    }


    private void createPdf(String id, String localdescricao, String enderecolocal, String bairro, String cidade, String descricaoequipamento, String tag, String centrolucro_descricao, String numerocl, String modelo, String numeroserie, String btu, String numeroOS, String dataAbertura, String tiposervico, String dataExecucao, String quantPrevCriadas, String quantPrevExecutadas, String quantPrevPendentes, String quantCorretivaCriadas, String quantCorretivaExecutada, String quantCorretivaPendentes, String fabricante, String tipoequipamento, String fornecedor) throws FileNotFoundException, DocumentException {

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
            htmlString.append(new String("<td width='51%'><span class='arial'>Local: " + localdescricao + " , " +enderecolocal + " , " +  bairro + " , " + cidade + "</span></td>"));
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


            //Historico Visitas
            htmlString.append(new String("<table width='100%' border='1' cellpadding='2' cellspacing='7'> "));
            htmlString.append(new String("<thbody>"));
            htmlString.append(new String("<caption><h3 class='arial'><span style='text-align: center;'>Historico Visitas</span></h3></caption>"));
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
        Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        target.setDataAndType(uri,"application/pdf");
        target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Intent intent = Intent.createChooser(target, "Como deseja abrir PDF");
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Caso não possua arquivo leitor PDF irá solicitar instalação

            Toast.makeText(mContext, "Você não possui leitor arquivo PDF", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.adobe.reader&hl=pt_BR"));
            // i.setData(Uri.parse("market://details?id=com.adobe.reader&hl=en"));
            mContext.startActivity(i);
        }
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
