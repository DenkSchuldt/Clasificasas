package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Denny on 28/07/2014.
 */
public class publicar extends FragmentActivity {

    Button publicar;
    Spinner spinner_tipo, spinner_ciudad;
    ImageView portada;
    EditText costo, ubicacion, cuartos, banos, pisos, terreno, construccion, descripcion;
    int idglobal = 1;

    Uri mImageCaptureUri;
    boolean saved = false;
    String savedname = "";

    private Bitmap ImagenActual;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    String path="";
    String[] tipo = {
        "Venta", "Alquiler"
    };
    String[] ciudad = {
        "Ambato",
        "Babahoyo",
        "Cuenca",
        "Esmeraldas",
        "Guayaquil",
        "Ibarra",
        "Manabí",
        "Portoviejo",
        "Quevedo",
        "Quito",
        "Salinas"
    };

    GoogleMap googleMap;
    MapFragment mMapFragment;
    MarkerOptions mohome = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicar);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        prepareImagePicker();

        costo = (EditText) findViewById(R.id.publicar_costo);
        ubicacion = (EditText) findViewById(R.id.publicar_ubicacion);
        cuartos = (EditText) findViewById(R.id.publicar_cuartos);
        banos = (EditText) findViewById(R.id.publicar_banos);
        pisos = (EditText) findViewById(R.id.publicar_pisos);
        terreno = (EditText) findViewById(R.id.publicar_terreno);
        construccion = (EditText) findViewById(R.id.publicar_construccion);
        descripcion = (EditText) findViewById(R.id.publicar_descripcion);

        spinner_tipo = (Spinner)findViewById(R.id.publicar_tipo);
        ArrayAdapter<String> tipos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipo);
        spinner_tipo.setAdapter(tipos);

        spinner_ciudad = (Spinner)findViewById(R.id.publicar_ciudad);
        ArrayAdapter<String> ciudades = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudad);
        spinner_ciudad.setAdapter(ciudades);

        publicar = (Button) findViewById(R.id.publicar_button);
        publicar.setOnClickListener(publicarButtonhandler);

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        while(resultCode != ConnectionResult.SUCCESS){
            resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        }

        googleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mohome.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.locationpoint)));
        mohome.title("Ubicación");
        mohome.draggable(true);

        mostrarPosicionActual();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(publicar.this, MapViewer.class);
                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                intent.putExtra("TAG_COSTO", costo.getText().toString());
                intent.putExtra("TAG_TIPO",spinner_tipo.getSelectedItemPosition());
                intent.putExtra("TAG_FOTO", savedname);
                intent.putExtra("TAG_IDGLOBAL", idglobal);
                intent.putExtra("TAG_CIUDAD", spinner_ciudad.getSelectedItemPosition());
                intent.putExtra("TAG_UBICACION", ubicacion.getText().toString());
                intent.putExtra("TAG_LATITUD", mohome.getPosition().latitude);
                intent.putExtra("TAG_LONGITUD",mohome.getPosition().longitude);
                intent.putExtra("TAG_CUARTOS", cuartos.getText().toString());
                intent.putExtra("TAG_BANOS", banos.getText().toString());
                intent.putExtra("TAG_PISOS", pisos.getText().toString());
                intent.putExtra("TAG_TERRENO", terreno.getText().toString());
                intent.putExtra("TAG_CONSTRUCCION", construccion.getText().toString());
                intent.putExtra("TAG_DESCRIPCION", descripcion.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);
            }
        });

        Button publicar = (Button) findViewById(R.id.publicar_button);
        publicar.setOnClickListener(publicarButtonhandler);

        if(getIntent().hasExtra("TAG_FOTO")){
            File sdDir = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/fotos");
            File[] sdDirFiles = sdDir.listFiles();
            for(File singleFile : sdDirFiles){
                if(singleFile.getName().equals(getIntent().getExtras().getString("TAG_FOTO"))){
                    portada.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
                    savedname = getIntent().getExtras().getString("TAG_FOTO");
                    saved = true;
                }
            }
            idglobal = getIntent().getExtras().getInt("TAG_IDGLOBAL");
            costo.setText(getIntent().getExtras().getString("TAG_COSTO"));
            spinner_tipo.setSelection(getIntent().getExtras().getInt("TAG_TIPO"));
            spinner_ciudad.setSelection(getIntent().getExtras().getInt("TAG_CIUDAD"));
            cuartos.setText(getIntent().getExtras().getString("TAG_CUARTOS"));
            pisos.setText(getIntent().getExtras().getString("TAG_PISOS"));
            banos.setText(getIntent().getExtras().getString("TAG_BANOS"));
            terreno.setText(getIntent().getExtras().getString("TAG_TERRENO"));
            construccion.setText(getIntent().getExtras().getString("TAG_CONSTRUCCION"));
            descripcion.setText(getIntent().getExtras().getString("TAG_DESCRIPCION"));
            new GeocoderTaskInverso().execute(getIntent().getExtras().getDouble("TAG_LATITUD"),getIntent().getExtras().getDouble("TAG_LONGITUD"));
            mohome.position(new LatLng(getIntent().getExtras().getDouble("TAG_LATITUD"), getIntent().getExtras().getDouble("TAG_LONGITUD")));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mohome.getPosition().latitude,mohome.getPosition().longitude), 18.0f));
            refreshMarkers();
        }

    }

    public void mostrarPosicionActual(){
        if(!verifyGoogleavAilability()){
            return;
        }
        googleMap.setMyLocationEnabled(true);
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        final String provider = locationManager.getBestProvider(criteria, true);
        final Location location = locationManager.getLastKnownLocation(provider);
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                refreshMarkers();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        if(location!=null){
            setMarker(location,googleMap,mohome);
            refreshMarkers();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
        locationManager.requestLocationUpdates(provider,20000,0,locationListener);
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

    public boolean verifyGoogleavAilability(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(publicar.this, usuario.class);
                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener publicarButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.publicar_button:
                    if(validar()) {
                        try {
                            if (publicarCasa()) {
                                Intent intent = new Intent(publicar.this, usuario.class);
                                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                                startActivity(intent);
                                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                                finish();
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = "Algo salió mal. Inténtelo nuevamente.";
                                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.publicar_portada:

                    break;
            }
        }
    };

    public boolean publicarCasa() throws IOException {
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/publicaciones.xml");
        boolean success = true;
        if (!newxmlfile.exists()) {
            success = newxmlfile.createNewFile();
            if (success) {
                FileOutputStream fileos = new FileOutputStream(newxmlfile);
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    serializer.startTag(null, "publicaciones");
                        serializer.startTag(null, "publicacion");
                            serializer.startTag(null,"id");
                            serializer.text("1");
                            serializer.endTag(null, "id");
                            serializer.startTag(null, "usuario");
                            serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                            serializer.endTag(null, "usuario");
                            serializer.startTag(null, "costo");
                            serializer.text(costo.getText().toString());
                            serializer.endTag(null, "costo");
                            serializer.startTag(null, "tipo");
                            serializer.text(spinner_tipo.getSelectedItem().toString());
                            serializer.endTag(null, "tipo");
                            serializer.startTag(null, "foto");
                            serializer.text(savedname);
                            serializer.endTag(null, "foto");
                            serializer.startTag(null, "ciudad");
                            serializer.text(spinner_ciudad.getSelectedItem().toString());
                            serializer.endTag(null, "ciudad");
                            serializer.startTag(null, "ubicacion");
                            serializer.text(ubicacion.getText().toString());
                            serializer.endTag(null, "ubicacion");
                            serializer.startTag(null, "latitud");
                            serializer.text("" + mohome.getPosition().latitude);
                            serializer.endTag(null, "latitud");
                            serializer.startTag(null, "longitud");
                            serializer.text("" + mohome.getPosition().longitude);
                            serializer.endTag(null, "longitud");
                            serializer.startTag(null, "cuartos");
                            serializer.text(cuartos.getText().toString());
                            serializer.endTag(null, "cuartos");
                            serializer.startTag(null, "banos");
                            serializer.text(banos.getText().toString());
                            serializer.endTag(null, "banos");
                            serializer.startTag(null, "pisos");
                            serializer.text(pisos.getText().toString());
                            serializer.endTag(null, "pisos");
                            serializer.startTag(null, "terreno");
                            serializer.text(terreno.getText().toString());
                            serializer.endTag(null, "terreno");
                            serializer.startTag(null, "construccion");
                            serializer.text(construccion.getText().toString());
                            serializer.endTag(null, "construccion");
                            serializer.startTag(null, "descripcion");
                            serializer.text(descripcion.getText().toString());
                            serializer.endTag(null, "descripcion");
                        serializer.endTag(null, "publicacion");
                    serializer.endTag(null, "publicaciones");
                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();
                    return true;
                } catch (Exception e) {
                    Log.e("Exception", "error occurred while creating xml file");
                }
            }
        }else {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList publicaciones = doc.getElementsByTagName("publicacion");
                FileOutputStream fileos = new FileOutputStream(newxmlfile);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(null, "publicaciones");
                for(int i=0; i<publicaciones.getLength(); i++){
                    serializer.startTag(null, "publicacion");
                        serializer.startTag(null,"id");
                        serializer.text(publicaciones.item(i).getChildNodes().item(1).getTextContent());
                        serializer.endTag(null, "id");
                        serializer.startTag(null,"usuario");
                        serializer.text(publicaciones.item(i).getChildNodes().item(3).getTextContent());
                        serializer.endTag(null, "usuario");
                        serializer.startTag(null,"costo");
                        serializer.text(publicaciones.item(i).getChildNodes().item(5).getTextContent());
                        serializer.endTag(null, "costo");
                        serializer.startTag(null, "tipo");
                        serializer.text(publicaciones.item(i).getChildNodes().item(7).getTextContent());
                        serializer.endTag(null, "tipo");
                        serializer.startTag(null, "foto");
                        serializer.text(publicaciones.item(i).getChildNodes().item(9).getTextContent());
                        serializer.endTag(null, "foto");
                        serializer.startTag(null, "ciudad");
                        serializer.text(publicaciones.item(i).getChildNodes().item(11).getTextContent());
                        serializer.endTag(null, "ciudad");
                        serializer.startTag(null, "ubicacion");
                        serializer.text(publicaciones.item(i).getChildNodes().item(13).getTextContent());
                        serializer.endTag(null, "ubicacion");
                        serializer.startTag(null, "latitud");
                        serializer.text(publicaciones.item(i).getChildNodes().item(15).getTextContent());
                        serializer.endTag(null, "latitud");
                        serializer.startTag(null, "longitud");
                        serializer.text(publicaciones.item(i).getChildNodes().item(17).getTextContent());
                        serializer.endTag(null, "longitud");
                        serializer.startTag(null, "cuartos");
                        serializer.text(publicaciones.item(i).getChildNodes().item(19).getTextContent());
                        serializer.endTag(null, "cuartos");
                        serializer.startTag(null, "banos");
                        serializer.text(publicaciones.item(i).getChildNodes().item(21).getTextContent());
                        serializer.endTag(null, "banos");
                        serializer.startTag(null, "pisos");
                        serializer.text(publicaciones.item(i).getChildNodes().item(23).getTextContent());
                        serializer.endTag(null, "pisos");
                        serializer.startTag(null, "terreno");
                        serializer.text(publicaciones.item(i).getChildNodes().item(25).getTextContent());
                        serializer.endTag(null, "terreno");
                        serializer.startTag(null, "construccion");
                        serializer.text(publicaciones.item(i).getChildNodes().item(27).getTextContent());
                        serializer.endTag(null, "construccion");
                        serializer.startTag(null, "descripcion");
                        serializer.text(publicaciones.item(i).getChildNodes().item(29).getTextContent());
                        serializer.endTag(null, "descripcion");
                    serializer.endTag(null, "publicacion");
                }
                serializer.startTag(null, "publicacion");
                    serializer.startTag(null,"id");
                    serializer.text("" + idglobal);
                    serializer.endTag(null, "id");
                    serializer.startTag(null, "usuario");
                    serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                    serializer.endTag(null, "usuario");
                    serializer.startTag(null, "costo");
                    serializer.text(costo.getText().toString());
                    serializer.endTag(null, "costo");
                    serializer.startTag(null, "tipo");
                    serializer.text(spinner_tipo.getSelectedItem().toString());
                    serializer.endTag(null, "tipo");
                    serializer.startTag(null, "foto");
                    serializer.text(savedname);
                    serializer.endTag(null, "foto");
                    serializer.startTag(null, "ciudad");
                    serializer.text(spinner_ciudad.getSelectedItem().toString());
                    serializer.endTag(null, "ciudad");
                    serializer.startTag(null, "ubicacion");
                    serializer.text(ubicacion.getText().toString());
                    serializer.endTag(null, "ubicacion");
                    serializer.startTag(null, "latitud");
                    serializer.text("" + mohome.getPosition().latitude);
                    serializer.endTag(null, "latitud");
                    serializer.startTag(null, "longitud");
                    serializer.text(""+mohome.getPosition().longitude);
                    serializer.endTag(null, "longitud");
                    serializer.startTag(null, "cuartos");
                    serializer.text(cuartos.getText().toString());
                    serializer.endTag(null, "cuartos");
                    serializer.startTag(null, "banos");
                    serializer.text(banos.getText().toString());
                    serializer.endTag(null, "banos");
                    serializer.startTag(null, "pisos");
                    serializer.text(pisos.getText().toString());
                    serializer.endTag(null, "pisos");
                    serializer.startTag(null, "terreno");
                    serializer.text(terreno.getText().toString());
                    serializer.endTag(null, "terreno");
                    serializer.startTag(null, "construccion");
                    serializer.text(construccion.getText().toString());
                    serializer.endTag(null, "construccion");
                    serializer.startTag(null, "descripcion");
                    serializer.text(descripcion.getText().toString());
                    serializer.endTag(null, "descripcion");
                serializer.endTag(null, "publicacion");
                serializer.endTag(null, "publicaciones");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
                return true;
            }catch (Exception e){}
            return false;
        }
        return false;
    }

    public boolean validar(){
        Context context = getApplicationContext();
        if(costo.getText().toString().equals("")) {
            CharSequence text = "Debe especificar un costo";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(savedname.equals("")){
            CharSequence text = "Debe especificar una fotografía de la casa";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(ubicacion.getText().toString().equals("")){
            CharSequence text = "Debe especificar la ubicación de la casa";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!cuartos.getText().toString().matches("[0-9]+")){
            CharSequence text = "Debe especificar un número de cuartos válido";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!banos.getText().toString().matches("[0-9]+")){
            CharSequence text = "Debe especificar un número de baños válido";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!pisos.getText().toString().matches("[0-9]+")){
            CharSequence text = "Debe especificar un número de pisos válido";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!terreno.getText().toString().matches("[0-9]+")){
            CharSequence text = "Las medidas del terreno no son válidas";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(!construccion.getText().toString().matches("[0-9]+")){
            CharSequence text = "Las medidas de la construcción no son válidas";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(descripcion.getText().toString().equals("")){
            CharSequence text = "Debe especificar una descripción para la casa a publicar";
            Toast toast = Toast.makeText(context,text, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private class GeocoderTaskInverso extends AsyncTask<Double, Void, List<Address>> {
        LatLng latLng;
        MarkerOptions markerOptions;
        @Override
        protected List<Address> doInBackground(Double... param) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(param[0],param[1], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses==null || addresses.size()==0){
                ubicacion.setText("Ubicación desconocida");
                return;
            }
            Address address = (Address) addresses.get(0);
            String addressText = String.format("%s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getCountryName());
            ubicacion.setText(addressText);
        }
    }

    public void prepareImagePicker() {
        final String[] items = new String[]{"Desde la cámara", "Desde la tarjeta SD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una imagen");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),getIntent().getExtras().getString("TAG_USUARIO")+"-"+idglobal+ ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Completar acción usando"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        portada = (ImageView) findViewById(R.id.publicar_portada);
        portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewIdPublicacion();
                dialog.show();
            }
        });
    }

    public void getNewIdPublicacion(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        try {
            System.out.println("ANTES DE ABRIR EL ARCHIVO");
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/publicaciones.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList id = doc.getElementsByTagName("id");
            for(int i=0; i<id.getLength(); i++){
                ids.add(Integer.parseInt(id.item(i).getTextContent()));
            }
            Collections.sort(ids);
            if(ids.size() != 0){
                int index = ids.size()-1;
                int last = ids.get(index);
                idglobal = last+2;
            }
        }catch (Exception e){
            System.out.println("CATCH!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Bitmap bitmap   = null;
        if (requestCode == PICK_FROM_FILE) {
            mImageCaptureUri = data.getData();
            path = getRealPathFromURI(mImageCaptureUri); //from Gallery
            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager
            if (path != null)
                bitmap  = BitmapFactory.decodeFile(path);
        } else {
            path = mImageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        ImagenActual = bitmap;
        File file = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/fotos",getIntent().getExtras().getString("TAG_USUARIO")+"-"+idglobal+ ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
            savedname = getIntent().getExtras().getString("TAG_USUARIO")+"-"+idglobal+ ".png";
            saved = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        portada.setImageBitmap(ImagenActual);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
