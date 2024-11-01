package com.rp_grf.jrmadeiras.Adapter.MenuAdapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Programas;

import java.util.ArrayList;

public class ProgramasAdapter extends RecyclerView.Adapter<ProgramasAdapter.MyViewHolder> {

    Context context;
    ArrayList<Programas> programas;

    public ProgramasAdapter(Context c, ArrayList<Programas> p) {
        context = c;
        programas = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.linha_programa, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_descricao.setText(programas.get(i).getDes_prg());

        final String getDescricao = programas.get(i).getDes_prg();
        //Define o codigo do programa em uma string
        final String getPrograma = "com.rp_grf.jrmadeiras.Telas.Programas." +
                programas.get(i).getCod_prg();

        try {
            //Transforma a String em uma chamada de Class
            final Class startActivity = Class.forName(getPrograma);

            //Inicia o programa ao apertar no card view
            myViewHolder.itemView.findViewById(R.id.linear_layout_linha).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, startActivity);
                    intent.putExtra("des_prg", getDescricao);
                    context.startActivity(intent);
                }
            });
        } catch (ClassNotFoundException ex) {
            String mensagem = "Erro ProgramasAdapter: " + ex.toString();
            Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return programas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_descricao = (TextView) itemView.findViewById(R.id.des_prg);
        }
    }
}
