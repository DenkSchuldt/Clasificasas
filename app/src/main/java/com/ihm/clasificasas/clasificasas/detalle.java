package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Denny on 27/07/2014.
 */
public class detalle extends FragmentActivity {

    ImageView phoneButton, emailButton, favButton, portada;
    TextView costo, ubicacion, cuartos, banos, pisos, terreno, construccion, vendedor, movil, correo;
    TextViewEx descripcion;
    Node publicacion;

    GoogleMap googleMap;
    MapFragment mMapFragment;
    MarkerOptions mohome = new MarkerOptions();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phoneButton = (ImageView) findViewById(R.id.detalle_phone_button);
        phoneButton.setOnClickListener(onClickListener);

        emailButton = (ImageView) findViewById(R.id.detalle_email_button);
        emailButton.setOnClickListener(onClickListener);

        favButton = (ImageView) findViewById(R.id.detalle_fav_button);
        if(!getIntent().hasExtra("TAG_USUARIO")){
            favButton.setVisibility(View.INVISIBLE);
        }

        costo = (TextView) findViewById(R.id.txtcosto);
        portada = (ImageView) findViewById(R.id.detalle_portada);
        ubicacion = (TextView) findViewById(R.id.txtdireccion);
        cuartos = (TextView) findViewById(R.id.txtcuartos);
        banos = (TextView) findViewById(R.id.txtbanos);
        pisos = (TextView) findViewById(R.id.txtpisos);
        terreno = (TextView) findViewById(R.id.txtterreno);
        construccion = (TextView) findViewById(R.id.txtcons);
        descripcion = (TextViewEx) findViewById(R.id.txtdes);
        vendedor = (TextView) findViewById(R.id.txtusu);
        movil = (TextView) findViewById(R.id.txtmobil);
        correo = (TextView) findViewById(R.id.txtmail);

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        while(resultCode != ConnectionResult.SUCCESS){
            resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        }

        googleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_detalle)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mohome.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.locationpoint)));
        mohome.title("Ubicación");
        mohome.draggable(true);
        obtenerCasa();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getIntent().hasExtra("TAG_USUARIO")){
                    if(getIntent().hasExtra("TAG_CIUDAD") && getIntent().hasExtra("TAG_TIPO")) {
                        Intent intent = new Intent(detalle.this, resultados.class);
                        intent.putExtra("TAG_CIUDAD", getIntent().getExtras().getString("TAG_CIUDAD"));
                        intent.putExtra("TAG_TIPO", getIntent().getExtras().getString("TAG_TIPO"));
                        intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                        intent.putExtra("TAG_PRESUPUESTO",getIntent().getExtras().getString("TAG_PRESUPUESTO"));
                        startActivity(intent);
                        overridePendingTransition(R.animator.pushupin, R.animator.pushupout);
                    }else{
                        Intent intent = new Intent(detalle.this, usuario.class);
                        intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                        startActivity(intent);
                        overridePendingTransition(R.animator.pushupin, R.animator.pushupout);
                    }
                }else{
                    Intent intent = new Intent(detalle.this, resultados.class);
                    intent.putExtra("TAG_CIUDAD", getIntent().getExtras().getString("TAG_CIUDAD"));
                    intent.putExtra("TAG_TIPO", getIntent().getExtras().getString("TAG_TIPO"));
                    intent.putExtra("TAG_PRESUPUESTO", getIntent().getExtras().getString("TAG_PRESUPUESTO"));
                    startActivity(intent);
                    overridePendingTransition(R.animator.pushupin, R.animator.pushupout);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detalle_phone_button:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + movil.getText()));
                startActivity(callIntent);
                break;
            case R.id.detalle_email_button:
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("plain/text");
                send.putExtra(Intent.EXTRA_EMAIL, new String[] { correo.getText().toString() });
                startActivity(Intent.createChooser(send, "Elija un cliente de correo:"));
                break;
        }
        }
    };

    public void obtenerCasa(){
        String id = getIntent().getExtras().getString("TAG_ID");
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/publicaciones.xml");
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            final NodeList publicacion = doc.getElementsByTagName("publicacion");
            for (int i = 0; i < publicacion.getLength(); i++) {
                if(publicacion.item(i).getChildNodes().item(1).getTextContent().equals(id)){
                    final Node p = publicacion.item(i);
                    costo.setText("$" + publicacion.item(i).getChildNodes().item(5).getTextContent());
                    favButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer integer = (Integer) favButton.getTag();
                            switch(integer){
                                case R.drawable.ic_action_star:
                                    favButton.setImageResource(R.drawable.ic_action_star_fav);
                                    favButton.setTag(R.drawable.ic_action_star_fav);
                                    guardarFavorito(p.getChildNodes().item(1).getTextContent());
                                    break;
                                case R.drawable.ic_action_star_fav:
                                    favButton.setImageResource(R.drawable.ic_action_star);
                                    favButton.setTag(R.drawable.ic_action_star);
                                    removerFavorito(p.getChildNodes().item(1).getTextContent());
                                    break;
                            }
                        }
                    });
                    if(checkFavorito(publicacion.item(i).getChildNodes().item(1).getTextContent())){
                        favButton.setImageResource(R.drawable.ic_action_star_fav);
                        favButton.setTag(R.drawable.ic_action_star_fav);
                    }else{
                        favButton.setImageResource(R.drawable.ic_action_star);
                        favButton.setTag(R.drawable.ic_action_star);
                    }
                    File sdDir = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/fotos");
                    File[] sdDirFiles = sdDir.listFiles();
                    for(File singleFile : sdDirFiles){
                        if(singleFile.getName().equals(publicacion.item(i).getChildNodes().item(9).getTextContent())){
                            portada.setImageDrawable(Drawable.createFromPath(singleFile.getAbsolutePath()));
                        }
                    }
                    ubicacion.setText("Ubicación: " + publicacion.item(i).getChildNodes().item(13).getTextContent());
                    cuartos.setText("Cuartos: " + publicacion.item(i).getChildNodes().item(19).getTextContent());
                    banos.setText("Baños: " + publicacion.item(i).getChildNodes().item(21).getTextContent());
                    pisos.setText("Pisos: " + publicacion.item(i).getChildNodes().item(23).getTextContent());
                    terreno.setText("Terreno: " + publicacion.item(i).getChildNodes().item(25).getTextContent() + " m\u00B2");
                    construccion.setText("Construcción: " + publicacion.item(i).getChildNodes().item(27).getTextContent() + " m\u00B2");
                    if(publicacion.item(i).getChildNodes().item(29).getTextContent().length() < 20)
                        descripcion.setText(publicacion.item(i).getChildNodes().item(29).getTextContent());
                    else
                        descripcion.setText(publicacion.item(i).getChildNodes().item(29).getTextContent(),true);

                    mohome.position(new LatLng(Double.parseDouble(publicacion.item(i).getChildNodes().item(15).getTextContent()),Double.parseDouble(publicacion.item(i).getChildNodes().item(17).getTextContent())));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mohome.getPosition().latitude,mohome.getPosition().longitude), 18.0f));
                    refreshMarkers();

                    File usuarios = new File(Environment.getExternalStorageDirectory() + "/CLASIFICASAS/usuarios.xml");
                    InputStream is2 = new FileInputStream(usuarios.getPath());
                    DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db2 = dbf2.newDocumentBuilder();
                    Document doc2 = db2.parse(new InputSource(is2));
                    doc2.getDocumentElement().normalize();
                    NodeList usuario = doc2.getElementsByTagName("usuario");
                    for (int j = 0; j < usuario.getLength(); j++) {
                        if(publicacion.item(i).getChildNodes().item(3).getTextContent().equals(usuario.item(j).getChildNodes().item(9).getTextContent())) {
                            vendedor.setText("Resposable: " + usuario.item(j).getChildNodes().item(1).getTextContent() + " " +usuario.item(j).getChildNodes().item(3).getTextContent());
                            movil.setText(usuario.item(j).getChildNodes().item(5).getTextContent());
                            correo.setText(usuario.item(j).getChildNodes().item(7).getTextContent());
                        }
                    }
                }
            }
        }catch (Exception e){
        }
    }

    private void refreshMarkers(){
        googleMap.clear();
        googleMap.addMarker(mohome);
    }

    public boolean checkFavorito(String idpublicacion){
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
        if (newxmlfile.exists()) {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList favoritos = doc.getElementsByTagName("favorito");
                for (int i = 0; i < favoritos.getLength(); i++) {
                    if (favoritos.item(i).getChildNodes().item(1).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO")) && favoritos.item(i).getChildNodes().item(3).getTextContent().equals(idpublicacion)) {
                        return true;
                    }
                }
            } catch (Exception e) {}
        }
        return false;
    }

    public void guardarFavorito(String idpublicacion) {
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
        boolean success = true;
        if (!newxmlfile.exists()) {
            try {
                success = newxmlfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (success) {
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                XmlSerializer serializer = Xml.newSerializer();
                try {
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                    serializer.startTag(null, "favoritos");
                    serializer.startTag(null, "favorito");
                    serializer.startTag(null,"usuario");
                    serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                    serializer.endTag(null, "usuario");
                    serializer.startTag(null, "idpublicacion");
                    serializer.text(idpublicacion);
                    serializer.endTag(null, "idpublicacion");
                    serializer.endTag(null, "favorito");
                    serializer.endTag(null, "favoritos");
                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();
                } catch (Exception e) {
                    Log.e("Exception","error occurred while creating xml file");
                }
            }
        }else {
            try {
                InputStream is = new FileInputStream(newxmlfile.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList favoritos = doc.getElementsByTagName("favorito");
                FileOutputStream fileos = new FileOutputStream(newxmlfile);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(null, "favoritos");
                for(int i=0; i<favoritos.getLength(); i++){
                    serializer.startTag(null, "favorito");
                    serializer.startTag(null,"usuario");
                    serializer.text(favoritos.item(i).getChildNodes().item(1).getTextContent());
                    serializer.endTag(null, "usuario");
                    serializer.startTag(null, "idpublicacion");
                    serializer.text(favoritos.item(i).getChildNodes().item(3).getTextContent());
                    serializer.endTag(null, "idpublicacion");
                    serializer.endTag(null, "favorito");
                }
                serializer.startTag(null, "favorito");
                serializer.startTag(null,"usuario");
                serializer.text(getIntent().getExtras().getString("TAG_USUARIO"));
                serializer.endTag(null, "usuario");
                serializer.startTag(null, "idpublicacion");
                serializer.text(idpublicacion);
                serializer.endTag(null, "idpublicacion");
                serializer.endTag(null, "favorito");
                serializer.endTag(null, "favoritos");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
            }catch (Exception e){}
        }
    }

    public void removerFavorito(String idpublicacion) {
        try{
            File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/favoritos.xml");
            InputStream is = new FileInputStream(newxmlfile.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();
            NodeList favoritos = doc.getElementsByTagName("favorito");
            FileOutputStream fileos = new FileOutputStream(newxmlfile);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "favoritos");
            for(int i=0; i<favoritos.getLength(); i++){
                if(!(favoritos.item(i).getChildNodes().item(1).getTextContent().equals(getIntent().getExtras().getString("TAG_USUARIO")) && favoritos.item(i).getChildNodes().item(3).getTextContent().equals(idpublicacion))){
                    serializer.startTag(null, "favorito");
                    serializer.startTag(null, "usuario");
                    serializer.text(favoritos.item(i).getChildNodes().item(1).getTextContent());
                    serializer.endTag(null, "usuario");
                    serializer.startTag(null, "idpublicacion");
                    serializer.text(favoritos.item(i).getChildNodes().item(3).getTextContent());
                    serializer.endTag(null, "idpublicacion");
                    serializer.endTag(null, "favorito");
                }
            }
            serializer.endTag(null, "favoritos");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
        }catch (Exception e){}
    }
}
