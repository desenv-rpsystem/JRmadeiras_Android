package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.LiberacaoAdapters.RegistroLiberacaoAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Lib_reg_nts;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class MOB003 extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    Boolean flag_online;

    RecyclerView w_registro;

    TextView w_text_registro;

    ArrayList<Lib_reg_nts> lista_registro;

    RegistroLiberacaoAdapter registroLiberacaoAdapter;

    String descricao_programa;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob003);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Lib_reg_nts");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        IniciarCampos();

        verificarStatusConexao();

    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
        } else {
            alertMessage("Erro ao verificar se há conexão!");
        }
    }

    private void IniciarCampos() {
        w_registro = (RecyclerView) findViewById(R.id.recycler_view_liberar_documento);

        w_text_registro = (TextView) findViewById(R.id.text_view_liberar_documento);

        lista_registro = new ArrayList<Lib_reg_nts>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);
    }

    private void verificarStatusConexao() {

        if (flag_online == true) {
            consultarDocumento();
        } else if (flag_online == false) {
            alertMessage("Atenção!\nSem conexão com a nuvem.");
        }
    }

    private void consultarDocumento() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_registro.clear();
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    for (DataSnapshot retornoBanco : retorno.getChildren()) {
                        Lib_reg_nts tab_registro = retornoBanco.getValue(Lib_reg_nts.class);
                        if (tab_registro.getDat_lib_doc().equals("") && tab_registro.getUsu_lib_doc().equals("")) {
                            lista_registro.add(tab_registro);
                        }
                    }
                }

                if (lista_registro.isEmpty()){
                    mostrarMensagemSemRegistro(true);
                } else {
                    mostrarMensagemSemRegistro(false);
                }

                Collections.sort(lista_registro);

                registroLiberacaoAdapter = new RegistroLiberacaoAdapter(MOB003.this, lista_registro);
                w_registro.setAdapter(registroLiberacaoAdapter);
                registroLiberacaoAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void mostrarMensagemSemRegistro(Boolean flag) {

        if (flag == true) {
            w_registro.setVisibility(View.GONE);

            w_text_registro.setVisibility(View.VISIBLE);
            w_text_registro.setText("Não há documentos para liberação!");
        } else {
            w_registro.setVisibility(View.VISIBLE);

            w_text_registro.setVisibility(View.GONE);
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
                Toasty.info(MOB003.this,
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

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB003.this);
        //alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
