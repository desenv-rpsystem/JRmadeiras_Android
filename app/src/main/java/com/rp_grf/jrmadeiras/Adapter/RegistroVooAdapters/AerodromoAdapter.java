package com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Interfaces.RetornaAerodromo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;

import java.util.ArrayList;

public class AerodromoAdapter extends RecyclerView.Adapter<AerodromoAdapter.MyViewHolder> {

    Context context;
    Dialog dialog;
    ArrayList<Aerodromo> registro;
    RetornaAerodromo retornaAerodromo_interface;

    public AerodromoAdapter(Context c, Dialog d, ArrayList<Aerodromo> r, RetornaAerodromo retornaAerodromo) {
        context = c;
        dialog = d;
        registro = r;
        retornaAerodromo_interface = retornaAerodromo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_busca_aerodromo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo.setText(registro.get(i).getCod_aer().toString());
        myViewHolder.w_nome.setText(registro.get(i).getNom_aer());
        myViewHolder.w_sigla.setText(registro.get(i).getSig_aer());

        final String cod_aer = registro.get(i).getCod_aer().toString();
        final String nom_aer = registro.get(i).getNom_aer();
        final String sig_aer = registro.get(i).getSig_aer();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retornaAerodromo_interface.recebeAerodromo(cod_aer, nom_aer, sig_aer);

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
        TextView w_nome;
        TextView w_sigla;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo = (TextView) itemView.findViewById(R.id.cod_aer_busca);
            w_nome = (TextView) itemView.findViewById(R.id.nom_aer_busca);
            w_sigla = (TextView) itemView.findViewById(R.id.sig_aer_busca);
        }
    }
}
