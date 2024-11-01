package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters.MapaRetiraAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaEntrega;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaRetira;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class MOB006 extends AppCompatActivity {

    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    View view;
    AlertMessage alertMessage;

    Boolean flag_online;

    RecyclerView w_registro_mapa;

    TextView w_text_registro;

    ArrayList<Romaneio_ent> lista_romaneio_ent;

    MapaRetiraAdapter mapaRetiraAdapter;

    String descricao_programa;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob006);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Romaneio_ret");

        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        alertMessage = new AlertMessage(this);

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
            alertMessage.error(view, this, "Erro", "Não foi possível verificar se há conexão!");
        }
    }

    private void IniciarCampos() {

        w_registro_mapa = (RecyclerView) findViewById(R.id.recycler_view_mapa_entrega);

        w_text_registro = (TextView) findViewById(R.id.text_view_mapa_entrega);

        lista_romaneio_ent = new ArrayList<Romaneio_ent>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);

    }

    private void verificarStatusConexao() {

        if (flag_online == true) {
            BancoMapaRetira bancoMapaRetira = new BancoMapaRetira(this);
            bancoMapaRetira.limparBanco();

            consultarMapaRetira_Online();
        } else if (flag_online == false) {
            alertMessage.warning(view, MOB006.this, "Atenção", "Sem conexão com a nuvem");
        }
    }

    private void consultarMapaRetira_Online() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_romaneio_ent.clear();
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Romaneio_ent tab_romaneio = retornoBanco.getValue(Romaneio_ent.class);
                    if (tab_romaneio.getDat_lib_re() == null &&
                            tab_romaneio.getUsu_lib_re() == null &&
                            tab_romaneio.getHor_lib_re() == null) {
                        lista_romaneio_ent.add(tab_romaneio);
                    }
                }

                if (lista_romaneio_ent.isEmpty()) {
                    mostrarMensagemSemRegistro(true);
                } else {
                    mostrarMensagemSemRegistro(false);
                }

                Collections.sort(lista_romaneio_ent);

                mapaRetiraAdapter = new MapaRetiraAdapter(MOB006.this, lista_romaneio_ent);
                w_registro_mapa.setAdapter(mapaRetiraAdapter);
                mapaRetiraAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.warning(view, MOB006.this, "Erro",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private void mostrarMensagemSemRegistro(Boolean flag) {

        if (flag == true) {
            w_registro_mapa.setVisibility(View.GONE);

            w_text_registro.setVisibility(View.VISIBLE);
            w_text_registro.setText("Não há romaneios de retira!");
        } else {
            w_registro_mapa.setVisibility(View.VISIBLE);

            w_text_registro.setVisibility(View.GONE);
        }

    }

    private void consultarMapaEntrega_Offline() {

        BancoMapaEntrega bancoMapaEntrega = new BancoMapaEntrega(this);

        ArrayList<Romaneio_ent> retorno_romaneio_ent = bancoMapaEntrega.getRomaneio_Ent();

        lista_romaneio_ent.clear();

        try {

            for (int i = 0; i < retorno_romaneio_ent.size(); i++) {

                Romaneio_ent tab_romaneio_ent = new Romaneio_ent();

                Long num_seq = retorno_romaneio_ent.get(i).getNum_seq();
                Long num_reg_nts = retorno_romaneio_ent.get(i).getNum_reg_nts();
                String cod_ser = retorno_romaneio_ent.get(i).getCod_ser();
                String num_doc = retorno_romaneio_ent.get(i).getNum_doc();
                String nom_cli = retorno_romaneio_ent.get(i).getNom_cli();
                String end_cli = retorno_romaneio_ent.get(i).getEnd_cli();
                String num_end_cli = retorno_romaneio_ent.get(i).getNum_end_cli();
                String com_bai_cli = retorno_romaneio_ent.get(i).getCom_bai_cli();
                String nom_mun = retorno_romaneio_ent.get(i).getNom_mun();
                String cod_uni_fed = retorno_romaneio_ent.get(i).getCod_uni_fed();
                String cep_cli = retorno_romaneio_ent.get(i).getCep_cli();
                String fon_cli = retorno_romaneio_ent.get(i).getFon_cli();
                String fon_cli_2 = retorno_romaneio_ent.get(i).getFon_cli_2();
                String nom_ven = retorno_romaneio_ent.get(i).getNom_ven();
                String dat_lib_re = retorno_romaneio_ent.get(i).getDat_lib_re();
                String usu_lib_re = retorno_romaneio_ent.get(i).getUsu_lib_re();
                String hor_lib_re = retorno_romaneio_ent.get(i).getHor_lib_re();

                tab_romaneio_ent.setNum_seq(num_seq);
                tab_romaneio_ent.setNum_reg_nts(num_reg_nts);
                tab_romaneio_ent.setCod_ser(cod_ser);
                tab_romaneio_ent.setNum_doc(num_doc);
                tab_romaneio_ent.setNom_cli(nom_cli);
                tab_romaneio_ent.setEnd_cli(end_cli);
                tab_romaneio_ent.setNum_end_cli(num_end_cli);
                tab_romaneio_ent.setCom_bai_cli(com_bai_cli);
                tab_romaneio_ent.setNom_mun(nom_mun);
                tab_romaneio_ent.setCod_uni_fed(cod_uni_fed);
                tab_romaneio_ent.setCep_cli(cep_cli);
                tab_romaneio_ent.setFon_cli(fon_cli);
                tab_romaneio_ent.setFon_cli_2(fon_cli_2);
                tab_romaneio_ent.setNom_ven(nom_ven);
                tab_romaneio_ent.setDat_lib_re(dat_lib_re);
                tab_romaneio_ent.setUsu_lib_re(usu_lib_re);
                tab_romaneio_ent.setHor_lib_re(hor_lib_re);

                if (tab_romaneio_ent.getDat_lib_re().equals("") &&
                        tab_romaneio_ent.getUsu_lib_re().equals("") &&
                        tab_romaneio_ent.getHor_lib_re().equals("")) {
                    lista_romaneio_ent.add(tab_romaneio_ent);
                }

                if (lista_romaneio_ent.isEmpty()) {
                    alertSmall("Não há romaneios de retira!");
                }

                Collections.sort(lista_romaneio_ent);

                mapaRetiraAdapter = new MapaRetiraAdapter(MOB006.this, lista_romaneio_ent);
                w_registro_mapa.setAdapter(mapaRetiraAdapter);
                mapaRetiraAdapter.notifyDataSetChanged();

            }

        } catch (Exception ex) {
            System.out.println("consultarMapaEntrega_Offline: " + ex.toString());
        }

        bancoMapaEntrega.close();

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
                Toasty.info(MOB006.this,
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
        Toast.makeText(MOB006.this, s, Toast.LENGTH_SHORT).show();
    }

}