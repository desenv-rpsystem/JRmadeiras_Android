package com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Item_pdc;

import java.util.ArrayList;

public class PedidoCompraAdapter extends RecyclerView.Adapter<PedidoCompraAdapter.MyViewHolder>{
        Context context;
        ArrayList<Item_pdc> item_pdc;

    public PedidoCompraAdapter(Context c, ArrayList<Item_pdc> i) {
        context = c;
        item_pdc = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.linha_item_pdc, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (item_pdc.isEmpty()){
            myViewHolder.w_numero.setText("Sem Registro");
            myViewHolder.w_data.setText("N/A");
            myViewHolder.w_pendente.setText("N/A");
        } else {
            myViewHolder.w_numero.setText(item_pdc.get(i).getNum_pdc());
            myViewHolder.w_data.setText(item_pdc.get(i).getDat_ent());
            myViewHolder.w_pendente.setText(item_pdc.get(i).getQtd_pen());
        }


    }

    @Override
    public int getItemCount() {
        return (item_pdc == null || item_pdc.isEmpty()) ? 1 : item_pdc.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_numero;
        TextView w_data;
        TextView w_pendente;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_numero = (TextView) itemView.findViewById(R.id.linha_num_ped);
            w_data = (TextView) itemView.findViewById(R.id.linha_dat_ent);
            w_pendente = (TextView) itemView.findViewById(R.id.linha_qtd_pen);
        }
    }
}