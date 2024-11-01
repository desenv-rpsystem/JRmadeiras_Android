package com.rp_grf.jrmadeiras.Adapter.MenuAdapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Modulos;
import com.rp_grf.jrmadeiras.Telas.Menu.ProgramasActivity;

import java.util.ArrayList;

public class ModulosAdapter extends RecyclerView.Adapter<ModulosAdapter.MyViewHolder> {

    Context context;
    ArrayList<Modulos> modulos;

    public ModulosAdapter(Context c, ArrayList<Modulos> p) {
        context = c;
        modulos = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.card_modulo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // myViewHolder.w_sequencia.setText(modulos.get(i).getNum_seq());
        myViewHolder.w_descricao.setText(modulos.get(i).getNom_mod());

        final String getCodigo = modulos.get(i).getCod_mod();
        final String getDescricao = modulos.get(i).getNom_mod();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProgramasActivity.class);
                intent.putExtra("cod_mod",getCodigo);
                intent.putExtra("nom_mod",getDescricao);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modulos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            w_descricao = (TextView) itemView.findViewById(R.id.nom_mod);
        }
    }
}
