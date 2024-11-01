package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters.ItemAdapter;
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.AerodromoAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgenda;
import com.rp_grf.jrmadeiras.SQLite.BancoPrecoEstoque;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

/**
 * Consulta de Preço e Estoque
 * Autor: André Castro
 */

public class MOB001 extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoPrecoEstoque bancoPrecoEstoque;

    Boolean flag_online;

    EditText w_texto_pesquisa;
    EditText w_texto_marca;

    CardView w_botao_pesquisa;

    RecyclerView w_item;

    ArrayList<Item> lista_item;
    ItemAdapter itemAdapter;

    String descricao_programa;

    ProgressDialog dialog, dialogPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob001);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Item");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        verificarStatusConexao();

        cliqueBotaoPesquisar();

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
        w_texto_pesquisa = (EditText) findViewById(R.id.edit_text_pesquisar);
        w_texto_marca = (EditText) findViewById(R.id.edit_text_marca);

        w_botao_pesquisa = (CardView) findViewById(R.id.card_view_pesquisar);

        w_item = (RecyclerView) findViewById(R.id.recycler_view_preco_estoque);

        lista_item = new ArrayList<Item>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);
        dialogPesquisa = new ProgressDialog(this, R.style.DialogTheme);
    }

    private void verificarStatusConexao() {

        if (flag_online == true){
            consultarItem_Online();
        } else if (flag_online == false){
            consultarItem_Offline();
        }

    }

    private void consultarItem_Online() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_item.clear();
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    for (DataSnapshot retornoBanco : retorno.getChildren()) {
                        Item tab_item = retornoBanco.getValue(Item.class);
                        lista_item.add(tab_item);
                    }
                }

                Collections.sort(lista_item);

                itemAdapter = new ItemAdapter(MOB001.this, lista_item);
                w_item.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void cliqueBotaoPesquisar() {
        w_botao_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Fecha o teclado ao clicar no botão
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Método de pesquisar
                pesquisar(w_texto_pesquisa.getText().toString(),
                        w_texto_marca.getText().toString());

            }
        });
    }

    private void pesquisar(final String texto_pesquisa, final String texto_marca) {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        dialogPesquisa.show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                referenceDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista_item.clear();
                        for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                            for (DataSnapshot retornoBanco : retorno.getChildren()) {
                                Item tab_item = retornoBanco.getValue(Item.class);
                                String nom_ite = tab_item.getNom_ite();
                                String nom_mar = tab_item.getNom_mar();
                                String cod_ite = tab_item.getCod_ite().toString().trim();
                                if (pesquisaMultipla(texto_marca, nom_mar) == true) {
                                    if (pesquisaMultipla(texto_pesquisa, nom_ite) == true ||
                                            pesquisaMultipla(texto_pesquisa, cod_ite) == true)
                                        lista_item.add(tab_item);
                                }
                            }
                        }

                        Collections.sort(lista_item);

                        itemAdapter = new ItemAdapter(MOB001.this, lista_item);
                        w_item.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();

                        dialogPesquisa.dismiss();

                        if (lista_item.isEmpty()) {
                            alertSmall("Nenhum Registro Encontrado !!!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertMessage("Não foi possível acessar a base de dados !!!\n" +
                                "Favor reiniciar o aplicativo.");
                    }
                });
            }
        });
    }

    private boolean pesquisaMultipla(String texto_pesquisa, String texto_banco) {

        Boolean retorno = false;

        String[] array_separador = texto_pesquisa.split("[ *]+");


        for (int i = 0; i < array_separador.length; i++) {
            if (texto_banco.toLowerCase().contains(array_separador[i])) {
                retorno = true;
            } else {
                retorno = false;
                break;
            }
        }

        return retorno;
    }

    private void consultarItem_Offline(){
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();

        //Método de pesquisar
        pesquisar(w_texto_pesquisa.getText().toString(),
                w_texto_marca.getText().toString());

        String texto_pesquisa = w_texto_pesquisa.getText().toString();
        String texto_marca    = w_texto_marca.getText().toString();

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Item> retornoItem = bancoPrecoEstoque.getItem();

        lista_item.clear();

        try{

            System.out.println("retornoItem.size : " + retornoItem.size());

            for (int i = 0; i < retornoItem.size(); i++){

                Item tab_item = new Item();

                Long   cod_ite = retornoItem.get(i).getCod_ite();
                String nom_ite = retornoItem.get(i).getNom_ite();
                String ref_ite = retornoItem.get(i).getRef_ite();
                String nom_mar = retornoItem.get(i).getNom_mar();
                String cod_uni = retornoItem.get(i).getCod_uni();
                String val_uni = retornoItem.get(i).getVal_uni();
                String qtd_est = retornoItem.get(i).getQtd_est();

                tab_item.setCod_ite(cod_ite);
                tab_item.setNom_ite(nom_ite);
                tab_item.setRef_ite(ref_ite);
                tab_item.setNom_mar(nom_mar);
                tab_item.setCod_uni(cod_uni);
                tab_item.setVal_uni(val_uni);
                tab_item.setQtd_est(qtd_est);


                if ((validaPesquisa(texto_pesquisa, String.valueOf(cod_ite)) == true) ||
                        (validaPesquisa(texto_pesquisa, nom_ite) == true) ||
                        (validaPesquisa(texto_pesquisa, ref_ite) == true) ){

                    if (texto_marca != null){

                        if (validaPesquisa(texto_pesquisa, nom_mar) == true) {

                            lista_item.add(tab_item);

                        }

                    } else {

                        lista_item.add(tab_item);

                    }

                }
            }

            Collections.sort(lista_item);

            itemAdapter = new ItemAdapter(MOB001.this, lista_item);
            w_item.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();

            dialog.dismiss();

            bancoPrecoEstoque.close();

            if (lista_item.isEmpty()) {
                System.out.println("ENTROU LISTA ITEM VAZIO");
                alertSmall("Nenhum Item Encontrado !!!");
            }

        }catch (Exception ex){
            System.out.println("Construir Item: " + ex.toString());
        }

    }


    private boolean validaPesquisa(String texto_pesquisa, String texto_banco) {
        Boolean retorno = false;

        String[] array_separador = texto_pesquisa.toLowerCase().split("[ *]+");

        try {
            for (int i = 0; i < array_separador.length; i++) {
                if (texto_banco.toLowerCase().contains(array_separador[i])) {
                    retorno = true;
                } else {
                    retorno = false;
                    break;
                }
            }
        } catch (Exception ex) {
            alertSmall(ex.toString());
        }

        return retorno;
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
                Toasty.info(MOB001.this,
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

    private void alertSmall(String s) {
        Toast.makeText(MOB001.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB001.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
