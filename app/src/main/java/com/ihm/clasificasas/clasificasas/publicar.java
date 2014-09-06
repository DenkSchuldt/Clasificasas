package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denny on 28/07/2014.
 */
public class publicar extends FragmentActivity {

    Button publicar;
    Spinner spinner_tipo, spinner_ciudad;
    EditText costo, ubicacion, cuartos, banos, pisos, terreno, construccion, descripcion;
    String url_publicar = "http://"+IP.address+"/clasificasas/publicarcasa.php";
    String url_upload = "http://"+IP.address+"/clasificasas/upload_file.php";
    String TAG_SUCCESS = "success";
    String TAG_USUARIO = "usuario";
    String TAG_MESSAGE = "message";
    int serverResponseCode = 0;
    JSONParser jsonParser = new JSONParser();

    private Uri mImageCaptureUri;
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

    SupportMapFragment fm;
    GoogleMap googleMap;
    MapFragment mMapFragment;
    MarkerOptions moself=new MarkerOptions();
    MarkerOptions mohome=new MarkerOptions();


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

        publicar = (Button) findViewById(R.id.publicar);
        publicar.setOnClickListener(publicarButtonhandler);

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        moself.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        moself.title("yo");

        mohome.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home)));
        mohome.title("Casa");
        mohome.draggable(true);
        //mostrarPosicionActual();
        Button btn_find = (Button) findViewById(R.id.button);
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            EditText etLocation = (EditText) findViewById(R.id.publicar_ubicacion);
            String location = etLocation.getText().toString();
            if(location!=null && !location.equals("")){
                new GeocoderTask().execute(location);
            }
            }
        };
        btn_find.setOnClickListener(findClickListener);
    }

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
                setMarker(location, googleMap, moself);
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
            //PLACE THE INITIAL MARKER
            setMarker(location,googleMap,moself);
            setMarker(location,googleMap,mohome);
            refreshMarkers();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
        locationManager.requestLocationUpdates(provider,20000,0,locationListener);
    }
    private void refreshMarkers(){
        googleMap.clear();
        googleMap.addMarker(moself);
        googleMap.addMarker(mohome);
    }
    private void setMarker(Location location,GoogleMap googleMap,MarkerOptions mo){
        LatLng Position = new LatLng(location.getLatitude(),location.getLongitude());
        mo.position(Position);
        mo.snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(publicar.this, usuario.class);
                intent.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener publicarButtonhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.publicar:
                    //Intent buscar = new Intent(publicar.this, com.ihm.clasificasas.clasificasas.buscar.class);
                    //startActivity(buscar);
                    //overridePendingTransition(R.animator.pushleftin, R.animator.pushleftout);

                    File file = new File(path);
                    uploadPhoto(file);

                    break;
            }
        }
    };

    public void prepareImagePicker() {
        final String[] items = new String[]{"From Camera", "From SD Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            if (item == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(),
                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
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
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
            }
            }
        });
        final AlertDialog dialog = builder.create();
        findViewById(R.id.publicar_portada).setOnClickListener(new View.OnClickListener() {
            //mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dialog.show();
            ((ImageButton)findViewById(R.id.publicar_portada)).setImageBitmap(ImagenActual);
            ((ImageButton)findViewById(R.id.publicar_portada)).setVisibility(ImageButton.VISIBLE);
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
        ImagenActual=bitmap;
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        LatLng latLng;
        MarkerOptions markerOptions;
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
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
            // Clears all the existing markers on the map
            googleMap.clear();
            // Adding Markers on Google Map for each matching address
            for(int i=0;addresses!=null&&i<addresses.size();i++){
                Address address = (Address) addresses.get(i);
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                googleMap.addMarker(markerOptions);
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    /**
     * Background Async Task to Create new product
     * */
    class PublicarCasa extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> p = new ArrayList<NameValuePair>();

            p.add(new BasicNameValuePair("usuario",getIntent().getExtras().getString("TAG_USUARIO")));
            //p.add(new BasicNameValuePair("latitud", user.getText().toString()));
            //p.add(new BasicNameValuePair("longitud", user.getText().toString()));
            p.add(new BasicNameValuePair("cuartos", cuartos.getText().toString()));
            p.add(new BasicNameValuePair("pisos", pisos.getText().toString()));
            p.add(new BasicNameValuePair("descripcion", descripcion.getText().toString()));
            p.add(new BasicNameValuePair("costo", costo.getText().toString()));
            //p.add(new BasicNameValuePair("fotoportada", user.getText().toString()));
            p.add(new BasicNameValuePair("terreno", terreno.toString().toString()));
            p.add(new BasicNameValuePair("construccion", construccion.getText().toString()));
            p.add(new BasicNameValuePair("direccion", ubicacion.getText().toString()));
            p.add(new BasicNameValuePair("ventaoalquiler", spinner_tipo.getSelectedItem().toString()));
            p.add(new BasicNameValuePair("ciudad", spinner_ciudad.getSelectedItem().toString()));
            p.add(new BasicNameValuePair("banos", banos.getText().toString()));

            JSONObject json = jsonParser.makeHttpRequest(url_publicar,"POST", p);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), usuario.class);
                    i.putExtra("TAG_USUARIO", getIntent().getExtras().getString("TAG_USUARIO"));
                    startActivity(i);
                    overridePendingTransition(R.animator.shrinka, R.animator.shrinkb);
                    finish();
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void uploadPhoto(final File f) {
        new Thread(new Runnable() {
            public void run() {
                uploadFile(f);
            }
        }).start();
    }

    public int uploadFile(File sourceFile) {
        String fileName = sourceFile.getName();
        String extension = getFileExtension(fileName);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        if (!sourceFile.isFile()) {
            runOnUiThread(new Runnable() {
                public void run() {}
            });
            return 0;
        }
        else {
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url_upload);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();


                FileInputStream fileInputStream = new FileInputStream(sourceFile);


                MultipartEntity mpEntity = new MultipartEntity();
                ContentBody cbFile = new FileBody(file, "image/"+extension);
                mpEntity.addPart("userfile", cbFile);


                httppost.setEntity(mpEntity);
                System.out.println("executing request " + httppost.getRequestLine());
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();



                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serverResponseCode;
        }
    }

    public String getFileExtension(String name) {
        String extension = "";
        int index = name.lastIndexOf('.');
        for(int i=index+1; i<name.length(); i++){
            extension+=name.charAt(i);
        }
        return extension;
    }

}
