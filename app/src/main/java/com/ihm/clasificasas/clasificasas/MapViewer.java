package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapViewer extends FragmentActivity {

    SupportMapFragment fm;
    GoogleMap googleMap;
    MapFragment mMapFragment;
    MarkerOptions mohome = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewer);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextViewEx txtView = (TextViewEx) findViewById(R.id.textviewex);
        txtView.setText("Mantenga presionado el indicador de posición rojo, y luego muevalo para determinar la ubicación de la casa.",true);

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mohome.position(latLng);
                googleMap.clear();
                googleMap.addMarker(mohome);
            }
        });
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mohome.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.locationpoint)));
        mohome.title("Ubicación");
        mohome.draggable(true);

        mostrarPosicionActual();

        if(getIntent().hasExtra("TAG_LATITUD")){
            mohome.position(new LatLng(getIntent().getExtras().getDouble("TAG_LATITUD"),getIntent().getExtras().getDouble("TAG_LONGITUD")));
            refreshMarkers();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getIntent().getExtras().getDouble("TAG_LATITUD"),getIntent().getExtras().getDouble("TAG_LONGITUD")), 18.0f));
        }

        Button btn_find = (Button) findViewById(R.id.button);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLocation = (EditText) findViewById(R.id.buscar_ubicacion);
                String location = etLocation.getText().toString();
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        });

        Button listo = (Button) findViewById(R.id.listo);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMarkers();
                Intent publicar = new Intent(MapViewer.this,publicar.class);
                publicar.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                publicar.putExtra("TAG_LATITUD", mohome.getPosition().latitude);
                publicar.putExtra("TAG_LONGITUD", mohome.getPosition().longitude);
                publicar.putExtra("TAG_FOTO", getIntent().getExtras().getString("TAG_FOTO"));
                publicar.putExtra("TAG_IDGLOBAL", getIntent().getExtras().getInt("TAG_IDGLOBAL"));
                publicar.putExtra("TAG_CUARTOS", getIntent().getExtras().getString("TAG_CUARTOS"));
                publicar.putExtra("TAG_PISOS", getIntent().getExtras().getString("TAG_PISOS"));
                publicar.putExtra("TAG_DESCRIPCION", getIntent().getExtras().getString("TAG_DESCRIPCION"));
                publicar.putExtra("TAG_COSTO", getIntent().getExtras().getString("TAG_COSTO"));
                publicar.putExtra("TAG_TERRENO", getIntent().getExtras().getString("TAG_TERRENO"));
                publicar.putExtra("TAG_CONSTRUCCION", getIntent().getExtras().getString("TAG_CONSTRUCCION"));
                publicar.putExtra("TAG_TIPO", getIntent().getExtras().getInt("TAG_TIPO"));
                publicar.putExtra("TAG_CIUDAD", getIntent().getExtras().getInt("TAG_CIUDAD"));
                publicar.putExtra("TAG_BANOS", getIntent().getExtras().getString("TAG_BANOS"));
                startActivity(publicar);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MapViewer.this, publicar.class);
                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                intent.putExtra("TAG_LATITUD", mohome.getPosition().latitude);
                intent.putExtra("TAG_LONGITUD", mohome.getPosition().longitude);
                intent.putExtra("TAG_FOTO", getIntent().getExtras().getString("TAG_FOTO"));
                intent.putExtra("TAG_IDGLOBAL", getIntent().getExtras().getInt("TAG_IDGLOBAL"));
                intent.putExtra("TAG_CUARTOS", getIntent().getExtras().getString("TAG_CUARTOS"));
                intent.putExtra("TAG_PISOS", getIntent().getExtras().getString("TAG_PISOS"));
                intent.putExtra("TAG_DESCRIPCION", getIntent().getExtras().getString("TAG_DESCRIPCION"));
                intent.putExtra("TAG_COSTO", getIntent().getExtras().getString("TAG_COSTO"));
                intent.putExtra("TAG_TERRENO", getIntent().getExtras().getString("TAG_TERRENO"));
                intent.putExtra("TAG_CONSTRUCCION", getIntent().getExtras().getString("TAG_CONSTRUCCION"));
                intent.putExtra("TAG_TIPO", getIntent().getExtras().getInt("TAG_TIPO"));
                intent.putExtra("TAG_CIUDAD", getIntent().getExtras().getInt("TAG_CIUDAD"));
                intent.putExtra("TAG_BANOS", getIntent().getExtras().getString("TAG_BANOS"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mostrarPosicionActual(){
        if(!verifygoogleavailability()){
            return;
        }
        googleMap.setMyLocationEnabled(true);
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        final String provider = locationManager.getBestProvider(criteria, true);
        final Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            setMarker(location,googleMap,mohome);
            refreshMarkers();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
    }

    private void refreshMarkers(){
        googleMap.clear();
        googleMap.addMarker(mohome);
    }

    private void setMarker(Location location,GoogleMap googleMap,MarkerOptions mo){
        LatLng Position = new LatLng(location.getLatitude(),location.getLongitude());
        mo.position(Position);
        mo.snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude());
    }

    public boolean verifygoogleavailability(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            return false;
        }
        return true;
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        LatLng latLng;
        MarkerOptions markerOptions;
        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
            googleMap.clear();
            for(int i=0;addresses!=null&&i<addresses.size();i++){
                Address address = (Address) addresses.get(i);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());
                mohome.position(latLng);
                refreshMarkers();
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}


