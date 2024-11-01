package com.rp_grf.jrmadeiras.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.AerodromoAdapter;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAerodromo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;

import java.util.ArrayList;
import java.util.Collections;

public class BuscaAerodromoFragment extends DialogFragment {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    private static RetornaAerodromo retornaAerodromo_interface;

    private boolean flag_online;

    private Long codigo_aerodromo;
    private String nome_aerodromo;
    private String sigla_aerodromo;

    private BuscaAerodromoFragment.FragmentAListener listener;

    private EditText w_pesquisa_aerodromo;
    private CardView w_botao_pesquisa;

    RecyclerView w_aerodromo;

    private ArrayList<Aerodromo> lista_aerodromo;
    private AerodromoAdapter aerodromoAdapter;

    ProgressDialog dialogPesquisa;


    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    public BuscaAerodromoFragment() {

    }

    public static BuscaAerodromoFragment newInstance(String title, RetornaAerodromo retornaAerodromo) {
        BuscaAerodromoFragment frag = new BuscaAerodromoFragment();
        Bundle args = new Bundle();
        args.putString("", title);
        frag.setArguments(args);
        retornaAerodromo_interface = retornaAerodromo;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca_aerodromo, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Chama os dados temporarios do usuário
        SharedPreferences settings = this.getActivity().getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Aerodromo");

        iniciarCampos(view);

        if (flag_online) {
            consultarAerodromo_Online("");
        } else {
            consultarAerodromo_Offline("");
        }

        cliqueBotaoPesquisar();

        String title = getArguments().getString("title", "");
        getDialog().setTitle(title);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
        } else {
            alertSmall("Erro ao verificar se há conexão!");
        }
    }

    private void iniciarCampos(View view) {
        w_pesquisa_aerodromo = view.findViewById(R.id.edit_text_pesquisar_aerodromo);
        w_pesquisa_aerodromo.setHint("Pesquisar Aerodromo");

        w_botao_pesquisa = view.findViewById(R.id.card_view_pesquisar_aerodromo);

        w_aerodromo = view.findViewById(R.id.recycler_view_aerodromo);

        lista_aerodromo = new ArrayList<Aerodromo>();

        dialogPesquisa = new ProgressDialog(getActivity(), R.style.DialogTheme);
    }

    private void cliqueBotaoPesquisar() {
        w_botao_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fecha o teclado ao clicar no botão
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                String texto_pesquisa = w_pesquisa_aerodromo.getText().toString();

                if (flag_online) {
                    consultarAerodromo_Online(texto_pesquisa);
                } else {
                    consultarAerodromo_Offline(texto_pesquisa);
                }
            }
        });
    }

    //Faz a consulta no banco Firebase para filtrar a lista de aerodromos
    private void consultarAerodromo_Online(final String texto_pesquisa) {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.show();

        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_aerodromo.clear();

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Aerodromo tab_aerodromo = retorno.getValue(Aerodromo.class);
                    String cod_aer = tab_aerodromo.getCod_aer().toString().trim();
                    String nom_aer = tab_aerodromo.getNom_aer();
                    String sig_aer = tab_aerodromo.getSig_aer();

                    if ((validaPesquisa(texto_pesquisa, cod_aer) == true) ||
                            (validaPesquisa(texto_pesquisa, nom_aer) == true) ||
                            (validaPesquisa(texto_pesquisa, sig_aer) == true)) {

                        lista_aerodromo.add(tab_aerodromo);

                    }

                }

                Collections.sort(lista_aerodromo);

                aerodromoAdapter = new AerodromoAdapter(getActivity(), getDialog(),
                        lista_aerodromo, retornaAerodromo_interface);
                w_aerodromo.setAdapter(aerodromoAdapter);
                aerodromoAdapter.notifyDataSetChanged();

                dialogPesquisa.dismiss();

                if (lista_aerodromo.isEmpty()) {
                    alertSmall("Nenhum Registro Encontrado !!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    //Faz a consulta no banco SQLite para filtrar a lista de alunos
    private void consultarAerodromo_Offline(final String texto_pesquisa) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Aerodromo> retornoAerodromo = bancoRegistroVoo.getAerodromo();

        lista_aerodromo.clear();

        for (int i = 0; i < retornoAerodromo.size(); i++) {
            Aerodromo tab_aerodromo = new Aerodromo();

            String cod_aer = retornoAerodromo.get(i).getCod_aer().toString();
            String nom_aer = retornoAerodromo.get(i).getNom_aer();
            String sig_aer = retornoAerodromo.get(i).getSig_aer();

            tab_aerodromo.setCod_aer(retornoAerodromo.get(i).getCod_aer());
            tab_aerodromo.setNom_aer(retornoAerodromo.get(i).getNom_aer());
            tab_aerodromo.setSig_aer(retornoAerodromo.get(i).getSig_aer());

            if ((validaPesquisa(texto_pesquisa, cod_aer) == true) ||
                    (validaPesquisa(texto_pesquisa, nom_aer) == true) ||
                    (validaPesquisa(texto_pesquisa, sig_aer) == true)) {

                lista_aerodromo.add(tab_aerodromo);

            }
        }

        Collections.sort(lista_aerodromo);

        aerodromoAdapter = new AerodromoAdapter(getActivity(), getDialog(),
                lista_aerodromo, retornaAerodromo_interface);
        w_aerodromo.setAdapter(aerodromoAdapter);
        aerodromoAdapter.notifyDataSetChanged();

        if (lista_aerodromo.isEmpty()) {
            alertSmall("Nenhum Registro Encontrado !!!");
        }
    }

    private boolean validaPesquisa(String texto_pesquisa, String texto_banco) {
        Boolean retorno = false;

        String[] array_separador = texto_pesquisa.toLowerCase().split("[ *]+");

        try {
            for (int i = 0; i < array_separador.length; i++) {
                if (texto_banco.toLowerCase().contains(array_separador[i])) {
                    retorno = true;
                } else {
                    retorno = false;
                    break;
                }
            }
        } catch (Exception ex) {
            alertSmall(ex.toString());
        }

        return retorno;
    }

    private void alertSmall(String s) {
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        System.out.println(s);
    }
}