package com.http;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;


public class HttpWrapper {
	public static final int TIMEOUT_MILLISEC=8000;
    private static HttpClient httpclient = new DefaultHttpClient();
    
    static HttpResponse response = null;
    
    public static String search_key = "";

    public static HttpResponse connectServer(HttpEntity o)
    {    	
    	HttpPost httppost = new HttpPost("http://lagumusic.com/mobile/result.php?search=" + search_key);
    	
    	HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,
                TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        
        httppost.setParams(httpParams);
        
    	httppost.setEntity(o);
    	
		try {
			response = null;
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	return response;
    }
	
	public synchronized static JSONObject getData(){
		try {
	        // Add your data		
			
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("key", "key"));
	        
	        // Execute HTTP Post Request	        
	        JSONObject json = JSONFunctions.getJSONfromURL(connectServer(new UrlEncodedFormEntity(nameValuePairs)));
	        
	        return json;
	    } catch(Exception e){
	        e.printStackTrace();
	    }
	    return null;
	}
}

