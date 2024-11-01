package com.rp_grf.jrmadeiras.Adapter.PrecoEstoqueAdapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Telas.Programas.MOB001_A;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> item;

    public ItemAdapter(Context c, ArrayList<Item> i) {
        context = c;
        item = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_preco_estoque, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo.setText(item.get(i).getCod_ite().toString());
        myViewHolder.w_descricao.setText(item.get(i).getNom_ite());
        myViewHolder.w_referencia.setText(item.get(i).getRef_ite());
        myViewHolder.w_marca.setText(item.get(i).getNom_mar());
        myViewHolder.w_unidade.setText(item.get(i).getCod_uni());
        myViewHolder.w_valor_unitario.setText(item.get(i).getVal_uni());
        myViewHolder.w_quantidade_estoque.setText(item.get(i).getQtd_est());

        final String cod_ite = item.get(i).getCod_ite().toString();
        final String nom_ite = item.get(i).getNom_ite();
        final String ref_ite = item.get(i).getRef_ite();
        final String nom_mar = item.get(i).getNom_mar();
        final String cod_uni = item.get(i).getCod_uni();
        final String val_uni = item.get(i).getVal_uni();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MOB001_A.class);
                intent.putExtra("cod_ite",cod_ite);
                intent.putExtra("nom_ite",nom_ite);
                intent.putExtra("ref_ite",ref_ite);
                intent.putExtra("nom_mar",nom_mar);
                intent.putExtra("cod_uni",cod_uni);
                intent.putExtra("val_uni",val_uni);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_codigo;
        TextView w_descricao;
        TextView w_referencia;
        TextView w_marca;
        TextView w_unidade;
        TextView w_valor_unitario;
        TextView w_quantidade_estoque;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo = (TextView) itemView.findViewById(R.id.cod_ite);
            w_descricao = (TextView) itemView.findViewById(R.id.nom_ite);
            w_referencia = (TextView) itemView.findViewById(R.id.ref_ite);
            w_marca = (TextView) itemView.findViewById(R.id.nom_mar);
            w_unidade = (TextView) itemView.findViewById(R.id.cod_uni);
            w_valor_unitario = (TextView) itemView.findViewById(R.id.val_uni);
            w_quantidade_estoque = (TextView) itemView.findViewById(R.id.qtd_est);
        }
    }

}
