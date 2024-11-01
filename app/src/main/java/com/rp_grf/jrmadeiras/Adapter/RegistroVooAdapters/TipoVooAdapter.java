package com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Interfaces.RetornaTipo_voo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Tipo_voo;

import java.util.ArrayList;

public class TipoVooAdapter extends RecyclerView.Adapter<TipoVooAdapter.MyViewHolder> {

    Context context;
    Dialog dialog;
    ArrayList<Tipo_voo> registro;
    RetornaTipo_voo retornaTipo_voo_interface;

    public TipoVooAdapter(Context c, Dialog d, ArrayList<Tipo_voo> r, RetornaTipo_voo retornaTipo_voo) {
        context = c;
        dialog = d;
        registro = r;
        retornaTipo_voo_interface = retornaTipo_voo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_busca_tipo_voo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo.setText(registro.get(i).getCod_tip_voo().toString());
        myViewHolder.w_tipo.setText(registro.get(i).getNom_tip_voo());

        final String cod_tip_voo = registro.get(i).getCod_tip_voo().toString();
        final String nom_tip_voo = registro.get(i).getNom_tip_voo();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retornaTipo_voo_interface.recebeTipo_voo(cod_tip_voo, nom_tip_voo);

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
        TextView w_tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo = (TextView) itemView.findViewById(R.id.cod_tip_voo_busca);
            w_tipo = (TextView) itemView.findViewById(R.id.nom_tip_voo_busca);
        }
    }
}
