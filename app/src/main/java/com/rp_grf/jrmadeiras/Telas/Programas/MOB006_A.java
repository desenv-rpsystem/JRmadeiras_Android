package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters.ItemRetiraAdapter;
import com.rp_grf.jrmadeiras.Fragments.AssinaturaFragment;
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.Interfaces.RetornaQuantidade;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAssinatura;
import com.rp_grf.jrmadeiras.SQLite.BancoFotos;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaRetira;
import com.rp_grf.jrmadeiras.Tabelas.Fotos;
import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * Itens do Mapa de Retira
 */

public class MOB006_A extends AppCompatActivity {

    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_romaneio_liberado;
    private static DatabaseReference referenceDB_romaneio;
    private static DatabaseReference referenceDB_item_romaneio;

    ArrayList<Item_romaneio_ent> item_romaneio_ent_atualizado;

    View view;
    AlertMessage alertMessage;

    boolean flag_online;

    boolean flag_liberado;

    boolean flag_assinatura_enviada = false;

    boolean flag_cancelado = false;

    RecyclerView w_registro_mapa;

    ArrayList<Romaneio_ent> lista_romaneio_ent;
    ArrayList<Item_romaneio_ent> lista_item_romaneio_ent;

    ItemRetiraAdapter itemRetiraAdapter;

    TextView w_serie;
    TextView w_documento;
    TextView w_sequencia;

    Button w_btn_confirmar;

    String descricao_programa;

