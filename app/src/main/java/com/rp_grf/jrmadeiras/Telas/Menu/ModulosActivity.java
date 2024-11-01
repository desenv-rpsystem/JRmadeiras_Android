package com.rp_grf.jrmadeiras.Telas.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.MenuAdapters.ModulosAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoModulos;
import com.rp_grf.jrmadeiras.Tabelas.Modulos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class ModulosActivity extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoModulos bancoModulos;

    Boolean flag_online;

    TextView w_descricao;

    RecyclerView w_modulos;

    ArrayList<Modulos> lista_modulos;
    ModulosAdapter modulosAdapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulos);
        getSupportActionBar().setTitle("Módulos");

        bancoModulos = new BancoModulos(this);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Modulos"); //Define a referencia como sendo da tabela Modulos

        //Chama o progressDialog
        dialog = new ProgressDialog(this, R.style.DialogTheme);
        dialog.setMessage("Carregando Modulos,\npor favor aguarde...");
        dialog.show();

        iniciarCampos();

        //Chama os dados temporarios da flag de "conectado?"
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);
    }

    private void iniciarCampos(){
        w_descricao = (TextView) findViewById(R.id.nom_mod);

        w_modulos = (RecyclerView) findViewById(R.id.recycler_view_modulos);

        lista_modulos = new ArrayList<Modulos>();
    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login){
        if (flag_login.equals("Sim")) {
            flag_online = true;
            preencheListaModulos_Online();
        } else if (flag_login.equals("Nao")){
            flag_online = false;
            preencheListaModulos_Offline();
        } else {
            alertMessage("Erro ao verificar se há conexão!");
        }
    }

    private void preencheListaModulos_Online() {
        try {

            referenceDB.addValueEventListener(new ValueEventListener() {
                //Pega os modulos cadastrados no banco e preenche a grid do layout
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lista_modulos.clear();

                    //Chama os dados temporarios do usuário
                    SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                    String cod_usu_tmp = settings.getString("cod_usu_tmp", "");

                    //Consulta o banco Firebase
                    for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                        //Popula a classe Modulos com os dados do banco
                        Modulos tab_modulos = retornoBanco.getValue(Modulos.class);
                        //Verifica a permissão antes de preencher a lista com o modulo
                        if (tab_modulos.getPer_mod().contains(cod_usu_tmp)) {
                            lista_modulos.add(tab_modulos);
                        }
                    }
                    Collections.sort(lista_modulos); //Ordena a lista de modulos pela sequência

                    modulosAdapter = new ModulosAdapter(ModulosActivity.this, lista_modulos);

                    orientation(modulosAdapter);

                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alertMessage("Não foi possível acessar a base de dados !!!\n" +
                            "Favor reiniciar o aplicativo.");
                }
            });
        } catch (Exception ex) {
            alertMessage("JAVA: " + ex.toString());
        }
    }

    private void preencheListaModulos_Offline(){
        try {
            ArrayList<Modulos> retornoModulos = bancoModulos.getModulos();

            lista_modulos.clear();

            //Chama os dados temporarios do usuário
            SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
            String cod_usu_tmp = settings.getString("cod_usu_tmp", "");

            //Percorre o retorno
            for (int i = 0; i < retornoModulos.size(); i++) {
                //Verifica se o usuario tem permissão no modulo
                if (retornoModulos.get(i).getPer_mod().contains(cod_usu_tmp)){
                    lista_modulos.add(retornoModulos.get(i)); //se tiver permissão, é adicionado à lista
                }
            }

            Collections.sort(lista_modulos); //Ordena a lista de modulos pela sequência

            modulosAdapter = new ModulosAdapter(ModulosActivity.this, lista_modulos);

            orientation(modulosAdapter);

            dialog.dismiss();
        } catch (Exception ex) {
            alertSmall("Erro: " + ex.toString());
        }
    }

    //Altera o número de colunas do GridLayout dependendo da orientação do aparelho
    private void orientation(ModulosAdapter modulosAdapter) {
        if (ModulosActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            w_modulos.setLayoutManager(new GridLayoutManager(w_modulos.getContext(), 2));
            w_modulos.setAdapter(modulosAdapter);
            modulosAdapter.notifyDataSetChanged();
        } else {
            w_modulos.setLayoutManager(new GridLayoutManager(w_modulos.getContext(), 3));
            w_modulos.setAdapter(modulosAdapter);
            modulosAdapter.notifyDataSetChanged();
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
            //Botão voltar
            case android.R.id.home:
                super.onBackPressed();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(ModulosActivity.this,
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
        Toast.makeText(ModulosActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ModulosActivity.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
