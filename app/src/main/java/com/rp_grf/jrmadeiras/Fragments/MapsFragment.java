package com.rp_grf.jrmadeiras.Fragments;

import androidx.fragment.app.DialogFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.rp_grf.jrmadeiras.Utils.AlertMessage;
import com.rp_grf.jrmadeiras.Utils.Coordenadas;
import com.rp_grf.jrmadeiras.Utils.GpsTracker;
import com.rp_grf.jrmadeiras.R;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends DialogFragment implements OnMapReadyCallback {

    AlertMessage alertMessage;

    ArrayList<Coordenadas> coordenadas;
    String[] polyline;
    GoogleMap mMap;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragment_mapa_view);
        mapFragment.getMapAsync(this);

        Bundle bundle = getArguments();

        coordenadas = (ArrayList<Coordenadas>) bundle.getSerializable("coordenadas");
        polyline = (String[]) bundle.getSerializable("polyline");

        return rootView;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        List<LatLng> latlngs = new ArrayList<>();
        for (int i = 0; i < coordenadas.size(); i++) {
            latlngs.add(new LatLng(coordenadas.get(i).getLatitude(), coordenadas.get(i).getLongitude()));
        }

        GpsTracker gpsTracker = new GpsTracker(getActivity());

        Coordenadas coordenadas = new Coordenadas();
        coordenadas.setLatitude(gpsTracker.getLatitude());
        coordenadas.setLongitude(gpsTracker.getLongitude());

        LatLng latLng = new LatLng(coordenadas.getLatitude(), coordenadas.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

        /*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);
         */

        List<LatLng> path = null;

        for (int i = 0; i < polyline.length; i++) {
            path = PolyUtil.decode(polyline[i]);
        }

        PolylineOptions polylineOptions = new PolylineOptions().addAll(path).width(5).color(Color.BLUE).geodesic(true);
        mMap.addPolyline(polylineOptions);

        ArrayList<MarkerOptions> markerOptions = getMarcadores();

        for (int i = 0; i < markerOptions.size(); i++) {
            mMap.addMarker(markerOptions.get(i));
        }
    }

    private ArrayList<MarkerOptions> getMarcadores() {
        MarkerOptions marker;
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();

        for (int i = 0; i < coordenadas.size(); i++) {
            marker = new MarkerOptions();
            LatLng latLng = new LatLng(coordenadas.get(i).getLatitude(), coordenadas.get(i).getLongitude());
            marker.position(latLng);
            markerOptions.add(marker);
        }

        return markerOptions;
    }

    private void gerarLinhas() {

    }

    private void getDirections() {

    }

}