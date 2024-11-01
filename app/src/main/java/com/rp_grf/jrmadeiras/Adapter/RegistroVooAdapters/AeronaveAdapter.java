package com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Interfaces.RetornaAeronave;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;

import java.util.ArrayList;

public class AeronaveAdapter extends RecyclerView.Adapter<AeronaveAdapter.MyViewHolder> {

    Context context;
    Dialog dialog;
    ArrayList<Aeronave> registro;
    RetornaAeronave retornaAeronave_interface;

    public AeronaveAdapter(Context c, Dialog d, ArrayList<Aeronave> r, RetornaAeronave retornaAeronave) {
        context = c;
        dialog = d;
        registro = r;
        retornaAeronave_interface = retornaAeronave;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_busca_aeronave, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo.setText(registro.get(i).getCod_bem().toString());
        myViewHolder.w_prefixo.setText(registro.get(i).getPre_bem());
        //myViewHolder.w_nome.setText(registro.get(i).getNom_bem());
        myViewHolder.w_tipo.setText(registro.get(i).getTip_bem());

        final String cod_bem = registro.get(i).getCod_bem().toString();
        final String pre_bem = registro.get(i).getPre_bem();
        final String nom_bem = registro.get(i).getNom_bem();
        final String tip_bem = registro.get(i).getTip_bem();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retornaAeronave_interface.recebeAeronave(cod_bem, pre_bem, nom_bem, tip_bem);

                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return registro.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_codigo;
        TextView w_prefixo;
        TextView w_nome;
        TextView w_tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo = (TextView) itemView.findViewById(R.id.cod_bem_busca);
            w_prefixo = (TextView) itemView.findViewById(R.id.pre_bem_busca);
            //w_nome = (TextView) itemView.findViewById(R.id.nom_bem_busca);
            w_tipo = (TextView) itemView.findViewById(R.id.tip_bem_busca);
        }
    }
}

