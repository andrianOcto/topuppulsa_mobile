package RequestServer;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Setting.Server;

/**
 * Created by octo on 6/27/2015.
 */
public class RequestTask extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    private Context context;
    JSONParser request=new JSONParser();
    public JSONObject json;
    Server server = new Server();
    public ContentValues params;
    String urlService;
    String loading;
    String methodReq;

    public RequestTask(Context context,String url,String loadingMsg,String method,ContentValues parameter)
    {
        super();
        this.context = context;
        loading=loadingMsg;
        urlService=url;
        params=parameter;
        methodReq=method;
    }

    @Override
    protected String doInBackground(String... arg0) {

        // getting JSON string from URL
        json = request.makeHttpRequest(server.getPath()+urlService, methodReq, params);

        return null;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(loading);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        pDialog.dismiss();

        super.onPostExecute(result);

        synchronized (this) {
            this.notify();
        }
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        pDialog.dismiss();
    }
}
