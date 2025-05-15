package com.univbechar.mygreenhouse;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private SMSReceiver smsReceiver;


    CardView cardViewPOMP1,cardViewPOMP2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // PERMISSION
    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_CODE);
    }

    // recieve FUNCTOION
    private void setupSmsReceiver() {
        smsReceiver = new SMSReceiver();
        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, intentFilter);

        // Register a content observer to listen for changes to the SMS inbox
        getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true
                , new SMSContentObserver(new Handler()));
    }


    // PERMISSION
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupSmsReceiver();
            } else {

                Toast.makeText(this, "Permission SMS refusée. Impossible de lire les messages.", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    // SMS FUNCTION
    private class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                try {
                    displayLastReceivedMessage();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private class SMSContentObserver extends ContentObserver {
        public SMSContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                displayLastReceivedMessage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    //                                  DISPLAY MESSAGE

    private void displayLastReceivedMessage() throws Exception {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null && cursor.moveToFirst()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow("body"));

            //Toast.makeText(this, "message = "+message, Toast.LENGTH_SHORT).show();


            // to confirme
            if (message.equals("n")){


                //gridLayout.setBackgroundResource(R.drawable.bachground_button_red);

            }

            if (message.equals("System ON")){

                Toast.makeText(this,"System on", Toast.LENGTH_SHORT).show();
            }

            if (message.equals("System activated (off)")){

                cardViewPOMP1.setVisibility(View.INVISIBLE);
                cardViewPOMP2.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Your system not acivated", Toast.LENGTH_SHORT).show();
            }




            cursor.close();
        } else {

            //messageTextView.setText("Aucun message trouvé dans la boîte de réception.");
        }
    }





}