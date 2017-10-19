package com.linder.messagelindertec;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnenviar;
    private EditText person, conten;
    //Variales de permiso
    final private int REQUEST_CODE_ASK_PERMISON = 124;
    int hasMessagePermison;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void accsserPermison() {
        hasMessagePermison = checkSelfPermission(Manifest.permission.SEND_SMS);
        if (hasMessagePermison != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISON);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_ASK_PERMISON: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permiso de mensaje concedido", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accsserPermison();

        btnenviar = (Button) findViewById(R.id.btnenviar);
        conten = (EditText) findViewById(R.id.contentMessage);
        person = (EditText) findViewById(R.id.numberPerson);

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(getApplicationContext(), "Mensahe enviado...", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(getApplicationContext(), "No se pudo enviar SMS", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(getApplicationContext(), "Servicio no diponible", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(getApplicationContext(), "PDU (Protocol Data Unit) es NULL", Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(getApplicationContext(), "Fallor por falta de vida", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter("SMS_SENT"));

                SmsManager sms = SmsManager.getDefault();
                if (person.getText().toString().length() > 0 &&
                        conten.getText().toString().length() > 0) {
                    sms.sendTextMessage(person.getText().toString(), null, conten.getText().toString(), sentIntent, null);
                } else {
                    Toast.makeText(getApplicationContext(), "No se puede enviar ponga cosas buenas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}