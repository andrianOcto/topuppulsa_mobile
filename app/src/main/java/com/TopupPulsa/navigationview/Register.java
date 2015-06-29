package com.TopupPulsa.navigationview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import RequestServer.RequestMaster;

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
        TextView backLogin = (TextView)findViewById(R.id.button_backtoLogin);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });

        //listener Register (Pada saat button Register di klik)
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get value from editText
                Email = (EditText) findViewById(R.id.editText_email_register);
                Nama = (EditText) findViewById(R.id.editText_username_register);
                Password = (EditText) findViewById(R.id.editText_password_register);
                Number = (EditText) findViewById(R.id.editText_nomorhp);

                //Add parameter to requestTask
                ContentValues content = new ContentValues();
                content.put("phone", Number.getText().toString());
                content.put("name",  Nama.getText().toString());
                content.put("email", Email.getText().toString());
                content.put("password",  Password.getText().toString());

                RequestTask request = new RequestTask(Register.this, "/api/register", "Loading . . .", "GET", content);
                request.execute();

            }
        });
    }

    private class RequestTask extends RequestMaster {

        public RequestTask(Context context, String url, String loadingMsg, String method, ContentValues parameter) {
            super(context, url, loadingMsg, method, parameter);
        }

        @Override
        protected void onPostExecute(String result) {
            //digunakan untuk mendefinisikan proses setelah proses dengan menggunakan variable json
            //dokumentasi lebih lanjut dapat dilihat pada kelas RequestMaster

            try {
                super.onPostExecute(result);

                //Hide loadingDialog
                this.dismissDialog();

                //Cek apakah response berhasil atau tidak
                if (json.get("status_code").toString().equals("200") && json.get("response").toString().equals("OK")) {
                    //Show message
                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    //back to home (Login)
                    Intent i = new Intent(Register.this, Login.class);
                    startActivity(i);
                    finish();
                }
                else
                    //something wrong
                    Log.i("error",json.get("message").toString() + json.get("result")+json.get("response"));
            }
            catch (Exception e)
            {
                Log.i("Error",e.getMessage());
            }
        }
    }

}
