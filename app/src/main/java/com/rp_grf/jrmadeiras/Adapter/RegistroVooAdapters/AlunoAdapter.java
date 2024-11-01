package com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Interfaces.RetornaAluno;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Cliente;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.MyViewHolder> {

    Context context;
    Dialog dialog;
    ArrayList<Cliente> registro;
    RetornaAluno retornaAluno_interface;

    public AlunoAdapter(Context c, Dialog d, ArrayList<Cliente> r, RetornaAluno retornaAluno) {
        context = c;
        dialog = d;
        registro = r;
        retornaAluno_interface = retornaAluno;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_busca_aluno, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo.setText(registro.get(i).getCod_cli().toString());
        myViewHolder.w_nome.setText(registro.get(i).getNom_cli());
        myViewHolder.w_fantasia.setText(registro.get(i).getNom_fan_cli());
        myViewHolder.w_anac.setText(registro.get(i).getCod_alt());
        myViewHolder.w_exame.setText(registro.get(i).getDat_exa_med());

        final String cod_cli = registro.get(i).getCod_cli().toString();
        final String nom_cli = registro.get(i).getNom_cli();
        final String nom_fan_cli = registro.get(i).getNom_fan_cli();
        final String cod_alt = registro.get(i).getCod_alt();
        final String dat_exa_med = registro.get(i).getDat_exa_med();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retornaAluno_interface.recebeAluno(
                        cod_cli, nom_cli, nom_fan_cli, cod_alt, dat_exa_med);

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
        TextView w_fantasia;
        TextView w_anac;
        TextView w_exame;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_codigo = (TextView) itemView.findViewById(R.id.cod_cli_busca);
            w_nome = (TextView) itemView.findViewById(R.id.nom_cli_busca);
            w_fantasia = (TextView) itemView.findViewById(R.id.nom_fan_cli_busca);
            w_anac = (TextView) itemView.findViewById(R.id.cod_alt_busca);
            w_exame = (TextView) itemView.findViewById(R.id.dat_exa_med_busca);
        }
    }
}
