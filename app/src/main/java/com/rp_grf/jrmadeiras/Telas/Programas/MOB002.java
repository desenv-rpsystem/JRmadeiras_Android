package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.rp_grf.jrmadeiras.Utils.FocusHandler;
import com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters.ItemAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoPrecoEstoque;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Tabelas.Item_cod_bar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MOB002 extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_item;
    private static DatabaseReference referenceDB_item_cod_bar;

    //SQLite - Banco
    private BancoPrecoEstoque bancoPrecoEstoque;

    Boolean flag_online;

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

    RecyclerView w_item;

    ArrayList<Item> lista_item;
    ArrayList<Item_cod_bar> lista_item_cod_bar;
    ItemAdapter itemAdapter;

    String descricao_programa;

    ProgressDialog dialog, dialogPesquisa;

    TextView w_codigo;

    Integer contador;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob002);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contador = 0;

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scannerView = new ZXingScannerView(this);
        focusHandler = new FocusHandler(new Handler(), scannerView);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_item = firebaseDB.getReference("Item");
        referenceDB_item_cod_bar = firebaseDB.getReference("Item_cod_bar");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        cliqueBotao();

        desabilitarBotoes(false);

    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
            System.out.println("teste flagtrue" + flag_online);
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
            System.out.println("teste flagfalse" + flag_online);
        } else {
            alertMessage("Erro ao verificar se há conexão!");
        }
    }

    private void iniciarCampos() {
        w_layout = (ConstraintLayout) findViewById(R.id.layout_mob002);

        w_botao_leitor = (CardView) findViewById(R.id.card_view_botao_leitor);
        w_botao_cancelar = (CardView) findViewById(R.id.card_view_botao_cancelar);
        w_botao_foco = (CardView) findViewById(R.id.card_view_botao_foco);

        w_icone_leitor = (ImageView) findViewById(R.id.image_view_barcode_leitor);
        w_icone_cancelar = (ImageView) findViewById(R.id.image_view_barcode_cancelar) ;
        w_icone_foco = (ImageView) findViewById(R.id.image_view_barcode_foco) ;

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_barcode);
        w_item_vazio = (FrameLayout) findViewById(R.id.layout_item_vazio);

        w_item = (RecyclerView) findViewById(R.id.recycler_view_preco_estoque);

        lista_item = new ArrayList<Item>();
        lista_item_cod_bar = new ArrayList<Item_cod_bar>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);
        dialogPesquisa = new ProgressDialog(this, R.style.DialogTheme);

        w_codigo = (TextView) findViewById(R.id.text_view_codigo_barras);
    }

    // Cria o botão na Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botao_offline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Define a ação dos botões da Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB002.this,
                        "Atenção!\nSem conexão com a nuvem.",
                        Toast.LENGTH_SHORT, true).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Oculta o botão se retornar que foi possível conectar com a nuvem
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (flag_online == true) {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(false).setEnabled(false);
        } else {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(true).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void cliqueBotao() {
        w_botao_leitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                w_codigo.setText("0");
                scannerView.setAutoFocus(false);
                frameLayout.addView(scannerView);
                desabilitarBotoes(true);
            }
        });

        w_botao_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.removeView(scannerView);
                scannerView.resumeCameraPreview(MOB002.this);
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

        if(flag != true) {
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


    @Override
    public void handleResult(Result result) {

        if (flag_online == true){
            consultarCodigoBarras_Online(result);
        } else if (flag_online == false){
            consultarCodigoBarras_Offline(result);
        }

        frameLayout.removeView(scannerView);
        scannerView.resumeCameraPreview(this);
        focusHandler.stop();

        desabilitarBotoes(false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        focusHandler.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.setAutoFocus(false);
        scannerView.startCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
        focusHandler.stop();
    }


    private void consultarCodigoBarras_Online(final Result result) {
        dialogPesquisa.setMessage("Consultando,\npor favor aguarde...");
        dialogPesquisa.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        dialogPesquisa.setCancelable(false);
        dialogPesquisa.show();
        referenceDB_item_cod_bar.child(result.getText())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                            Item_cod_bar tab_item_cod_bar = retornoBanco.getValue(Item_cod_bar.class);

                            if (tab_item_cod_bar.getCod_bar_ite().equals(result.getText())) {
                                w_codigo.setText(tab_item_cod_bar.getCod_ite().toString());
                                w_item_vazio.setVisibility(View.GONE);
                                break;
                            }

                        }



                        consultarItem_Online(w_codigo.getText().toString(), result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertMessage("Não foi possível acessar a base de dados !!!\n" +
                                "Favor reiniciar o aplicativo.");
                    }
                });
    }

    private void consultarCodigoBarras_Offline(final Result result){
        dialogPesquisa.setMessage("Consultando,\npor favor aguarde...");
        dialogPesquisa.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        dialogPesquisa.setCancelable(false);
        dialogPesquisa.show();

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Item_cod_bar> retornoItem_cod_bar = bancoPrecoEstoque.getItem_cod_bar();

        lista_item_cod_bar.clear();

        try{

            System.out.println("retornoItem_cod_bar.size : " + retornoItem_cod_bar.size());

            for (int i = 0; i < retornoItem_cod_bar.size(); i++){

                Item_cod_bar tab_item_cod_bar = new Item_cod_bar();

                String cod_bar_ite = retornoItem_cod_bar.get(i).getCod_bar_ite();
                Long   cod_ite     = retornoItem_cod_bar.get(i).getCod_ite();

                tab_item_cod_bar.setCod_bar_ite(cod_bar_ite);
                tab_item_cod_bar.setCod_ite(cod_ite);

                String resultado = result.getText();

                if (tab_item_cod_bar.getCod_bar_ite().equals(result.getText())) {
                    w_codigo.setText(tab_item_cod_bar.getCod_ite().toString());
                    w_item_vazio.setVisibility(View.GONE);
                    break;
                }
            }

            consultarItem_Offline(w_codigo.getText().toString(), result);

            Collections.sort(lista_item_cod_bar);

            dialogPesquisa.dismiss();

            bancoPrecoEstoque.close();

        }catch (Exception ex){
            System.out.println("Construir Item_cod_bar: " + ex.toString());
        }

    }

    private void consultarItem_Online(final String cod_ite, final Result result) {
        referenceDB_item.child(cod_ite)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista_item.clear();
                        for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                            Item tab_item = retornoBanco.getValue(Item.class);
                            if (tab_item.getCod_ite().toString().equals(cod_ite)) {
                                lista_item.add(tab_item);
                                break;
                            }
                        }

                        itemAdapter = new ItemAdapter(MOB002.this, lista_item);
                        w_item.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();

                        dialogPesquisa.dismiss();

                        if (lista_item.isEmpty()) {
                            w_item_vazio.setVisibility(View.VISIBLE);
                            alertMessage("Nenhum Item Encontrado !!!\n\nResultado da Leitura: " + result.getText());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertMessage("Não foi possível acessar a base de dados !!!\n" +
                                "Favor reiniciar o aplicativo.");
                    }
                });
    }

    private void consultarItem_Offline(final String cod_ite, final Result result){

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Item> retornoItem = bancoPrecoEstoque.getItem();

        lista_item.clear();

        try{

            System.out.println("retornoItem.size : " + retornoItem.size());

            for (int i = 0; i < retornoItem.size(); i++){

                Item tab_item = new Item();

                Long cod_ite_pe = retornoItem.get(i).getCod_ite();  /*pe=Preço e Estoque*/
                String nom_ite  = retornoItem.get(i).getNom_ite();
                String ref_ite  = retornoItem.get(i).getRef_ite();
                String nom_mar  = retornoItem.get(i).getNom_mar();
                String cod_uni  = retornoItem.get(i).getCod_uni();
                String val_uni  = retornoItem.get(i).getVal_uni();
                String qtd_est  = retornoItem.get(i).getQtd_est();

                tab_item.setCod_ite(cod_ite_pe);
                tab_item.setNom_ite(nom_ite);
                tab_item.setRef_ite(ref_ite);
                tab_item.setNom_mar(nom_mar);
                tab_item.setCod_uni(cod_uni);
                tab_item.setVal_uni(val_uni);
                tab_item.setQtd_est(qtd_est);

                if (tab_item.getCod_ite().toString().equals(cod_ite)) {
                    lista_item.add(tab_item);
                    break;
                }

            }

            dialogPesquisa.dismiss();

            itemAdapter = new ItemAdapter(MOB002.this, lista_item);
            w_item.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();

            if (lista_item.isEmpty()) {
                w_item_vazio.setVisibility(View.VISIBLE);
                alertMessage("Nenhum Item Encontrado !!!\n\nResultado da Leitura: " + result.getText());
            }

        }catch (Exception ex){
            System.out.println("Construir Item: " + ex.toString());
        }

    }

    private void alertSmall(String s) {
        Toast.makeText(MOB002.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB002.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}
