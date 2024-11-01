package com.rp_grf.jrmadeiras.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.rp_grf.jrmadeiras.Utils.GpsTracker;
import com.rp_grf.jrmadeiras.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Autor: André Castro
 * Data de criação: 27/01/2020
 */
public class MapaGpsFragment extends DialogFragment {

    private static final String SAVED_STATE_CAMERA = "saved_state_camera";
    private static final String SAVED_STATE_RENDER = "saved_state_render";
    private static final String SAVED_STATE_LOCATION = "saved_state_location";

    private View dialogView;
    private MapView w_mapView;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private MapboxMap mapa_mapbox;
    private Location lastLocation;

    private List<Point> paradas = new ArrayList<>();
    private Point origem;
    private Point destino;

    private String endereco;

    @CameraMode.Mode
    private int cameraMode = CameraMode.TRACKING;

    @RenderMode.Mode
    private int renderMode = RenderMode.NORMAL;

    public MapaGpsFragment() {
        // Required empty public constructor
    }

    public static MapaGpsFragment newInstance() {
        MapaGpsFragment fragment = new MapaGpsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return dialogView;
    }

     */

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        Mapbox.getInstance(requireActivity(), getString(R.string.access_token)); //precisa vir antes do R.layout

        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        dialogView = inflater.inflate(R.layout.fragment_mapa, null);

        Bundle bundle = getArguments();
        endereco = bundle.getString("endereco", "");

        AlertDialog.Builder endWorkAlert = new AlertDialog.Builder(requireActivity());
        endWorkAlert.setView(dialogView);

        endWorkAlert.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //Preparar mapa
        w_mapView = (MapView) dialogView.findViewById(R.id.fragment_mapView);
        w_mapView.onCreate(savedInstanceState);

        // Check and use saved instance state in case of device rotation
        if (savedInstanceState != null) {
            cameraMode = savedInstanceState.getInt(SAVED_STATE_CAMERA);
            renderMode = savedInstanceState.getInt(SAVED_STATE_RENDER);
            lastLocation = savedInstanceState.getParcelable(SAVED_STATE_LOCATION);
        }

        //verificarPermissao();

        //criarRota();

        iniciarNavigation();

        //botaoMeuLocal();

        return endWorkAlert.create();
    }

    // Verifica as permissões para abrir o mapa
    private void verificarPermissao() {
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            carregarMapa();
        } else {
            permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                    alertMessage("Para consultar o mapa é necessário permitir o uso do GPS");
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted) {
                        carregarMapa();
                    } else {
                        dismiss();
                    }
                }
            });
            permissionsManager.requestLocationPermissions(getActivity());
        }

    }

    @SuppressLint("MissingPermission")
    private void carregarMapa() {
        w_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                //MapaFragment.this.mapboxMap = mapboxMap;
                mapa_mapbox = mapboxMap;
                mapboxMap.setStyle(Style.TRAFFIC_DAY, new Style.OnStyleLoaded() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Retrieve and customize the Maps SDK's LocationComponent
                        locationComponent = mapboxMap.getLocationComponent();
                        locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions
                                        .builder(getActivity(), style)
                                        .useDefaultLocationEngine(true)
                                        .locationEngineRequest(new LocationEngineRequest.Builder(750)
                                                .setFastestInterval(750)
                                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                                .build())
                                        .build());

                        locationComponent.setLocationComponentEnabled(true);
                        //locationComponent.addOnLocationClickListener(this);
                        //locationComponent.addOnCameraTrackingChangedListener(this);
                        locationComponent.setCameraMode(CameraMode.TRACKING);
                        locationComponent.setRenderMode(RenderMode.COMPASS);
                        locationComponent.forceLocationUpdate(lastLocation);
                        locationComponent.setRenderMode(RenderMode.GPS);
                        locationComponent.zoomWhileTracking(16, 750);
                        locationComponent.tiltWhileTracking(45);

                    }
                });
            }
        });
    }


    private void iniciarNavigation() {
        //MapboxNavigation navigation = new MapboxNavigation(getActivity().getApplicationContext(), getString(R.string.access_token));
        //criarRota();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);

        View viewAlertMapa = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_gps,
                (LinearLayout) dialogView.findViewById(R.id.dialog_gps_LinearLayout)
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
                abrirGoogleMaps();
            }
        });

        //Define a ação do botão Waze do alertDialog
        viewAlertMapa.findViewById(R.id.dialog_gps_botao_waze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirWaze();
            }
        });

        alertMapa.show();

    }

    private void abrirGoogleMaps() {
        addDestino();

        Uri uri = Uri.parse("google.navigation:q=" + destino.latitude() + "," + destino.longitude());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void abrirWaze() {
        addDestino();

        String uri = "waze://?ll=" + destino.latitude() + ", " + destino.longitude() + "&navigate=yes";

        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    //cria a melhor rota usando a origem e o destino
    private void criarRota() {

        addOrigem();
        addDestino();

        NavigationRoute.builder(getActivity().getApplicationContext())
                .accessToken(getString(R.string.access_token))
                .origin(origem)
                .destination(destino)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            alertMessage("Erro ao gerar rota!");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            alertMessage("Nenhuma rota encontrada para o destino: \n " + endereco);
                            return;
                        }

                        DirectionsRoute route = response.body().routes().get(0);

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(route)
                                .shouldSimulateRoute(false)
                                .build();

                        NavigationLauncher.startNavigation(getActivity(), options);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        alertMessage("Erro ao gerar rota: " + t.toString());
                        System.out.println("MapaGpsFragment " + t.toString());
                    }
                });

    }

    //adiciona as coordenadas de origem na lista de rotas
    private void addOrigem() {
        GpsTracker gpsTracker = new GpsTracker(getActivity());

        if (gpsTracker.canGetLocation()) {

            Double latitude = gpsTracker.getLatitude();
            Double longitude = gpsTracker.getLongitude();
            origem = Point.fromLngLat(longitude, latitude);
            paradas.add(origem);

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    //recebe o endereco da entrega e consulta as coordenadas no google
    private void addDestino() {
        GpsTracker gpsTracker = new GpsTracker(getActivity());

        if (gpsTracker.canGetLocation()) {

            try {

                Geocoder geocoder = new Geocoder(getActivity());

                List<Address> retornoGoogle = geocoder.getFromLocationName(endereco, 1);
                Address address = retornoGoogle.get(0);

                Double latitude = address.getLatitude();
                Double longitude = address.getLongitude();
                destino = Point.fromLngLat(longitude, latitude);
                paradas.add(destino);

            } catch (IOException ex) {
                System.out.println("MapaFragment: " + ex.toString());
            }

        } else {
            //gpsTracker.showSettingsAlert();
        }

    }

    private void botaoMeuLocal() {
        dialogView.findViewById(R.id.fragment_mapa_botao_local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.zoomWhileTracking(16);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onStart() {
        super.onStart();
        w_mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        w_mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        w_mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        w_mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        w_mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        w_mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        w_mapView.onDestroy();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        w_mapView.onSaveInstanceState(outState);

        outState.putInt(SAVED_STATE_CAMERA, cameraMode);
        outState.putInt(SAVED_STATE_RENDER, renderMode);
        if (locationComponent != null) {
            outState.putParcelable(SAVED_STATE_LOCATION, locationComponent.getLastKnownLocation());
        }
    }

    private void alertMessage(String s) {
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alert.setTitle("Atenção");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}