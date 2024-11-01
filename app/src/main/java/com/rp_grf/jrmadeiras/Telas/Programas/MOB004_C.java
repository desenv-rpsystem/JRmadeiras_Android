package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.rp_grf.jrmadeiras.Utils.DecimalDigitsInputFilter;
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MOB004_C extends AppCompatActivity {

    Boolean flag_online;

    Boolean flag_diario;
    Boolean flag_naveg;

    Boolean flag_noturno_timepicker;
    Boolean flag_vfr_timepicker;

    String descricao_programa;
    String codigo_agenda;
    String codigo_agenda_anterior;

    String codigo_pre_voo;

    String flag_partida;
    String flag_corte;

    String codigo_aluno;
    String nome_aluno;
    String nome_instrutor;
    String codigo_aeronave;
    String nome_aeronave;
    String codigo_tipo_voo;
    String nome_tipo_voo;
    String codigo_origem;
    String sigla_origem;
    String codigo_destino;
    String sigla_destino;

    String data_voo;
    String hora_partida;
    String minuto_partida;
    String hora_decolagem;
    String minuto_decolagem;
    String hora_pouso;
    String minuto_pouso;
    String hora_corte;
    String minuto_corte;

    String hor_inicial_flight;
    String hor_final_flight;
    String tempo_voo_flight;
    String numero_pousos;

    String litros_combustivel;
    String galao_combustivel;

    String hor_inicial_hobbs;
    String hor_final_hobbs;
    String tempo_voo_hobbs;

    String tempo_hobbs_diurno;

    String hora_diurno;
    String minuto_diurno;
    String hora_noturno;
    String minuto_noturno;

    String hora_ifrr;
    String minuto_ifrr;
    String hora_ifrc;
    String minuto_ifrc;
    String hora_vfr;
    String minuto_vfr;

    String diario_bordo;
    String sobre_voo;
    String navegacao;

    String dry_wet;

    String observacoes;

    EditText w_edit_hor_inicial;
    EditText w_edit_hor_final;
    EditText w_edit_tempo_voo;

    TextView w_hora_diurno;
    TextView w_hora_noturno;

    CardView w_botao_diurno;
    CardView w_botao_noturno;

    CheckBox w_checkbox_noturno;
    CheckBox w_checkbox_ifr;

    TextView w_hora_ifrr;
    TextView w_hora_ifrc;
    TextView w_hora_vfr;

    CardView w_botao_ifrr;
    CardView w_botao_ifrc;
    CardView w_botao_vfr;

    EditText w_edit_diario_bordo_numero;
    EditText w_edit_diario_bordo_pagina;
    EditText w_edit_diario_bordo_linha;

    EditText w_edit_sobre_voo;

    CheckBox w_checkbox_naveg;

    EditText w_edit_observacoes;

    CheckBox w_checkbox_dry;
    CheckBox w_checkbox_wet;

    ImageView w_icone_diurno;
    ImageView w_icone_noturno;
    ImageView w_icone_ifrr;
    ImageView w_icone_vfr;

    Button w_btn_seguinte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob004_c);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getExtra();

        flag_diario = false;
        flag_naveg = false;

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        iniciarCampos();

        w_edit_hor_inicial.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        filtrarCasasDecimais();

        cliqueBotao();

        travarDiurno();

        travarVFR();

        calcularDiurno(false);

        calcularVfr(false);

        habilitarNoturno(false);

        habilitarIFR(false);

        calcularTempoVoo();

        focusChange();

        iniciarProximaAgenda();

        iniciarAgendaTemporaria();

        System.out.println("MOB004_C - cod_pre_voo " + codigo_pre_voo);

    }

    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", descricao_programa);
        intent.putExtra("codigo_agenda", codigo_agenda);

        intent.putExtra("cod_pre_voo", codigo_pre_voo);

        intent.putExtra("flag_partida", flag_partida);
        intent.putExtra("flag_corte", flag_corte);

        intent.putExtra("cod_cli", codigo_aluno);
        intent.putExtra("cod_bem", codigo_aeronave);
        intent.putExtra("cod_tip_voo", codigo_tipo_voo);
        intent.putExtra("cod_aer_des", codigo_destino);
        intent.putExtra("cod_aer_ori", codigo_origem);

        intent.putExtra("nom_cli", nome_aluno);
        intent.putExtra("nom_cli_ins", nome_instrutor);
        intent.putExtra("nom_bem", nome_aeronave);
        intent.putExtra("nom_tip_voo", nome_tipo_voo);
        intent.putExtra("sig_aer_ori", sigla_origem);
        intent.putExtra("sig_aer_des", sigla_destino);

        intent.putExtra("dat_reg_voo", data_voo);
        intent.putExtra("hor_par", hora_partida);
        intent.putExtra("min_par", minuto_partida);
        intent.putExtra("hor_dec", hora_decolagem);
        intent.putExtra("min_dec", minuto_decolagem);
        intent.putExtra("hor_pou", hora_pouso);
        intent.putExtra("min_pou", minuto_pouso);
        intent.putExtra("hor_cor", hora_corte);
        intent.putExtra("min_cor", minuto_corte);

        intent.putExtra("hor_ini_fli", hor_inicial_flight);
        intent.putExtra("hor_fin_fli", hor_final_flight);
        intent.putExtra("tmp_voo_fli", tempo_voo_flight);
        intent.putExtra("num_pou", numero_pousos);

        intent.putExtra("lit_com", litros_combustivel);
        intent.putExtra("gal_com", galao_combustivel);

        intent.putExtra("hor_ini_hob", hor_inicial_hobbs);
        intent.putExtra("hor_fin_hob", hor_final_hobbs);
        intent.putExtra("tmp_voo_hob", tempo_voo_hobbs);

        intent.putExtra("hor_diu", hora_diurno);
        intent.putExtra("min_diu", minuto_diurno);
        intent.putExtra("hor_not", hora_noturno);
        intent.putExtra("min_not", minuto_noturno);

        intent.putExtra("hor_ifrr", hora_ifrr);
        intent.putExtra("min_ifrr", minuto_ifrr);
        intent.putExtra("hor_ifrc", hora_ifrc);
        intent.putExtra("min_ifrc", minuto_ifrc);
        intent.putExtra("hor_vfr", hora_vfr);
        intent.putExtra("min_vfr", minuto_vfr);

        intent.putExtra("inf_dia_bor", diario_bordo);
        intent.putExtra("sob_voo_mn", sobre_voo);
        intent.putExtra("flg_nav", navegacao);

        intent.putExtra("mod_dry_wet", dry_wet);

        intent.putExtra("obs_reg_voo", observacoes);
    }

    private void getExtra() {
        codigo_agenda = getIntent().getStringExtra("codigo_agenda");
        codigo_agenda_anterior = getIntent().getStringExtra("codigo_agenda_anterior");

        codigo_pre_voo = getIntent().getStringExtra("cod_pre_voo");

        //MOB004
        codigo_aluno = getIntent().getStringExtra("cod_cli");
        nome_aluno = getIntent().getStringExtra("nom_cli");
        nome_instrutor = getIntent().getStringExtra("nom_cli_ins");
        codigo_aeronave = getIntent().getStringExtra("cod_bem");
        nome_aeronave = getIntent().getStringExtra("nom_bem");
        codigo_tipo_voo = getIntent().getStringExtra("cod_tip_voo");
        nome_tipo_voo = getIntent().getStringExtra("nom_tip_voo");
        codigo_origem = getIntent().getStringExtra("cod_aer_ori");
        sigla_origem = getIntent().getStringExtra("sig_aer_ori");
        codigo_destino = getIntent().getStringExtra("cod_aer_des");
        sigla_destino = getIntent().getStringExtra("sig_aer_des");

        //MOB004_A
        data_voo = getIntent().getStringExtra("dat_reg_voo");
        hora_partida = getIntent().getStringExtra("hor_par");
        minuto_partida = getIntent().getStringExtra("min_par");
        hora_decolagem = getIntent().getStringExtra("hor_dec");
        minuto_decolagem = getIntent().getStringExtra("min_dec");
        hora_pouso = getIntent().getStringExtra("hor_pou");
        minuto_pouso = getIntent().getStringExtra("min_pou");
        hora_corte = getIntent().getStringExtra("hor_cor");
        minuto_corte = getIntent().getStringExtra("min_cor");
        flag_partida = getIntent().getStringExtra("flag_partida");
        flag_corte = getIntent().getStringExtra("flag_corte");

        //MOB004_B
        hor_inicial_flight = getIntent().getStringExtra("hor_ini_fli");
        hor_final_flight = getIntent().getStringExtra("hor_fin_fli");
        tempo_voo_flight = getIntent().getStringExtra("tmp_voo_fli");
        numero_pousos = getIntent().getStringExtra("num_pou");

        litros_combustivel = getIntent().getStringExtra("lit_com");
        galao_combustivel = getIntent().getStringExtra("gal_com");

        //MOB004_C
        hor_inicial_hobbs = getIntent().getStringExtra("hor_ini_hob");
        hor_final_hobbs = getIntent().getStringExtra("hor_fin_hob");
        tempo_voo_hobbs = getIntent().getStringExtra("tmp_voo_hob");

        hora_diurno = getIntent().getStringExtra("hor_diu");
        minuto_diurno = getIntent().getStringExtra("min_diu");
        hora_noturno = getIntent().getStringExtra("hor_not");
        minuto_noturno = getIntent().getStringExtra("min_not");

        hora_ifrr = getIntent().getStringExtra("hor_ifrr");
        minuto_ifrr = getIntent().getStringExtra("min_ifrr");
        hora_ifrc = getIntent().getStringExtra("hor_ifrc");
        minuto_ifrc = getIntent().getStringExtra("min_ifrc");
        hora_vfr = getIntent().getStringExtra("hor_vfr");
        minuto_vfr = getIntent().getStringExtra("min_vfr");

        diario_bordo = getIntent().getStringExtra("inf_dia_bor");
        sobre_voo = getIntent().getStringExtra("sob_voo_mn");
        navegacao = getIntent().getStringExtra("flg_nav");

        dry_wet = getIntent().getStringExtra("mod_dry_wet");

        observacoes = getIntent().getStringExtra("obs_reg_voo");

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

        w_edit_hor_inicial = (EditText) findViewById(R.id.edit_text_hobbs_inicial);
        w_edit_hor_final = (EditText) findViewById(R.id.edit_text_hobbs_final);

        w_edit_tempo_voo = (EditText) findViewById(R.id.edit_text_hobbs_tempo_voo);

        w_checkbox_noturno = (CheckBox) findViewById(R.id.checkbox_noturno);

        w_hora_diurno = (TextView) findViewById(R.id.txt_diurno);
        w_hora_noturno = (TextView) findViewById(R.id.txt_noturno);

        w_botao_diurno = (CardView) findViewById(R.id.card_view_botao_diurno);
        w_botao_noturno = (CardView) findViewById(R.id.card_view_botao_noturno);

        w_checkbox_ifr = (CheckBox) findViewById(R.id.checkbox_ifr);

        w_hora_ifrr = (TextView) findViewById(R.id.txt_ifrr);
        //w_hora_ifrc = (TextView) findViewById(R.id.txt_ifrc);
        w_hora_vfr = (TextView) findViewById(R.id.txt_vfr);

        w_botao_ifrr = (CardView) findViewById(R.id.card_view_botao_ifrr);
        //w_botao_ifrc = (CardView) findViewById(R.id.card_view_botao_ifrc);
        w_botao_vfr = (CardView) findViewById(R.id.card_view_botao_vfr);

        w_edit_diario_bordo_numero = (EditText) findViewById(R.id.edit_text_diario_bordo_numero);
        w_edit_diario_bordo_pagina = (EditText) findViewById(R.id.edit_text_diario_bordo_pagina);
        w_edit_diario_bordo_linha = (EditText) findViewById(R.id.edit_text_diario_bordo_linha);

        w_edit_sobre_voo = (EditText) findViewById(R.id.edit_text_sobre_voo);

        w_checkbox_naveg = (CheckBox) findViewById(R.id.checkbox_naveg);

        w_edit_observacoes = (EditText) findViewById(R.id.edit_text_hobbs_observacoes);

        w_checkbox_dry = (CheckBox) findViewById(R.id.checkbox_dry);
        w_checkbox_wet = (CheckBox) findViewById(R.id.checkbox_wet);

        w_icone_diurno = (ImageView) findViewById(R.id.image_view_registro_voo_diurno);
        w_icone_noturno = (ImageView) findViewById(R.id.image_view_registro_voo_noturno);
        w_icone_ifrr = (ImageView) findViewById(R.id.image_view_registro_voo_ifrr);
        w_icone_vfr = (ImageView) findViewById(R.id.image_view_registro_voo_vfr);

        w_btn_seguinte = (Button) findViewById(R.id.button_seguinte);
    }

    //Preenche os campos com os dados salvos na tabela temporaria
    private void iniciarAgendaTemporaria() {
        BancoAgendaTemp bancoAgendaTemp = new BancoAgendaTemp(this);

        try {

            final ArrayList<Agenda> dadosAgendaTemp = bancoAgendaTemp.getAgenda();

            //Percorre os arraylist das agendas salvos na tabela temporaria
            for (int i = 0; i <= dadosAgendaTemp.size(); i++) {

                //Compara se o código das agendas salvas é o mesmo da agenda atual
                if (dadosAgendaTemp.get(i).getCod_age().toString().equals(codigo_agenda)) {
                    //Só preenche se tiver um tempo de voo salvo
                    if (dadosAgendaTemp.get(i).getTmp_voo_hob() != null) {

                        if (dadosAgendaTemp.get(i).getHor_ini_hob() != null) {
                            w_edit_hor_inicial.setText(dadosAgendaTemp.get(i).getHor_ini_hob());
                        }

                        w_edit_hor_final.setText(dadosAgendaTemp.get(i).getHor_fin_hob());

                        calcularTempoVoo();

                        //

                        //Força o click do checkbox caso o horário seja diferente de 00 : 00
                        if (!w_hora_noturno.getText().toString().equals("00 : 00")) {
                            w_checkbox_noturno.performClick();
                        }

                        w_hora_diurno.setText(dadosAgendaTemp.get(i).getHor_diu() + " : "
                                + dadosAgendaTemp.get(i).getMin_diu());

                        w_hora_noturno.setText(dadosAgendaTemp.get(i).getHor_not() + " : "
                                + dadosAgendaTemp.get(i).getMin_not());

                        //

                        w_hora_ifrr.setText(dadosAgendaTemp.get(i).getHor_ifrr() + " : "
                                + dadosAgendaTemp.get(i).getMin_ifrr());

                        /*
                        w_hora_ifrc.setText(dadosAgendaTemp.get(i).getHor_ifrc() + " : "
                                + dadosAgendaTemp.get(i).getMin_ifrc());

                         */

                        //Força o click do checkbox caso o horário seja diferente de 00 : 00
                        if (!w_hora_ifrr.getText().toString().equals("00 : 00")) {
                            w_checkbox_ifr.performClick();
                        }

                        w_hora_vfr.setText(dadosAgendaTemp.get(i).getHor_vfr() + " : "
                                + dadosAgendaTemp.get(i).getMin_vfr());

                        //

                        String numero = "";
                        String pagina = "";
                        String linha = "";

                        String diario = dadosAgendaTemp.get(i).getInf_dia_bor();

                        int contador_numero = 0;
                        int contador_pagina = 0;
                        int contador_linha = 0;

                        //percorre a string de diario até a primeira barra
                        while (contador_numero < diario.length()) {
                            if (diario.charAt(contador_numero) == '/') {
                                contador_pagina = contador_numero + 1;
                                break;
                            } else {
                                //grava o caractere no numero
                                numero = numero + diario.charAt(contador_numero);
                                contador_numero++;
                            }
                        }

                        //percorre a string de diario até a segunda barra
                        while (contador_pagina < diario.length()) {
                            if (diario.charAt(contador_pagina) == '/') {
                                contador_linha = contador_pagina + 1;
                                break;
                            } else {
                                //grava o caractere na pagina
                                pagina = pagina + diario.charAt(contador_pagina);
                                contador_pagina++;
                            }
                        }

                        //percorre o final da string de diario
                        while (contador_linha < diario.length()) {
                            //grava o caractere na linha
                            linha = linha + diario.charAt(contador_linha);
                            contador_linha++;
                        }

                        w_edit_diario_bordo_numero.setText(numero.trim());
                        w_edit_diario_bordo_pagina.setText(pagina.trim());
                        w_edit_diario_bordo_linha.setText(linha.trim());

                        //

                        w_edit_sobre_voo.setText(dadosAgendaTemp.get(i).getSob_voo_mn());

                        //

                        flag_naveg = Boolean.parseBoolean(dadosAgendaTemp.get(i).getFlg_nav());
                        //Força o click do checkbox se a flag for true
                        if (flag_naveg == true) {
                            w_checkbox_naveg.performClick();
                        }

                        //

                        dry_wet = dadosAgendaTemp.get(i).getMod_dry_wet();

                        if (dry_wet.equals("DRY")) {
                            w_checkbox_dry.performClick();
                        } else if (dry_wet.equals("WET")) {
                            w_checkbox_wet.performClick();
                        }


                        //

                        w_edit_observacoes.setText(dadosAgendaTemp.get(i).getObs_reg_voo());

                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MOB004_C - Não foi possível iniciar uma nova agenda temporaria:\n" + ex.toString());
        }

    }

    // Salva no banco SQLite os dados temporarios da agenda
    private void salvarAgendaTemporaria() {
        BancoAgendaTemp bancoAgendaTemp = new BancoAgendaTemp(this);

        divideHorasMinutos(); //Chama o método que separa hora e minuto

        final ArrayList<Agenda> dadosAgendaTemp = bancoAgendaTemp.getAgenda();

        Agenda tab_agenda = new Agenda();

        for (int i = 0; i < dadosAgendaTemp.size(); i++) {
            if (dadosAgendaTemp.get(i).getCod_age().toString().equals(codigo_agenda)) {
                tab_agenda = dadosAgendaTemp.get(i);
            }
        }

        tab_agenda.setCod_age(Long.parseLong(codigo_agenda));

        tab_agenda.setHor_ini_hob(w_edit_hor_inicial.getText().toString());
        tab_agenda.setHor_fin_hob(w_edit_hor_final.getText().toString());
        tab_agenda.setTmp_voo_hob(w_edit_tempo_voo.getText().toString());

        divideHorasMinutos();

        tab_agenda.setHor_diu(hora_diurno);
        tab_agenda.setMin_diu(minuto_diurno);
        tab_agenda.setHor_not(hora_noturno);
        tab_agenda.setMin_not(minuto_noturno);

        tab_agenda.setHor_ifrr(hora_ifrr);
        tab_agenda.setMin_ifrr(minuto_ifrr);
        tab_agenda.setHor_ifrc(hora_ifrc);
        tab_agenda.setMin_ifrc(minuto_ifrc);
        tab_agenda.setHor_vfr(hora_vfr);
        tab_agenda.setMin_vfr(minuto_vfr);

        diario_bordo = w_edit_diario_bordo_numero.getText().toString() + " / " +
                w_edit_diario_bordo_pagina.getText().toString() + " / " +
                w_edit_diario_bordo_linha.getText().toString();

        diario_bordo = diario_bordo.replace(".", "");

        tab_agenda.setInf_dia_bor(diario_bordo);

        sobre_voo = w_edit_sobre_voo.getText().toString();

        tab_agenda.setSob_voo_mn(sobre_voo);

        navegacao = flag_naveg.toString();

        tab_agenda.setFlg_nav(navegacao);

        tab_agenda.setMod_dry_wet(dry_wet);

        observacoes = w_edit_observacoes.getText().toString();

        tab_agenda.setObs_reg_voo(observacoes);

        bancoAgendaTemp.updateAgenda(tab_agenda);
    }


    private void iniciarProximaAgenda() {
        //Só executa o método se for uma agenda continuação de outra
        if (codigo_agenda_anterior != null) {
            w_edit_hor_inicial.setText(hor_final_hobbs);

            w_edit_hor_inicial.setEnabled(false);
        }
    }

    //Método que muda a cor do botão de diurno e desabilita
    private void travarDiurno() {

        w_botao_diurno.setEnabled(false);

        w_botao_diurno.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_diurno.setImageResource(R.drawable.icone_relogio);

    }


    //Método que muda a cor dos botões de IFR e desabilita
    private void travarVFR() {

        w_botao_vfr.setEnabled(false);

        w_botao_vfr.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_vfr.setImageResource(R.drawable.icone_relogio);

    }

    //Filtra as casas decimais que o usuário pode digitar no campo
    private void filtrarCasasDecimais() {
        w_edit_hor_inicial.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 1)});

        w_edit_hor_final.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 1)});

        w_edit_diario_bordo_numero.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(3, 1)});

        w_edit_diario_bordo_pagina.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(3, 1)});

        w_edit_diario_bordo_linha.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(2, 1)});
    }

    private void cliqueBotao() {
        w_botao_diurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_diurno); //Chama o Dialog de horario no clique do botão
            }
        });

        w_botao_noturno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_noturno_timepicker = false;

                timePickerNoturno((TextView) w_hora_noturno); //Chama o Dialog de horario no clique do botão
            }
        });

        w_botao_ifrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_vfr_timepicker = false;

                timePickerVFR((TextView) w_hora_ifrr); //Chama o Dialog de horario no clique do botão
            }
        });

        /* Decomentar o botão de IFRC se precisar
        w_botao_ifrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_ifrc); //Chama o Dialog de horario no clique do botão
            }
        });
        */

        w_botao_vfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_vfr); //Chama o Dialog de horario no clique do botão
            }
        });

        w_checkbox_noturno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v);
            }
        });

        w_checkbox_ifr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v);
            }
        });

        w_checkbox_naveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v);
            }
        });

        w_checkbox_dry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v);
            }
        });

        w_checkbox_wet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v);
            }
        });

        w_btn_seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hor_inicial_hobbs = w_edit_hor_inicial.getText().toString();
                hor_final_hobbs = w_edit_hor_final.getText().toString();
                tempo_voo_hobbs = w_edit_tempo_voo.getText().toString();

                diario_bordo = w_edit_diario_bordo_numero.getText().toString() + " / " +
                        w_edit_diario_bordo_pagina.getText().toString() + " / " +
                        w_edit_diario_bordo_linha.getText().toString();

                diario_bordo = diario_bordo.replace(".", "");

                sobre_voo = w_edit_sobre_voo.getText().toString();

                navegacao = flag_naveg.toString();

                observacoes = w_edit_observacoes.getText().toString();

                divideHorasMinutos();

                Boolean validado = validarCampos();

                if (validado == true) {

                    if (validarDiurnoNoturno() == true) {

                        Intent intent = new Intent(getBaseContext(), MOB004_D.class);

                        setExtra(intent);

                        salvarAgendaTemporaria();

                        startActivity(intent);

                    }
                }
            }
        });
    }

    private void cliqueCheckBox(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // Verifica se a checkbox está marcada
        switch (view.getId()) {
            case R.id.checkbox_noturno:
                if (checked) {
                    habilitarNoturno(true);
                } else {
                    habilitarNoturno(false);
                    calcularDiurno(false);
                }
                break;
            case R.id.checkbox_ifr:
                if (checked) {
                    habilitarIFR(true);
                } else {
                    habilitarIFR(false);
                    calcularVfr(false);
                }
                break;
            case R.id.checkbox_naveg:
                if (checked) {
                    flag_naveg = true;
                } else {
                    flag_naveg = false;
                }
                break;
            case R.id.checkbox_dry:
                if (checked) {
                    dry_wet = "DRY";
                    w_checkbox_wet.setChecked(false);
                }
                break;
            case R.id.checkbox_wet:
                if (checked) {
                    dry_wet = "WET";
                    w_checkbox_dry.setChecked(false);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

    //Método que muda a cor do botão de noturno e habilita ou desabilita
    private void habilitarNoturno(boolean marcado) {

        w_botao_noturno.setEnabled(marcado);

        if (marcado == true) {
            w_botao_noturno.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            w_icone_noturno.setImageResource(R.drawable.icone_relogio_branco);
        } else {
            w_hora_noturno.setText("00 : 00");
            w_botao_noturno.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_noturno.setImageResource(R.drawable.icone_relogio);
        }

    }

    //Calcula o horario de diurno caso tenha noturno
    private void calcularDiurno(boolean flag_noturno) {

        boolean corte = Boolean.parseBoolean(flag_corte);

        divideHorasMinutos();  //Chama o método que divide horas e minutos dos campos

        String horario_partida = hora_partida + ":" + minuto_partida;
        String horario_decolagem = hora_decolagem + ":" + minuto_decolagem;
        String horario_pouso = hora_pouso + ":" + minuto_pouso;
        String horario_corte = hora_corte + ":" + minuto_corte;

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); //se colocar hh minusculo, calcula pelo sistema AM PM

            Date time_partida = simpleDateFormat.parse(horario_partida);
            Date time_decolagem = simpleDateFormat.parse(horario_decolagem);
            Date time_pouso = simpleDateFormat.parse(horario_pouso);
            Date time_corte = simpleDateFormat.parse(horario_corte);

            long calculo_time;

            //Verifica se foi feito corte ou não
            if (corte == true) {

                //verifica se tem partida
                if (flag_partida.equals("true")) {
                    calculo_time = time_corte.getTime() - time_partida.getTime(); // corte - partida
                } else {
                    calculo_time = time_corte.getTime() - time_decolagem.getTime(); // corte - decolagem
                }

            }
            //Chama esse se não foi feito corte
            else {

                //verifica se tem partida
                if (flag_partida.equals("true")) {
                    calculo_time = time_pouso.getTime() - time_partida.getTime(); // pouso - partida
                } else {
                    calculo_time = time_pouso.getTime() - time_decolagem.getTime(); // pouso - decolagem
                }

            }

            int hora_hobbs = (int) (calculo_time / (1000 * 60 * 60));
            int minuto_hobbs = (int) ((calculo_time / (1000 * 60)) % 60);

            String hora_hobbs_decimal = String.valueOf(hora_hobbs);

            if (hora_hobbs <= 9) {
                //adiciona '0' no inicio da hora se o número for menor que 10
                hora_hobbs_decimal = "0" + hora_hobbs;
            }

            String minuto_hobbs_decimal = String.valueOf(minuto_hobbs);

            if (minuto_hobbs <= 9) {
                //adiciona '0' no inicio do minuto se o número for menor que 10
                minuto_hobbs_decimal = "0" + minuto_hobbs;
            }

            tempo_hobbs_diurno = hora_hobbs_decimal + " : " + minuto_hobbs_decimal;

            if (flag_noturno == true) {
                calcularNoturno(tempo_hobbs_diurno);
            } else {
                w_hora_diurno.setText(tempo_hobbs_diurno);
            }

        } catch (Exception ex) {
            System.out.println("Calcular Diurno: " + ex.toString());
        }

    }

    //Método que calcula o tempo diurno subtraindo do tempo noturno
    private void calcularNoturno(String tempo_hobbs_diurno) {

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm"); //se colocar hh minusculo, calcula pelo sistema AM PM

            Date time_noturno = simpleDateFormat.parse(w_hora_noturno.getText().toString());
            Date time_hobbs = simpleDateFormat.parse(tempo_hobbs_diurno);

            long calculo_noturno = time_hobbs.getTime() - time_noturno.getTime();

            int hora_noturno = (int) (calculo_noturno / (1000 * 60 * 60));
            int minuto_noturno = (int) ((calculo_noturno / (1000 * 60)) % 60);

            String hora_noturno_decimal = String.valueOf(hora_noturno);

            if (hora_noturno <= 9) {
                //adiciona '0' no inicio da hora se o número for menor que 10
                hora_noturno_decimal = "0" + hora_noturno;
            }

            String minuto_noturno_decimal = String.valueOf(minuto_noturno);

            if (minuto_noturno <= 9) {
                //adiciona '0' no inicio do minuto se o número for menor que 10
                minuto_noturno_decimal = "0" + minuto_noturno;
            }

            String tempo_diurno = hora_noturno_decimal + " : " + minuto_noturno_decimal;

            if ((hora_noturno < 0) || (minuto_noturno < 0)) {
                alertMessage("Tempo de voo noturno inválido!");
                w_hora_noturno.setText("00 : 00");
            } else {
                w_hora_diurno.setText(tempo_diurno);
            }

        } catch (Exception ex) {
            System.out.println("Calcular Noturno: " + ex.toString());
        }


    }

    //Método que muda a cor do botão de IFRR e habilita ou desabilita
    private void habilitarIFR(boolean marcado) {

        w_botao_ifrr.setEnabled(marcado);

        if (marcado == true) {
            w_botao_ifrr.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            w_icone_ifrr.setImageResource(R.drawable.icone_relogio_branco);
        } else {
            w_hora_ifrr.setText("00 : 00");
            w_botao_ifrr.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_ifrr.setImageResource(R.drawable.icone_relogio);
        }
    }

    private void calcularVfr(boolean flag_ifr) {

        //Colocar um método de atualizaTempoHobbs e chamar os dois métodos de calcular

        try {

            divideHorasMinutos();  //Chama o método que divide horas e minutos dos campos

            if (flag_ifr == true) {

                String horario_IFR = hora_ifrr + ":" + minuto_ifrr;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); //se colocar hh minusculo, calcula pelo sistema AM PM

                Date time_IFR = simpleDateFormat.parse(horario_IFR);
                Date time_hobbs = simpleDateFormat.parse(tempo_hobbs_diurno.replaceAll("\\s+", ""));

                long calculo_time = time_hobbs.getTime() - time_IFR.getTime();

                int hora_vfr = (int) (calculo_time / (1000 * 60 * 60));
                int minuto_vfr = (int) ((calculo_time / (1000 * 60)) % 60);

                String hora_vfr_decimal = String.valueOf(hora_vfr);

                if (hora_vfr <= 9) {
                    //adiciona '0' no inicio da hora se o número for menor que 10
                    hora_vfr_decimal = "0" + hora_vfr;
                }

                String minuto_vfr_decimal = String.valueOf(minuto_vfr);

                if (minuto_vfr <= 9) {
                    //adiciona '0' no inicio do minuto se o número for menor que 10
                    minuto_vfr_decimal = "0" + minuto_vfr;
                }

                String tempo_vfr = hora_vfr_decimal + " : " + minuto_vfr_decimal;

                if ((hora_vfr < 0) || (minuto_vfr < 0)) {
                    alertMessage("IFR inválido!");
                    w_hora_ifrr.setText("00 : 00");
                } else {
                    w_hora_vfr.setText(tempo_vfr);
                }

            } else {
                w_hora_vfr.setText(tempo_hobbs_diurno);
            }

        } catch (Exception ex) {
            System.out.println("calcular VFR " + ex);
        }

    }

    //Método que atribui o horário separado ás variaveis
    private void divideHorasMinutos() {
        //Chama o método que separa Hora e Minuto do campo Diurno
        String[] arrayDiurno = separaHorario((TextView) w_hora_diurno);

        hora_diurno = arrayDiurno[0];
        minuto_diurno = arrayDiurno[1];

        //Chama o método que separa Hora e Minuto do campo Noturno
        String[] arrayNoturno = separaHorario((TextView) w_hora_noturno);

        hora_noturno = arrayNoturno[0];
        minuto_noturno = arrayNoturno[1];

        //Chama o método que separa Hora e Minuto de IFRR
        String[] arrayIFRR = separaHorario((TextView) w_hora_ifrr);

        hora_ifrr = arrayIFRR[0];
        minuto_ifrr = arrayIFRR[1];

        /* Apagar aqui se for usar novamente esse campo
        //Chama o método que separa Hora e Minuto de IFRC
        String[] arrayIFRC = separaHorario((TextView) w_hora_ifrc);

        hora_ifrc = arrayIFRC[0];
        minuto_ifrc = arrayIFRC[1];
        */

        hora_ifrc = "00";
        minuto_ifrc = "00";

        //Chama o método que separa Hora e Minuto de VFR
        String[] arrayVFR = separaHorario((TextView) w_hora_vfr);

        hora_vfr = arrayVFR[0];
        minuto_vfr = arrayVFR[1];
    }

    private boolean validarCampos() {

        String tempo_voo = w_edit_tempo_voo.getText().toString();

        tempo_voo = tempo_voo.replace(".", ""); //Apaga o ponto do começo

        tempo_voo = tempo_voo.replace(",", "."); //Troca a virgula por ponto

        tempo_voo_hobbs = tempo_voo;

        //

        //Verifica se o tempo de vôo é menor que zero
        if (Float.parseFloat(tempo_voo) <= 0) {

            alertMessage("Tempo de voo inválido!");
            return false;

            //Verifica se os campos estão preenchidos
        } else if (w_edit_hor_inicial.getText().toString().isEmpty() ||
                w_edit_hor_final.getText().toString().isEmpty() ||
                w_edit_sobre_voo.getText().toString().isEmpty() ||
                w_edit_diario_bordo_numero.getText().toString().isEmpty() ||
                w_edit_diario_bordo_pagina.getText().toString().isEmpty() ||
                w_edit_diario_bordo_linha.getText().toString().isEmpty()) {

            alertMessage("Preencha todos os campos antes de continuar!");
            return false;

            //Verifica se os dois checkbox estão desmarcados
        } else if (!w_checkbox_dry.isChecked() && !w_checkbox_wet.isChecked()){
            alertMessage("Selecione uma modalidade DRY ou WET!");
            return false;
        }else{
            //Verifica se os campos são númericos
            if (w_edit_hor_inicial.getText().toString().trim().equals(".") || w_edit_hor_inicial.getText().toString().trim().equals(" ") ||
                    w_edit_hor_final.getText().toString().trim().equals(".") || w_edit_hor_final.getText().toString().trim().equals(" ") ||
                    w_edit_sobre_voo.getText().toString().trim().equals(".") || w_edit_sobre_voo.getText().toString().trim().equals(" ") ||
                    w_edit_diario_bordo_numero.getText().toString().equals(".") || w_edit_diario_bordo_numero.getText().toString().equals(" ") ||
                    w_edit_diario_bordo_pagina.getText().toString().equals(".") || w_edit_diario_bordo_pagina.getText().toString().equals(" ") ||
                    w_edit_diario_bordo_linha.getText().toString().equals(".") || w_edit_diario_bordo_linha.getText().toString().equals(" ")) {

                alertMessage("Os campos precisam ser númericos!");
                return false;

            } else {
                return true;
            }
        }
    }

    //Converte o tempo diurno e noturno em decimal e valida
    private boolean validarDiurnoNoturno() {

        Float horasDiurno = Float.parseFloat(hora_diurno.replace(".", ""));
        Float minutosDiurno = Float.parseFloat(minuto_diurno.replace(".", ""));

        Float diurno = horasDiurno + (minutosDiurno / 60);

        Float horasNoturno = Float.parseFloat(hora_noturno.replace(".", ""));
        Float minutosNoturno = Float.parseFloat(minuto_noturno.replace(".", ""));

        Float noturno = horasNoturno + (minutosNoturno / 60);

        Float calculo = diurno + noturno;

        Float tempo_hobbs = Float.parseFloat(w_edit_tempo_voo.getText().toString().replace(",", "."));

        Double margem_positiva = calculo + 0.1;

        Double margem_negativa = calculo - 0.1;

        if (tempo_hobbs < margem_negativa || tempo_hobbs > margem_positiva) {
            alertMessage("Tempo Diurno + Noturno precisa ser igual ao Tempo Hobbs!");
            return false;
        } else {
            return true;
        }

    }

    //Método que chama o calculo quando o usuario tira o cursor do EditText
    private void focusChange() {

        w_edit_hor_inicial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //w_edit_hor_inicial.setText(ajustaCampo(w_edit_hor_inicial));
                calcularTempoVoo();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //w_edit_hor_inicial.setText(ajustaCampo(w_edit_hor_inicial));
                calcularTempoVoo();
            }
        });

        w_edit_hor_final.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //w_edit_hor_final.setText(ajustaCampo(w_edit_hor_final));
                calcularTempoVoo();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // w_edit_hor_final.setText(ajustaCampo(w_edit_hor_final));
                calcularTempoVoo();
            }
        });
    }

    //Faz o calculo do tempo de vôo e atribui ao campo
    private void calcularTempoVoo() {

        try {

            String txt_inicial = w_edit_hor_inicial.getText().toString();
            String txt_final = w_edit_hor_final.getText().toString();

            if (txt_inicial.equals("")) {
                txt_inicial = "0";
            }

            if (txt_final.equals("")) {
                txt_final = "0";
            }

            Float hor_inicial = Float.parseFloat(txt_inicial);
            Float hor_final = Float.parseFloat(txt_final);

            Float calculo = hor_final - hor_inicial;

            String tempo_de_voo = new DecimalFormat("#,##0.0").format(calculo);

            w_edit_tempo_voo.setText(tempo_de_voo.toString());

        } catch (Exception ex) {
            System.out.println("MOB004_C calcularTempoVoo() erro: " + ex.toString());
        }
    }

    //Método que separa Hora e Minuto do TextView
    private String[] separaHorario(final TextView textView) {

        String texto = textView.getText().toString();
        String percorre_hora = "";
        String percorre_minuto = "";

        //Loop para percorrer a String
        for (int i = 0; i < textView.length(); i++) {
            if (texto.charAt(i) == ':') {

                //Loop que percorre a String após os dois pontos
                for (int j = i + 1; j < textView.length(); j++) {
                    percorre_minuto = percorre_minuto + texto.charAt(j);
                }

                break;
            }
            percorre_hora = percorre_hora + texto.charAt(i);
        }

        //Limpa os espaços das Strings
        percorre_hora = percorre_hora.replaceAll("\\s+", "");
        percorre_minuto = percorre_minuto.replaceAll("\\s+", "");

        //Para retornar duas Strings, precisa usar um array
        String[] array = new String[2];
        array[0] = percorre_hora;
        array[1] = percorre_minuto;

        return array;
    }

    //Executa o TimePickerDialog pro botão de relógio de noturno
    private void timePickerNoturno(final TextView textView) {

        //Tratamento do horário pelo usuario
        final TimePickerDialog.OnTimeSetListener onTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        String hora_texto = String.valueOf(hour);
                        String minuto_texto = String.valueOf(min);

                        if (hour <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            hora_texto = "0" + hour;
                        }

                        if (min <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            minuto_texto = "0" + min;
                        }

                        textView.setText(hora_texto + " : " + minuto_texto);

                        if (flag_noturno_timepicker == true) {
                            calcularDiurno(true);
                        }

                    }
                };

        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY); //Recebe a hora atual
        int minuto = calendar.get(Calendar.MINUTE); //Recebe o minuto atual

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this,
                        R.style.PickerTheme, //Define o tema do Dialog
                        onTimeSetListener, //Define o tratamento do retorno
                        hora, minuto, //Hora e minuto inicial
                        DateFormat.is24HourFormat(this)); //Define o formato do relogio

        timePickerDialog.setTitle("");

        timePickerDialog.show();

        flag_noturno_timepicker = true;

    }

    //Executa o TimePickerDialog pro botão de relógio de noturno
    private void timePickerVFR(final TextView textView) {

        //Tratamento do horário pelo usuario
        final TimePickerDialog.OnTimeSetListener onTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        String hora_texto = String.valueOf(hour);
                        String minuto_texto = String.valueOf(min);

                        if (hour <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            hora_texto = "0" + hour;
                        }

                        if (min <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            minuto_texto = "0" + min;
                        }

                        textView.setText(hora_texto + " : " + minuto_texto);

                        if (flag_vfr_timepicker == true) {
                            calcularVfr(true);
                        }

                    }
                };

        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY); //Recebe a hora atual
        int minuto = calendar.get(Calendar.MINUTE); //Recebe o minuto atual

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this,
                        R.style.PickerTheme, //Define o tema do Dialog
                        onTimeSetListener, //Define o tratamento do retorno
                        hora, minuto, //Hora e minuto inicial
                        DateFormat.is24HourFormat(this)); //Define o formato do relogio

        timePickerDialog.setTitle("");

        timePickerDialog.show();

        flag_vfr_timepicker = true;

    }

    //Executa o TimePickerDialog
    private void timePicker(final TextView textView) {

        //Tratamento do horário pelo usuario
        final TimePickerDialog.OnTimeSetListener onTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        String hora_texto = String.valueOf(hour);
                        String minuto_texto = String.valueOf(min);

                        if (hour <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            hora_texto = "0" + hour;
                        }

                        if (min <= 9) {
                            //adiciona '0' no inicio da hora se o número for menor que 10
                            minuto_texto = "0" + min;
                        }

                        textView.setText(hora_texto + " : " + minuto_texto);

                    }
                };

        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY); //Recebe a hora atual
        int minuto = calendar.get(Calendar.MINUTE); //Recebe o minuto atual

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this,
                        R.style.PickerTheme, //Define o tema do Dialog
                        onTimeSetListener, //Define o tratamento do retorno
                        hora, minuto, //Hora e minuto inicial
                        DateFormat.is24HourFormat(this)); //Define o formato do relogio

        timePickerDialog.setTitle("");

        timePickerDialog.show();

    }

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
        getMenuInflater().inflate(R.menu.botao_salvar, menu);
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
            //Botão salvar
            case R.id.botao_salvar_action_bar:
                salvarAgendaTemporaria();
                Toasty.warning(MOB004_C.this,
                        "Salvo!",
                        Toast.LENGTH_SHORT, true).show();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB004_C.this,
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
        Toast.makeText(MOB004_C.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004_C.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
