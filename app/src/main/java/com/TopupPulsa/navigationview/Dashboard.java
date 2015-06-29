package com.TopupPulsa.navigationview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Setting.AppConfig;

/**
 * Created by deny on 6/23/15.
 */
public class Dashboard extends Fragment {
    TextView nameAccount;
    TextView saldoAccount;
    SharedPreferences sharedpreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_content,container,false);

        //inisiasi data dari server
        sharedpreferences =  getActivity().getSharedPreferences(AppConfig.nameApp, Context.MODE_PRIVATE);;
        nameAccount = (TextView) v.findViewById(R.id.text_account);
        saldoAccount= (TextView) v.findViewById(R.id.text_saldo_sekarang);

        //set to text view
        nameAccount.setText(sharedpreferences.getString(AppConfig.userName,"unknown"));
        saldoAccount.setText(String.valueOf(sharedpreferences.getInt(AppConfig.userSaldo,0)));

        return v;
    }
}