    String numero_romaneio;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob006_a);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        descricao_programa = getIntent().getStringExtra("des_prg");

        numero_romaneio = getIntent().getStringExtra("num_rom");

        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        alertMessage = new AlertMessage(this);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_romaneio_liberado = firebaseDB.getReference("Romaneio_lib");
        referenceDB_romaneio = firebaseDB.getReference("Romaneio_ret");
        referenceDB_item_romaneio = firebaseDB.getReference("Item_romaneio_ret");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        cliqueBotao();

        verificarStatusConexao();

        iniciarDocumento();

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

    private void limparAssinaturas() {
        BancoFotos bancoFotos = new BancoFotos(this);

        bancoFotos.limparBanco();

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        bancoAssinatura.limparBanco();
    }

    private void iniciarCampos() {

        w_registro_mapa = (RecyclerView) findViewById(R.id.recycler_view_confirmar_mapa_entrega);

        w_btn_confirmar = (Button) findViewById(R.id.button_confirmar_entrega);

        item_romaneio_ent_atualizado = new ArrayList<>();

        lista_item_romaneio_ent = new ArrayList<>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);

    }

    private void cliqueBotao() {
        w_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarAssinatura() == true) {

                    if (flag_online == true) {

                        enviarFotos();
                        enviarAssinaturas();

                        limparAssinaturas();

                        finalizarRomaneio();

                    } else {
                        alertMessage.warning(view, MOB006_A.this, "Atenção",
                                "Não foi possível enviar a assinatura!\n" +
                                        "Verifique sua conexão e tente novamente.");
                    }

                } else {
                    alertMessage.warning(view, MOB006_A.this, "Atenção",
                            "É obrigatória a assinatura do cliente!");
                }
            }
        });
    }

    private void finalizarRomaneio() {
        if (flag_assinatura_enviada == true) {
            gravarRomaneio_liberado();
            gravarItem_Romaneio_Online();

            if (flag_cancelado) {
                Toasty.error(MOB006_A.this,
                        "Romaneio Cancelado!",
                        Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.success(MOB006_A.this,
                        "Entrega Confirmada!",
                        Toast.LENGTH_SHORT, true).show();
            }


            transformarConfirmarEmFinalizar();
         /*   setResult(RESULT_OK);
            finish();*/
        } else {
            alertMessage.info(view, MOB006_A.this, "Sem Conexão com a Nuvem",
                    "Não foi possível confirmar a entrega!\n" +
                            "Tente reiniciar o aplicativo.");
        }

    }

    private void iniciarDocumento() {
        w_serie = (TextView) findViewById(R.id.cod_ser_item_mapa_entrega);
        w_serie.setText(getIntent().getStringExtra("cod_ser"));

        w_documento = (TextView) findViewById(R.id.num_doc_item_mapa_entrega);
        w_documento.setText("Documento " + getIntent().getStringExtra("num_doc"));

        w_sequencia = (TextView) findViewById(R.id.num_seq_item_mapa_entrega);
        w_sequencia.setText("Seq. " + getIntent().getStringExtra("num_seq"));
    }

    private void transformarConfirmarEmFinalizar() {
        w_btn_confirmar.setText("Finalizar");
        w_btn_confirmar.setBackgroundColor(MOB006_A.this.getResources().getColor(R.color.errorColor));
        w_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    //Recebe a data atual no celular e retorna como String
    private String retornarData() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_atual = sdf.format(date);

        //String data_atual = DateFormat.getDateInstance().format(calendar.getTime());
        //data_atual.replace(".", "/");

        return data_atual;
    }

    //Recebe a hora atual no celular e retorna como String
    private String retornarHora() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String hora_atual = sdf.format(date);

        return hora_atual;
    }

    private void verificarStatusConexao() {

        if (flag_online == true) {
            verificarBanco();
        } else if (flag_online == false) {
            alertMessage.warning(view, this, "Atenção",
                    "Sem conexão com a nuvem!\n" +
                            "Reinicie o aplicativo e tente novamente.");
        }

    }

    //Verifica se o banco sqlite está preenchido
    private void verificarBanco() {
        BancoMapaRetira bancoMapaRetira = new BancoMapaRetira(this);
        ArrayList<Item_romaneio_ent> lista_item = bancoMapaRetira.getItem_Romaneio_Ent();

        if (lista_item.isEmpty()) {
            System.out.println("verificarBanco empty");
            consultarItemMapaRetira_Online();
        } else {
            System.out.println("verificarBanco_" + lista_item.size());
            consultarItemMapaRetira_Local();
        }

        bancoMapaRetira.close();
    }

    //Consulta na tabela item_romaneio_ret na nuvem
    private void consultarItemMapaRetira_Online() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB_item_romaneio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_item_romaneio_ent.clear();
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    for (DataSnapshot retornoSequencia : retornoBanco.getChildren()) {
                        Item_romaneio_ent tab_item_romaneio = retornoSequencia.getValue(Item_romaneio_ent.class);

                        String num_rom = String.valueOf(tab_item_romaneio.getNum_rom());

                        if (num_rom.equals(numero_romaneio))
                            lista_item_romaneio_ent.add(tab_item_romaneio);

                    }
                }

                try {
                    if (lista_item_romaneio_ent.isEmpty()) {
                        alertMessage.info(view, MOB006_A.this, "Romaneio de retira", "Não há itens no pedido!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Collections.sort(lista_item_romaneio_ent);

                itemRetiraAdapter = new ItemRetiraAdapter(MOB006_A.this, lista_item_romaneio_ent, retornaQuantidade);
                w_registro_mapa.setAdapter(itemRetiraAdapter);
                itemRetiraAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB006_A.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    //Consulta no BancoMapaRetira a tabela Item_romaneio_ret
    private void consultarItemMapaRetira_Local() {
        BancoMapaRetira bancoMapaRetira = new BancoMapaRetira(this);

        ArrayList<Item_romaneio_ent> retorno_item_romaneio_ent = bancoMapaRetira.getItem_Romaneio_Ent();

        lista_item_romaneio_ent.clear();

        try {

            for (int i = 0; i < retorno_item_romaneio_ent.size(); i++) {

                Item_romaneio_ent tab_item_romaneio = new Item_romaneio_ent();

                String num_rom = retorno_item_romaneio_ent.get(i).getNum_rom();
                String num_seq = retorno_item_romaneio_ent.get(i).getNum_seq();
                Long cod_ite = retorno_item_romaneio_ent.get(i).getCod_ite();
                Long num_reg_nts = retorno_item_romaneio_ent.get(i).getNum_reg_nts();
                String nom_ite = retorno_item_romaneio_ent.get(i).getNom_ite();
                String qtd_ite = retorno_item_romaneio_ent.get(i).getQtd_ite();
                String qtd_pen = retorno_item_romaneio_ent.get(i).getQtd_pen();
                String cod_uni = retorno_item_romaneio_ent.get(i).getCod_uni();
                String cod_dep = retorno_item_romaneio_ent.get(i).getCod_dep();
                String nom_dep = retorno_item_romaneio_ent.get(i).getNom_dep();
                String cod_ide_loc = retorno_item_romaneio_ent.get(i).getCod_ide_loc();

                tab_item_romaneio.setNum_rom(num_rom);
                tab_item_romaneio.setNum_seq(num_seq);
                tab_item_romaneio.setCod_ite(cod_ite);
                tab_item_romaneio.setNum_reg_nts(num_reg_nts);
                tab_item_romaneio.setNom_ite(nom_ite);
                tab_item_romaneio.setQtd_ite(qtd_ite);
                tab_item_romaneio.setQtd_pen(qtd_pen);
                tab_item_romaneio.setCod_uni(cod_uni);
                tab_item_romaneio.setCod_dep(cod_dep);
                tab_item_romaneio.setNom_dep(nom_dep);
                tab_item_romaneio.setCod_ide_loc(cod_ide_loc);

                if (num_rom.equals(numero_romaneio))
                    lista_item_romaneio_ent.add(tab_item_romaneio);
            }

            if (lista_item_romaneio_ent.isEmpty()) {
                alertSmall("Não há itens no romaneio de entrega!");
            }

            Collections.sort(lista_item_romaneio_ent);

            itemRetiraAdapter = new ItemRetiraAdapter(MOB006_A.this, lista_item_romaneio_ent, retornaQuantidade);
            w_registro_mapa.setAdapter(itemRetiraAdapter);
            itemRetiraAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        bancoMapaRetira.close();

    }

    private void limparRomaneio_Online() {
        System.out.println("limparRomaneio_Online");
        referenceDB_romaneio.child(numero_romaneio).removeValue();
    }

    private void limparItemRomaneio_Online() {
        System.out.println("limparItemRomaneio_Online");
        //referenceDB_item_romaneio.child(numero_romaneio).removeValue();
    }

    //Grava na tabela Romaneio_lib na nuvem
    private void gravarRomaneio_liberado() {
        referenceDB_romaneio_liberado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    Romaneio_lib tab_romaneio_lib = new Romaneio_lib();

                    tab_romaneio_lib.setNum_rom(numero_romaneio);

                    String modelo_celular = Build.MANUFACTURER + " " + Build.MODEL;
                    tab_romaneio_lib.setMod_cel(modelo_celular);

                    tab_romaneio_lib.setFlg_can(String.valueOf(flag_cancelado)); // Flag de cancelamento

                    //Chama os dados temporarios do usuário
                    SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                    String usuario_tmp = settings.getString("cod_usu_tmp", "");

                    if (flag_cancelado) { // Cancelamento
                        tab_romaneio_lib.setUsu_can(usuario_tmp);
                        tab_romaneio_lib.setHor_can(retornarHora());
                        tab_romaneio_lib.setDat_can(retornarData());
                    } else { // Confirmação
                        tab_romaneio_lib.setUsu_ent(usuario_tmp);
                        tab_romaneio_lib.setHor_ent(retornarHora());
                        tab_romaneio_lib.setDat_ent(retornarData());
                    }

                    referenceDB_romaneio_liberado.child(numero_romaneio).setValue(tab_romaneio_lib);

                    referenceDB_romaneio_liberado.removeEventListener(this);

                    //Limpar romaneio_ret e ite_romaneio_ret
                    limparRomaneio_Online();
                    limparItemRomaneio_Online();

                } catch (Exception ex) {
                    alertMessage.error(view, MOB006_A.this,
                            "Não foi possível gravar liberação do romaneio " + numero_romaneio,
                            "Erro: " + ex.getMessage().toString() + "\n\n" +
                                    "Contate o suporte para mais detalhes.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB006_A.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private void gravarItem_Romaneio_Online() {
        referenceDB_item_romaneio.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Item_romaneio_ent tab_item_romaneio_ent = new Item_romaneio_ent();
                    System.out.println("gravarItem_Romaneio_Online***");
                    //Verifica se a lista local está vazia
                    if (lista_item_romaneio_ent.isEmpty()) {
                        System.out.println("ista_item_romaneio_ent.isEmpty");
                        int numero_sequencia = 0;
                        for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                            tab_item_romaneio_ent = retornoBanco.child(String.valueOf(numero_sequencia)).getValue(Item_romaneio_ent.class);

                            int sequencia_atual = Integer.parseInt(tab_item_romaneio_ent.getNum_seq());
                            for (int i = 0; i < item_romaneio_ent_atualizado.size(); i++) {
                                int sequencia_atualizado = Integer.parseInt(item_romaneio_ent_atualizado.get(i).getNum_seq());

                                if (sequencia_atual == sequencia_atualizado) {
                                    String quantidade_pendente = item_romaneio_ent_atualizado.get(i).getQtd_pen();
                                    tab_item_romaneio_ent.setQtd_pen(quantidade_pendente);
                                }
                            }

                            referenceDB_item_romaneio.child(numero_romaneio).child(String.valueOf(sequencia_atual)).setValue(tab_item_romaneio_ent);
                            numero_sequencia++;

                        }
                    } else {
                        System.out.println("ista_item_romaneio_ent else");
                        for (int x = 0; x < lista_item_romaneio_ent.size(); x++) {
                            tab_item_romaneio_ent = lista_item_romaneio_ent.get(x);

                            int sequencia_atual = Integer.parseInt(tab_item_romaneio_ent.getNum_seq());
                            for (int i = 0; i < item_romaneio_ent_atualizado.size(); i++) {
                                int sequencia_atualizado = Integer.parseInt(item_romaneio_ent_atualizado.get(i).getNum_seq());

                                if (sequencia_atual == sequencia_atualizado) {
                                    String quantidade_pendente = item_romaneio_ent_atualizado.get(i).getQtd_pen();
                                    tab_item_romaneio_ent.setQtd_pen(quantidade_pendente);
                                }
                            }

                            referenceDB_item_romaneio.child(numero_romaneio).child(String.valueOf(sequencia_atual)).setValue(tab_item_romaneio_ent);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    alertMessage.error(view, MOB006_A.this, "Erro ao gravar itens",
                            "Número do romaneio: " + numero_romaneio +
                                    "\n" + ex.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB006_A.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    RetornaQuantidade retornaQuantidade = new RetornaQuantidade() {
        @Override
        public void recebeQuantidade(Item_romaneio_ent retorno_item_romaneio_ent) {
            item_romaneio_ent_atualizado.add(retorno_item_romaneio_ent);
        }
    };

    /*
    //Grava na tabela romaneio_ret na nuvem
    private void gravarRomaneio_Online() {
        referenceDB_romaneio.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {

                        Romaneio_ent tab_romaneio = retornoBanco.getValue(Romaneio_ent.class);

                        tab_romaneio.setDat_lib_re(retornarData());
                        tab_romaneio.setHor_lib_re(retornarHora());

                        //Chama os dados temporarios do usuário
                        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                        String usuario_tmp = settings.getString("cod_usu_tmp", "");

                        tab_romaneio.setUsu_lib_re(usuario_tmp);

                        referenceDB_romaneio.child(numero_romaneio).setValue(tab_romaneio);
                        flag_liberado = true;


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    alertMessage.error(view, MOB006_A.this, "Erro ao gravar pedido",
                            "Número do romaneio: " + numero_romaneio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB006_A.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private Romaneio_ent getRomaneio_Local() {
        BancoMapaRetira bancoMapaRetira = new BancoMapaRetira(this);

        ArrayList<Romaneio_ent> retorno_romaneio_ent = bancoMapaRetira.getRomaneio_Ent();

        Romaneio_ent tab_romaneio = null;

        for (int i = 0; i < retorno_romaneio_ent.size(); i++) {

            tab_romaneio = new Romaneio_ent();

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

            tab_romaneio.setNum_seq(num_seq);
            tab_romaneio.setNum_reg_nts(num_reg_nts);
            tab_romaneio.setCod_ser(cod_ser);
            tab_romaneio.setNum_doc(num_doc);
            tab_romaneio.setNom_cli(nom_cli);
            tab_romaneio.setEnd_cli(end_cli);
            tab_romaneio.setNum_end_cli(num_end_cli);
            tab_romaneio.setCom_bai_cli(com_bai_cli);
            tab_romaneio.setNom_mun(nom_mun);
            tab_romaneio.setCod_uni_fed(cod_uni_fed);
            tab_romaneio.setCep_cli(cep_cli);
            tab_romaneio.setFon_cli(fon_cli);
            tab_romaneio.setFon_cli_2(fon_cli_2);
            tab_romaneio.setNom_ven(nom_ven);
            tab_romaneio.setDat_lib_re(dat_lib_re);
            tab_romaneio.setUsu_lib_re(usu_lib_re);
            tab_romaneio.setHor_lib_re(hor_lib_re);


            tab_romaneio.setDat_lib_re(retornarData());
            tab_romaneio.setHor_lib_re(retornarHora());

            //Chama os dados temporarios do usuário
            SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
            String usuario_tmp = settings.getString("cod_usu_tmp", "");

            tab_romaneio.setUsu_lib_re(usuario_tmp);

        }

        bancoMapaRetira.close();

        return tab_romaneio;

    }
     */

    //TODO - Rotina de Cancelamento

    private void abrirMensagemCancelamento() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        iniciarAssinaturaCancelamento();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MOB006_A.this);
        builder.setMessage("Deseja cancelar este romaneio?")
                .setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener)
                .show();
    }

    public void cancelarRomaneioNuvem() {
        referenceDB_romaneio_liberado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    w_btn_confirmar.setEnabled(false);
                    flag_cancelado = true;
                    referenceDB_romaneio_liberado.child(numero_romaneio).child("flg_can").setValue("true");

                    enviarFotos();
                    enviarAssinaturas();

                    referenceDB_romaneio.removeEventListener(this);
                } catch (Exception ex) {
                    alertMessage.error(view, MOB006_A.this,
                            "Não foi possível gravar cancelamento do romaneio " + numero_romaneio,
                            "Erro: " + ex.getMessage().toString() + "\n\n" +
                                    "Contate o suporte para mais detalhes.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage.error(view, MOB006_A.this, "Erro ao conectar",
                        "Não foi possível acessar a base de dados !!!\n" +
                                "Contate o suporte para mais detalhes.");
            }
        });
    }

    private void mudarBotaoCancelamento() {
        w_btn_confirmar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialog_error)));
        w_btn_confirmar.setText("CONFIRMAR CANCELAMENTO");
    }


    //

    private boolean validarAssinatura() {

        boolean validado = false;

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        final ArrayList<Fotos> dadosAssinatura = bancoAssinatura.getAssinatura();

        for (int i = 0; i < dadosAssinatura.size(); i++) {

            if (dadosAssinatura.get(i).getCodigo_registro().equals(numero_romaneio)) {
                validado = true;
            }
        }

        return validado;
    }

    //Envia as fotos para a nuvem
    private void enviarFotos() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoFotos bancoFotos = new BancoFotos(this);

        final ArrayList<Fotos> dadosFotos = bancoFotos.getFotos();

        //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
        for (int i = 0; i < dadosFotos.size(); i++) {

            if (dadosFotos.get(i).getCodigo_registro().equals(numero_romaneio)) {
                String nome_foto = dadosFotos.get(i).getNome_arquivo();
                Uri caminho_foto = Uri.parse(dadosFotos.get(i).getCaminho_foto());

                final StorageReference banco = storageRef.child("fotos/" + nome_foto);

                banco.putFile(caminho_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //mensagem indicando sucesso
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertMessage("Ocorreu um erro ao enviar a foto, verifique sua conexão!");
                    }
                });

            }
        }
    }

    //Envia as assinaturas para a nuvem
    public void enviarAssinaturas() {
        dialog.setMessage("Enviando assinatura,\npor favor aguarde...");
        dialog.setCancelable(false);
        dialog.show();

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        final ArrayList<Fotos> dadosAssinatura = bancoAssinatura.getAssinatura();

        //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
        for (int i = 0; i < dadosAssinatura.size(); i++) {
            if (dadosAssinatura.get(i).getCodigo_registro().equals(numero_romaneio)) {

                String nome_imagem = dadosAssinatura.get(i).getNome_arquivo();
                Uri caminho_imagem = Uri.parse(dadosAssinatura.get(i).getCaminho_foto());

                final StorageReference banco = storageRef.child("assinaturas/" + nome_imagem);

                banco.putFile(caminho_imagem).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //alertMessage.success(view, MOB006_A.this, "Retira " + numero_romaneio + " confirmado",
                                //"Assinatura enviada:\n" + nome_imagem);
                                Toasty.info(MOB006_A.this,
                                        "Assinatura enviada:\n" + nome_imagem,
                                        Toast.LENGTH_LONG, true).show();

                                flag_assinatura_enviada = true;

                                dialog.dismiss();

                                if (flag_cancelado) {
                                    limparAssinaturas();
                                    finalizarRomaneio();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertMessage.error(view, MOB006_A.this,
                                "Erro",
                                "Ocorreu um erro ao enviar a assinatura, verifique sua conexão!");
                    }
                });

            }
        }
    }

    //Método que abre o fragment de tirar foto
    private void iniciarCamera() {
        FragmentManager fragment = getSupportFragmentManager();
        TirarFotoFragment alertDialog = TirarFotoFragment.newInstance("Foto");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", numero_romaneio);
        //Nome da foto no banco Storage
        bundle.putString("nome", "MAPA_RETIRA_" + numero_romaneio + "_");

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    //Método que abre o fragment de assinatura
    private void iniciarAssinatura() {
        FragmentManager fragment = getSupportFragmentManager();
        AssinaturaFragment alertDialog = AssinaturaFragment.newInstance("Assinatura");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", numero_romaneio);
        //Mensagem que vai aparecer na tela de assinatura
        bundle.putString("confirmacao", "Confirmo recebimento de mercadoria");
        //Nome da assinatura no banco Storage
        bundle.putString("nome", "MAPA_RETIRA_" + numero_romaneio + "_");

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    //Método que abre o fragment de assinatura para cancelamento
    private void iniciarAssinaturaCancelamento() {
        FragmentManager fragment = getSupportFragmentManager();
        AssinaturaFragment alertDialog = AssinaturaFragment.newInstance("Assinatura");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", numero_romaneio);
        //Mensagem que vai aparecer na tela de assinatura
        bundle.putString("confirmacao", "Confirmo cancelamento do romaneio");
        //Nome da assinatura no banco Storage
        bundle.putString("nome", "MAPA_RETIRA_" + numero_romaneio + "_CANC_");
        bundle.putString("cancelamento", "true");

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    // Cria o botão na Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botao_cancelar, menu);
        getMenuInflater().inflate(R.menu.botao_camera, menu);
        getMenuInflater().inflate(R.menu.botao_assinar, menu);
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
            //Botão camera
            case R.id.botao_cancelar_action_bar:
                abrirMensagemCancelamento();
                return true;
            //Botão camera
            case R.id.botao_camera_action_bar:
                iniciarCamera();
                return true;
            //Botão assinatura
            case R.id.botao_assinar_action_bar:
                iniciarAssinatura();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB006_A.this,
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
        Toast.makeText(MOB006_A.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB006_A.this);
        //alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}