package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rp_grf.jrmadeiras.Fragments.BuscaAerodromoFragment;
import com.rp_grf.jrmadeiras.Fragments.BuscaAeronaveFragment;
import com.rp_grf.jrmadeiras.Fragments.BuscaAlunoFragment;
import com.rp_grf.jrmadeiras.Fragments.BuscaTipoVooFragment;
import com.rp_grf.jrmadeiras.Fragments.PreVooFragment;
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAerodromo;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAeronave;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAluno;
import com.rp_grf.jrmadeiras.Interfaces.RetornaPreVoo;
import com.rp_grf.jrmadeiras.Interfaces.RetornaTipo_voo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgenda;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Registro de vôo
 * Autor: André Castro
 */

public class MOB004 extends AppCompatActivity {

    private BancoAgendaTemp bancoAgendaTemp;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Boolean flag_online;

    String descricao_programa;
    String codigo_agenda;
    String codigo_agenda_anterior;

    String codigo_aluno;
    String nome_aluno;
    String nome_fantasia_aluno;
    String codigo_alternativo;
    String data_exame_medico;

    String codigo_aeronave;
    String prefixo_aeronave;
    String nome_aeronave;
    String tipo_aeronave;

    String codigo_tipo_voo;
    String nome_tipo_voo;

    String codigo_origem;
    String nome_origem;
    String sigla_origem;

    String codigo_destino;
    String nome_destino;
    String sigla_destino;

    String codigo_pre_voo;

    Intent intent;

    TextView w_nome_aluno;
    TextView w_nome_instrutor;
    TextView w_nome_aeronave;
    TextView w_nome_tipo_voo;
    TextView w_sigla_origem;
    TextView w_sigla_destino;

    TextView w_texto_botao_prevoo;

    CardView w_botao_prevoo;
    CardView w_botao_aluno;
    CardView w_botao_aeronave;
    CardView w_botao_tipo_voo;
    CardView w_botao_origem;
    CardView w_botao_destino;

    ImageView w_icone_aluno;
    ImageView w_icone_aeronave;
    ImageView w_icone_tipo_voo;
    ImageView w_icone_origem;
    ImageView w_icone_destino;

    Button w_btn_seguinte;

