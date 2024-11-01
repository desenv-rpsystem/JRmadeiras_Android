package com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Utils.GpsTracker;
import com.rp_grf.jrmadeiras.Fragments.MapaGpsFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;
import com.rp_grf.jrmadeiras.Telas.Programas.MOB007_A;

import java.util.ArrayList;

public class MapaEntregaAdapter extends RecyclerView.Adapter<MapaEntregaAdapter.MyViewHolder> {

    Context context;
    ArrayList<Romaneio_ent> romaneio_ent;

    public MapaEntregaAdapter(Context c, ArrayList<Romaneio_ent> i) {
        context = c;
        romaneio_ent = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.item_mapa_entrega, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.w_sequencia_entrega.setText(romaneio_ent.get(i).getNum_seq().toString());
        myViewHolder.w_serie.setText(romaneio_ent.get(i).getCod_ser());
        myViewHolder.w_numero_documento.setText(romaneio_ent.get(i).getNum_doc());
        myViewHolder.w_nome_cliente.setText(romaneio_ent.get(i).getNom_cli());
        myViewHolder.w_endereco.setText(romaneio_ent.get(i).getEnd_cli());
        myViewHolder.w_numero_endereco.setText(romaneio_ent.get(i).getNum_end_cli());
        myViewHolder.w_complemento_bairro.setText(romaneio_ent.get(i).getCom_bai_cli());
        myViewHolder.w_municipio.setText(romaneio_ent.get(i).getNom_mun());
        myViewHolder.w_uf.setText(romaneio_ent.get(i).getCod_uni_fed());
        myViewHolder.w_cep.setText(romaneio_ent.get(i).getCep_cli());
        myViewHolder.w_telefone.setText(romaneio_ent.get(i).getFon_cli() + "   " + romaneio_ent.get(i).getFon_cli_2());
        // myViewHolder.w_telefone_2.setText(romaneio_ent.get(i).getFon_cli_2());
        myViewHolder.w_nome_vendedor.setText(romaneio_ent.get(i).getNom_ven());

        final String num_seq = romaneio_ent.get(i).getNum_seq().toString();
        final String num_reg_nts = romaneio_ent.get(i).getNum_reg_nts().toString();
        final String cod_ser = romaneio_ent.get(i).getCod_ser();
        final String num_doc = romaneio_ent.get(i).getNum_doc();
        final String nom_cli = romaneio_ent.get(i).getNom_cli();
        final String end_cli = romaneio_ent.get(i).getEnd_cli();
        final String num_end_cli = romaneio_ent.get(i).getNum_end_cli();
        final String com_bai_cli = romaneio_ent.get(i).getCom_bai_cli();
        final String nom_mun = romaneio_ent.get(i).getNom_mun();
        final String cod_uni_fed = romaneio_ent.get(i).getCod_uni_fed();
        final String cep_cli = romaneio_ent.get(i).getCep_cli();
        final String fon_cli = romaneio_ent.get(i).getFon_cli();
        final String fon_cli_2 = romaneio_ent.get(i).getFon_cli_2();
        final String nom_ven = romaneio_ent.get(i).getNom_ven();

        //Abre o Fragment de mapa
        myViewHolder.w_cardview_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapaGpsFragment alertDialog = MapaGpsFragment.newInstance();

                String endereco = end_cli + " " + num_end_cli + " " + com_bai_cli + " " + nom_mun + " " + cod_uni_fed + " " + cep_cli;

                GpsTracker gpsTracker = new GpsTracker(context);

                LatLng latLng = gpsTracker.getLocationFromAddress(context, endereco);

                String coordenadas = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);

                abrirDialog(view, coordenadas);

                /*
                Bundle bundle = new Bundle();
                bundle.putString("endereco", endereco);

                alertDialog.setArguments(bundle);

                alertDialog.show(((MOB007) context).getSupportFragmentManager(), "fragment_alert");
                 */

            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MOB007_A.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.putExtra("num_reg_nts", num_reg_nts);
                intent.putExtra("num_seq", num_seq);
                intent.putExtra("cod_ser", cod_ser);
                intent.putExtra("num_doc", num_doc);
                intent.putExtra("nom_cli", nom_cli);
                intent.putExtra("end_cli", end_cli);
                intent.putExtra("num_end_cli", num_end_cli);
                intent.putExtra("com_bai_cli", com_bai_cli);
                intent.putExtra("nom_mun", nom_mun);
                intent.putExtra("cod_uni_fed", cod_uni_fed);
                intent.putExtra("cep_cli", cep_cli);
                intent.putExtra("fon_cli", fon_cli);
                intent.putExtra("fon_cli_2", fon_cli_2);
                intent.putExtra("nom_ven", nom_ven);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return romaneio_ent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView w_sequencia_entrega;
        TextView w_serie;
        TextView w_numero_documento;
        TextView w_nome_cliente;
        TextView w_endereco;
        TextView w_numero_endereco;
        TextView w_complemento_bairro;
        TextView w_municipio;
        TextView w_uf;
        TextView w_cep;
        TextView w_telefone;
        TextView w_telefone_2;
        TextView w_nome_vendedor;

