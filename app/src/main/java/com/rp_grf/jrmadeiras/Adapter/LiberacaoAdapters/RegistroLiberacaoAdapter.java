package com.rp_grf.jrmadeiras.Adapter.LiberacaoAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Lib_reg_nts;
import com.rp_grf.jrmadeiras.Telas.Programas.MOB003_A;

import java.util.ArrayList;

public class RegistroLiberacaoAdapter extends RecyclerView.Adapter<RegistroLiberacaoAdapter.MyViewHolder>{

    Context context;
    ArrayList<Lib_reg_nts> registro;

    public RegistroLiberacaoAdapter(Context c, ArrayList<Lib_reg_nts> r) {
        context = c;
        registro = r;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_liberar_documento, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_registro.setText(registro.get(i).getNum_reg_nts().toString());
        myViewHolder.w_estabelecimento.setText(registro.get(i).getCod_est().toString());
        myViewHolder.w_serie.setText(registro.get(i).getCod_ser());
        myViewHolder.w_documento.setText(registro.get(i).getNum_doc());
        myViewHolder.w_cliente.setText(registro.get(i).getCod_cli());
        myViewHolder.w_vendedor.setText(registro.get(i).getCod_ven());


        final String num_reg_nts = registro.get(i).getNum_reg_nts().toString();
        final String cod_est = registro.get(i).getCod_est().toString();
        final String cod_ser = registro.get(i).getCod_ser();
        final String num_doc = registro.get(i).getNum_doc();
        final String cod_cli = registro.get(i).getCod_cli();
        final String cod_ven = registro.get(i).getCod_ven();
        final String val_cus_tot = registro.get(i).getVal_cus_tot();
        final String val_ven_tot = registro.get(i).getVal_ven_tot();
        final String val_fre = registro.get(i).getVal_fre();
        final String val_des_fin = registro.get(i).getVal_des_fin();
        final String por_mar = registro.get(i).getPor_mar();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MOB003_A.class);
                intent.putExtra("num_reg_nts",num_reg_nts);
                intent.putExtra("cod_est",cod_est);
                intent.putExtra("cod_ser",cod_ser);
                intent.putExtra("num_doc",num_doc);
                intent.putExtra("cod_cli",cod_cli);
                intent.putExtra("cod_ven",cod_ven);
                intent.putExtra("val_cus_tot",val_cus_tot);
                intent.putExtra("val_ven_tot",val_ven_tot);
                intent.putExtra("val_fre",val_fre);
                intent.putExtra("val_des_fin",val_des_fin);
                intent.putExtra("por_mar",por_mar);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return registro.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_registro;
        TextView w_estabelecimento;
        TextView w_serie;
        TextView w_documento;
        TextView w_cliente;
        TextView w_vendedor;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_registro = (TextView) itemView.findViewById(R.id.num_reg_nts);
            w_estabelecimento = (TextView) itemView.findViewById(R.id.cod_est);
            w_serie = (TextView) itemView.findViewById(R.id.cod_ser);
            w_documento = (TextView) itemView.findViewById(R.id.num_doc);
            w_cliente = (TextView) itemView.findViewById(R.id.cod_cli);
            w_vendedor = (TextView) itemView.findViewById(R.id.cod_ven);

        }
    }

}
