package com.rp_grf.jrmadeiras.Telas.Menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
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
import com.rp_grf.jrmadeiras.Adapter.MenuAdapters.ProgramasAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoProgramas;
import com.rp_grf.jrmadeiras.Tabelas.Programas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class ProgramasActivity extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoProgramas bancoProgramas;

    Boolean flag_online;

    RecyclerView w_programas;

    ArrayList<Programas> lista_programas;
    ProgramasAdapter programasAdapter;

    String codigo_modulo;
    String descricao_modulo;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        codigo_modulo = getIntent().getStringExtra("cod_mod");
        descricao_modulo = getIntent().getStringExtra("nom_mod");

        bancoProgramas = new BancoProgramas(this);

        getSupportActionBar().setTitle(descricao_modulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Programas");

        //Chama o progressDialog
        dialog = new ProgressDialog(this, R.style.DialogTheme);
        dialog.setMessage("Carregando Menu,\npor favor aguarde...");
        dialog.setCancelable(false);
        dialog.show();

        iniciarCampos();

        //Chama os dados temporarios da flag de "conectado?"
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

    }

    private void iniciarCampos() {
        w_programas = (RecyclerView) findViewById(R.id.recycler_view_programas);

        lista_programas = new ArrayList<Programas>();
    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
            preencheListaProgramas_Online();
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
            preencheListaProgramas_Offline();
        } else {
            alertMessage("Erro ao verificar se há conexão!");
        }
    }

    private void preencheListaProgramas_Online() {
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_programas.clear();
                //Pega os programas cadastrados no banco e preenche a lista do layout
                //Chama os dados temporarios do usuário
                SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                String cod_usu_tmp = settings.getString("cod_usu_tmp", "");
                //Consulta o banco Firebase
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Programas tab_programas = retornoBanco.getValue(Programas.class);
                    //Verifica se o programa faz parte do módulo
                    if (tab_programas.getCod_mod().equals(codigo_modulo)) {
                        //Verifica a permissão antes de preencher a lista com o programa
                        if (tab_programas.getPer_prg().contains(cod_usu_tmp)) {
                            lista_programas.add(tab_programas);
                        }
                    }
                }

                Collections.sort(lista_programas); //Ordena a lista de programas pela sequência

                programasAdapter = new ProgramasAdapter(ProgramasActivity.this, lista_programas);
                w_programas.setAdapter(programasAdapter);
                programasAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível retornar os programas !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    private void preencheListaProgramas_Offline() {
        try {
            ArrayList<Programas> retornoProgramas = bancoProgramas.getProgramas();

            lista_programas.clear();

            //Chama os dados temporarios do usuário
            SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
            String cod_usu_tmp = settings.getString("cod_usu_tmp", "");

            //Percorre o retorno
            for (int i = 0; i < retornoProgramas.size(); i++) {
                if (retornoProgramas.get(i).getCod_mod().equals(codigo_modulo)) {
                    //Verifica se o usuario tem permissão no programa
                    if (retornoProgramas.get(i).getPer_prg().contains(cod_usu_tmp)) {
                        lista_programas.add(retornoProgramas.get(i)); //se tiver permissão, é adicionado à lista
                    }
                }
            }

            Collections.sort(lista_programas); //Ordena a lista de programas pela sequência

            programasAdapter = new ProgramasAdapter(ProgramasActivity.this, lista_programas);
            w_programas.setAdapter(programasAdapter);
            programasAdapter.notifyDataSetChanged();

            dialog.dismiss();

        } catch (Exception ex) {
            alertSmall("Erro em preencheListaProgramas_Offline: " + ex.toString());
            System.out.println("Erro em preencheListaProgramas_Offline: " + ex.toString());
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
                Toasty.info(ProgramasActivity.this,
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
        Toast.makeText(ProgramasActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ProgramasActivity.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
