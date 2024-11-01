package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Utils.GpsTracker;
import com.rp_grf.jrmadeiras.Utils.HTTPRequest;
import com.rp_grf.jrmadeiras.Utils.Coordenadas;
import com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters.MapaEntregaAdapter;
import com.rp_grf.jrmadeiras.Fragments.MapsFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaEntrega;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MOB007 extends AppCompatActivity {

    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    View view;
    AlertMessage alertMessage;

    String[] polyline;

    Boolean flag_online;

    RecyclerView w_registro_mapa;

    TextView w_text_registro;

    ArrayList<Romaneio_ent> lista_romaneio_ent;

    MapaEntregaAdapter mapaEntregaAdapter;

    String descricao_programa;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob007);

        descricao_programa = getIntent().getStringExtra("des_prg");

        getSupportActionBar().setTitle(descricao_programa);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        alertMessage = new AlertMessage(this);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Romaneio_ent");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        verificarStatusConexao();

    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
        } else {
            alertMessage.error(view, this, "Erro", "Não foi possível verificar se há conexão!");
        }
    }

    private void iniciarCampos() {

        w_registro_mapa = (RecyclerView) findViewById(R.id.recycler_view_mapa_entrega);

        w_text_registro = (TextView) findViewById(R.id.text_view_mapa_entrega);

        lista_romaneio_ent = new ArrayList<Romaneio_ent>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);

    }

    private void verificarStatusConexao() {

        if (flag_online == true) {
            consultarMapaEntrega_Online(false);
        } else if (flag_online == false) {
            alertMessage.warning(view, this, "Atenção",
                    "Sem conexão com a nuvem!\n" +
                            "Reinicie o aplicativo e tente novamente.");
        }
    }

    private void consultarMapaEntrega_Online(Boolean flag_rota) {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_romaneio_ent.clear();
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Romaneio_ent tab_romaneio = retornoBanco.getValue(Romaneio_ent.class);
                    if (tab_romaneio.getDat_lib_re().equals("") && tab_romaneio.getUsu_lib_re().equals("") && tab_romaneio.getHor_lib_re().equals("")) {
                        lista_romaneio_ent.add(tab_romaneio);
                    }
                }

                if (lista_romaneio_ent.isEmpty()) {
                    mostrarMensagemSemRegistro(true);
                } else {
                    mostrarMensagemSemRegistro(false);
                }

                if (flag_rota == true) {
                    lista_romaneio_ent = gerarRota(lista_romaneio_ent); //Ordena pela menor rota
                    abrirMapa(lista_romaneio_ent, polyline);
                } else {
                    Collections.sort(lista_romaneio_ent); //Ordena pela sequencia
                }

                mapaEntregaAdapter = new MapaEntregaAdapter(MOB007.this, lista_romaneio_ent);
                w_registro_mapa.setAdapter(mapaEntregaAdapter);
                mapaEntregaAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void mostrarMensagemSemRegistro(Boolean flag) {

        if (flag == true) {
            w_registro_mapa.setVisibility(View.GONE);

            w_text_registro.setVisibility(View.VISIBLE);
            w_text_registro.setText("Não há romaneios de entrega!");
        } else {
            w_registro_mapa.setVisibility(View.VISIBLE);

            w_text_registro.setVisibility(View.GONE);
        }

    }

    private void consultarMapaEntrega_Offline() {

        BancoMapaEntrega bancoMapaEntrega = new BancoMapaEntrega(this);

        ArrayList<Romaneio_ent> retorno_romaneio_ent = bancoMapaEntrega.getRomaneio_Ent();

        lista_romaneio_ent.clear();

        try {

            for (int i = 0; i < retorno_romaneio_ent.size(); i++) {

                Romaneio_ent tab_romaneio_ent = new Romaneio_ent();

                Long num_seq = retorno_romaneio_ent.get(i).getNum_seq();
                Long num_reg_nts = retorno_romaneio_ent.get(i).getNum_reg_nts();
                String cod_ser = retorno_romaneio_ent.get(i).getCod_ser();
                String num_doc = retorno_romaneio_ent.get(i).getNum_doc();
                String nom_cli = retorno_romaneio_ent.get(i).getNom_cli();
                String end_cli = retorno_romaneio_ent.get(i).getEnd_cli();
                String num_end_cli = retorno_romaneio_ent.get(i).getNum_end_cli();
                String com_bai_cli = retorno_romaneio_ent.get(i).getCom_bai_cli();
                String nom_mun = retorno_romaneio_ent.get(i).getNom_mun();
                String cod_uni_fed = retorno_romaneio_ent.get(i).getCod_uni_fed();
                String cep_cli = retorno_romaneio_ent.get(i).getCep_cli();
                String fon_cli = retorno_romaneio_ent.get(i).getFon_cli();
                String fon_cli_2 = retorno_romaneio_ent.get(i).getFon_cli_2();
                String nom_ven = retorno_romaneio_ent.get(i).getNom_ven();
                String dat_lib_re = retorno_romaneio_ent.get(i).getDat_lib_re();
                String usu_lib_re = retorno_romaneio_ent.get(i).getUsu_lib_re();
                String hor_lib_re = retorno_romaneio_ent.get(i).getHor_lib_re();

                tab_romaneio_ent.setNum_seq(num_seq);
                tab_romaneio_ent.setNum_reg_nts(num_reg_nts);
                tab_romaneio_ent.setCod_ser(cod_ser);
                tab_romaneio_ent.setNum_doc(num_doc);
                tab_romaneio_ent.setNom_cli(nom_cli);
                tab_romaneio_ent.setEnd_cli(end_cli);
                tab_romaneio_ent.setNum_end_cli(num_end_cli);
                tab_romaneio_ent.setCom_bai_cli(com_bai_cli);
                tab_romaneio_ent.setNom_mun(nom_mun);
                tab_romaneio_ent.setCod_uni_fed(cod_uni_fed);
                tab_romaneio_ent.setCep_cli(cep_cli);
                tab_romaneio_ent.setFon_cli(fon_cli);
                tab_romaneio_ent.setFon_cli_2(fon_cli_2);
                tab_romaneio_ent.setNom_ven(nom_ven);
                tab_romaneio_ent.setDat_lib_re(dat_lib_re);
                tab_romaneio_ent.setUsu_lib_re(usu_lib_re);
                tab_romaneio_ent.setHor_lib_re(hor_lib_re);

                if (tab_romaneio_ent.getDat_lib_re().equals("") &&
                        tab_romaneio_ent.getUsu_lib_re().equals("") &&
                        tab_romaneio_ent.getHor_lib_re().equals("")) {
                    lista_romaneio_ent.add(tab_romaneio_ent);
                }

                if (lista_romaneio_ent.isEmpty()) {
                    alertSmall("Não há romaneios de entrega!");
                }

                Collections.sort(lista_romaneio_ent);

                mapaEntregaAdapter = new MapaEntregaAdapter(MOB007.this, lista_romaneio_ent);
                w_registro_mapa.setAdapter(mapaEntregaAdapter);
                mapaEntregaAdapter.notifyDataSetChanged();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        bancoMapaEntrega.close();

    }

    private void abrirMapa(ArrayList<Romaneio_ent> lista_romaneio_ent, String[] polyline) {

        try {
            ArrayList<Coordenadas> coordenadas = coordenadasRomaneio(lista_romaneio_ent);

            Bundle bundle = new Bundle();
            bundle.putSerializable("coordenadas", coordenadas);
            bundle.putSerializable("polyline", polyline);

            FragmentManager fragmentManager = getSupportFragmentManager();

            MapsFragment mapsFragment = new MapsFragment();

            mapsFragment.setArguments(bundle);

            mapsFragment.show(fragmentManager, null);

            //new MapsFragment().show(getSupportFragmentManager(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            alertMessage.error(view, this, "Erro", "Não foi possível carregar o mapa.\n\n" + ex.toString());
        }
    }

    //Gera a rota por meio de uma requisição HTTP e ordena os itens do recyclerView
    private ArrayList<Romaneio_ent> gerarRota(ArrayList<Romaneio_ent> lista_romaneio_ent) {
        try {

            System.out.println("Entrou gerarRota");

            ArrayList<Coordenadas> paradas = new ArrayList<>();
            paradas = coordenadasRomaneio(lista_romaneio_ent);

            GpsTracker gpsTracker = new GpsTracker(this);
            String meuLocal = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude(); //retorna o local atual do aparelho

            String url = "https://maps.googleapis.com/maps/api/directions/xml?" + //o tipo pode ser xml ou json
                    "origin=" + meuLocal + //ponto inicial
                    "&destination=" + meuLocal + //ponto final
                    "&language=pt-BR" + //define a linguagem
                    "&mode=driving" + //define o tipo de trajeto
                    "&waypoints=optimize:true"; //define que será ordenado pela melhor rota

            //percorre a lista de paradas e adiciona as coordenadas à url
            //for (int i = 0; i < paradas.size(); i++) {
            for (int i = 0; i < 25; i++) {
                Coordenadas coordenadas = paradas.get(i);
                String latitude = String.valueOf(coordenadas.getLatitude());
                String longitude = String.valueOf(coordenadas.getLongitude());

                url = url + "|" + latitude + "," + longitude;

            }

            //recebe a chave da Google Directions Api no xml de strings
            url = url + "&key=" + getResources().getString(R.string.chave_google_directions_api);

            System.out.println("url " + url);

            String xml = new HTTPRequest().execute(url).get();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            Element rootElement = document.getDocumentElement();

            String[] conteudoXml = getXmlString("waypoint_index", rootElement);
            polyline = getXmlString("points", rootElement);

            lista_romaneio_ent = ordenarParadas(conteudoXml, lista_romaneio_ent);

            return lista_romaneio_ent;

        } catch (Exception ex) {
            alertMessage.error(view, this, "Erro", "Não foi possível gerar a rota.\n\n" + ex.toString());
            ex.printStackTrace();
            return lista_romaneio_ent;
        }

    }

    private ArrayList<Coordenadas> coordenadasRomaneio(ArrayList<Romaneio_ent> lista_romaneio_ent) {

        GpsTracker gpsTracker = new GpsTracker(this);

        ArrayList<Coordenadas> paradas = new ArrayList<>();

        //Faz um loop na list_romaneio_ent para pegar o endereço de cada entrega
        for (int i = 0; i < lista_romaneio_ent.size(); i++) {
            String rua = lista_romaneio_ent.get(i).getEnd_cli();
            String numero = lista_romaneio_ent.get(i).getNum_end_cli();
            String bairro = lista_romaneio_ent.get(i).getCom_bai_cli();
            String municipio = lista_romaneio_ent.get(i).getNom_mun();
            String estado = lista_romaneio_ent.get(i).getCod_uni_fed();
            String cep = lista_romaneio_ent.get(i).getCep_cli();

            String endereco = rua + " " + numero + " " + bairro + " " + municipio + " " + estado;

            Coordenadas coordenadas = new Coordenadas();

            coordenadas.setRegistro(String.valueOf(lista_romaneio_ent.get(i).getNum_reg_nts()));
            coordenadas.setLatitude(gpsTracker.getLocationFromAddress(this, endereco).latitude);
            coordenadas.setLongitude(gpsTracker.getLocationFromAddress(this, endereco).longitude);

            paradas.add(coordenadas); //adiciona as coordenadas na lista de paradas
        }

        return paradas;
    }

    //Retorna o conteudo da tag no xml
    private String[] getXmlString(String tagName, Element element) {
        int tamanho = element.getElementsByTagName(tagName).getLength();

        String[] retorno = new String[tamanho];

        for (int i = 0; i < tamanho; i++) {
            retorno[i] = element.getElementsByTagName(tagName).item(i).getTextContent();
        }

        return retorno;
    }

    private ArrayList<Romaneio_ent> ordenarParadas(String[] conteudoXml, ArrayList<Romaneio_ent> lista_romaneio_ent) {

        int tamanhoParadas = lista_romaneio_ent.size();

        ArrayList<Romaneio_ent> romaneio_ent_ordenado = new ArrayList<>();

        for (int i = 0; i < tamanhoParadas; i++) {
            int index = Integer.parseInt(conteudoXml[i]);

            romaneio_ent_ordenado.add(lista_romaneio_ent.get(index));
        }

        return romaneio_ent_ordenado;

    }

    // Cria o botão na Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.botao_mapa, menu);
        getMenuInflater().inflate(R.menu.botao_offline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Define a ação dos botões da Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            //Botão bandeira
            case R.id.botao_mapa_action_bar:
                consultarMapaEntrega_Online(true);
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                alertMessage.info(view, this, "Em modo offline",
                        "Não foi possível se conectar á nuvem.\n");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Oculta o botão se retornar que foi possível conectar com a nuvem
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (flag_online == true) {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(false).setEnabled(false);
        } else {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(true).setEnabled(true);
            menu.findItem(R.id.botao_mapa_action_bar)
                    .setVisible(false).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void alertSmall(String s) {
        Toast.makeText(MOB007.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB007.this);
        //alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}