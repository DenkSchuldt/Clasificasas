package com.ihm.clasificasas.clasificasas;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Denny on 27/07/2014.
 */
public class usuario  extends FragmentActivity {
    SupportMapFragment fm;
    GoogleMap googleMap;
    Button buscar;
    Button publicar;
    MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);
        //fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //googleMap = fm.getMap();
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //mostrarPosicionActual();

        buscar = (Button) findViewById(R.id.usuario_buscar);
        publicar = (Button) findViewById(R.id.usuario_publicar);

        buscar.setOnClickListener(usuarioButtonhandler);
        publicar.setOnClickListener(usuarioButtonhandler);
    }

    private String getShaKey() {
        //fucnion para saber si esta bien registrado el codigo de googlemaps
        //ME SALE EXCEPTION DE NOMBRE NO ENCONTRADO?¿?¿
        String strRet="";
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ihm.clasificasas.clasificasas",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
               // Log.v("TAG", "KeyHash:" + Base64.encodeToString(md.digest(),
                strRet="KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            strRet="EXCEPTION NOMBRE NO ENCONTRADO";
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            strRet="EXCEPTION ALGORITMO NO";
        }
        return strRet;
    }

    View.OnClickListener usuarioButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usuario_buscar:
                Intent buscar = new Intent(usuario.this, com.ihm.clasificasas.clasificasas.buscar.class);
                startActivity(buscar);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
            case R.id.usuario_publicar:
                Intent publicar = new Intent(usuario.this, publicar.class);
                startActivity(publicar);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            break;
        }
        }
    };

    public void mostrarPosicionActual(){
        // Getting reference to the SupportMapFragment of activity_main.xml
        // Getting GoogleMap object from the fragment
        if(!verifygoogleavailability()){
            return;
        }
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);
        // Getting LocationManager object from System Service LOCATION_SERVICE
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        final String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        final Location location = locationManager.getLastKnownLocation(provider);
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // redraw the marker when get location update.
                drawMarker(location, googleMap);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        if(location!=null){
            //PLACE THE INITIAL MARKER
            drawMarker(location, googleMap);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 18.0f));
        locationManager.requestLocationUpdates(provider,20000,0,locationListener);
    }

    private void drawMarker(Location location,GoogleMap googleMap){
        googleMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions mo=new MarkerOptions();
        mo.position(currentPosition);
        mo.snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude());
        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mo.title("ME");
        googleMap.addMarker(mo);
    }

    public boolean verifygoogleavailability(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        try {
            menu.findItem(R.id.usuario).setTitle(getIntent().getExtras().getString("TAG_USUARIO"));
        }catch (Exception e){}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.usuario:
                break;
            case R.id.configuracion:
                break;
            case R.id.salir:
                Intent main = new Intent(usuario.this, main.class);
                startActivity(main);
                overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
