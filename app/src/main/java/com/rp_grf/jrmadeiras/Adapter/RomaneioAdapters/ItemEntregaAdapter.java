package com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;

import java.util.ArrayList;

public class ItemEntregaAdapter extends RecyclerView.Adapter<ItemEntregaAdapter.MyViewHolder> {
    Context context;
    ArrayList<Item_romaneio_ent> item_romaneio_ent;

    public ItemEntregaAdapter(Context c, ArrayList<Item_romaneio_ent> i) {
        context = c;
        item_romaneio_ent = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_confirmar_entrega, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo_item.setText(item_romaneio_ent.get(i).getCod_ite().toString());
        myViewHolder.w_descricao_item.setText(item_romaneio_ent.get(i).getNom_ite());
        myViewHolder.w_quantidade_item.setText(item_romaneio_ent.get(i).getQtd_ite());
        myViewHolder.w_unidade_item.setText(item_romaneio_ent.get(i).getCod_uni());
    }

    @Override
    public int getItemCount() {
        return item_romaneio_ent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_codigo_item;
        TextView w_descricao_item;
        TextView w_quantidade_item;
        TextView w_unidade_item;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo_item = (TextView) itemView.findViewById(R.id.mapa_entrega_cod_ite);
            w_descricao_item = (TextView) itemView.findViewById(R.id.mapa_entrega_nom_ite);
            w_quantidade_item = (TextView) itemView.findViewById(R.id.mapa_entrega_qtd_ite);
            w_unidade_item = (TextView) itemView.findViewById(R.id.mapa_entrega_cod_uni);
        }
    }

}
