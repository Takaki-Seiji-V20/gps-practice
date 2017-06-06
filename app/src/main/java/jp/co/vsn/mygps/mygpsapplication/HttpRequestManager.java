package jp.co.vsn.mygps.mygpsapplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class HttpRequestManager {
    private static final int TIME_OUT = 15000;

    public static String sendGetData(String url) throws SocketException {
        String response = null;
        try {
            HttpParams httpParms = new BasicHttpParams();
            httpParms.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
            httpParms.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
            HttpClient httpClient = new DefaultHttpClient(httpParms);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setParams(httpParms);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() < 400) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    sb.append(readLine);
                }
                response = sb.toString();
                inputStream.close();
            }
        } catch (Exception e) {
            throw new SocketException();
        }
        if (response == null)
            throw new SocketException();
        return response;
    }
}
