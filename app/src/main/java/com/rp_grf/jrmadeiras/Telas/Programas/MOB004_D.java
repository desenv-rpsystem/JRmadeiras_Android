package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgenda;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.SQLite.BancoFotos;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;
import com.rp_grf.jrmadeiras.Tabelas.Fotos;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo_Aux;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MOB004_D extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoAgenda bancoAgenda;
    private BancoRegistroVoo bancoRegistroVoo;

    int contador;

    Boolean flag_online;

    Boolean flag_partida;
    Boolean flag_corte;

    String codigo_agenda;

    String codigo_pre_voo;

    String descricao_programa;
    String nome_aluno;
    String codigo_instrutor;

    String hora_tempo_voo;
    String minuto_tempo_voo;

    TextView w_nome_aluno;
    TextView w_codigo_aluno;
    TextView w_nome_instrutor;

    TextView w_data_voo;
    TextView w_nome_tipo_voo;
    TextView w_origem;
    TextView w_destino;

    TextView w_horario_partida;
    TextView w_horario_decolagem;
    TextView w_horario_pouso;
    TextView w_horario_corte;
    TextView w_horario_tempo_voo;

    TextView w_hor_inicial_flight;
    TextView w_hor_final_flight;
    TextView w_tempo_voo_flight;
    TextView w_pousos;

    TextView w_litros_combustivel;
    TextView w_galao_combustivel;

    TextView w_hor_inicial_hobbs;
    TextView w_hor_final_hobbs;
    TextView w_tempo_voo_hobbs;

    TextView w_diurno;
    TextView w_noturno;
    TextView w_IFRR;
    TextView w_IFRC;
    TextView w_VFR;

    TextView w_diario_bordo;
    TextView w_sobre_voo;
    TextView w_naveg;

    TextView w_connected;
    TextView w_contador;

    TextView w_observacoes;

    TextView w_dry_wet;

    Button w_btn_confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob004_d);

        contador = 1;

        descricao_programa = "Resumo";
        nome_aluno = getIntent().getStringExtra("nom_cli");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Agenda");

        bancoAgenda = new BancoAgenda(this);

        w_connected = (TextView) findViewById(R.id.txt_registro_connected);
        w_contador = (TextView) findViewById(R.id.txt_registro_contador);
        w_contador.setText("");

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");
        codigo_instrutor = settings.getString("cod_usu_tmp", "");

        isConnected(flag_login);

        //Inicia os campos da tela
        iniciarAlunoInstrutor();

        iniciarInfoVoo();

        iniciarHorarios();

        iniciarFlight();

        iniciarCombustivel();

        iniciarHobbs();

        iniciarHorariosHobbs();

        iniciarDiarioNavegSobreVoo();

        iniciarBotao();

        //Ação do botão
        cliqueBotao();

        getLastAgenda();

        System.out.println("MOB004_D - cod_pre_voo " + codigo_pre_voo);

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

    private void iniciarAlunoInstrutor() {
        w_nome_aluno = (TextView) findViewById(R.id.txt_registro_nom_cli);
        w_nome_aluno.setText(getIntent().getStringExtra("nom_cli"));

        w_codigo_aluno = (TextView) findViewById(R.id.txt_registro_cod_cli);
        w_codigo_aluno.setText(getIntent().getStringExtra("cod_cli"));

        w_nome_instrutor = (TextView) findViewById(R.id.txt_registro_nom_cli_ins);
        w_nome_instrutor.setText(getIntent().getStringExtra("nom_cli_ins"));
    }

    private void iniciarInfoVoo() {
        w_data_voo = (TextView) findViewById(R.id.txt_registro_dat_registro);
        w_data_voo.setText(getIntent().getStringExtra("dat_reg_voo"));

        w_nome_tipo_voo = (TextView) findViewById(R.id.txt_registro_nom_tip_voo);
        w_nome_tipo_voo.setText(getIntent().getStringExtra("nom_tip_voo"));

        w_origem = (TextView) findViewById(R.id.txt_registro_cod_aer_ori);
        w_origem.setText(getIntent().getStringExtra("sig_aer_ori"));

        w_destino = (TextView) findViewById(R.id.txt_registro_cod_aer_des);
        w_destino.setText(getIntent().getStringExtra("sig_aer_des"));
    }

    private void iniciarHorarios() {
        w_horario_partida = (TextView) findViewById(R.id.txt_registro_hor_par);
        String horario_partida =
                getIntent().getStringExtra("hor_par") +
                        " : " +
                        getIntent().getStringExtra("min_par");
        w_horario_partida.setText(horario_partida);

        w_horario_decolagem = (TextView) findViewById(R.id.txt_registro_hor_dec);
        String horario_decolagem =
                getIntent().getStringExtra("hor_dec") +
                        " : " +
                        getIntent().getStringExtra("min_dec");
        w_horario_decolagem.setText(horario_decolagem);

        w_horario_pouso = (TextView) findViewById(R.id.txt_registro_hor_pou);
        String horario_pouso =
                getIntent().getStringExtra("hor_pou") +
                        " : " +
                        getIntent().getStringExtra("min_pou");
        w_horario_pouso.setText(horario_pouso);

        w_horario_corte = (TextView) findViewById(R.id.txt_registro_hor_cor);
        String horario_corte = getIntent().getStringExtra("hor_cor") +
                " : " +
                getIntent().getStringExtra("min_cor");
        w_horario_corte.setText(horario_corte);

        w_horario_tempo_voo = (TextView) findViewById(R.id.txt_registro_hor_voo);
        String horario_tempo_voo = calcularTempoVoo(
                getIntent().getStringExtra("hor_pou"),
                getIntent().getStringExtra("min_pou"),
                getIntent().getStringExtra("hor_dec"),
                getIntent().getStringExtra("min_dec")
        );
        w_horario_tempo_voo.setText(horario_tempo_voo);
    }

    private void iniciarFlight() {
        w_hor_inicial_flight = (TextView) findViewById(R.id.txt_registro_hor_ini_fli);
        w_hor_inicial_flight.setText(getIntent().getStringExtra("hor_ini_fli"));

        w_hor_final_flight = (TextView) findViewById(R.id.txt_registro_hor_fin_fli);
        w_hor_final_flight.setText(getIntent().getStringExtra("hor_fin_fli"));

        w_tempo_voo_flight = (TextView) findViewById(R.id.txt_registro_tmp_voo_fli);
        w_tempo_voo_flight.setText(getIntent().getStringExtra("tmp_voo_fli"));

        w_pousos = (TextView) findViewById(R.id.txt_registro_num_pou);
        w_pousos.setText(getIntent().getStringExtra("num_pou"));
    }

    private void iniciarCombustivel() {
        w_litros_combustivel = (TextView) findViewById(R.id.txt_registro_lit_com);
        w_litros_combustivel.setText(getIntent().getStringExtra("lit_com"));

        w_galao_combustivel = (TextView) findViewById(R.id.txt_registro_gal_com);
        w_galao_combustivel.setText(getIntent().getStringExtra("gal_com"));
    }

    private void iniciarHobbs() {
        w_hor_inicial_hobbs = (TextView) findViewById(R.id.txt_registro_hor_ini_hob);
        w_hor_inicial_hobbs.setText(getIntent().getStringExtra("hor_ini_hob"));

        w_hor_final_hobbs = (TextView) findViewById(R.id.txt_registro_hor_fin_hob);
        w_hor_final_hobbs.setText(getIntent().getStringExtra("hor_fin_hob"));

        w_tempo_voo_hobbs = (TextView) findViewById(R.id.txt_registro_tmp_voo_hob);
        w_tempo_voo_hobbs.setText(getIntent().getStringExtra("tmp_voo_hob"));
    }

    private void iniciarHorariosHobbs() {
        w_diurno = (TextView) findViewById(R.id.txt_registro_hor_diu);
        String horario_diurno = getIntent().getStringExtra("hor_diu") +
                " : " +
                getIntent().getStringExtra("min_diu");
        w_diurno.setText(horario_diurno);

        w_noturno = (TextView) findViewById(R.id.txt_registro_hor_not);
        String horario_noturno = getIntent().getStringExtra("hor_not") +
                " : " +
                getIntent().getStringExtra("min_not");
        w_noturno.setText(horario_noturno);

        w_IFRR = (TextView) findViewById(R.id.txt_registro_hor_ifrr);
        String horario_ifrr = getIntent().getStringExtra("hor_ifrr") +
                " : " +
                getIntent().getStringExtra("min_ifrr");
        w_IFRR.setText(horario_ifrr);

        w_IFRC = (TextView) findViewById(R.id.txt_registro_hor_ifrc);
        String horario_ifrc = getIntent().getStringExtra("hor_ifrc") +
                " : " +
                getIntent().getStringExtra("min_ifrc");
        w_IFRC.setText(horario_ifrc);

        w_VFR = (TextView) findViewById(R.id.txt_registro_hor_vfr);
        String horario_vfr = getIntent().getStringExtra("hor_vfr") +
                " : " +
                getIntent().getStringExtra("min_vfr");
        w_VFR.setText(horario_vfr);
    }

    private void iniciarDiarioNavegSobreVoo() {

        w_diario_bordo = (TextView) findViewById(R.id.txt_registro_inf_dia_bor);
        w_diario_bordo.setText(getIntent().getStringExtra("inf_dia_bor"));

        //

        w_sobre_voo = (TextView) findViewById(R.id.txt_registro_sob_voo_mn);
        w_sobre_voo.setText(getIntent().getStringExtra("sob_voo_mn"));

        //

        w_naveg = (TextView) findViewById(R.id.txt_registro_flg_nav);
        w_naveg.setText(getIntent().getStringExtra("flg_nav"));

        String flag_naveg = getIntent().getStringExtra("flg_nav");

        if (flag_naveg.equals("true")) {
            w_naveg.setText("Sim");
        } else if (flag_naveg.equals("false")) {
            w_naveg.setText("Não");
        } else {
            w_naveg.setText("-");
        }

        //

        w_dry_wet = (TextView) findViewById(R.id.txt_registro_dry_wet);
        w_dry_wet.setText(getIntent().getStringExtra("mod_dry_wet"));

        //

        w_observacoes = (TextView) findViewById(R.id.txt_registro_obs_reg_voo);
        w_observacoes.setText(getIntent().getStringExtra("obs_reg_voo"));
    }

    private String calcularTempoVoo(String hor_pou, String min_pou, String hor_dec, String min_dec) {
        try {
            int hora_pouso = Integer.parseInt(String.valueOf(hor_pou));
            int minuto_pouso = Integer.parseInt(String.valueOf(min_pou));

            int hora_decolagem = Integer.parseInt(String.valueOf(hor_dec));
            int minuto_decolagem = Integer.parseInt(String.valueOf(min_dec));

            // Usei as mesmas formulas que o Geraldo usou no programa fat250-a
            int tempo_pouso = (hora_pouso * 3600) + (minuto_pouso * 60);

            int tempo_decolagem = (hora_decolagem * 3600) + (minuto_decolagem * 60);

            int tempo_voo = tempo_pouso - tempo_decolagem;

            while (tempo_voo > 86400) {
                tempo_voo = tempo_voo - 86400;
            }

            int hora_voo = (tempo_voo / 3600);

            int minuto_voo = (tempo_voo - (hora_voo * 3600)) / 60;

            String hora_voo_decimal = String.valueOf(hora_voo);

            if (hora_voo <= 9) {
                hora_voo_decimal = "";
                //adiciona '0' no inicio da hora se o número for menor que 10
                hora_voo_decimal = "0" + hora_voo;
            }

            String minuto_voo_decimal = String.valueOf(minuto_voo);

            if (minuto_voo <= 9) {
                minuto_voo_decimal = "";
                //adiciona '0' no inicio do minuto se o número for menor que 10
                minuto_voo_decimal = "0" + minuto_voo;
            }

            String horario_tempo_voo = hora_voo_decimal + " : " + minuto_voo_decimal;

            hora_tempo_voo = hora_voo_decimal;
            minuto_tempo_voo = minuto_voo_decimal;

            return horario_tempo_voo;

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return "-- : --";
        }
    }

    private void iniciarBotao() {
        w_btn_confirmar = (Button) findViewById(R.id.button_confirmar);
    }

    private void cliqueBotao() {
        w_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preencherAgenda();
            }
        });
    }

    //Recebe o número do último registro gravado no banco
    private void getLastAgenda() {

        w_connected.setText(flag_online.toString());

        firebaseDB.getReference(".info/connected")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue(Boolean.class);
                        if (connected) {
                            w_connected.setText("true");
                        } else {
                            w_connected.setText("false");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertSmall("Conexão Cancelada!");
                    }
                });

        String contador_flag = w_connected.getText().toString();

        Boolean flag_online_agenda = Boolean.parseBoolean(contador_flag);

        if (flag_online_agenda == true) {
            referenceDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int contador = 0;

                    try {
                        for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                            Agenda tab_agenda = retorno.getValue(Agenda.class);

                            contador = Integer.parseInt(tab_agenda.getCod_age().toString());

                        }
                    } catch (Exception ex) {
                        //contador = 0;
                    }

                    contador++;

                    w_contador.setText(String.valueOf(contador));

                    System.out.println("MOB_contador " + contador);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alertMessage("Não foi possível acessar a base de dados !!!\n" +
                            "Favor reiniciar o aplicativo.");
                }
            });
        } else {

            bancoAgenda = new BancoAgenda(this);

            int contador = 1;

            while (contador < bancoAgenda.getAgenda().size()) {

                contador++;
            }

            w_contador.setText(String.valueOf(contador));
        }

    }

    //Preenche o objeto Agenda
    private void preencherAgenda() {

        String codigo_pre_voo = getIntent().getStringExtra("cod_pre_voo");

        System.out.println("PREENCHER AGENDA");
        System.out.println(codigo_pre_voo);

        String codigo_aluno = getIntent().getStringExtra("cod_cli");
        String codigo_aeronave = getIntent().getStringExtra("cod_bem");
        String codigo_tipo_voo = getIntent().getStringExtra("cod_tip_voo");
        String codigo_origem = getIntent().getStringExtra("cod_aer_ori");
        String codigo_destino = getIntent().getStringExtra("cod_aer_des");

        String data_voo = getIntent().getStringExtra("dat_reg_voo");
        String hora_partida = getIntent().getStringExtra("hor_par");
        String minuto_partida = getIntent().getStringExtra("min_par");
        String hora_decolagem = getIntent().getStringExtra("hor_dec");
        String minuto_decolagem = getIntent().getStringExtra("min_dec");
        String hora_pouso = getIntent().getStringExtra("hor_pou");
        String minuto_pouso = getIntent().getStringExtra("min_pou");
        String hora_corte = getIntent().getStringExtra("hor_cor");
        String minuto_corte = getIntent().getStringExtra("min_cor");

        String hor_inicial_flight = getIntent().getStringExtra("hor_ini_fli");
        String hor_final_flight = getIntent().getStringExtra("hor_fin_fli");
        String tempo_voo_flight = getIntent().getStringExtra("tmp_voo_fli");
        String numero_pousos = getIntent().getStringExtra("num_pou");

        String litros_combustivel = getIntent().getStringExtra("lit_com");
        String galao_combustivel = getIntent().getStringExtra("gal_com");

        String hor_inicial_hobbs = getIntent().getStringExtra("hor_ini_hob");
        String hor_final_hobbs = getIntent().getStringExtra("hor_fin_hob");
        String tempo_voo_hobbs = getIntent().getStringExtra("tmp_voo_hob");

        String hora_diurno = getIntent().getStringExtra("hor_diu");
        String minuto_diurno = getIntent().getStringExtra("min_diu");
        String hora_noturno = getIntent().getStringExtra("hor_not");
        String minuto_noturno = getIntent().getStringExtra("min_not");

        String hora_ifrr = getIntent().getStringExtra("hor_ifrr");
        String minuto_ifrr = getIntent().getStringExtra("min_ifrr");
        String hora_ifrc = getIntent().getStringExtra("hor_ifrc");
        String minuto_ifrc = getIntent().getStringExtra("min_ifrc");
        String hora_vfr = getIntent().getStringExtra("hor_vfr");
        String minuto_vfr = getIntent().getStringExtra("min_vfr");

        String diario_bordo = getIntent().getStringExtra("inf_dia_bor");
        String sobre_voo = getIntent().getStringExtra("sob_voo_mn");
        String navegacao = getIntent().getStringExtra("flg_nav");

        String dry_wet = getIntent().getStringExtra("mod_dry_wet");

        String observacoes = getIntent().getStringExtra("obs_reg_voo");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH); //Recebe o dia atual
        int month = calendar.get(Calendar.MONTH); //Recebe o mês atual
        int year = calendar.get(Calendar.YEAR); //Recebe o ano atual

        String dia = String.valueOf(day);
        String mes = String.valueOf(month + 1);
        String ano = String.valueOf(year);

        if (day <= 9) {
            //adiciona '0' no inicio do dia se o número for menor que 10
            dia = "0" + dia;
        }

        if (month + 1 <= 9) {
            //adiciona '0' no inicio do mês se o número for menor que 10
            mes = "0" + mes;
        }

        String data_liberacao = dia + "/" + mes + "/" + ano;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String hora = String.valueOf(hour);
        String minuto = String.valueOf(minute);

        String hora_liberacao_decimal = hora;

        if (Integer.parseInt(hora) <= 9) {
            //adiciona '0' no inicio da hora se o número for menor que 10
            hora_liberacao_decimal = "0" + hora;
        }

        String minuto_liberacao_decimal = minuto;

        if (Integer.parseInt(minuto) <= 9) {
            //adiciona '0' no inicio do minuto se o número for menor que 10
            minuto_liberacao_decimal = "0" + minuto;
        }

        String hora_liberacao = hora_liberacao_decimal + ":" + minuto_liberacao_decimal;

        if (codigo_pre_voo == null) {
            codigo_pre_voo = "0";
        }

        try {

            Agenda tab_agenda = new Agenda(
                    Long.parseLong(w_contador.getText().toString()),
                    Long.parseLong(codigo_pre_voo),
                    Long.parseLong(codigo_aluno),
                    codigo_instrutor,
                    Long.parseLong(codigo_aeronave),
                    Long.parseLong(codigo_tipo_voo),
                    Long.parseLong(codigo_origem),
                    Long.parseLong(codigo_destino),

                    data_voo,
                    hora_partida,
                    minuto_partida,
                    hora_decolagem,
                    minuto_decolagem,
                    hora_pouso,
                    minuto_pouso,
                    hora_corte,
                    minuto_corte,
                    hora_tempo_voo,
                    minuto_tempo_voo,

                    hor_inicial_flight,
                    hor_final_flight,
                    tempo_voo_flight,
                    numero_pousos,

                    litros_combustivel,
                    galao_combustivel,

                    hor_inicial_hobbs,
                    hor_final_hobbs,
                    tempo_voo_hobbs,

                    hora_diurno,
                    minuto_diurno,
                    hora_noturno,
                    minuto_noturno,

                    hora_ifrr,
                    minuto_ifrr,
                    hora_ifrc,
                    minuto_ifrc,
                    hora_vfr,
                    minuto_vfr,

                    diario_bordo,
                    sobre_voo,
                    navegacao,

                    dry_wet,

                    observacoes,

                    data_liberacao,
                    hora_liberacao,
                    "RV", //Envia que foi feito pelo Registro de voo MOB004
                    "" //tempo de não voo que não se aplica à essa tela
            );

            tab_agenda = verificarNulos(tab_agenda);

            confirmarLiberacao(tab_agenda);

        } catch (Exception ex) {

            Agenda tab_agenda = new Agenda(
                    Long.parseLong("999"),
                    Long.parseLong(codigo_pre_voo),
                    Long.parseLong(codigo_aluno),
                    codigo_instrutor,
                    Long.parseLong(codigo_aeronave),
                    Long.parseLong(codigo_tipo_voo),
                    Long.parseLong(codigo_origem),
                    Long.parseLong(codigo_destino),

                    data_voo,
                    hora_partida,
                    minuto_partida,
                    hora_decolagem,
                    minuto_decolagem,
                    hora_pouso,
                    minuto_pouso,
                    hora_corte,
                    minuto_corte,
                    hora_tempo_voo,
                    minuto_tempo_voo,

                    hor_inicial_flight,
                    hor_final_flight,
                    tempo_voo_flight,
                    numero_pousos,

                    litros_combustivel,
                    galao_combustivel,

                    hor_inicial_hobbs,
                    hor_final_hobbs,
                    tempo_voo_hobbs,

                    hora_diurno,
                    minuto_diurno,
                    hora_noturno,
                    minuto_noturno,

                    hora_ifrr,
                    minuto_ifrr,
                    hora_ifrc,
                    minuto_ifrc,
                    hora_vfr,
                    minuto_vfr,

                    diario_bordo,
                    sobre_voo,
                    navegacao,

                    dry_wet,

                    observacoes,

                    data_liberacao,
                    hora_liberacao,
                    "RV", //Envia que foi feito pelo Registro de voo MOB004
                    "" //tempo de não voo que não se aplica à essa tela
            );

            tab_agenda = verificarNulos(tab_agenda);

            confirmarLiberacao(tab_agenda);

        }

    }


    //Verifica se tem campos NULL e substitui por EMPTY
    private Agenda verificarNulos(Agenda tab_agenda) {
        for (Field field : tab_agenda.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(tab_agenda) == null) {
                    field.set(tab_agenda, "0");
                }
            } catch (IllegalAccessException ex) {
                System.out.println("Verificar Nulos: " + ex.toString());
            }
        }

        return tab_agenda;
    }

    // Abre uma caixa de mensagem de confirmação
    private void confirmarLiberacao(final Agenda tab_agenda) {
        DialogInterface.OnClickListener diaglogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        verificarConexao(tab_agenda);
                        validarCorte();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004_D.this);
        String mensagem = "Confirma registro de vôo para o aluno " +
                "<b>" + nome_aluno + "</b>" + " ?";
        alert.setMessage(Html.fromHtml(mensagem));
        alert.setPositiveButton("SIM", diaglogClick);
        alert.setNegativeButton("NAO", diaglogClick);
        alert.setCancelable(false);
        alert.show();
    }

    //Verifica se o celular consegue se conectar ao Firebase
    private void verificarConexao(final Agenda tab_agenda) {
        firebaseDB.getReference(".info/connected")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue(Boolean.class);
                        if (connected) {
                            enviarFotos_Online();
                            gravarAgenda_Online(tab_agenda);
                            gravarPre_voo_Online(tab_agenda);
                        } else {
                            enviarFotos_Offline();
                            gravarAgenda_Offline(tab_agenda);
                            gravarPre_voo_Offline(tab_agenda);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertSmall("Conexão Cancelada!");
                    }
                });
    }

    //Verifica se foi feito o Corte para gerar ou não outra agenda
    private void validarCorte() {
        flag_corte = Boolean.parseBoolean(getIntent().getStringExtra("flag_corte"));

        if (flag_corte == false) {

            Intent intent = new Intent(getBaseContext(), MOB004.class);

            codigo_agenda = w_contador.getText().toString();

            codigo_pre_voo = getIntent().getStringExtra("cod_pre_voo");

            setExtra(intent);

            startActivity(intent);
        }
    }

    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", getIntent().getStringExtra("des_prg"));
        intent.putExtra("codigo_agenda_anterior", codigo_agenda);

        intent.putExtra("cod_pre_voo", codigo_pre_voo);

        //MOB004
        intent.putExtra("cod_cli", getIntent().getStringExtra("cod_cli"));
        intent.putExtra("nom_cli", getIntent().getStringExtra("nom_cli"));
        intent.putExtra("nom_cli_ins", getIntent().getStringExtra("nom_cli_ins"));
        intent.putExtra("cod_bem", getIntent().getStringExtra("cod_bem"));
        intent.putExtra("nom_bem", getIntent().getStringExtra("nom_bem"));
        intent.putExtra("cod_tip_voo", getIntent().getStringExtra("cod_tip_voo"));
        intent.putExtra("nom_tip_voo", getIntent().getStringExtra("nom_tip_voo"));
        intent.putExtra("cod_aer_ori", getIntent().getStringExtra("cod_aer_ori"));
        intent.putExtra("sig_aer_ori", getIntent().getStringExtra("sig_aer_ori"));
        intent.putExtra("cod_aer_des", getIntent().getStringExtra("cod_aer_des"));
        intent.putExtra("sig_aer_des", getIntent().getStringExtra("sig_aer_des"));

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

        intent.putExtra("mod_dry_wet", getIntent().getStringExtra("mod_dry_wet"));

    }

    // Grava na tabela Agenda do banco Firebase
    private void gravarAgenda_Online(final Agenda tab_agenda) {
        referenceDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    referenceDB.child(tab_agenda.getCod_age().toString()).setValue(tab_agenda);
                }

                w_btn_confirmar.setText("Registro Confirmado");
                w_btn_confirmar.setEnabled(false);


                Toasty.success(MOB004_D.this,
                        "Registro de voo enviado para a nuvem!",
                        Toast.LENGTH_SHORT, true).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    // Grava na tabela Agenda do banco SQLite
    private void gravarAgenda_Offline(Agenda tab_agenda) {
        try {

            bancoAgenda = new BancoAgenda(this);
            bancoAgenda.setAgenda(tab_agenda);

            w_btn_confirmar.setText("Registro Confirmado");
            w_btn_confirmar.setEnabled(false);

            Toasty.info(MOB004_D.this,
                    "Registro de voo salvo no banco offline!",
                    Toast.LENGTH_SHORT, true).show();

            System.out.println("Gravar Agenda Offline: " + tab_agenda.getCod_age());

            bancoAgenda.close();

        } catch (Exception ex) {
            System.out.println("Gravar Agenda: " + ex);
            alertMessage("Erro ao gravar agenda offline!");
        }

    }

    // Grava o codigo da agenda no campo da tabela Pre_voo do Firebase
    private void gravarPre_voo_Online(final Agenda tab_agenda) {
        final DatabaseReference referencePre_voo = firebaseDB.getReference("Pre_voo");

        referencePre_voo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Pre_voo tab_pre_voo = retorno.getValue(Pre_voo.class);

                    if ((tab_pre_voo.getCod_pre_voo() == tab_agenda.getCod_pre_voo()) &&
                            (tab_pre_voo.getCod_age() == Long.parseLong("0"))) {

                        tab_pre_voo.setCod_age(tab_agenda.getCod_age());

                        referencePre_voo.child(tab_pre_voo.getCod_pre_voo().toString()).setValue(tab_pre_voo);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    // Grava na tabela Pre_voo do banco SQLite
    private void gravarPre_voo_Offline(Agenda tab_agenda) {
        try {

            BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(this);

            ArrayList<Pre_voo> retornoPre_voo = bancoRegistroVoo.getPre_voo();

            for (int i = 0; i < retornoPre_voo.size(); i++) {
                Pre_voo tab_pre_voo = new Pre_voo();

                Long cod_pre_voo = retornoPre_voo.get(i).getCod_pre_voo();
                Long cod_age = retornoPre_voo.get(i).getCod_age();
                Long cod_cli = retornoPre_voo.get(i).getCod_cli();
                String nom_cli = retornoPre_voo.get(i).getNom_cli();
                Long cod_bem = retornoPre_voo.get(i).getCod_bem();
                String pre_bem = retornoPre_voo.get(i).getPre_bem();
                Long cod_tip_voo = retornoPre_voo.get(i).getCod_tip_voo();
                String nom_tip_voo = retornoPre_voo.get(i).getNom_tip_voo();
                Long cod_aer_ori = retornoPre_voo.get(i).getCod_aer_ori();
                String sig_aer_ori = retornoPre_voo.get(i).getSig_aer_ori();
                Long cod_aer_des = retornoPre_voo.get(i).getCod_aer_des();
                String sig_aer_des = retornoPre_voo.get(i).getSig_aer_ori();
                String cod_cli_ins = retornoPre_voo.get(i).getCod_cli_ins();

                tab_pre_voo.setCod_pre_voo(cod_pre_voo);
                tab_pre_voo.setCod_age(cod_age);
                tab_pre_voo.setCod_cli(cod_cli);
                tab_pre_voo.setNom_cli(nom_cli);
                tab_pre_voo.setCod_bem(cod_bem);
                tab_pre_voo.setPre_bem(pre_bem);
                tab_pre_voo.setCod_tip_voo(cod_tip_voo);
                tab_pre_voo.setNom_tip_voo(nom_tip_voo);
                tab_pre_voo.setCod_aer_ori(cod_aer_ori);
                tab_pre_voo.setSig_aer_ori(sig_aer_ori);
                tab_pre_voo.setCod_aer_des(cod_aer_des);
                tab_pre_voo.setSig_aer_des(sig_aer_des);
                tab_pre_voo.setCod_cli_ins(cod_cli_ins);

                String pre_voo_codigo = String.valueOf(tab_pre_voo.getCod_pre_voo());
                String agenda_pre_voo = String.valueOf(tab_agenda.getCod_pre_voo());
                String pre_voo_agenda = String.valueOf(tab_pre_voo.getCod_age());

                if (pre_voo_codigo.equals(agenda_pre_voo) &&
                        (pre_voo_agenda.equals("0"))) {

                    tab_pre_voo.setCod_age(tab_agenda.getCod_age());

                    bancoRegistroVoo.updatePre_voo(tab_pre_voo);

                }
            }

            bancoRegistroVoo.close();

        } catch (Exception ex) {
            System.out.println("Gravar Agenda: " + ex);
            alertMessage("Erro ao gravar agenda offline!");
        }

    }

    //Envia as fotos para a nuvem
    private void enviarFotos_Online() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoFotos bancoFotos = new BancoFotos(this);

        final ArrayList<Fotos> dadosFotos = bancoFotos.getFotos();

        String codigo_agenda_temporaria = getIntent().getStringExtra("codigo_agenda");

        //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
        for (int i = 0; i < dadosFotos.size(); i++) {

            if (dadosFotos.get(i).getCodigo_registro().equals(codigo_agenda_temporaria)) {
                String nome_foto = "AGENDA_" + w_contador.getText().toString() + "_" + dadosFotos.get(i).getNome_arquivo();
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

    // Grava na tabela Agenda do banco SQLite
    private void enviarFotos_Offline() {
        try {

            bancoAgenda = new BancoAgenda(this);

            BancoFotos bancoFotos = new BancoFotos(this);

            final ArrayList<Fotos> dadosFotos = bancoFotos.getFotos();

            String codigo_agenda_temporaria = getIntent().getStringExtra("codigo_agenda");

            //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
            for (int i = 0; i < dadosFotos.size(); i++) {

                if (dadosFotos.get(i).getCodigo_registro().equals(codigo_agenda_temporaria)) {

                    String agenda = w_contador.getText().toString();
                    String nome_foto = "AGENDA_" + agenda + "_" + dadosFotos.get(i).getNome_arquivo();
                    Uri caminho_foto = Uri.parse(dadosFotos.get(i).getCaminho_foto());

                    bancoAgenda.setFotos(agenda, nome_foto, String.valueOf(caminho_foto));
                }
            }

            bancoAgenda.close();

        } catch (Exception ex) {
            System.out.println("Enviar Fotos: " + ex);
            alertMessage("Erro ao gravar fotos offline!");
        }

    }

    private void iniciarCamera() {
        FragmentManager fragment = getSupportFragmentManager();
        TirarFotoFragment alertDialog = TirarFotoFragment.newInstance("Foto");

        String codigo_agenda_temporaria = getIntent().getStringExtra("codigo_agenda");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", codigo_agenda_temporaria);

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
                Toasty.info(MOB004_D.this,
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
        Toast.makeText(MOB004_D.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004_D.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
