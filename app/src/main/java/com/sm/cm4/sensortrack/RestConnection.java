package com.sm.cm4.sensortrack;

import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RestConnection{
    private static String protocol = "http";
    private String address;
    private static Integer port = 8080;
    private String url = null;
    private boolean urlValid;

    public boolean hasValidUrl(){
	return urlValid;
    }

    public static boolean validUrl( String url){
	if( url == null) return false;
	if ( url.contains(protocol) && url.contains(String.valueOf(port)) ){
	    return true;
	}
        return false;
    }
    public void setUrl(String url){
        this.url =  protocol+"://"+url+":"+port;
        Log.i("listen", "listening connection construida" +this.url);
	if(validUrl(this.url)){
	    this.urlValid  = true;
	}else{
	    this.urlValid = false;
	}
    }
    public String getUrl(){
	// return protocol+"://"+address+":"+port;
	return url;
    }
    public boolean registerDevice( String device_ip, String device_name){
    // Create a new HttpClient and Post Header
     String url  = this.url+"/dev";

    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(url);

    try {
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("device_name", device_name));
        nameValuePairs.add(new BasicNameValuePair("device_ip", device_ip ));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
	if( response.getStatusLine().getStatusCode() == 200){
	    return true;
	}
    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
        return false;
} 
}
