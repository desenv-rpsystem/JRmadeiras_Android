package com.rp_grf.jrmadeiras.Telas.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.rp_grf.jrmadeiras.Utils.AnimacaoCarregamento;
import com.rp_grf.jrmadeiras.R;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CarregandoActivity extends AppCompatActivity {

    private TextView w_porcentagem;
    private ProgressBar w_barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregamento);
        getSupportActionBar().hide();

        iniciarCampos();

        animacaoCarregamento();
    }

    private void iniciarCampos() {
        w_porcentagem = findViewById(R.id.text_view_porcentagem);
        w_barra = findViewById(R.id.progress_bar_carregamento);

        w_barra.setMax(100);
        w_porcentagem.setScaleY(3f);
    }

    public void animacaoCarregamento() {
        AnimacaoCarregamento animacao = new AnimacaoCarregamento(this, w_barra, w_porcentagem, 0, 100);
        animacao.setDuration(1500);
        w_barra.setAnimation(animacao);
    }
}
