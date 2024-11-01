package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Utils.DecimalDigitsInputFilter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgenda;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;

import java.lang.reflect.Field;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MOB005_A extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoAgenda bancoAgenda;
    private BancoRegistroVoo bancoRegistroVoo;

    int contador;

    Boolean flag_online;

    String descricao_programa;
    String nome_aluno;
    String codigo_instrutor;

    TextView w_nome_aluno;
    TextView w_codigo_aluno;
    TextView w_nome_instrutor;

    TextView w_data_voo;
    TextView w_nome_tipo_voo;
    TextView w_sigla_aeronave;
    TextView w_tempo_voo;

    TextView w_contador;

    Button w_btn_confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob005_a);

        contador = 1;

        descricao_programa = "Resumo";
        nome_aluno = getIntent().getStringExtra("nom_cli");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Agenda");

        bancoAgenda = new BancoAgenda(this);

        w_contador = (TextView) findViewById(R.id.txt_registro_naovoo_contador);

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");
        codigo_instrutor = settings.getString("cod_usu_tmp", "");

        isConnected(flag_login);

        //Inicia os campos da tela
        iniciarAlunoInstrutor();

        iniciarInfoVoo();

        iniciarBotao();

        cliqueBotao();

        filtrarCasasDecimais();

        getLastAgenda();
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
        w_nome_aluno = (TextView) findViewById(R.id.txt_registro_naovoo_nom_cli);
        w_nome_aluno.setText(getIntent().getStringExtra("nom_cli"));

        w_codigo_aluno = (TextView) findViewById(R.id.txt_registro_naovoo_cod_cli);
        w_codigo_aluno.setText(getIntent().getStringExtra("cod_cli"));

        w_nome_instrutor = (TextView) findViewById(R.id.txt_registro_naovoo_nom_cli_ins);
        w_nome_instrutor.setText(getIntent().getStringExtra("nom_cli_ins"));
    }

    private void iniciarInfoVoo() {
        w_data_voo = (TextView) findViewById(R.id.txt_registro_naovoo_dat_registro);
        w_data_voo.setText(separaData());

        w_nome_tipo_voo = (TextView) findViewById(R.id.txt_registro_naovoo_nom_tip_voo);
        w_nome_tipo_voo.setText(getIntent().getStringExtra("nom_tip_voo"));

        w_sigla_aeronave = (TextView) findViewById(R.id.txt_registro_naovoo_sigla_aeronave);
        w_sigla_aeronave.setText(getIntent().getStringExtra("nom_bem"));

        w_tempo_voo = (TextView) findViewById(R.id.txt_registro_naovoo_tempo_voo);
        w_tempo_voo.setText(getIntent().getStringExtra("tmp_nao_voo"));
    }

    //Cria uma String da data atual e retorna a String
    private String separaData(){
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH); //Recebe o dia atual
        int mes = calendar.get(Calendar.MONTH); //Recebe o mês atual
        int ano = calendar.get(Calendar.YEAR); //Recebe o ano atual

        String data_atual = (dia + " / " + mes + " / " + ano);

        return data_atual;
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

    //Filtra as casas decimais que o usuário pode digitar no campo
    private void filtrarCasasDecimais() {
        w_tempo_voo.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(4, 1)});
    }

    //Recebe o número do último registro gravado no banco
    private void getLastAgenda() {

        if (flag_online) {
            referenceDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                        contador++;
                    }

                    w_contador.setText(String.valueOf(contador));

                    System.out.println(w_contador.getText().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alertMessage("Não foi possível acessar a base de dados !!!\n" +
                            "Favor reiniciar o aplicativo.");
                }
            });
        } else {

            bancoAgenda = new BancoAgenda(this);

            while (contador < bancoAgenda.getAgenda().size()) {
                contador++;
            }

            w_contador.setText(String.valueOf(contador));

            System.out.println(w_contador.getText().toString());
        }

    }

    //Preenche o objeto Agenda
    private void preencherAgenda() {

        String codigo_aluno = getIntent().getStringExtra("cod_cli");
        String codigo_aeronave = getIntent().getStringExtra("cod_bem");
        String codigo_tipo_voo = getIntent().getStringExtra("cod_tip_voo");
        String codigo_origem = "0";
        String codigo_destino = "0";

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

        String observacoes =  getIntent().getStringExtra("obs_reg_voo");

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

        String hora_liberacao = hora + ":" + minuto;

        String tempo_nao_voo = w_tempo_voo.getText().toString();

        try {

            Agenda tab_agenda = new Agenda(
                    Long.parseLong(w_contador.getText().toString()),
                    Long.parseLong("0"),
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
                    "",
                    "",

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
                    "RNV", //Envia que foi feito pelo Registro de voo MOB005
                    tempo_nao_voo
            );

            tab_agenda = verificarNulos(tab_agenda);

            confirmarLiberacao(tab_agenda);

        } catch (Exception ex) {
            System.out.println("MOB005_A " + ex.toString());
            Toasty.error(MOB005_A.this, "Ocorreu um erro ao tentar gravar a agenda.\nTente novamente!",
                    Toast.LENGTH_SHORT, true).show();
            MOB005_A.this.finish();
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
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(MOB005_A.this);
        String mensagem = "Confirma registro de não vôo para o aluno " +
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
                            gravarAgenda_Online(tab_agenda);
                        } else {
                            gravarAgenda_Offline(tab_agenda);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertSmall("Conexão Cancelada!");
                    }
                });
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

                Toasty.success(MOB005_A.this,
                        "Registro de não voo enviado para a nuvem!",
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

            Toasty.info(MOB005_A.this,
                    "Registro de não voo salvo no banco offline!",
                    Toast.LENGTH_SHORT, true).show();

            bancoAgenda.close();

        } catch (Exception ex) {
            System.out.println("Gravar Agenda: " + ex);
            alertMessage("Erro ao gravar agenda offline!");
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
                Toasty.info(MOB005_A.this,
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
        Toast.makeText(MOB005_A.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB005_A.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}