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

import RequestServer.RequestMaster;
import Setting.AppConfig;

/**
 * Created by deny on 6/23/15.
 */
public class TopupPulsa extends Fragment {

    SharedPreferences sharedpreferences;
    TextView nameAccount;
    TextView saldoAccount;
    Spinner spinnerBank;
    EditText rekeningNumber;
    EditText message;
    Button topupButton;
    EditText nominal;

    String bank;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pulsa_content,container,false);

        //inisiasi data dari server
        sharedpreferences =  getActivity().getSharedPreferences(AppConfig.nameApp, Context.MODE_PRIVATE);;
        nameAccount = (TextView) v.findViewById(R.id.text_account2);
        saldoAccount= (TextView) v.findViewById(R.id.text_saldo_sekarang);
        spinnerBank = (Spinner) v.findViewById(R.id.spinner_bank);
        rekeningNumber= (EditText) v.findViewById(R.id.editText_norek);
        message     = (EditText) v.findViewById(R.id.editText_message);
        nominal     = (EditText) v.findViewById(R.id.editText_nominal_topup);
        topupButton = (Button) v.findViewById(R.id.button_topup);
        
        //set to text view
        nameAccount.setText(sharedpreferences.getString(AppConfig.userName,"unknown"));
        saldoAccount.setText(String.valueOf(sharedpreferences.getInt(AppConfig.userSaldo, 0)));

        topupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get value from spinner
                bank = spinnerBank.getSelectedItem().toString();

                ContentValues content = new ContentValues();
                try {
                    //Add parameter to requestTask
                    content.put("idmember", String.valueOf(sharedpreferences.getInt(AppConfig.userID, -99)));
                    content.put("rekening", rekeningNumber.getText().toString());
                    content.put("bank", bank);
                    content.put("nominal", nominal.getText().toString());
                    content.put("message",message.getText().toString());
                }
                catch (Exception e) {}
                //request to server
                RequestTask request = new RequestTask(getActivity(), "/api/topup", "Loading . . .", "POST", content);
                request.execute();
            }
        });

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

            //Hide loadingDialog
            this.dismissDialog();

            try {
                if (json.get("status_code").toString().equals("200") && json.get("message").toString().equals("Success Request Topup"))
                {
                    //Show message
                    Toast.makeText(getActivity(), json.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Show message
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    System.out.println(json.toString());
                }
            }
            catch (Exception e){}
        }
    }
}