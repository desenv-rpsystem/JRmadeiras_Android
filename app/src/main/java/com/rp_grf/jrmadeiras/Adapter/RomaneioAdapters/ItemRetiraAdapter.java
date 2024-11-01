package com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Interfaces.RetornaQuantidade;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;

import java.util.ArrayList;

public class ItemRetiraAdapter extends RecyclerView.Adapter<ItemRetiraAdapter.MyViewHolder> {
    Context context;
    ArrayList<Item_romaneio_ent> item_romaneio_ent;

    RetornaQuantidade retornaQuantidade_interface;

    public ItemRetiraAdapter(Context c, ArrayList<Item_romaneio_ent> i, RetornaQuantidade retornaQuantidade) {
        context = c;
        item_romaneio_ent = i;
        retornaQuantidade_interface = retornaQuantidade;
    }

    @NonNull
    @Override
    public ItemRetiraAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemRetiraAdapter.MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_confirmar_retira, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRetiraAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.w_codigo_item.setText(item_romaneio_ent.get(i).getCod_ite().toString());
        myViewHolder.w_descricao_item.setText(item_romaneio_ent.get(i).getNom_ite());
        myViewHolder.w_quantidade_item.setText(item_romaneio_ent.get(i).getQtd_ite());
        myViewHolder.w_unidade_item.setText(item_romaneio_ent.get(i).getCod_uni());

        String deposito = item_romaneio_ent.get(i).getCod_dep() + " " + item_romaneio_ent.get(i).getNom_dep();
        myViewHolder.w_deposito_item.setText(deposito);

