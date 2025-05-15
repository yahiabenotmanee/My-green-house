package com.univbechar.mygreenhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class data_enc_Activity extends AppCompatActivity {

    EditText edt_in;
    Button btn_done;
    TextView tx_out1,tx_out2;



    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_enc);

edt_in=findViewById(R.id.edt_insetdata);
tx_out1=findViewById(R.id.txt_data1);
tx_out2=findViewById(R.id.txt_data2);
btn_done=findViewById(R.id.btn_done);

String indata = edt_in.getText().toString();

btn_done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {




        Intent intent = new Intent(data_enc_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
});


    }


    // CHIFFREMENT

    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, String secretKey) throws Exception {
        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKey generateKey(String secretKey) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }
    // BEFORE END

}