package RequestServer;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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

import Setting.Server;

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
                URL url;
                HttpURLConnection connection = null;
                try {
                    Set<Map.Entry<String, Object>> s=params.valueSet();
                    Iterator itr = s.iterator();
                    int counter = 0;

                    while(itr.hasNext())
                    {
                        //digunakan untuk ? dan & penghubung parameter
                        if(counter == 0)
                            param += "";
                        else
                            param += "&";

                        Map.Entry me = (Map.Entry)itr.next();
                        String key = me.getKey().toString();
                        Object value =  me.getValue();

                        param+= key + "=" + value;
                        counter++;
                    }

                    //Create connection
                    url = new URL(server);
                    System.out.println(server);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");

                    connection.setRequestProperty("Content-Length", "" +
                            Integer.toString(param.getBytes().length));
                    connection.setRequestProperty("Content-Language", "en-US");

                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                    connection.setRequestProperty("Accept","*/*");

                    //Send request
                    DataOutputStream wr = new DataOutputStream (
                            connection.getOutputStream ());
                    wr.writeBytes (param);
                    wr.flush();
                    wr.close ();

                    //Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    json=response.toString();

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;

                } finally {

                    if(connection != null) {
                        connection.disconnect();
                    }
                }

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
            if(method.equals("GET")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            }

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
