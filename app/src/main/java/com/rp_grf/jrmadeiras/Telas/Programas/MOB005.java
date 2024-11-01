package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rp_grf.jrmadeiras.Fragments.BuscaAeronaveFragment;
import com.rp_grf.jrmadeiras.Fragments.BuscaAlunoFragment;
import com.rp_grf.jrmadeiras.Fragments.BuscaTipoVooFragment;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAeronave;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAluno;
import com.rp_grf.jrmadeiras.Interfaces.RetornaTipo_voo;
import com.rp_grf.jrmadeiras.R;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class MOB005 extends AppCompatActivity {

    Boolean flag_online;

    String descricao_programa;

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

    TextView w_nome_aluno;
    TextView w_nome_instrutor;
    TextView w_nome_aeronave;
    TextView w_nome_tipo_voo;

    EditText w_tempo_voo;

    CardView w_botao_aluno;
    CardView w_botao_aeronave;
    CardView w_botao_tipo_voo;

    Button w_btn_seguinte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob005);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Chama os dados temporarios do usuário
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String nom_usu_tmp = settings.getString("nom_usu_tmp", "");
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        w_nome_instrutor.setText(nom_usu_tmp);

        cliqueBotao();

    }

    //Método que define os parametros que serão enviados para a proxima tela
    private void setExtra(Intent intent) {
        intent.putExtra("des_prg", descricao_programa);

        intent.putExtra("nom_cli_ins", w_nome_instrutor.getText().toString());
        intent.putExtra("cod_cli", codigo_aluno.toString());
        intent.putExtra("nom_cli", w_nome_aluno.getText().toString());
        intent.putExtra("cod_bem", codigo_aeronave.toString());
        intent.putExtra("nom_bem", w_nome_aeronave.getText().toString());
        intent.putExtra("cod_tip_voo", codigo_tipo_voo.toString());
        intent.putExtra("nom_tip_voo", w_nome_tipo_voo.getText().toString());

        intent.putExtra("tmp_nao_voo", w_tempo_voo.getText().toString());

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
        w_nome_aluno = (TextView) findViewById(R.id.txt_naovoo_aluno);
        w_nome_instrutor = (TextView) findViewById(R.id.txt_naovoo_instrutor);
        w_nome_aeronave = (TextView) findViewById(R.id.txt_naovoo_aeronave);
        w_nome_tipo_voo = (TextView) findViewById(R.id.txt_naovoo_tipo_voo);

        w_tempo_voo = (EditText) findViewById(R.id.edit_text_naovoo_tempo_voo);

        //Botões
        w_botao_aluno = (CardView) findViewById(R.id.card_view_naovoo_botao_aluno);
        w_botao_aeronave = (CardView) findViewById(R.id.card_view_naovoo_botao_aeronave);
        w_botao_tipo_voo = (CardView) findViewById(R.id.card_view_naovoo_botao_tipo_voo);

        w_btn_seguinte = (Button) findViewById(R.id.button_naovoo_seguinte);

    }

    private void cliqueBotao() {

        w_botao_aluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        w_btn_seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validado = validarCampos();

                if (validado == true) {

                    Intent intent = new Intent(getBaseContext(), MOB005_A.class);

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
                w_tempo_voo.getText().toString().isEmpty()
        ) {
            return false;
        } else {
            return true;
        }
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
        }
    };

    RetornaTipo_voo retornaTipo_voo = new RetornaTipo_voo() {
        @Override
        public void recebeTipo_voo(String cod_tip_voo, String nom_tip_voo) {
            codigo_tipo_voo = cod_tip_voo;
            nome_tipo_voo = nom_tip_voo;

            w_nome_tipo_voo.setText(nom_tip_voo);
        }
    };

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
                Toasty.info(MOB005.this,
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
        Toast.makeText(MOB005.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB005.this);
        alert.setTitle("");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}