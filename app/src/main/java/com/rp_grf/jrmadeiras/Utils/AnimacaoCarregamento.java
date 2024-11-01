package com.rp_grf.jrmadeiras.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rp_grf.jrmadeiras.Telas.Menu.LoginActivity;

public class AnimacaoCarregamento extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    private float inicio;
    private float fim;

    public AnimacaoCarregamento(Context context, ProgressBar progressBar, TextView textView, float inicio, float fim) {
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float valor = inicio + (fim - inicio) * interpolatedTime;
        progressBar.setProgress((int)valor);
        textView.setText((int)valor + " %");

        if (valor == fim) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }
}
