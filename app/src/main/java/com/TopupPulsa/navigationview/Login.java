package com.TopupPulsa.navigationview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLDecoder;

import RequestServer.RequestMaster;
import Setting.AppConfig;

public class Login extends AppCompatActivity {
    EditText Email;
    EditText Password;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedpreferences = getSharedPreferences(AppConfig.nameApp, Context.MODE_PRIVATE);

        Button toRegister = (Button)findViewById(R.id.button_register);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Login.this, Register.class);
                startActivity(it);

                overridePendingTransition(R.animator.anim, R.animator.anim2);
            }
        });

        Button toHome = (Button)findViewById(R.id.login);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get value from editText
                Email = (EditText) findViewById(R.id.editText_username);
                Password = (EditText) findViewById(R.id.editText_password);

                ContentValues content = new ContentValues();
                try {
                    //Add parameter to requestTask
                    content.put("email", URLDecoder.decode(Email.getText().toString(), "UTF-8"));
                    content.put("password", Password.getText().toString());
                }
                catch (Exception e)
                {

                }

                RequestTask request = new RequestTask(Login.this, "/api/loginmember", "Sign in . . .", "GET", content);
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
                if (json.get("status_code").toString().equals("200") && json.get("response").toString().equals("OK")
                        && json.get("message").toString().equals("Login success.")) {

                    //Show message
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    JSONObject test= json.getJSONObject("result");

                    //Save account to temporary
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putInt(AppConfig.userID, json.getJSONObject("result").getJSONObject("account").getInt("id"));
                    editor.putInt(AppConfig.userSaldo, json.getJSONObject("result").getJSONObject("account").getInt("saldo"));
                    editor.putString(AppConfig.userEmail, json.getJSONObject("result").getJSONObject("account").getString("email"));
                    editor.putString(AppConfig.userName, json.getJSONObject("result").getJSONObject("account").getString("name"));
                    editor.commit();

                    //back to home (Login)
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                    //something wrong
                    Log.i("error", json.get("message").toString() + json.get("result") + json.get("response"));
            }
            catch (Exception e)
            {
                Log.i("Error",e.getMessage());
            }
        }
    }
}
