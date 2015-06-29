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
import android.widget.TextView;

import RequestServer.RequestMaster;
import Setting.AppConfig;

/**
 * Created by deny on 6/23/15.
 */
public class TopupPulsa extends Fragment {

    SharedPreferences sharedpreferences;
    TextView nameAccount;
    TextView saldoAccount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pulsa_content,container,false);

        //inisiasi data dari server
        sharedpreferences =  getActivity().getSharedPreferences(AppConfig.nameApp, Context.MODE_PRIVATE);;
        nameAccount = (TextView) v.findViewById(R.id.text_account2);
        saldoAccount= (TextView) v.findViewById(R.id.text_saldo_sekarang);

        //set to text view
        nameAccount.setText(sharedpreferences.getString(AppConfig.userName,"unknown"));
        saldoAccount.setText(String.valueOf(sharedpreferences.getInt(AppConfig.userSaldo,0)));
        
        return v;
    }
    private class RequestTask extends RequestMaster{

        public RequestTask(Context context, String url, String loadingMsg, String method, ContentValues parameter) {
            super(context, url, loadingMsg, method, parameter);
        }

        @Override
        protected void onPostExecute(String result) {
            //digunakan untuk mendefinisikan proses setelah proses dengan menggunakan variable json
            //dokumentasi lebih lanjut dapat dilihat pada kelas RequestMaster
            super.onPostExecute(result);


        }
    }
}