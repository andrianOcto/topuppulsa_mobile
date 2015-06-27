package com.TopupPulsa.navigationview;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import RequestServer.RequestTask;

public class Register extends AppCompatActivity {

    //initiation Variable
    EditText Email;
    EditText Nama;
    EditText Password;
    EditText Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button regButton    = (Button) findViewById(R.id.button_register_submit);



        //listener back to Login
        TextView backLogin = (TextView)findViewById(R.id.text_backtoLogin);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });

        //listener Register
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get value from editText
                Email       = (EditText) findViewById(R.id.editText_email_register);
                Nama        = (EditText) findViewById(R.id.editText_username_register);
                Password    = (EditText) findViewById(R.id.editText_password_register);
                Number      = (EditText) findViewById(R.id.editText_nomorhp);

                Log.i("ets", Email.getText().toString());
                ContentValues content = new ContentValues();
                content.put("password",Password.getText().toString());
                content.put("name",Nama.getText().toString());
                content.put("email",Email.getText().toString());
                content.put("username", Number.getText().toString());

                RequestTask request = new RequestTask(Register.this,"/api/register","Loading . . .","GET",content);

                request.execute();
//                try {
//                    request.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }

}
