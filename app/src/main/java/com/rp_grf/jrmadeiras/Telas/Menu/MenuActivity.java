package com.rp_grf.jrmadeiras.Telas.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.rp_grf.jrmadeiras.R;

public class MenuActivity extends AppCompatActivity {

    GridLayout grid_menu;
    CardView botao_parametros, botao_suprimentos, botao_comercial,
            botao_contabilidade, botao_financeiro, botao_controladoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("MENU - TESTE");

        iniciarCampos();

        modoPaisagem();

        cliqueBotao();
    }

    private void iniciarCampos() {
        grid_menu = (GridLayout) findViewById(R.id.grid_layout);

        botao_parametros = (CardView) findViewById(R.id.card_parametros);
        botao_suprimentos = (CardView) findViewById(R.id.card_suprimentos);
        botao_comercial = (CardView) findViewById(R.id.card_comercial);
        botao_financeiro = (CardView) findViewById(R.id.card_financeiro);
        botao_contabilidade = (CardView) findViewById(R.id.card_contabilidade);
        botao_controladoria = (CardView) findViewById(R.id.card_controladoria);
    }

    private void modoPaisagem() {
        if (MenuActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            grid_menu.setColumnCount(2);
        } else {
            grid_menu.setColumnCount(3);
        }
    }

    private void cliqueBotao() {
        botao_parametros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProgramasActivity.class);
                startActivity(intent);
            }
        });

        botao_suprimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        botao_comercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        botao_financeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        botao_contabilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        /*
        botao_controladoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ControladoriaActivity.class);
                startActivity(intent);
            }
        });

         */
        botao_controladoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ModulosActivity.class);
                startActivity(intent);
            }
        });




    }

}
