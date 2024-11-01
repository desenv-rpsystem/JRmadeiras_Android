package com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Interfaces.RetornaPreVoo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo_Aux;

import java.util.ArrayList;

public class PreVooAdapter extends RecyclerView.Adapter<PreVooAdapter.MyViewHolder> {

    Context context;
    Dialog dialog;
    ArrayList<Pre_voo_Aux> registro;
    RetornaPreVoo retornaPreVoo_interface;

    public PreVooAdapter(Context c, Dialog d, ArrayList<Pre_voo_Aux> r, RetornaPreVoo retornaPreVoo) {
        context = c;
        dialog = d;
        registro = r;
        retornaPreVoo_interface = retornaPreVoo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_prevoo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo_prevoo.setText(registro.get(i).getCod_pre_voo().toString());
        myViewHolder.w_nome_aluno.setText(registro.get(i).getNom_cli());
        myViewHolder.w_prefixo_aeronave.setText(registro.get(i).getPre_bem());;
        myViewHolder.w_tipo_voo.setText(registro.get(i).getNom_tip_voo());
        myViewHolder.w_data.setText(registro.get(i).getDat_mov_agv());
        myViewHolder.w_hora.setText(registro.get(i).getPer_ini_agv());


        final String cod_pre_voo = registro.get(i).getCod_pre_voo().toString();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retornaPreVoo_interface.recebePreVoo(cod_pre_voo);

                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return registro.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_codigo_prevoo;
        TextView w_nome_aluno;
        TextView w_prefixo_aeronave;
        TextView w_tipo_voo;
        TextView w_data;
        TextView w_hora;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo_prevoo = (TextView) itemView.findViewById(R.id.prevoo_cod_pre_voo);
            w_nome_aluno = (TextView) itemView.findViewById(R.id.prevoo_nom_cli);
            w_prefixo_aeronave = (TextView) itemView.findViewById(R.id.prevoo_pre_bem);
            w_tipo_voo = (TextView) itemView.findViewById(R.id.prevoo_nom_tip_voo);
            w_data = (TextView) itemView.findViewById(R.id.prevoo_dat_mov_agv);
            w_hora = (TextView) itemView.findViewById(R.id.prevoo_hor_mov_agv);
        }
    }
}
