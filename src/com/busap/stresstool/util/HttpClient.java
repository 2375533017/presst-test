package com.busap.stresstool.util;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.util.UUID;
  
public class HttpClient {  
    private HttpURLConnection http;  
    private String sessionid;  
    
    public void openConnection(String httpURL) {  
        try {  
            URL url = new URL(httpURL);  
            http = (HttpURLConnection) url.openConnection();  
            
            http.setDoInput(true);  
            http.setDoOutput(true);  
            http.setRequestMethod("GET");  
            http.setUseCaches(false);  
            http.setInstanceFollowRedirects(false);  
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            http  
                .setRequestProperty(  
                    "User-Agent",  
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.4) Gecko/20100503 Firefox/3.6.4");  
            http.setRequestProperty(  
                "Accept",  
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
            http.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");  
            http.setRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");  
            http.setRequestProperty("Keep-Alive", "115");  
            http.setRequestProperty("Connection", "keep-alive");  
            http.setConnectTimeout(500000);  
            if (sessionid != null) {  
                http.setRequestProperty("Cookie", sessionid);  
            }  
        } catch (MalformedURLException e1) {  
            e1.printStackTrace();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
    /**  
     * @return the sessionid  
     */  
    public String getSessionid() {  
        return sessionid;  
    }  
    public void request(String data) throws IOException {  
        OutputStream out = null;  
        try {  
            out = http.getOutputStream();  
            byte[] requsetContent = data.getBytes();  
            out.write(requsetContent);  
            out.flush();  
        } finally {  
            out.close();  
        }  
    }  
    public void response() throws IOException {  
        String cks = http.getHeaderField("Set-Cookie");  
        if (cks != null && sessionid == null) {  
            sessionid = cks.substring(0, cks.indexOf(";"));  
        }  
        int code = http.getResponseCode();  
        if (code == HttpURLConnection.HTTP_OK) {  
            InputStream in = null;  
            BufferedReader reader = null;  
            try {  
                in = http.getInputStream();  
                reader = new BufferedReader(new InputStreamReader(in));  
                String line = null;  
                System.out.print("接受内容: ");  
                while ((line = reader.readLine()) != null) {  
                    System.out.println(new String(line.getBytes(), "UTF-8"));  
                }  
            } finally {  
                reader.close();  
            }  
        } else {  
            System.out.println("Status Code "+http.getResponseCode());  
        }  
    }  
    public void closeConnection() {  
        http.disconnect();  
    }  
}  
