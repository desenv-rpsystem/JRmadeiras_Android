package com.rp_grf.jrmadeiras.Adapter.LiberacaoAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Det_lib_reg_nts;

import java.util.ArrayList;

public class DetalheLiberacaoAdapter extends RecyclerView.Adapter<DetalheLiberacaoAdapter.MyViewHolder>{


    Context context;
    ArrayList<Det_lib_reg_nts> detalhe;

    public DetalheLiberacaoAdapter(Context c, ArrayList<Det_lib_reg_nts> r) {
        context = c;
        detalhe = r;
    }

    @NonNull
    @Override
    public DetalheLiberacaoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DetalheLiberacaoAdapter.MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.linha_item_detalhe, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetalheLiberacaoAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.w_descricao.setText(detalhe.get(i).getNom_ite());
        myViewHolder.w_quantidade.setText(detalhe.get(i).getQtd_ite());
        myViewHolder.w_unidade.setText(detalhe.get(i).getCod_uni());
        myViewHolder.w_valor.setText(detalhe.get(i).getVal_tot_ite());
        myViewHolder.w_margem.setText(detalhe.get(i).getPor_mar());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return detalhe.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_descricao;
        TextView w_quantidade;
        TextView w_unidade;
        TextView w_valor;
        TextView w_margem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_descricao = (TextView) itemView.findViewById(R.id.linha_detalhes_nom_ite);
            w_quantidade = (TextView) itemView.findViewById(R.id.linha_detalhes_qtd_ite);
            w_unidade = (TextView) itemView.findViewById(R.id.linha_detalhes_cod_uni);
            w_valor = (TextView) itemView.findViewById(R.id.linha_detalhes_val_uni);
            w_margem = (TextView) itemView.findViewById(R.id.linha_detalhes_por_mar);

        }
    }
}
