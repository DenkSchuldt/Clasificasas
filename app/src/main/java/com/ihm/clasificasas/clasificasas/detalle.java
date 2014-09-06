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
        }
        }
    };

}
