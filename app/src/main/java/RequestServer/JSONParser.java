package RequestServer;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

/**
 * Created by octo on 6/27/2015.
 */

//Kelas yang digunakan untuk mengambil data dari server dengan menggunakan method request tertentu
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    String param = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String server, String method,
                                      ContentValues params) {

        // Making HTTP request
        try {

            // check for request method POST
            if(method.equals("POST")){
                //constants
                System.out.println("POST REQUEST");
                URL url = new URL(server);
                String message = new JSONObject().toString();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                Uri.Builder builder = new Uri.Builder();

                Set<Map.Entry<String, Object>> s=params.valueSet();
                Iterator itr = s.iterator();

                while(itr.hasNext())
                {
                    Map.Entry me = (Map.Entry)itr.next();
                    String key = me.getKey().toString();
                    Object value =  me.getValue();

                    builder.appendQueryParameter(key,value.toString());
                }

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();

                //open
                conn.connect();

                //do somehting with response
                is = conn.getInputStream();

            }else if(method.equals("GET")){
                // request method is GET
                //constants
                System.out.println("GET REQUEST");
                //digunakan untuk membuat parameter dari method GET
                Set<Map.Entry<String, Object>> s=params.valueSet();
                Iterator itr = s.iterator();
                int counter = 0;

                while(itr.hasNext())
                {
                    //digunakan untuk ? dan & penghubung parameter
                    if(counter == 0)
                        param += "?";
                    else
                        param += "&";

                    Map.Entry me = (Map.Entry)itr.next();
                    String key = me.getKey().toString();
                    Object value =  me.getValue();

                    param+= key + "=" + value;
                    counter++;
                }
                URL url = new URL(server+param);

                String message = new JSONObject().toString();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");

                //open
                conn.connect();

                //do somehting with response
                is = conn.getInputStream();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}
