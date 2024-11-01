package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MOB004_A extends AppCompatActivity {

    Boolean flag_online;

    Boolean flag_partida;
    Boolean flag_corte;
    Boolean flag_novo;

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

    CardView w_botao_data_voo;
    CardView w_botao_partida;
    CardView w_botao_decolagem;
    CardView w_botao_pouso;
    CardView w_botao_corte;

    CheckBox w_checkbox_partida;
    CheckBox w_checkbox_corte;

    TextView w_data_voo;
    TextView w_hora_partida;
    TextView w_hora_decolagem;
    TextView w_hora_pouso;
    TextView w_hora_corte;

    ImageView w_icone_data_voo;
    ImageView w_icone_partida;
    ImageView w_icone_decolagem;
    ImageView w_icone_pouso;
    ImageView w_icone_corte;

    Button w_btn_seguinte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob004_a);

        descricao_programa = getIntent().getStringExtra("des_prg");

        flag_partida = false;
        flag_corte = false;
        flag_novo = false;

        getExtra();

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        cliqueBotao();

        habilitarPartida(false);
        habilitarCorte(false);

        iniciarProximaAgenda();

        iniciarAgendaTemporaria();

        System.out.println("MOB004_A - cod_pre_voo " + codigo_pre_voo);
    }

    //Método que define os parametros que serão enviados para a proxima tela
    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", descricao_programa);
        intent.putExtra("codigo_agenda", codigo_agenda);
        intent.putExtra("codigo_agenda_anterior", codigo_agenda_anterior);

        intent.putExtra("cod_pre_voo", codigo_pre_voo);

        intent.putExtra("flag_partida", flag_partida.toString());
        intent.putExtra("flag_corte", flag_corte.toString());

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

        data_voo = w_data_voo.getText().toString();
        data_voo = data_voo.replaceAll("\\s+", ""); //Remove os espaços
        intent.putExtra("dat_reg_voo", data_voo);

        intent.putExtra("hor_par", hora_partida);
        intent.putExtra("min_par", minuto_partida);
        intent.putExtra("hor_dec", hora_decolagem);
        intent.putExtra("min_dec", minuto_decolagem);
        intent.putExtra("hor_pou", hora_pouso);
        intent.putExtra("min_pou", minuto_pouso);
        intent.putExtra("hor_cor", hora_corte);
        intent.putExtra("min_cor", minuto_corte);

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
        codigo_agenda = getIntent().getStringExtra("codigo_agenda");
        codigo_agenda_anterior = getIntent().getStringExtra("codigo_agenda_anterior");

        codigo_pre_voo = getIntent().getStringExtra("cod_pre_voo");

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

        data_voo = getIntent().getStringExtra("dat_reg_voo");
        hora_partida = getIntent().getStringExtra("hor_par");
        minuto_partida = getIntent().getStringExtra("min_par");
        hora_decolagem = getIntent().getStringExtra("hor_dec");
        minuto_decolagem = getIntent().getStringExtra("min_dec");
        hora_pouso = getIntent().getStringExtra("hor_pou");
        minuto_pouso = getIntent().getStringExtra("min_pou");
        hora_corte = getIntent().getStringExtra("hor_cor");
        minuto_corte = getIntent().getStringExtra("min_cor");
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

    //Atribui o item do layout às variaveis
    private void iniciarCampos() {
        w_botao_data_voo = (CardView) findViewById(R.id.card_view_botao_data_voo);
        w_botao_partida = (CardView) findViewById(R.id.card_view_botao_partida);
        w_botao_decolagem = (CardView) findViewById(R.id.card_view_botao_decolagem);
        w_botao_pouso = (CardView) findViewById(R.id.card_view_botao_pouso);
        w_botao_corte = (CardView) findViewById(R.id.card_view_botao_corte);

        w_checkbox_partida = (CheckBox) findViewById(R.id.checkbox_partida);
        w_checkbox_corte = (CheckBox) findViewById(R.id.checkbox_corte);

        w_data_voo = (TextView) findViewById(R.id.txt_data_voo);
        w_hora_partida = (TextView) findViewById(R.id.txt_partida);
        w_hora_decolagem = (TextView) findViewById(R.id.txt_decolagem);
        w_hora_pouso = (TextView) findViewById(R.id.txt_pouso);
        w_hora_corte = (TextView) findViewById(R.id.txt_corte);

        w_icone_data_voo = (ImageView) findViewById(R.id.image_view_registro_voo_data);
        w_icone_partida = (ImageView) findViewById(R.id.image_view_registro_voo_partida);
        w_icone_decolagem = (ImageView) findViewById(R.id.image_view_registro_voo_decolagem);
        w_icone_pouso = (ImageView) findViewById(R.id.image_view_registro_voo_pouso);
        w_icone_corte = (ImageView) findViewById(R.id.image_view_registro_voo_corte);

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
                    //Só preenche se tiver uma data salva
                    if (dadosAgendaTemp.get(i).getDat_reg_voo() != null) {
                        w_data_voo.setText(dadosAgendaTemp.get(i).getDat_reg_voo());

                        w_hora_partida.setText(
                                dadosAgendaTemp.get(i).getHor_par() + " : " + dadosAgendaTemp.get(i).getMin_par()
                        );

                        w_hora_decolagem.setText(
                                dadosAgendaTemp.get(i).getHor_dec() + " : " + dadosAgendaTemp.get(i).getMin_dec()
                        );

                        w_hora_pouso.setText(
                                dadosAgendaTemp.get(i).getHor_pou() + " : " + dadosAgendaTemp.get(i).getMin_pou()
                        );

                        w_hora_corte.setText(
                                dadosAgendaTemp.get(i).getHor_cor() + " : " + dadosAgendaTemp.get(i).getMin_cor()
                        );

                        //Força o click do checkbox caso o horário seja diferente de 00 : 00
                        if (!w_hora_partida.getText().toString().equals("00 : 00")) {
                            w_checkbox_partida.setChecked(true);
                        }

                        //Força o click do checkbox caso o horário seja diferente de 00 : 00
                        if (!w_hora_corte.getText().toString().equals("00 : 00")) {
                            w_checkbox_corte.performClick();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("MOB004_A - Não foi possível iniciar uma nova agenda temporaria:\n" + ex.toString());
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

        tab_agenda.setDat_reg_voo(w_data_voo.getText().toString());
        tab_agenda.setHor_par(hora_partida);
        tab_agenda.setMin_par(minuto_partida);
        tab_agenda.setHor_dec(hora_decolagem);
        tab_agenda.setMin_dec(minuto_decolagem);
        tab_agenda.setHor_pou(hora_pouso);
        tab_agenda.setMin_pou(minuto_pouso);
        tab_agenda.setHor_cor(hora_corte);
        tab_agenda.setMin_cor(minuto_corte);

        bancoAgendaTemp.updateAgenda(tab_agenda);
    }

    private void iniciarProximaAgenda() {
        //Só executa o método se for uma agenda continuação de outra
        if (codigo_agenda_anterior != null) {
            w_data_voo.setText(data_voo);
            //w_hora_partida.setText(hora_partida + " : " + minuto_partida);

            //coloca a hora de pouso da agenda anterior na decolagem
            w_hora_decolagem.setText(hora_pouso + " : " + minuto_pouso);

            /*
            Boolean marcar_checkbox_partida = Boolean.parseBoolean(getIntent().getStringExtra("flag_partida"));

            if (marcar_checkbox_partida == true) {
                w_checkbox_partida.performClick();
            }
            */

            travarCalendario();
            travarPartida();
            travarDecolagem();

            flag_novo = true;
        }
    }

    //Método que muda a cor do botão de calendario e desabilita
    private void travarCalendario() {

        w_botao_data_voo.setEnabled(false);

        w_botao_data_voo.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_data_voo.setImageResource(R.drawable.icone_calendario);

    }

    //Método que muda a cor do botão de partida e desabilita
    private void travarPartida() {

        w_checkbox_partida.setEnabled(false);

        w_botao_partida.setEnabled(false);

        w_botao_partida.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_partida.setImageResource(R.drawable.icone_relogio);

    }

    //Método que muda a cor do botão de decolagem e desabilita
    private void travarDecolagem() {

        w_botao_decolagem.setEnabled(false);

        w_botao_decolagem.setCardBackgroundColor(getResources().getColor(R.color.cinza));
        w_icone_decolagem.setImageResource(R.drawable.icone_relogio);

    }

    //Método que trata o clique de cada botão
    private void cliqueBotao() {
        w_botao_data_voo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker((TextView) w_data_voo); //Chama o Dialog de data no clique do botão
            }
        });

        w_botao_partida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_partida); //Chama o Dialog de hora no clique do botão
            }
        });

        w_botao_decolagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_decolagem); //Chama o Dialog de hora no clique do botão
            }
        });

        w_botao_pouso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_pouso); //Chama o Dialog de hora no clique do botão
            }
        });

        w_botao_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker((TextView) w_hora_corte); //Chama o Dialog de hora no clique do botão
            }
        });

        w_checkbox_partida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v); //Chama o método que trata o clique na CheckBox
            }
        });

        w_checkbox_corte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueCheckBox(v); //Chama o método que trata o clique na CheckBox
            }
        });

        w_btn_seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                divideHorasMinutos();

                boolean validarData = validarData();

                boolean validarHorario = validarHorario();

                if (validarData == true) {

                    if (validarHorario == true) {
                        Intent intent = new Intent(getBaseContext(), MOB004_B.class);

                        setExtra(intent); //Atribui os parametros que serão enviados para a proxima tela

                        salvarAgendaTemporaria();

                        startActivity(intent); //Inicia a tela
                    }
                }

            }
        });
    }

    //Método que atribui o horário separado ás variaveis
    private void divideHorasMinutos() {
        //Chama o método que separa Hora e Minuto de Partida
        String[] arrayPartida = separaHorario((TextView) w_hora_partida);

        hora_partida = arrayPartida[0];
        minuto_partida = arrayPartida[1];

        //Chama o método que separa Hora e Minuto de Decolagem
        String[] arrayDecolagem = separaHorario((TextView) w_hora_decolagem);

        hora_decolagem = arrayDecolagem[0];
        minuto_decolagem = arrayDecolagem[1];

        //Chama o método que separa Hora e Minuto de Pouso
        String[] arrayPouso = separaHorario((TextView) w_hora_pouso);

        hora_pouso = arrayPouso[0];
        minuto_pouso = arrayPouso[1];

        //Chama o método que separa Hora e Minuto de Corte
        String[] arrayCorte = separaHorario((TextView) w_hora_corte);

        hora_corte = arrayCorte[0];
        minuto_corte = arrayCorte[1];
    }

    private boolean validarData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy");
        simpleDateFormat.setLenient(false);

        boolean data_validada;

        try {
            Date data_inicial = simpleDateFormat.parse("01 / 01 / 2000");
            Date data_atual = new Date();
            Date data_selecionada = simpleDateFormat.parse(w_data_voo.getText().toString());

            if (data_selecionada.before(data_inicial) /*|| data_selecionada.after(data_atual)*/) {
                alertMessage("Data inválida!");
                data_validada = false;
            } else {
                data_validada = true;
            }
        } catch (ParseException ex) {
            //Essa parte é chamada quando o usuário não seleciona uma data
            if (flag_novo == true) {
                data_validada = true; //Valida se for uma agenda continuação
            } else {
                alertMessage("Data não preenchida!");
                data_validada = false;
            }

        }

        return data_validada;
    }

    private boolean validarHorario() {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH : mm");

        boolean horario_validado = true;

        try {

            Date partida = simpleDateFormat.parse(w_hora_partida.getText().toString());
            Date decolagem = simpleDateFormat.parse(w_hora_decolagem.getText().toString());
            Date pouso = simpleDateFormat.parse(w_hora_pouso.getText().toString());
            Date corte = simpleDateFormat.parse(w_hora_corte.getText().toString());

            if (pouso.compareTo(decolagem) < 0) {

                alertMessage("Verifique o horário de pouso!");
                horario_validado = false;

                return horario_validado; // Quebra a sequência de IF
            }

            if (flag_partida == true) {

                if (decolagem.compareTo(partida) < 0) {

                    alertMessage("Verifique o horário de decolagem!");
                    horario_validado = false;

                    return horario_validado; // Quebra a sequência de IF
                }
            }

            if (flag_corte == true) {

                if ((corte.compareTo(pouso) < 0) || (corte.compareTo(partida) < 0)) {

                    alertMessage("Verifique o horário de corte!");
                    horario_validado = false;

                    return horario_validado; // Quebra a sequência de IF
                }
            }

        } catch (ParseException ex) {
            System.out.println("Horário não preenchido!");

            System.out.println(ex.toString());

            horario_validado = false;
        }

        return horario_validado;
    }

    private void cliqueCheckBox(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // Verifica se a checkbox está marcada
        switch (view.getId()) {
            case R.id.checkbox_partida:
                if (checked) {
                    flag_partida = true;
                    habilitarPartida(true);
                } else {
                    flag_partida = false;
                    habilitarPartida(false);
                }
                break;
            case R.id.checkbox_corte:
                if (checked) {
                    flag_corte = true;
                    habilitarCorte(true);
                } else {
                    flag_corte = false;
                    habilitarCorte(false);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
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


    //Método que muda a cor do botão de partida e habilita ou desabilita
    private void habilitarPartida(boolean marcado) {

        w_botao_partida.setEnabled(marcado);

        if (marcado == true) {
            w_botao_partida.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            w_icone_partida.setImageResource(R.drawable.icone_relogio_branco);
        } else {
            w_hora_partida.setText("00 : 00");
            w_botao_partida.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_partida.setImageResource(R.drawable.icone_relogio);
        }
    }

    //Método que muda a cor do botão de corte e habilita ou desabilita
    private void habilitarCorte(boolean marcado) {

        w_botao_corte.setEnabled(marcado);

        if (marcado == true) {
            w_botao_corte.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            w_icone_corte.setImageResource(R.drawable.icone_relogio_branco);
        } else {
            w_hora_corte.setText("00 : 00");
            w_botao_corte.setCardBackgroundColor(getResources().getColor(R.color.cinza));
            w_icone_corte.setImageResource(R.drawable.icone_relogio);
        }
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

    //Executa o DatePickerDialog
    private void datePicker(final TextView textView) {

        //Tratamento da data selecionada pelo usuario
        final DatePickerDialog.OnDateSetListener onDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String dia = String.valueOf(day);
                        String mes = String.valueOf(month + 1);
                        String ano = String.valueOf(year);

                        if (day <= 9) {
                            //adiciona '0' no inicio do dia se o número for menor que 10
                            dia = "0" + day;
                        }

                        if (month + 1 <= 9) {
                            //adiciona '0' no inicio do mês se o número for menor que 10
                            mes = "0" + String.valueOf(month + 1);
                        }

                        textView.setText(dia + " / " + mes + " / " + ano);
                    }
                };

        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH); //Recebe o dia atual
        int mes = calendar.get(Calendar.MONTH); //Recebe o mês atual
        int ano = calendar.get(Calendar.YEAR); //Recebe o ano atual

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this,
                        R.style.PickerTheme, //Define o tema do Dialog
                        onDateSetListener, //Define o tratamento do retorno
                        dia, mes, ano);

        datePickerDialog.updateDate(ano, mes, dia); //Define para que o Dialog comece nessa data

        datePickerDialog.setTitle("");

        datePickerDialog.show();
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
                Toasty.warning(MOB004_A.this,
                        "Salvo!",
                        Toast.LENGTH_SHORT, true).show();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB004_A.this,
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
        Toast.makeText(MOB004_A.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB004_A.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