    Agenda tab_agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob004);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getExtra();

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = new Intent(getBaseContext(), MOB004_A.class);

        iniciarCampos();

        cliqueBotao();

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String nom_usu_tmp = settings.getString("nom_usu_tmp", "");
        String flag_login = settings.getString("flag_online", "");

        w_nome_instrutor.setText(nom_usu_tmp);

        isConnected(flag_login);

        iniciarAgendaTemporaria();

        iniciarProximaAgenda();

        System.out.println("MOB004 - cod_pre_voo " + codigo_pre_voo);

    }

    //Método que define os parametros que serão enviados para a proxima tela
    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", descricao_programa);
        intent.putExtra("codigo_agenda", codigo_agenda);
        intent.putExtra("codigo_agenda_anterior", codigo_agenda_anterior);

        intent.putExtra("cod_pre_voo", codigo_pre_voo);

        intent.putExtra("nom_cli_ins", w_nome_instrutor.getText().toString());
        intent.putExtra("cod_cli", codigo_aluno.toString());
        intent.putExtra("nom_cli", w_nome_aluno.getText().toString());
        intent.putExtra("cod_bem", codigo_aeronave.toString());
        intent.putExtra("nom_bem", w_nome_aeronave.getText().toString());
        intent.putExtra("cod_tip_voo", codigo_tipo_voo.toString());
        intent.putExtra("nom_tip_voo", w_nome_tipo_voo.getText().toString());
        intent.putExtra("cod_aer_ori", codigo_origem.toString());
        intent.putExtra("sig_aer_ori", w_sigla_origem.getText().toString());
        intent.putExtra("cod_aer_des", codigo_destino.toString());
        intent.putExtra("sig_aer_des", w_sigla_destino.getText().toString());

        //MOB004_A
        intent.putExtra("dat_reg_voo", getIntent().getStringExtra("dat_reg_voo"));
        intent.putExtra("hor_par", getIntent().getStringExtra("hor_par"));
        intent.putExtra("min_par", getIntent().getStringExtra("min_par"));
        intent.putExtra("hor_dec", getIntent().getStringExtra("hor_dec"));
        intent.putExtra("min_dec", getIntent().getStringExtra("min_dec"));
        intent.putExtra("hor_pou", getIntent().getStringExtra("hor_pou"));
        intent.putExtra("min_pou", getIntent().getStringExtra("min_pou"));
        intent.putExtra("hor_cor", getIntent().getStringExtra("hor_cor"));
        intent.putExtra("min_cor", getIntent().getStringExtra("min_cor"));

        intent.putExtra("flag_partida", getIntent().getStringExtra("flag_partida"));
        intent.putExtra("flag_corte", getIntent().getStringExtra("flag_corte"));

        //MOB004_B
        intent.putExtra("hor_ini_fli", getIntent().getStringExtra("hor_ini_fli"));
        intent.putExtra("hor_fin_fli", getIntent().getStringExtra("hor_fin_fli"));
        intent.putExtra("tmp_voo_fli", getIntent().getStringExtra("tmp_voo_fli"));
        intent.putExtra("num_pou", getIntent().getStringExtra("num_pou"));

        intent.putExtra("lit_com", getIntent().getStringExtra("lit_com"));
        intent.putExtra("gal_com", getIntent().getStringExtra("gal_com"));

        //MOB004_C
        intent.putExtra("hor_ini_hob", getIntent().getStringExtra("hor_ini_hob"));
        intent.putExtra("hor_fin_hob", getIntent().getStringExtra("hor_fin_hob"));
        intent.putExtra("tmp_voo_hob", getIntent().getStringExtra("tmp_voo_hob"));

        intent.putExtra("hor_diu", getIntent().getStringExtra("hor_diu"));
        intent.putExtra("min_diu", getIntent().getStringExtra("min_diu"));
        intent.putExtra("hor_not", getIntent().getStringExtra("hor_not"));
        intent.putExtra("min_not", getIntent().getStringExtra("min_not"));

        intent.putExtra("hor_ifrr", getIntent().getStringExtra("hor_ifrr"));
        intent.putExtra("min_ifrr", getIntent().getStringExtra("min_ifrr"));
        intent.putExtra("hor_ifrc", getIntent().getStringExtra("hor_ifrc"));
        intent.putExtra("min_ifrc", getIntent().getStringExtra("min_ifrc"));
        intent.putExtra("hor_vfr", getIntent().getStringExtra("hor_vfr"));
        intent.putExtra("min_vfr", getIntent().getStringExtra("min_vfr"));

        intent.putExtra("inf_dia_bor", getIntent().getStringExtra("inf_dia_bor"));
        intent.putExtra("sob_voo_mn", getIntent().getStringExtra("sob_voo_mn"));
        intent.putExtra("flg_nav", getIntent().getStringExtra("flg_nav"));

        intent.putExtra("obs_reg_voo", getIntent().getStringExtra("obs_reg_voo"));

    }

    private void getExtra() {
        codigo_agenda_anterior = getIntent().getStringExtra("codigo_agenda_anterior");

        codigo_pre_voo = getIntent().getStringExtra("cod_pre_voo");

        codigo_aluno = getIntent().getStringExtra("cod_cli");
        nome_aluno = getIntent().getStringExtra("nom_cli");
        codigo_aeronave = getIntent().getStringExtra("cod_bem");
        nome_aeronave = getIntent().getStringExtra("nom_bem");
        codigo_tipo_voo = getIntent().getStringExtra("cod_tip_voo");
        nome_tipo_voo = getIntent().getStringExtra("nom_tip_voo");
        codigo_origem = getIntent().getStringExtra("cod_aer_ori");
        sigla_origem = getIntent().getStringExtra("sig_aer_ori");
        codigo_destino = getIntent().getStringExtra("cod_aer_des");
        sigla_destino = getIntent().getStringExtra("sig_aer_des");
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

    private void iniciarCampos() {
        //Campos de texto
        w_nome_instrutor = (TextView) findViewById(R.id.txt_instrutor);
        w_nome_aluno = (TextView) findViewById(R.id.txt_aluno);
        w_nome_aeronave = (TextView) findViewById(R.id.txt_aeronave);
        w_nome_tipo_voo = (TextView) findViewById(R.id.txt_tipo_voo);
        w_sigla_origem = (TextView) findViewById(R.id.txt_origem);
        w_sigla_destino = (TextView) findViewById(R.id.txt_destino);

        //Botões
        w_botao_prevoo = (CardView) findViewById(R.id.card_view_botao_prevoo);
        w_botao_aluno = (CardView) findViewById(R.id.card_view_botao_aluno);
        w_botao_aeronave = (CardView) findViewById(R.id.card_view_botao_aeronave);
        w_botao_tipo_voo = (CardView) findViewById(R.id.card_view_botao_tipo_voo);
        w_botao_origem = (CardView) findViewById(R.id.card_view_botao_origem);
        w_botao_destino = (CardView) findViewById(R.id.card_view_botao_destino);

        w_btn_seguinte = (Button) findViewById(R.id.button_seguinte);

        //Icones
        w_icone_aluno = (ImageView) findViewById(R.id.image_view_icone_aluno);
        w_icone_aeronave = (ImageView) findViewById(R.id.image_view_icone_aeronave);
        w_icone_tipo_voo = (ImageView) findViewById(R.id.image_view_icone_tipo_voo);
        w_icone_origem = (ImageView) findViewById(R.id.image_view_icone_origem);
        w_icone_destino = (ImageView) findViewById(R.id.image_view_icone_destino);

        //Texto Botão
        w_texto_botao_prevoo = (TextView) findViewById(R.id.text_view_botao_prevoo);
    }

    private void cliqueBotao() {

        w_botao_prevoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                PreVooFragment alertDialog = PreVooFragment.newInstance("",
                        retornaAluno,
                        retornaAeronave,
                        retornaTipo_voo,
                        retornaOrigem,
                        retornaDestino,
                        retornaPreVoo);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_botao_aluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                BuscaAlunoFragment alertDialog =
                        BuscaAlunoFragment.newInstance("", retornaAluno);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_botao_aeronave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                BuscaAeronaveFragment alertDialog =
                        BuscaAeronaveFragment.newInstance("", retornaAeronave);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_botao_tipo_voo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                BuscaTipoVooFragment alertDialog =
                        BuscaTipoVooFragment.newInstance("", retornaTipo_voo);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_botao_origem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                BuscaAerodromoFragment alertDialog =
                        BuscaAerodromoFragment.newInstance("", retornaOrigem);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_botao_destino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getSupportFragmentManager();
                BuscaAerodromoFragment alertDialog =
                        BuscaAerodromoFragment.newInstance("", retornaDestino);
                alertDialog.show(fragment, "fragment_alert");
            }
        });

        w_btn_seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean validado = validarCampos();

                if (validado == true) {

                    salvarAgendaTemporaria();

                    setExtra(intent);

                    startActivity(intent);

                } else {
                    alertMessage("Preencha todos os campos antes de continuar!");
                }
            }
        });
    }

    //Valida se todos os campos foram preenchidos
    private boolean validarCampos() {
        if (w_nome_aluno.getText().toString().equals(". . .") ||
                w_nome_aeronave.getText().toString().equals(". . .") ||
                w_nome_tipo_voo.getText().toString().equals(". . .") ||
                w_sigla_origem.getText().toString().equals(". . .") ||
                w_sigla_destino.getText().toString().equals(". . .")
        ) {
            return false;
        } else {
            return true;
        }
    }

    //Conta quantas agendas temporárias tem salvas no banco SQLite e gera um novo código
    private void iniciarAgendaTemporaria() {
        bancoAgendaTemp = new BancoAgendaTemp(this);

        try {
            final ArrayList<Agenda> dadosAgenda = bancoAgendaTemp.getAgenda();

            int contador = 0;

            while (contador <= dadosAgenda.size()) {
                contador++;
            }

            tab_agenda = new Agenda();
            tab_agenda.setCod_age((long) (contador + 1)); //Inicia uma nova agenda

            codigo_agenda = String.valueOf(tab_agenda.getCod_age());

        } catch (Exception ex) {
            System.out.println("MOB004 - Não foi possível iniciar uma nova agenda temporaria:\n" + ex.toString());
        }
    }

    // Salva no banco SQLite os dados temporarios da agenda
    private void salvarAgendaTemporaria() {
        bancoAgendaTemp = new BancoAgendaTemp(this);

        final ArrayList<Agenda> dadosAgendaTemp = bancoAgendaTemp.getAgenda();

        for (int i = 0; i < dadosAgendaTemp.size(); i++) {
            if (dadosAgendaTemp.get(i).getCod_age().toString().equals(codigo_agenda)) {
                tab_agenda = dadosAgendaTemp.get(i);
            }
        }

        tab_agenda.setCod_cli(Long.parseLong(codigo_aluno));
        tab_agenda.setCod_bem(Long.parseLong(codigo_aeronave));
        tab_agenda.setCod_tip_voo(Long.parseLong(codigo_tipo_voo));
        tab_agenda.setCod_aer_ori(Long.parseLong(codigo_origem));
        tab_agenda.setCod_aer_des(Long.parseLong(codigo_destino));

        if (codigo_agenda_anterior == null) {
            codigo_agenda = tab_agenda.getCod_age().toString();
        } else {
            int anterior = Integer.parseInt(tab_agenda.getCod_age().toString());
            int soma = anterior + 1;
            codigo_agenda = String.valueOf(soma);
        }

        bancoAgendaTemp.setAgenda(tab_agenda);
    }

    private void iniciarProximaAgenda() {
        //Só executa o método se for uma agenda continuação de outra
        if (codigo_agenda_anterior != null) {

            w_nome_aluno.setText(nome_aluno);
            w_nome_aeronave.setText(nome_aeronave);
            w_nome_tipo_voo.setText(nome_tipo_voo);

            w_sigla_origem.setText(sigla_destino); //Coloca o Destino da agenda anterior na Origem

            travarAgenda();
            travarAluno();
            travarAeronave();
            travarTipoVoo();
            travarOrigem();
        }
    }

    //Método que muda a cor do botão de Pre-Voo e desabilita
    private void travarAgenda() {

        w_botao_prevoo.setEnabled(false);

        w_botao_prevoo.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_texto_botao_prevoo.setTextColor(getResources().getColor(R.color.black));

    }

    //Método que muda a cor do botão de aluno e desabilita
    private void travarAluno() {

        w_botao_aluno.setEnabled(false);

        w_botao_aluno.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_aluno.setImageResource(R.drawable.icone_lupa_novo);

    }

    //Método que muda a cor do botão de aeronave e desabilita
    private void travarAeronave() {

        w_botao_aeronave.setEnabled(false);

        w_botao_aeronave.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_aeronave.setImageResource(R.drawable.icone_lupa_novo);

    }

    //Método que muda a cor do botão de tipo de voo e desabilita
    private void travarTipoVoo() {

        w_botao_tipo_voo.setEnabled(false);

        w_botao_tipo_voo.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_tipo_voo.setImageResource(R.drawable.icone_lupa_novo);

    }

    //Método que muda a cor do botão de origem e desabilita
    private void travarOrigem() {

        w_botao_origem.setEnabled(false);

        w_botao_origem.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_origem.setImageResource(R.drawable.icone_lupa_novo);

    }

    RetornaAluno retornaAluno = new RetornaAluno() {
        @Override
        public void recebeAluno(String cod_cli, String nom_cli, String nom_fan_cli,
                                String cod_alt, String dat_exa_med) {
            codigo_aluno = cod_cli;
            nome_aluno = nom_cli;
            nome_fantasia_aluno = nom_fan_cli;
            codigo_alternativo = cod_alt;
            data_exame_medico = dat_exa_med;

            w_nome_aluno.setText(nom_cli);
            intent.putExtra("cod_cli", codigo_aluno.toString());
        }
    };

    RetornaAeronave retornaAeronave = new RetornaAeronave() {
        @Override
        public void recebeAeronave(String cod_bem, String pre_bem, String nom_bem, String tip_bem) {
            codigo_aeronave = cod_bem;
            prefixo_aeronave = pre_bem;
            nome_aeronave = nom_bem;
            tipo_aeronave = tip_bem;

            w_nome_aeronave.setText(pre_bem);
            intent.putExtra("cod_bem", codigo_aeronave.toString());
        }
    };

    RetornaTipo_voo retornaTipo_voo = new RetornaTipo_voo() {
        @Override
        public void recebeTipo_voo(String cod_tip_voo, String nom_tip_voo) {
            codigo_tipo_voo = cod_tip_voo;
            nome_tipo_voo = nom_tip_voo;

            w_nome_tipo_voo.setText(nom_tip_voo);
            intent.putExtra("cod_tip_voo", codigo_tipo_voo.toString());
        }
    };

    RetornaAerodromo retornaOrigem = new RetornaAerodromo() {
        @Override
        public void recebeAerodromo(String cod_aer, String nom_aer, String sig_aer) {
            codigo_origem = cod_aer;
            nome_origem = nom_aer;
            sigla_origem = sig_aer;

            w_sigla_origem.setText(sig_aer);
            intent.putExtra("cod_aer_ori", codigo_origem.toString());
        }
    };

    RetornaAerodromo retornaDestino = new RetornaAerodromo() {
        @Override
        public void recebeAerodromo(String cod_aer, String nom_aer, String sig_aer) {
            codigo_destino = cod_aer;
            nome_destino = nom_aer;
            sigla_destino = sig_aer;

            w_sigla_destino.setText(sig_aer);
            intent.putExtra("cod_aer_des", codigo_destino.toString());
        }
    };

    RetornaPreVoo retornaPreVoo = new RetornaPreVoo() {
        @Override
        public void recebePreVoo(String cod_pre_voo) {

            travarAluno();
            travarAeronave();
            travarTipoVoo();

            codigo_pre_voo = cod_pre_voo;

            System.out.println("PREVOO MOB");
            System.out.println(codigo_pre_voo);

            intent.putExtra("cod_pre_voo", codigo_pre_voo.toString());

        }
    };

    private void iniciarCamera() {
        FragmentManager fragment = getSupportFragmentManager();
        TirarFotoFragment alertDialog = TirarFotoFragment.newInstance("Foto");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", codigo_agenda);

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    // Cria o botão na Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botao_camera, menu);
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
            case R.id.botao_camera_action_bar:
                iniciarCamera();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB004.this,
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
        Toast.makeText(MOB004.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
