package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.rp_grf.jrmadeiras.Utils.DecimalDigitsInputFilter;
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Autor: André
 */

public class MOB004_B extends AppCompatActivity {

    Boolean flag_online;

    String flag_partida;
    String flag_corte;

    String descricao_programa;
    String codigo_agenda;
    String codigo_agenda_anterior;

    String codigo_pre_voo;

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

    EditText w_edit_hor_inicial;
    EditText w_edit_hor_final;

    EditText w_edit_tempo_voo;

    EditText w_edit_pousos;

    EditText w_edit_litros;
    EditText w_edit_galao;

    Button w_btn_seguinte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob004_b);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getExtra();

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        filtrarCasasDecimais();

        cliqueBotao();

        calcularTempoVoo(); //Inicia o editText com 0,0

        calcularGalao(); //Inicia o editText com 0,0

        focusChange();

        iniciarProximaAgenda();

        iniciarAgendaTemporaria();

        System.out.println("MOB004_B - cod_pre_voo " + codigo_pre_voo);
    }

    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", descricao_programa);
        intent.putExtra("codigo_agenda", codigo_agenda);
        intent.putExtra("codigo_agenda_anterior", codigo_agenda_anterior);

        intent.putExtra("cod_pre_voo", codigo_pre_voo);

        intent.putExtra("flag_partida", flag_partida);
        intent.putExtra("flag_corte", flag_corte);

        intent.putExtra("cod_cli", codigo_aluno);
        intent.putExtra("cod_bem", codigo_aeronave);
        intent.putExtra("cod_tip_voo", codigo_tipo_voo);
        intent.putExtra("cod_aer_ori", codigo_origem);
        intent.putExtra("cod_aer_des", codigo_destino);

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
        w_edit_hor_inicial = (EditText) findViewById(R.id.edit_text_flight_inicial);
        w_edit_hor_final = (EditText) findViewById(R.id.edit_text_flight_final);

        w_edit_tempo_voo = (EditText) findViewById(R.id.edit_text_flight_tempo_voo);

        w_edit_pousos = (EditText) findViewById(R.id.edit_text_flight_pousos);

        w_edit_litros = (EditText) findViewById(R.id.edit_text_combustivel_litros);
        w_edit_galao = (EditText) findViewById(R.id.edit_text_combustivel_galao);

        w_btn_seguinte = (Button) findViewById(R.id.button_seguinte);
    }

    //Preenche os campos com os dados salvos na tabela temporaria
    private void iniciarAgendaTemporaria() {
        BancoAgendaTemp bancoAgendaTemp = new BancoAgendaTemp(this);

        try {

            final ArrayList<Agenda> dadosAgendaTemp = bancoAgendaTemp.getAgenda();

            //Percorre os arraylist das agendas salvos na tabela temporaria
            for (int i = 0; i < dadosAgendaTemp.size(); i++) {
                //Compara se o código das agendas salvas é o mesmo da agenda atual
                if (dadosAgendaTemp.get(i).getCod_age().toString().equals(codigo_agenda)) {
                    //Só preenche se tiver um número um tempo de voo salvo
                    if (dadosAgendaTemp.get(i).getTmp_voo_fli() != null) {
                        w_edit_hor_inicial.setText(dadosAgendaTemp.get(i).getHor_ini_fli());
                        w_edit_hor_final.setText(dadosAgendaTemp.get(i).getHor_fin_fli());
                        w_edit_pousos.setText(dadosAgendaTemp.get(i).getNum_pou());

                        w_edit_litros.setText(dadosAgendaTemp.get(i).getLit_com());
                        w_edit_galao.setText(dadosAgendaTemp.get(i).getGal_com());

                        calcularTempoVoo();
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("MOB004_B - Não foi possível iniciar uma nova agenda temporaria:\n" + ex.toString());
        }
    }

    // Salva no banco SQLite os dados temporarios da agenda
    private void salvarAgendaTemporaria() {
        BancoAgendaTemp bancoAgendaTemp = new BancoAgendaTemp(this);

        final ArrayList<Agenda> dadosAgendaTemp = bancoAgendaTemp.getAgenda();

        Agenda tab_agenda = new Agenda();

        for (int i = 0; i < dadosAgendaTemp.size(); i++) {
            if (dadosAgendaTemp.get(i).getCod_age().toString().equals(codigo_agenda)) {
                tab_agenda = dadosAgendaTemp.get(i);
            }
        }

        tab_agenda.setCod_age(Long.parseLong(codigo_agenda));

        tab_agenda.setHor_ini_fli(w_edit_hor_inicial.getText().toString());
        tab_agenda.setHor_fin_fli(w_edit_hor_final.getText().toString());
        tab_agenda.setTmp_voo_fli(w_edit_tempo_voo.getText().toString());
        tab_agenda.setNum_pou(w_edit_pousos.getText().toString());

        tab_agenda.setLit_com(w_edit_litros.getText().toString());
        tab_agenda.setGal_com(w_edit_galao.getText().toString());

        bancoAgendaTemp.updateAgenda(tab_agenda);
    }

    private void iniciarProximaAgenda() {
        //Só executa o método se for uma agenda continuação de outra
        if (codigo_agenda_anterior != null) {
            w_edit_hor_inicial.setText(hor_final_flight);

            w_edit_hor_inicial.setEnabled(false);
        }
    }

    //Filtra as casas decimais que o usuário pode digitar no campo
    private void filtrarCasasDecimais() {
        w_edit_hor_inicial.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 1)});

        w_edit_hor_final.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 1)});

        w_edit_galao.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 2)});

        w_edit_litros.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(6, 2)});
    }

    private void cliqueBotao() {
        w_btn_seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hor_inicial_flight = w_edit_hor_inicial.getText().toString();
                hor_final_flight = w_edit_hor_final.getText().toString();
                tempo_voo_flight = w_edit_tempo_voo.getText().toString();
                numero_pousos = w_edit_pousos.getText().toString();

                litros_combustivel = w_edit_litros.getText().toString();
                galao_combustivel = w_edit_galao.getText().toString();

                Boolean validado = validarCampos();

                if (validado == true) {
                    Intent intent = new Intent(getBaseContext(), MOB004_C.class);

                    setExtra(intent);

                    salvarAgendaTemporaria();

                    startActivity(intent);
                }
            }
        });
    }

    private boolean validarCampos() {

        //Tempo voo

        String tempo_voo = w_edit_tempo_voo.getText().toString();

        tempo_voo = tempo_voo.replace(".", ""); //Apaga o ponto do começo

        tempo_voo = tempo_voo.replace(",", "."); //Troca a virgula por ponto

        tempo_voo_flight = tempo_voo;

        //Litros

        String litros = w_edit_litros.getText().toString();

        litros = litros.replace(".", ""); //Apaga o ponto do começo

        litros = litros.replace(",", "."); //Troca a virgula por ponto

        litros_combustivel = litros;

        //Galão

        String galao = w_edit_galao.getText().toString();

        //galao = galao.replace(".", ""); //Apaga o ponto do começo

        galao = galao.replace(",", "."); //Troca a virgula por ponto

        galao_combustivel = galao;

        //

        //Verifica se o tempo de vôo é menor que zero
        if (Double.parseDouble(tempo_voo) <= 0) {

            alertMessage("Tempo de vôo inválido!");
            return false;

            //Verifica se os campos estão preenchidos
        } else if (w_edit_hor_inicial.getText().toString().isEmpty()
                || w_edit_hor_final.getText().toString().isEmpty()
                || w_edit_pousos.getText().toString().isEmpty()
                || w_edit_galao.getText().toString().isEmpty()) {

            alertMessage("Preencha todos os campos antes de continuar!");
            return false;

        } else {
            //Verifica se os campos são númericos
            if (w_edit_hor_inicial.getText().toString().trim().equals(".") || w_edit_hor_inicial.getText().toString().trim().equals(" ")
                    || w_edit_hor_final.getText().toString().trim().equals(".") || w_edit_hor_final.getText().toString().trim().equals(" ")
                    || w_edit_pousos.getText().toString().trim().equals(".") || w_edit_pousos.getText().toString().trim().equals(" ")
                    || w_edit_galao.getText().toString().trim().equals(".") || w_edit_galao.getText().toString().trim().equals(" ")) {

                alertMessage("Os campos precisam ser númericos!");
                return false;

            } else {
                return true;
            }
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
                calcularTempoVoo();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calcularTempoVoo();
            }
        });

        w_edit_hor_final.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularTempoVoo();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calcularTempoVoo();
            }
        });

        w_edit_galao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calcularGalao();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Faz o calculo do tempo de vôo e atribui ao campo
    private void calcularTempoVoo() {
        try {

            String txt_inicial = w_edit_hor_inicial.getText().toString();
            String txt_final = w_edit_hor_final.getText().toString();

            if (txt_inicial.equals("") || txt_inicial.equals(".")) {
                txt_inicial = "0";
            }

            if (txt_final.equals("") || txt_final.equals(".")) {
                txt_final = "0";
            }

            Float hor_inicial = Float.parseFloat(txt_inicial);
            Float hor_final = Float.parseFloat(txt_final);

            Float calculo = hor_final - hor_inicial;

            String tempo_de_voo = new DecimalFormat("#,##0.0").format(calculo);

            w_edit_tempo_voo.setText(tempo_de_voo);

        } catch (Exception ex) {
            System.out.println("MOB004_B calcularTempoVoo() erro: " + ex.toString());
        }

    }

    private void calcularGalao() {

        String txt_galao = w_edit_galao.getText().toString();

        if (txt_galao.equals("") || txt_galao.equals(".")) {
            txt_galao = "0";
        }

        Float galao = Float.parseFloat(txt_galao);

        double calculo = (galao * 3.78);

        String litros = new DecimalFormat("#,##0.00").format(calculo);

        w_edit_litros.setText(litros);

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
                Toasty.warning(MOB004_B.this,
                        "Salvo!",
                        Toast.LENGTH_SHORT, true).show();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB004_B.this,
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
        Toast.makeText(MOB004_B.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004_B.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
