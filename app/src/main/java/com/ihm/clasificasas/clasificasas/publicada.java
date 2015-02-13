package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Denny on 27/07/2014.
 */
public class publicada extends Activity {

    ImageView delete, portada;
    TextView costo, ubicacion, cuartos, banos, pisos, terreno, construccion, vendedor, movil, correo;
    TextViewEx descripcion;
    Node publicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicacion);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        delete = (ImageView) findViewById(R.id.publicacion_borrar);

        costo = (TextView) findViewById(R.id.publicacion_costo);
        portada = (ImageView) findViewById(R.id.publicacion_portada);
        ubicacion = (TextView) findViewById(R.id.publicacion_ubicacion);
        cuartos = (TextView) findViewById(R.id.publicacion_cuartos);
        banos = (TextView) findViewById(R.id.publicacion_banos);
        pisos = (TextView) findViewById(R.id.publicacion_pisos);
        terreno = (TextView) findViewById(R.id.publicacion_terreno);
        construccion = (TextView) findViewById(R.id.publicacion_construccion);
        descripcion = (TextViewEx) findViewById(R.id.publicacion_descripcion);

        obtenerCasa();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(publicada.this, usuario.class);
                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushupin, R.animator.pushupout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void obtenerCasa(){
        final String id = getIntent().getExtras().getString("TAG_ID");
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
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(publicada.this);
                            builder.setMessage("¿Está seguro de borrar la publicación?")
                                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int i) {
                                            eliminarPublicacion(id);
                                            eliminarDeFavoritos(id);
                                            Intent intent = new Intent(publicada.this, usuario.class);
                                            intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                                            startActivity(intent);
                                            overridePendingTransition(R.animator.pushupin, R.animator.pushupout);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
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
                }
            }
        }catch (Exception e){
        }
    }

    public void eliminarPublicacion(String idpublicacion) {
        try{
            File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/CLASIFICASAS/publicaciones.xml");
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
                if(!publicaciones.item(i).getChildNodes().item(1).getTextContent().equals(idpublicacion)){
                    serializer.startTag(null, "publicacion");
                        serializer.startTag(null,"id");
                        serializer.text(publicaciones.item(i).getChildNodes().item(1).getTextContent());
                        serializer.endTag(null, "id");
                        serializer.startTag(null, "usuario");
                        serializer.text(publicaciones.item(i).getChildNodes().item(3).getTextContent());
                        serializer.endTag(null, "usuario");
                        serializer.startTag(null, "costo");
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
            }
            serializer.endTag(null, "publicaciones");
            serializer.endDocument();
            serializer.flush();
            fileos.close();
        }catch (Exception e){}
    }

    public void eliminarDeFavoritos(String idpublicacion){
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
                if(!favoritos.item(i).getChildNodes().item(3).getTextContent().equals(idpublicacion)){
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