        CardView w_cardview_mapa;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            w_sequencia_entrega = (TextView) itemView.findViewById(R.id.mapa_entrega_num_seq);
            w_serie = (TextView) itemView.findViewById(R.id.mapa_entrega_cod_ser);
            w_numero_documento = (TextView) itemView.findViewById(R.id.mapa_entrega_num_doc);
            w_nome_cliente = (TextView) itemView.findViewById(R.id.mapa_entrega_nom_cli);
            w_endereco = (TextView) itemView.findViewById(R.id.mapa_entrega_end_cli);
            w_numero_endereco = (TextView) itemView.findViewById(R.id.mapa_entrega_num_end_cli);
            w_complemento_bairro = (TextView) itemView.findViewById(R.id.mapa_entrega_com_bai_cli);
            w_municipio = (TextView) itemView.findViewById(R.id.mapa_entrega_nom_mun);
            w_uf = (TextView) itemView.findViewById(R.id.mapa_entrega_cod_uni_fed);
            w_cep = (TextView) itemView.findViewById(R.id.mapa_entrega_cep_cli);
            w_telefone = (TextView) itemView.findViewById(R.id.mapa_entrega_fon_cli);
            //w_telefone_2 = (TextView) itemView.findViewById(R.id.mapa_entrega_fon_cli_2);
            w_nome_vendedor = (TextView) itemView.findViewById(R.id.mapa_entrega_nom_ven);

            w_cardview_mapa = (CardView) itemView.findViewById(R.id.mapa_entrega_card_view_botao_mapa);
        }
    }

    private void abrirDialog(@NonNull View view, final String coordenadas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(context).inflate(
                R.layout.dialog_gps,
                (LinearLayout) view.findViewById(R.id.dialog_gps_LinearLayout)
        );

        builder.setView(viewAlertMapa);

        //Define o titulo do alertDialog
        TextView titulo = (TextView) viewAlertMapa.findViewById(R.id.dialog_gps_titulo);
        titulo.setText("Abrir com...");

        final AlertDialog alertMapa = builder.create();

        //Define a ação do botão GoogleMaps do alertDialog
        viewAlertMapa.findViewById(R.id.dialog_gps_botao_googlemaps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGoogleMaps(view, coordenadas);
            }
        });

        //Define a ação do botão Waze do alertDialog
        viewAlertMapa.findViewById(R.id.dialog_gps_botao_waze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirWaze(view, coordenadas);
            }
        });

        //Define a ação do botão Here do alertDialog
        viewAlertMapa.findViewById(R.id.dialog_gps_botao_here).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHere(view, coordenadas);
            }
        });

        alertMapa.show();
    }

    //Usa a URI como intent para abrir a chamada de aplicativo externo
    private void abrirGoogleMaps(@NonNull View view, String coordenadas) {
        try {
            Uri uri = Uri.parse("google.navigation:q=" + coordenadas);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }


        } catch (Exception ex) {
            AlertMessage alertMessage = new AlertMessage(context);
            alertMessage.warning(view, context, "Atenção",
                    "Não foi possível carregar a rota.\n\nVerifique se o aplicativo selecionado está instalado.");
        }

    }

    //Usa a URI como intent para abrir a chamada de aplicativo externo
    private void abrirWaze(@NonNull View view, String coordenadas) {
        try {
            String uri = "waze://?ll=" + coordenadas + "&navigate=yes";

            context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception ex) {
            AlertMessage alertMessage = new AlertMessage(context);
            alertMessage.warning(view, context, "Atenção",
                    "Não foi possível carregar a rota.\n\nVerifique se o aplicativo selecionado está instalado.");
        }

    }

    //Usa a URI como intent para abrir a chamada de aplicativo externo
    private void abrirHere(@NonNull View view, String coordenadas) {
        try {
            String uri = "here.directions://v1.0/mylocation/" + coordenadas + "?m=w";

            context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception ex) {
            AlertMessage alertMessage = new AlertMessage(context);
            alertMessage.warning(view, context, "Atenção",
                    "Não foi possível carregar a rota.\n\nVerifique se o aplicativo selecionado está instalado.");
        }

    }

}