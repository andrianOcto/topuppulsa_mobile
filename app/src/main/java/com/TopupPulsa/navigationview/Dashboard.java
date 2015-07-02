package com.TopupPulsa.navigationview;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLDecoder;

import RequestServer.RequestMaster;
import Setting.AppConfig;

/**
 * Created by deny on 6/23/15.
 */
public class Dashboard extends Fragment {
    TextView nameAccount;
    TextView saldoAccount;
    EditText phoneNumber;
    Spinner nomimalSpinner;

    SharedPreferences sharedpreferences;
    Button buyTopup;
    int nominalValue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_content,container,false);

        //inisiasi data dari server
        sharedpreferences =  getActivity().getSharedPreferences(AppConfig.nameApp, Context.MODE_PRIVATE);;
        nameAccount = (TextView) v.findViewById(R.id.text_account);
        saldoAccount= (TextView) v.findViewById(R.id.text_saldo_sekarang);
        phoneNumber = (EditText) v.findViewById(R.id.editText_nomortujuan);
        buyTopup    = (Button) v.findViewById(R.id.button_beli_pulsa);
        nomimalSpinner     = (Spinner) v.findViewById(R.id.spinner_isipulsa);
        nominalValue=0;

        //set listener spinner (digunakan untuk mengubah 5k menjadi 5000 10k menjadi 10000 dst)
        nomimalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    nominalValue = 5000;
                else if (position == 1)
                    nominalValue = 10000;
                else if (position == 2)
                    nominalValue = 25000;
                else if (position == 3)
                    nominalValue = 50000;
                else if (position == 4)
                    nominalValue = 100000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set to text view
        nameAccount.setText(sharedpreferences.getString(AppConfig.userName,"unknown"));
        saldoAccount.setText(String.valueOf(sharedpreferences.getInt(AppConfig.userSaldo, 0)));

        //action when button buy clicked
        buyTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues content = new ContentValues();
                try {
                    //Add parameter to requestTask
                    content.put("idmember", String.valueOf(sharedpreferences.getInt(AppConfig.userID, -99)));
                    content.put("phonenumber", phoneNumber.getText().toString());
                    content.put("nominal", String.valueOf(nominalValue));
                }
                catch (Exception e)
                {

                }

                RequestTask request = new RequestTask(getActivity(), "/api/buypulsa", "Loading . . .", "GET", content);
                request.execute();

            }
        });

        return v;
    }


    private class RequestTask extends RequestMaster {

        public RequestTask(Context context, String url, String loadingMsg, String method, ContentValues parameter) {
            super(context, url, loadingMsg, method, parameter);
        }

        @Override
        protected void onPostExecute(String result) {
            //digunakan untuk mendefinisikan proses setelah proses dengan menggunakan variable json
            //dokumentasi lebih lanjut dapat dilihat pada kelas RequestMaster
            super.onPostExecute(result);

            //Hide loadingDialog
            this.dismissDialog();

            try {
                if (json.get("status_code").toString().equals("200") && json.get("message").toString().equals("Success Buy Pulsa")) {
                    //Show message
                    Toast.makeText(getActivity(), json.get("message").toString(), Toast.LENGTH_SHORT).show();

                    //Update textView agar sesuai
                    saldoAccount.setText(String.valueOf(sharedpreferences.getInt(AppConfig.userSaldo, -99) - nominalValue));

                    //update sharedpreference dengan value baru
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(AppConfig.userSaldo, sharedpreferences.getInt(AppConfig.userSaldo, -99) - nominalValue);
                }
                else
                {
                    //Show message
                    Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e) {}
        }
    }
}

