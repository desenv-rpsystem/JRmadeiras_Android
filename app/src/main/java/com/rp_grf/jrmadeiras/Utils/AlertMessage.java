package com.rp_grf.jrmadeiras.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rp_grf.jrmadeiras.R;

/**
 * Autor: André
 * Data de criação: 01/02/2021
 */

public class AlertMessage extends AlertDialog {

    public AlertMessage(Context context) {
        super(context);
    }

    public void success(@NonNull View view, Context context, String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(context).inflate(
                R.layout.dialog_success,
                (LinearLayout) view.findViewById(R.id.dialog_success_LinearLayout)
        );

        builder.setView(viewAlertMapa);

        //Define o titulo do alertDialog
        TextView success_titulo = (TextView) viewAlertMapa.findViewById(R.id.dialog_success_titulo);
        success_titulo.setText(titulo);

        //Define a mensagem do alertDialog
        TextView success_message = (TextView) viewAlertMapa.findViewById(R.id.dialog_success_mensagem);
        success_message.setText(mensagem);

        final AlertDialog alertDialog = builder.create();

        //Define o botao left como invisivel
        Button botaoLeft = (Button) viewAlertMapa.findViewById(R.id.dialog_success_botao_left);
        botaoLeft.setVisibility(View.INVISIBLE);

        //Define a ação do botão direito
        Button botaoRight = (Button) viewAlertMapa.findViewById(R.id.dialog_success_botao_right);
        botaoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void error(@NonNull View view, Context context, String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(context).inflate(
                R.layout.dialog_error,
                (LinearLayout) view.findViewById(R.id.dialog_error_LinearLayout)
        );

        builder.setView(viewAlertMapa);

        //Define o titulo do alertDialog
        TextView error_titulo = (TextView) viewAlertMapa.findViewById(R.id.dialog_error_titulo);
        error_titulo.setText(titulo);

        //Define a mensagem do alertDialog
        TextView error_message = (TextView) viewAlertMapa.findViewById(R.id.dialog_error_mensagem);
        error_message.setText(mensagem);

        final AlertDialog alertDialog = builder.create();

        //Define o botao left como invisivel
        Button botaoLeft = (Button) viewAlertMapa.findViewById(R.id.dialog_error_botao_left);
        botaoLeft.setVisibility(View.INVISIBLE);

        //Define a ação do botão direito
        Button botaoRight = (Button) viewAlertMapa.findViewById(R.id.dialog_error_botao_right);
        botaoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void warning(@NonNull View view, Context context, String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(context).inflate(
                R.layout.dialog_warning,
                (LinearLayout) view.findViewById(R.id.dialog_warning_LinearLayout)
        );

        builder.setView(viewAlertMapa);

        //Define o titulo do alertDialog
        TextView warning_titulo = (TextView) viewAlertMapa.findViewById(R.id.dialog_warning_titulo);
        warning_titulo.setText(titulo);

        //Define a mensagem do alertDialog
        TextView warning_message = (TextView) viewAlertMapa.findViewById(R.id.dialog_warning_mensagem);
        warning_message.setText(mensagem);

        final AlertDialog alertDialog = builder.create();

        //Define o botao left como invisivel
        Button botaoLeft = (Button) viewAlertMapa.findViewById(R.id.dialog_warning_botao_left);
        botaoLeft.setVisibility(View.INVISIBLE);

        //Define a ação do botão direito
        Button botaoRight = (Button) viewAlertMapa.findViewById(R.id.dialog_warning_botao_right);
        botaoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void info(@NonNull View view, Context context, String titulo, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(context).inflate(
                R.layout.dialog_info,
                (LinearLayout) view.findViewById(R.id.dialog_info_LinearLayout)
        );

        builder.setView(viewAlertMapa);

        //Define o titulo do alertDialog
        TextView info_titulo = (TextView) viewAlertMapa.findViewById(R.id.dialog_info_titulo);
        info_titulo.setText(titulo);

        //Define a mensagem do alertDialog
        TextView info_message = (TextView) viewAlertMapa.findViewById(R.id.dialog_info_mensagem);
        info_message.setText(mensagem);

        final AlertDialog alertDialog = builder.create();

        //Define o botao left como invisivel
        Button botaoLeft = (Button) viewAlertMapa.findViewById(R.id.dialog_info_botao_left);
        botaoLeft.setVisibility(View.INVISIBLE);

        //Define a ação do botão direito
        Button botaoRight = (Button) viewAlertMapa.findViewById(R.id.dialog_info_botao_right);
        botaoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
