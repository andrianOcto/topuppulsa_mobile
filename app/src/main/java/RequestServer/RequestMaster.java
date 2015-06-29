package RequestServer;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import Setting.Server;

/**
 * Created by octo on 6/27/2015.
 */
public class RequestMaster extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    private Context context;
    JSONParser request=new JSONParser();
    public JSONObject json;
    public ContentValues params;
    String urlService;
    String loading;
    String methodReq;
    public boolean stat=false;

    public RequestMaster(Context context, String url, String loadingMsg, String method, ContentValues parameter)
    {
        //super();
        this.context = context;
        loading=loadingMsg;
        urlService=url;
        params=parameter;
        methodReq=method;
    }

    @Override
    protected String doInBackground(String... arg0) {

        // getting JSON string from URL
        json = request.makeHttpRequest(Server.path + urlService, methodReq, params);

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
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        pDialog.dismiss();
    }

    public void dismissDialog()
    {
        pDialog.dismiss();
    }


}
