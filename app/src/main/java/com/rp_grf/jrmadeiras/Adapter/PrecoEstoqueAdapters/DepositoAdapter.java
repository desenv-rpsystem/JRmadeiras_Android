package com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Deposito;
import com.rp_grf.jrmadeiras.Tabelas.Item_dep;

import java.util.ArrayList;

public class DepositoAdapter extends RecyclerView.Adapter<DepositoAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item_dep> item_dep;
    ArrayList<Deposito> deposito;

    Long codigo_deposito;
    String nome_deposito;

    public DepositoAdapter(Context c, ArrayList<Item_dep> i,  ArrayList<Deposito> d) {
        context = c;
        item_dep = i;
        deposito = d;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.linha_item_dep, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    /*
        if (deposito == null || deposito.isEmpty()) {
            myViewHolder.w_descricao.setText("Sem Registro");
        } else {
            myViewHolder.w_descricao.setText(deposito.get(i).getNom_dep());
        }
    */
        if (item_dep == null || item_dep.isEmpty()) {
            myViewHolder.w_quantidade.setText("N/A");
            myViewHolder.w_descricao.setText("Sem Registro");
        } else {
            myViewHolder.w_quantidade.setText(item_dep.get(i).getQtd_est());
            myViewHolder.w_descricao.setText(item_dep.get(i).getNom_dep());
        }

    }

    @Override
    public int getItemCount() {
        return (item_dep == null || item_dep.isEmpty()) ? 1 : item_dep.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_descricao;
        TextView w_quantidade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_descricao = (TextView) itemView.findViewById(R.id.linha_nom_dep);
            w_quantidade = (TextView) itemView.findViewById(R.id.linha_qtd_est);
        }
    }
}