        myViewHolder.w_localizacao_item.setText(item_romaneio_ent.get(i).getCod_ide_loc());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarCor(myViewHolder);
            }
        });

        myViewHolder.w_icone_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String quantidade_item = item_romaneio_ent.get(i).getQtd_ite();
                    String quantidade_pendente = myViewHolder.w_quantidade_pendente.getText().toString();
                    abrirDialog(view, quantidade_item, quantidade_pendente, myViewHolder, item_romaneio_ent.get(i));
                } catch (Exception ex) {
                    AlertMessage alertMessage = new AlertMessage(context);
                    alertMessage.error(view, context, "Erro", ex.toString());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return item_romaneio_ent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Boolean flag_checked = false;

        ImageView w_icone_checked;
        TextView w_flag_checked;

        TextView w_codigo_texto;
        TextView w_codigo_item;

        TextView w_descricao_texto;
        TextView w_descricao_item;

        TextView w_quantidade_texto;
        TextView w_quantidade_item;

        TextView w_quantidade_pendente;

        TextView w_unidade_texto;
        TextView w_unidade_item;

        TextView w_deposito_texto;
        TextView w_deposito_item;

        TextView w_localizacao_texto;
        TextView w_localizacao_item;

        ImageView w_icone_editar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_icone_checked = (ImageView) itemView.findViewById(R.id.mapa_retira_icone_checked);
            w_flag_checked = (TextView) itemView.findViewById(R.id.mapa_retira_flag_checked);

            w_codigo_texto = (TextView) itemView.findViewById(R.id.mapa_retira_codigo);
            w_codigo_item = (TextView) itemView.findViewById(R.id.mapa_retira_cod_ite);

            w_descricao_texto = (TextView) itemView.findViewById(R.id.mapa_retira_descricao);
            w_descricao_item = (TextView) itemView.findViewById(R.id.mapa_retira_nom_ite);

            w_quantidade_texto = (TextView) itemView.findViewById(R.id.mapa_retira_quantidade);
            w_quantidade_item = (TextView) itemView.findViewById(R.id.mapa_retira_qtd_ite);

            w_quantidade_pendente = (TextView) itemView.findViewById(R.id.mapa_retira_pendente);

            w_unidade_texto = (TextView) itemView.findViewById(R.id.mapa_retira_unidade);
            w_unidade_item = (TextView) itemView.findViewById(R.id.mapa_retira_cod_uni);

            w_deposito_texto = (TextView) itemView.findViewById(R.id.mapa_retira_deposito);
            w_deposito_item = (TextView) itemView.findViewById(R.id.mapa_retira_cod_dep);

            w_localizacao_texto = (TextView) itemView.findViewById(R.id.mapa_retira_localizacao);
            w_localizacao_item = (TextView) itemView.findViewById(R.id.mapa_retira_ide_cod_loc);

            w_icone_editar = (ImageView) itemView.findViewById(R.id.mapa_retira_icone_editar);
        }
    }

    private void abrirDialog(@NonNull View view, String quantidade_item, String quantidade_pendete, MyViewHolder myViewHolder, Item_romaneio_ent item_romaneio_ent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertEditar = LayoutInflater.from(context).inflate(
                R.layout.dialog_editar_quantidade,
                (LinearLayout) view.findViewById(R.id.dialog_editar_quantidade_LinearLayout)
        );

        builder.setView(viewAlertEditar);

        //Define o titulo do alertDialog
        TextView titulo = (TextView) viewAlertEditar.findViewById(R.id.dialog_editar_quantidade_titulo);
        titulo.setText("Pêndencia de entrega");

        final AlertDialog alertEditar = builder.create();

        TextView w_qtd_ite = (TextView) viewAlertEditar.findViewById(R.id.dialog_editar_quantidade_qtd_ite);
        w_qtd_ite.setText(quantidade_item);

        EditText w_qtd_pen = (EditText) viewAlertEditar.findViewById(R.id.dialog_editar_quantidade_qtd_pen);
        w_qtd_pen.setText(quantidade_pendete);

        //Impede de começar com zero
        w_qtd_pen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (w_qtd_pen.getText().toString().matches("^0")) {
                    w_qtd_pen.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Define a ação do botão cancelar
        Button w_botao_cancelar = (Button) viewAlertEditar.findViewById(R.id.dialog_editar_quantidade_botao_cancelar);
        w_botao_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertEditar.dismiss();
            }
        });

        //Define a ação do botão salvar
        Button w_botao_gravar = (Button) viewAlertEditar.findViewById(R.id.dialog_editar_quantidade_botao_gravar);
        w_botao_gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String quantidade_saldo = w_qtd_ite.getText().toString(); //recebe a quantidade do item
                    String quantidade_pendente = w_qtd_pen.getText().toString(); //recebe a nova quantidade

                    System.out.println("w_qtd_pen+++ " + quantidade_pendente);

                    if (validarQuantidade(quantidade_saldo, quantidade_pendente) == true) {

                        if (quantidade_pendente.equals("") || quantidade_pendente.isEmpty()) {
                            myViewHolder.w_quantidade_pendente.setText("0");
                        } else {
                            myViewHolder.w_quantidade_pendente.setText(quantidade_pendente); //guarda a quantidade pendente
                        }

                        Item_romaneio_ent tab_item_romaneio_ent = atualizarQuantidade(myViewHolder, item_romaneio_ent);
                        retornaQuantidade_interface.recebeQuantidade(tab_item_romaneio_ent);
                        alertEditar.dismiss();
                    } else {
                        AlertMessage alertMessage = new AlertMessage(context);
                        alertMessage.warning(view, context, "Atenção",
                                "A quantidade pendente não pode ser maior que a quantidade à entregar");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    alertEditar.dismiss();
                }
            }
        });

        alertEditar.setCancelable(false);
        alertEditar.show();
    }

    private Boolean validarQuantidade(String qtd_ite, String qtd_pen) {

        Double saldo = Double.parseDouble(qtd_ite);
        Double pendente;

        try {
            pendente = Double.parseDouble(qtd_pen);
        } catch (Exception ex) {
            pendente = Double.parseDouble("0");
        }


        if (pendente > saldo) {
            return false;
        } else {
            return true;
        }
    }

    private Item_romaneio_ent atualizarQuantidade(MyViewHolder myViewHolder, Item_romaneio_ent param_item_romaneio_ent) {

        Item_romaneio_ent tab_item_romaneio_ent = new Item_romaneio_ent();

        tab_item_romaneio_ent = param_item_romaneio_ent;

        tab_item_romaneio_ent.setQtd_pen(myViewHolder.w_quantidade_pendente.getText().toString());

        return tab_item_romaneio_ent;
    }

    private void mudarCor(MyViewHolder myViewHolder) {

        if (myViewHolder.flag_checked == false) {

            myViewHolder.flag_checked = true;

            myViewHolder.w_codigo_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_codigo_item.setTextColor(context.getResources().getColor(R.color.checked_green));

            myViewHolder.w_descricao_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_descricao_item.setTextColor(context.getResources().getColor(R.color.checked_light_green));

            myViewHolder.w_quantidade_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_quantidade_item.setTextColor(context.getResources().getColor(R.color.checked_light_green));

            myViewHolder.w_unidade_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_unidade_item.setTextColor(context.getResources().getColor(R.color.checked_light_green));

            myViewHolder.w_deposito_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_deposito_item.setTextColor(context.getResources().getColor(R.color.checked_light_green));

            myViewHolder.w_localizacao_texto.setTextColor(context.getResources().getColor(R.color.checked_green));
            myViewHolder.w_localizacao_item.setTextColor(context.getResources().getColor(R.color.checked_light_green));

            myViewHolder.w_icone_checked.setImageResource(R.drawable.icone_checked_verde);

        } else if (myViewHolder.flag_checked == true) {

            myViewHolder.flag_checked = false;

            myViewHolder.w_codigo_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_codigo_item.setTextColor(context.getResources().getColor(R.color.black));

            myViewHolder.w_descricao_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_descricao_item.setTextColor(context.getResources().getColor(R.color.cinza_escuro));

            myViewHolder.w_quantidade_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_quantidade_item.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            myViewHolder.w_unidade_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_unidade_item.setTextColor(context.getResources().getColor(R.color.cinza_escuro));

            myViewHolder.w_deposito_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_deposito_item.setTextColor(context.getResources().getColor(R.color.cinza_escuro));

            myViewHolder.w_localizacao_texto.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.w_localizacao_item.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            myViewHolder.w_icone_checked.setImageResource(R.drawable.icone_unchecked);
        }

    }
}
