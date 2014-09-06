package com.ihm.clasificasas.clasificasas;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Denny on 27/07/2014.
 */
public class detalle extends Activity {

    ImageButton phoneButton, emailButton, uploadButton;
    TextView phone, email;
    int serverResponseCode = 0;
    final String uploadFilePath = "/mnt/sdcard/DCIM/Camera/";
    final String uploadFileName = "IMG_20140730_133741.jpg";
    String upLoadServerUri = "http://"+IP.address+"/clasificasas/upload_file.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        phoneButton = (ImageButton) findViewById(R.id.detalle_phone_button);
        phoneButton.setOnClickListener(onClickListener);

        emailButton = (ImageButton) findViewById(R.id.detalle_email_button);
        emailButton.setOnClickListener(onClickListener);

        uploadButton = (ImageButton) findViewById(R.id.detalle_upload_button);
        uploadButton.setOnClickListener(onClickListener);

        phone = (TextView) findViewById(R.id.detalle_phone);
        email = (TextView) findViewById(R.id.detalle_email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(detalle.this, buscar.class);
                startActivity(intent);
                overridePendingTransition(R.animator.pushrightin, R.animator.pushrightout);
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
                callIntent.setData(Uri.parse("tel:" + phone.getText()));
                startActivity(callIntent);
                break;
            case R.id.detalle_email_button:
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("plain/text");
                send.putExtra(Intent.EXTRA_EMAIL, new String[] { email.getText().toString() });
                startActivity(Intent.createChooser(send, "Elija un cliente de correo:"));
                break;
            case R.id.detalle_upload_button:
                HttpPost post = new HttpPost("http://192.168.10.104/clasificasas/");
                new Thread(new Runnable() {
                    public void run() {
                        uploadFile(uploadFilePath + "" + uploadFileName);
                    }
                }).start();
                break;
        }
        }
    };

    public int uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName);
            runOnUiThread(new Runnable() {
                public void run() {}
            });
            return 0;
        }
        else
        {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
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
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +uploadFileName;
                            Toast.makeText(detalle.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(detalle.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(detalle.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            return serverResponseCode;
        }
    }

}
