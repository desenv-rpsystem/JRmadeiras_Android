package com.rp_grf.jrmadeiras.Telas.Programas;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters.DepositoAdapter;
import com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters.ItemAdapter;
import com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters.PedidoCompraAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoPrecoEstoque;
import com.rp_grf.jrmadeiras.Tabelas.Deposito;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Tabelas.Item_dep;
import com.rp_grf.jrmadeiras.Tabelas.Item_pdc;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class MOB001_A extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_item_dep;
    private static DatabaseReference referenceDB_deposito;
    private static DatabaseReference referenceDB_item_pdc;

    Boolean flag_online;

    TextView w_nom_ite, w_ref_ite, w_nom_mar, w_cod_uni, w_val_uni;

    RecyclerView w_deposito;
    RecyclerView w_pdc;

    ArrayList<Item_dep> lista_item_dep;
    ArrayList<Deposito> lista_deposito;
    ArrayList<Deposito> lista_deposito_aux;
    ArrayList<Item_pdc> lista_item_pdc;

    DepositoAdapter depositoAdapter;
    PedidoCompraAdapter pedidoCompraAdapter;

    Long codigo_item;
    String cod_ite, nom_ite, ref_ite, nom_mar, cod_uni, val_uni;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob001_a);

        codigo_item = Long.parseLong(getIntent().getStringExtra("cod_ite"));
        cod_ite = getIntent().getStringExtra("cod_ite");
        nom_ite = getIntent().getStringExtra("nom_ite");
        ref_ite = getIntent().getStringExtra("ref_ite");
        nom_mar = getIntent().getStringExtra("nom_mar");
        cod_uni = getIntent().getStringExtra("cod_uni");
        val_uni = getIntent().getStringExtra("val_uni");

        getSupportActionBar().setTitle("Item " + codigo_item);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_item_dep = firebaseDB.getReference("Item_dep");
        referenceDB_deposito = firebaseDB.getReference("Deposito");
        referenceDB_item_pdc = firebaseDB.getReference("Item_pdc");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        verificarStatusConexao();

        w_nom_ite.setText(nom_ite);
        w_ref_ite.setText(ref_ite);
        w_nom_mar.setText(nom_mar);
        w_cod_uni.setText(cod_uni);
        w_val_uni.setText(val_uni);

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
        w_nom_ite = (TextView) findViewById(R.id.nom_ite);
        w_ref_ite = (TextView) findViewById(R.id.ref_ite);
        w_nom_mar = (TextView) findViewById(R.id.nom_mar);
        w_cod_uni = (TextView) findViewById(R.id.cod_uni);
        w_val_uni = (TextView) findViewById(R.id.val_uni);

        w_deposito = (RecyclerView) findViewById(R.id.recycler_view_deposito);
        w_pdc = (RecyclerView) findViewById(R.id.recycler_view_pdc);

        lista_item_dep = new ArrayList<Item_dep>();
        lista_deposito = new ArrayList<Deposito>();
        lista_deposito_aux = new ArrayList<Deposito>();
        lista_item_pdc = new ArrayList<Item_pdc>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);
    }

    private void verificarStatusConexao() {

        if (flag_online == true) {
            consultarItem_dep_Online();
            consultarPedidoCompra_Online();
        } else if (flag_online == false) {
            consultarItem_dep_Offline();
            consultarPedidoCompra_Offline();
        }

    }

    private interface OnGetDataListener {
        void callListaDeposito(ArrayList<Deposito> i_lista_deposito);
    }

    private void consultarItem_dep_Online() {
        referenceDB_item_dep.child(cod_ite).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_item_dep.clear();
                lista_deposito.clear();
                for (final DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Item_dep tab_item_dep = retornoBanco.getValue(Item_dep.class);
                    if (tab_item_dep.getCod_ite().toString().equals(cod_ite)) {
                        Double quantidade_estoque = Double.parseDouble(tab_item_dep.getQtd_est());
                        if (quantidade_estoque != Double.parseDouble("0")) {
                            lista_item_dep.add(tab_item_dep);
                            final Long cod_dep = tab_item_dep.getCod_dep();
                            consultarDeposito_Online(cod_dep, new OnGetDataListener() {
                                @Override
                                public void callListaDeposito(ArrayList<Deposito> i_lista_deposito) {
                                    for (int i = 0; i < i_lista_deposito.size(); i++) {
                                        lista_deposito.add(i_lista_deposito.get(i));
                                    }
                                }
                            });
                        }
                    }
                }

                depositoAdapter = new DepositoAdapter(MOB001_A.this, lista_item_dep, lista_deposito);
                w_deposito.setAdapter(depositoAdapter);
                depositoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

        w_deposito.setNestedScrollingEnabled(false);
    }

    private void consultarItem_dep_Offline() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Item_dep> retornoItem_dep = bancoPrecoEstoque.getItem_dep();

        lista_item_dep.clear();
        lista_deposito.clear();

        try {

            System.out.println("retornoItem_dep.size : " + retornoItem_dep.size());

            for (int i = 0; i < retornoItem_dep.size(); i++) {

                Item_dep tab_item_dep = new Item_dep();

                Long cod_ite = retornoItem_dep.get(i).getCod_ite();
                Long cod_dep = retornoItem_dep.get(i).getCod_dep();
                String qtd_est = retornoItem_dep.get(i).getQtd_est();
                String nom_dep = retornoItem_dep.get(i).getNom_dep();

                tab_item_dep.setCod_ite(cod_ite);
                tab_item_dep.setCod_dep(cod_dep);
                tab_item_dep.setQtd_est(qtd_est);
                tab_item_dep.setNom_dep(nom_dep);

                if (tab_item_dep.getCod_ite().toString().equals(this.cod_ite)) {
                    Double quantidade_estoque = Double.parseDouble(tab_item_dep.getQtd_est());
                    if (quantidade_estoque != Double.parseDouble("0")) {
                        lista_item_dep.add(tab_item_dep);
                        final Long cod_dep_ite = tab_item_dep.getCod_dep();
                        consultarDeposito_Offline(cod_dep_ite, new OnGetDataListener() {
                            @Override
                            public void callListaDeposito(ArrayList<Deposito> i_lista_deposito) {
                                for (int i = 0; i < i_lista_deposito.size(); i++) {
                                    lista_deposito.add(i_lista_deposito.get(i));
                                }
                            }
                        });
                    }
                }

            }

            Collections.sort(lista_item_dep);

            depositoAdapter = new DepositoAdapter(MOB001_A.this, lista_item_dep, lista_deposito);
            w_deposito.setAdapter(depositoAdapter);
            depositoAdapter.notifyDataSetChanged();

            dialog.dismiss();

            bancoPrecoEstoque.close();

            if (lista_item_dep.isEmpty()) {
                System.out.println("ENTROU LISTA Item_dep VAZIO");
                alertSmall("Nenhum Item_dep Encontrado !!!");
            }

        } catch (Exception ex) {
            System.out.println("Construir Item_dep: " + ex.toString());
        }

    }


    private void consultarDeposito_Online(final Long cod_dep, final OnGetDataListener listener) {
        referenceDB_deposito.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_deposito_aux.clear();
                for (final DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Deposito tab_deposito = retornoBanco.getValue(Deposito.class);
                    if (tab_deposito.getCod_dep().toString().equals(cod_dep.toString())) {
                        lista_deposito_aux.add(tab_deposito);
                    }
                }
                listener.callListaDeposito(lista_deposito_aux);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void consultarDeposito_Offline(final Long cod_dep, final OnGetDataListener listener){
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Deposito> retornoDeposito = bancoPrecoEstoque.getDeposito();

        lista_deposito_aux.clear();

        try{

            for (int i = 0; i < retornoDeposito.size(); i++){

                Deposito tab_deposito = new Deposito();

                Long   cod_dep_ite = retornoDeposito.get(i).getCod_dep();
                String nom_dep     = retornoDeposito.get(i).getNom_dep();

                tab_deposito.setCod_dep(cod_dep_ite);
                tab_deposito.setNom_dep(nom_dep);

                if (tab_deposito.getCod_dep().toString().equals(cod_dep.toString())) {
                    lista_deposito_aux.add(tab_deposito);
                }
            }

            listener.callListaDeposito(lista_deposito_aux);

            dialog.dismiss();

            bancoPrecoEstoque.close();

        }catch (Exception ex){
            System.out.println("Construir Deposito: " + ex.toString());
        }

    }

    private void consultarPedidoCompra_Online() {
        referenceDB_item_pdc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_item_pdc.clear();
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    for (DataSnapshot retorno : retornoBanco.getChildren()) {
                        Item_pdc tab_item_pdc = retorno.getValue(Item_pdc.class);
                        if (tab_item_pdc.getCod_ite().toString().equals(cod_ite)) {
                            Double quantidade_pendente = Double.parseDouble(tab_item_pdc.getQtd_pen());
                            if (quantidade_pendente != Double.parseDouble("0")) {
                                lista_item_pdc.add(tab_item_pdc);
                            }
                        }
                    }
                }

                Collections.sort(lista_item_pdc);

                pedidoCompraAdapter = new PedidoCompraAdapter(MOB001_A.this, lista_item_pdc);
                w_pdc.setAdapter(pedidoCompraAdapter);
                pedidoCompraAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

        w_pdc.setNestedScrollingEnabled(false);
    }

    private void consultarPedidoCompra_Offline() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();

        BancoPrecoEstoque bancoPrecoEstoque = new BancoPrecoEstoque(this);

        ArrayList<Item_pdc> retornoItem_pdc = bancoPrecoEstoque.getItem_pdc();

        lista_item_pdc.clear();

        try {

            System.out.println("retornoItem_dep.size : " + retornoItem_pdc.size());

            for (int i = 0; i < retornoItem_pdc.size(); i++) {

                Item_pdc tab_item_pdc = new Item_pdc();

                Long   cod_ite = retornoItem_pdc.get(i).getCod_ite();
                String num_pdc = retornoItem_pdc.get(i).getNum_pdc();
                String dat_ent = retornoItem_pdc.get(i).getDat_ent();
                String qtd_pen = retornoItem_pdc.get(i).getQtd_pen();

                tab_item_pdc.setCod_ite(cod_ite);
                tab_item_pdc.setNum_pdc(num_pdc);
                tab_item_pdc.setDat_ent(dat_ent);
                tab_item_pdc.setQtd_pen(qtd_pen);


                if (tab_item_pdc.getCod_ite().toString().equals(this.cod_ite)) {
                    Double quantidade_pendente = Double.parseDouble(tab_item_pdc.getQtd_pen());
                    if (quantidade_pendente != Double.parseDouble("0")) {
                        lista_item_pdc.add(tab_item_pdc);
                    }
                }

            }

            Collections.sort(lista_item_pdc);

            pedidoCompraAdapter = new PedidoCompraAdapter(MOB001_A.this, lista_item_pdc);
            w_pdc.setAdapter(pedidoCompraAdapter);
            pedidoCompraAdapter.notifyDataSetChanged();

            dialog.dismiss();

            bancoPrecoEstoque.close();

            if (lista_item_dep.isEmpty()) {
                System.out.println("ENTROU LISTA Item_pdc VAZIO");
                alertSmall("Nenhum Item_pdc Encontrado !!!");
            }

        } catch (Exception ex) {
            System.out.println("Construir Item_pdc: " + ex.toString());
        }

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
                Toasty.info(MOB001_A.this,
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
        Toast.makeText(MOB001_A.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB001_A.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}

