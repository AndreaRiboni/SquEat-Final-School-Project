package com.example.ap_v00;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String email;
    private EditText mailET;
    private String password;
    private EditText passwordET;
    private TextView registerET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mailET = (EditText) findViewById(R.id.mail);
        passwordET = (EditText) findViewById(R.id.password);
        registerET = (TextView) findViewById(R.id.signIn);

        registerET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void logIn(View v){
        //Logiche da applicare per l'autenticazione
    }
}
