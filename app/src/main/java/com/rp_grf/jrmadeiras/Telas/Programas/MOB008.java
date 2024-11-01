package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Utils.FocusHandler;
import com.rp_grf.jrmadeiras.Utils.HTTPRequest;
import com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters.MapaRetiraAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaRetira;
import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_lib;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MOB008 extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_romaneio_liberado;
    private static DatabaseReference referenceDB_romaneio;
    private static DatabaseReference referenceDB_item;

    View view;
    AlertMessage alertMessage;

    ZXingScannerView scannerView;
    FocusHandler focusHandler;

    ConstraintLayout w_layout;

    CardView w_botao_leitor;
    CardView w_botao_cancelar;
    CardView w_botao_foco;

    ImageView w_icone_leitor;
    ImageView w_icone_cancelar;
    ImageView w_icone_foco;

    FrameLayout frameLayout;
    FrameLayout w_item_vazio;

    RecyclerView w_romaneio;

    ArrayList<Romaneio_ent> lista_romaneio_ent;
    MapaRetiraAdapter mapaRetiraAdapter;

    String descricao_programa;

    ProgressDialog dialog, dialogPesquisa;

    TextView w_codigo;

    Integer contador;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob008);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contador = 0;

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scannerView = new ZXingScannerView(this);
        focusHandler = new FocusHandler(new Handler(), scannerView);

        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        alertMessage = new AlertMessage(this);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_romaneio_liberado = firebaseDB.getReference("Romaneio_lib");
        referenceDB_romaneio = firebaseDB.getReference("Romaneio_ret");
        referenceDB_item = firebaseDB.getReference("Item_romaneio_ret");

        iniciarCampos();

        cliqueBotao();

        desabilitarBotoes(false);

    }

    private void iniciarCampos() {
        w_layout = (ConstraintLayout) findViewById(R.id.layout_mob008);

        w_botao_leitor = (CardView) findViewById(R.id.card_view_botao_leitor);
        w_botao_cancelar = (CardView) findViewById(R.id.card_view_botao_cancelar);
        w_botao_foco = (CardView) findViewById(R.id.card_view_botao_foco);

        w_icone_leitor = (ImageView) findViewById(R.id.image_view_barcode_leitor);
        w_icone_cancelar = (ImageView) findViewById(R.id.image_view_barcode_cancelar);
        w_icone_foco = (ImageView) findViewById(R.id.image_view_barcode_foco);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_qrcode);
        w_item_vazio = (FrameLayout) findViewById(R.id.layout_item_vazio);

        w_romaneio = (RecyclerView) findViewById(R.id.recycler_view_romaneio_ent);

        lista_romaneio_ent = new ArrayList<Romaneio_ent>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);
        dialogPesquisa = new ProgressDialog(this, R.style.DialogTheme);

        w_codigo = (TextView) findViewById(R.id.text_view_codigo_barras);
    }

    private void cliqueBotao() {
        w_botao_leitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerView.setAutoFocus(false);
                frameLayout.addView(scannerView);
                desabilitarBotoes(true);
            }
        });

        w_botao_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.removeView(scannerView);
                scannerView.resumeCameraPreview(MOB008.this);
                desabilitarBotoes(false);
            }
        });

        w_botao_foco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerView.setAutoFocus(false);
                scannerView.setAutoFocus(true);

                w_botao_foco.setEnabled(false);

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                w_botao_foco.setEnabled(true);

                scannerView.setAutoFocus(false);
            }
        });
    }

    private void desabilitarBotoes(Boolean flag) {
        w_botao_foco.setEnabled(flag);
        w_botao_cancelar.setEnabled(flag);

        w_botao_leitor.setEnabled(!flag); //Contrario da flag

        //Diferente de true
        if (!flag) {
            w_botao_foco.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_foco.setImageResource(R.drawable.icone_foco);

            w_botao_cancelar.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_cancelar.setImageResource(R.drawable.icone_close);

            w_botao_leitor.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            w_icone_leitor.setImageResource(R.drawable.icone_barcode_branco);

        } else {
            w_botao_foco.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            w_icone_foco.setImageResource(R.drawable.icone_foco_branco);

            w_botao_cancelar.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            w_icone_cancelar.setImageResource(R.drawable.icone_close_branco);

            w_botao_leitor.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_leitor.setImageResource(R.drawable.icone_barcode);
        }

    }

    //Constroi o Adapter
    private void montarRomaneio_ret(Romaneio_ent tab_romaneio_ent) {
        try {

            if (tab_romaneio_ent == null) {
                w_item_vazio.setVisibility(View.VISIBLE);
                alertMessage.info(view, this, "Leitura QR Code",
                        "Nenhum pedido encontrado!");
            } else {
                verificarRomaneioLiberado(tab_romaneio_ent, new RetornaFlagLiberado() {
                    @Override
                    public void recebeFlag(boolean flag_liberado) {

                        if (flag_liberado == true){
                            w_item_vazio.setVisibility(View.VISIBLE);
                            w_romaneio.setAdapter(null);
                            alertMessage.warning(view, MOB008.this, "Atenção",
                                    "Este romaneio já foi liberado!");
                        } else {
                            w_item_vazio.setVisibility(View.GONE);
                            lista_romaneio_ent.clear();
                            lista_romaneio_ent.add(tab_romaneio_ent);
                            mapaRetiraAdapter = new MapaRetiraAdapter(MOB008.this, lista_romaneio_ent);
                            w_romaneio.setAdapter(mapaRetiraAdapter);
                            mapaRetiraAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            alertMessage.error(view, this, "Erro ao montar pedido", ex.toString());
        }

    }

    private interface RetornaFlagLiberado{
        void recebeFlag(boolean flag_liberado);
    }

    private void verificarRomaneioLiberado(Romaneio_ent tab_romaneio_ent, RetornaFlagLiberado retornaFlagLiberado){
        referenceDB_romaneio_liberado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag_liberado = false;
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Romaneio_lib tab_romaneio_lib = retornoBanco.getValue(Romaneio_lib.class);

                    String romaneio_liberado = tab_romaneio_lib.getNum_rom();
                    String romaneio_retira = tab_romaneio_ent.getNum_rom();

                    if (romaneio_liberado.equals(romaneio_retira)){
                        flag_liberado = true;
                    }

                }
                retornaFlagLiberado.recebeFlag(flag_liberado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB008.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private Romaneio_ent lerXML(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            Element rootElement = document.getDocumentElement();

            Romaneio_ent tab_romaneio_ent = new Romaneio_ent();

            gravarRomaneio(tab_romaneio_ent, rootElement);

            gravarItens(rootElement);

            return tab_romaneio_ent;

        } catch (Exception ex) {
            ex.printStackTrace();
            alertMessage.error(view, this, "Erro ao ler XML", ex.toString());
            return null;
        }
    }

    //Preenche a tabela Romaneio_ent com o conteudo das tag do XML
    private Romaneio_ent gravarRomaneio(Romaneio_ent tab_romaneio_ent, Element rootElement) {
        tab_romaneio_ent.setNum_rom(getTagUnico("num_rom", rootElement));
        tab_romaneio_ent.setNum_reg_nts(Long.parseLong(getTagUnico("num_reg_nts", rootElement)));
        tab_romaneio_ent.setNum_seq(Long.parseLong(getTagUnico("num_seq", rootElement)));
        tab_romaneio_ent.setCod_ser(getTagUnico("cod_ser", rootElement));
        tab_romaneio_ent.setNum_doc(getTagUnico("num_doc", rootElement));
        tab_romaneio_ent.setNom_cli(getTagUnico("nom_cli", rootElement));
        tab_romaneio_ent.setEnd_cli(getTagUnico("end_cli", rootElement));
        tab_romaneio_ent.setNum_end_cli(getTagUnico("num_end_cli", rootElement));
        tab_romaneio_ent.setCom_bai_cli(getTagUnico("com_bai_cli", rootElement));
        tab_romaneio_ent.setNom_mun(getTagUnico("nom_mun", rootElement));
        tab_romaneio_ent.setCod_uni_fed(getTagUnico("cod_uni_fed", rootElement));
        tab_romaneio_ent.setFon_cli(getTagUnico("fon_cli", rootElement));
        tab_romaneio_ent.setFon_cli_2(getTagUnico("fon_cli2", rootElement));
        tab_romaneio_ent.setNom_ven(getTagUnico("nom_ven", rootElement));

        gravarRomaneio_Online(tab_romaneio_ent);

        return tab_romaneio_ent;
    }

    private void gravarItens(Element rootElement) {
        BancoMapaRetira bancoMapaRetira = new BancoMapaRetira(this);
        bancoMapaRetira.limparBanco();

        String[] array_num_rom = getTagTodos("num_rom", rootElement);
        String[] array_num_seq = getTagTodos("num_seq", rootElement);
        String[] array_cod_ite = getTagTodos("cod_ite", rootElement);
        String[] array_num_reg_nts = getTagTodos("num_reg_nts", rootElement);
        String[] array_nom_ite = getTagTodos("nom_ite", rootElement);
        String[] array_qtd_ite = getTagTodos("qtd_ite", rootElement);
        String[] array_qtd_pen = getTagTodos("qtd_ate", rootElement); //Quantidade pendente
        String[] array_cod_uni = getTagTodos("cod_uni", rootElement);
        String[] array_cod_dep = getTagTodos("cod_dep", rootElement);
        String[] array_nom_dep = getTagTodos("nom_dep", rootElement);
        String[] array_cod_ide_loc = getTagTodos("cod_ide_loc", rootElement);

        for (int i = 0; i < array_cod_ite.length; i++) {

            Item_romaneio_ent tab_item_romaneio_ent = new Item_romaneio_ent();

            tab_item_romaneio_ent.setNum_rom(array_num_rom[i]);
            tab_item_romaneio_ent.setNum_seq(array_num_seq[i + 1]);
            tab_item_romaneio_ent.setCod_ite(Long.parseLong(array_cod_ite[i]));
            tab_item_romaneio_ent.setNum_reg_nts(Long.parseLong(array_num_reg_nts[i + 1]));
            tab_item_romaneio_ent.setNom_ite(array_nom_ite[i]);
            tab_item_romaneio_ent.setQtd_ite(array_qtd_ite[i]);
            tab_item_romaneio_ent.setQtd_pen(array_qtd_pen[i]);
            tab_item_romaneio_ent.setCod_uni(array_cod_uni[i]);
            tab_item_romaneio_ent.setCod_dep(array_cod_dep[i]);
            tab_item_romaneio_ent.setNom_dep(array_nom_dep[i]);
            tab_item_romaneio_ent.setCod_ide_loc(array_cod_ide_loc[i]);

            bancoMapaRetira.setItem_Romaneio_Ent(tab_item_romaneio_ent);

            //Grava os itens na tabela romaneio_ret na nuvem
            gravarItem_Online(tab_item_romaneio_ent);

        }


        bancoMapaRetira.close();

    }

    //Retorna o conteudo do XML da URL
    private String getXml(Result result) {
        try {
            return new HTTPRequest().execute(result.getText()).get();
        } catch (Exception ex) {
            ex.printStackTrace();
            alertMessage.error(view, this, "Erro ao acessar URL", ex.toString());
            return null;
        }
    }

    //Retorna o conteudo da primeira tag no xml
    private String getTagUnico(String tagName, Element element) {
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    //Retorna o conteudo da tag no xml em forma de array
    private String[] getTagTodos(String tagName, Element element) {
        int tamanho = element.getElementsByTagName(tagName).getLength();

        String[] retorno = new String[tamanho];

        for (int i = 0; i < tamanho; i++) {
            retorno[i] = element.getElementsByTagName(tagName).item(i).getTextContent();
        }

        return retorno;
    }

    //Ao ler QR Code
    @Override
    public void handleResult(Result result) {
        String xml = getXml(result);

        Romaneio_ent tab_romaneio_ent = lerXML(xml);

        montarRomaneio_ret(tab_romaneio_ent);

        frameLayout.removeView(scannerView);
        scannerView.resumeCameraPreview(this);
        focusHandler.stop();

        desabilitarBotoes(false);
    }

    //Ao parar a camera
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        focusHandler.stop();
    }

    //Ao voltar a camera após parar
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.setAutoFocus(false);
        scannerView.startCamera();
    }

    //Ao fechar a camera
    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
        focusHandler.stop();
    }

    private void gravarRomaneio_Online(Romaneio_ent tab_romaneio_ent) {
        referenceDB_romaneio.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String numero_romaneio = String.valueOf(tab_romaneio_ent.getNum_rom());

                referenceDB_romaneio.child(numero_romaneio).setValue(tab_romaneio_ent);
                referenceDB_romaneio.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.warning(view, MOB008.this, "Erro",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private void gravarItem_Online(Item_romaneio_ent tab_item_romaneio_ent) {
        referenceDB_item.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String numero_romaneio = String.valueOf(tab_item_romaneio_ent.getNum_rom());
                String sequencia = tab_item_romaneio_ent.getNum_seq();

                referenceDB_item.child(numero_romaneio).child(sequencia).setValue(tab_item_romaneio_ent);
                referenceDB_item.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.warning(view, MOB008.this, "Erro",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    //Reseta a Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    // Define a ação dos botões da Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}