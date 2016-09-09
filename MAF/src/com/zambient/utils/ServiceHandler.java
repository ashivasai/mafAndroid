package com.zambient.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceHandler {
	static String response = null;
	static String finalresponse = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String
	makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                    //httpPost.setParams(HTTP.CONTENT_TYPE,"application/json");
                    
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            
            
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
    
    public String makeServiceCallWithEntity(String url){
    	
    	HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		try {
			httpPost.setEntity(new StringEntity("[]"));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}

		try {

			HttpResponse response = httpClient.execute(httpPost);
			StringBuffer stringbuff = new StringBuffer("");
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bf.readLine()) != null) {
				stringbuff.append(line + LineSeparator);
			}
			bf.close();
			finalresponse = stringbuff.toString();
		} 
		catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		return finalresponse;
    }
    
 public HttpResponse makeServiceCallWithEntitybuff(String url){
    	
    	HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		HttpResponse response2=null;
		try {
			httpPost.setEntity(new StringEntity("[]"));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}

		try {

			response2 = httpClient.execute(httpPost);
			/*StringBuffer stringbuff = new StringBuffer("");
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bf.readLine()) != null) {
				stringbuff.append(line + LineSeparator);
			}
			bf.close();
			finalresponse = stringbuff.toString();*/
		} 
		catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return response2;
    }
    
 public String makeServiceCallWithPayLoad(String url,String payLoad){
    	
    	HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		try {
			httpPost.setEntity(new StringEntity(payLoad));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(httpPost);
			StringBuffer stringbuff = new StringBuffer("");
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bf.readLine()) != null) {
				stringbuff.append(line + LineSeparator);
			}
			bf.close();
			finalresponse = stringbuff.toString();
		} 
		catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		return finalresponse;
    }
}