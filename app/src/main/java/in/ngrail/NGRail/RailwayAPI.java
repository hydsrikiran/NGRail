package in.ngrail.NGRail;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by kiran on 05-11-2015.
 */
public class RailwayAPI{
    /*public static void main(String[] args){
        System.out.println(getPnrStatus("4114157403"));
    }*/


    public static String getRegister(String pnr) {
        if(pnr == null){ //PNR has length 10
            return "Invalid Search.";
        }
        String APIKEY="72436";  //Your API key
        String endpoint = pnr;
        //Log.d("endpoint",endpoint);
        BufferedReader rd = null;
        StringBuilder response = null;
        HttpURLConnection request=null;
        try{
            URL endpointUrl = new URL(endpoint);
            request = (HttpURLConnection)endpointUrl.openConnection();

            request.setReadTimeout(100000 /* milliseconds */);
            request.setConnectTimeout(150000 /* milliseconds */);
            request.setRequestMethod("GET");
            request.setDoInput(true);
            request.setRequestProperty("connection", "close");
            request.connect();
            rd  = new BufferedReader(new InputStreamReader(request.getInputStream()));
            response = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null){
                response.append(line + "\n");
            }
            request.disconnect();
            //System.out.println("response : "+response);
        } catch (MalformedURLException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. MalformedURLException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (ProtocolException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. ProtocolException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (IOException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. IOException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            //return "No Response. Exception. Please try again!!";
            //e.printStackTrace();
        } finally {
            try{
                request.disconnect();
            } catch(Exception e){
            }

            if(rd != null){
                try{
                    rd.close();
                } catch(IOException ex){
                }
                rd = null;
            }
        }

        return response != null ? response.toString() : "Back-end Server issue. Please try again.!!!";
    }

    public static String getPnrStatus(String pnr) {
        if(pnr == null){ //PNR has length 10
            return "Invalid REQUEST.";
        }
        String APIKEY="72436";  //Your API key
        //String endpoint = pnr;
        String endpoint = "http://api.ngrail.in/pnrstatus/pnr/"+pnr;
        BufferedReader rd = null;
        StringBuilder response = null;
        HttpURLConnection request=null;

        try{
            URL endpointUrl = new URL(endpoint);

            request = (HttpURLConnection)endpointUrl.openConnection();

            request.setReadTimeout(10000 /* milliseconds */);
            request.setConnectTimeout(15000 /* milliseconds */);
            request.setRequestMethod("GET");
            request.setDoInput(true);
            request.setRequestProperty("connection","close");
            request.connect();
            rd  = new BufferedReader(new InputStreamReader(request.getInputStream()));
            response = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null){
                response.append(line);
            }
            request.disconnect();
            if(response.toString().equals("0"))
            {
                return response.toString();
            }
        } catch (MalformedURLException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. MalformedURLException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (ProtocolException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. ProtocolException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (IOException e) {
            //System.out.println("Exception: " + e.getMessage());
            return "No Response. IOException. Please try again!!"+e.getMessage();
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            //return "No Response. Exception. Please try again!!";
            //e.printStackTrace();
        } finally {
            try{
                request.disconnect();
            } catch(Exception e){
                return "No Response. Exception. Please try again!!"+e.getMessage();
            }

            if(rd != null){
                try{
                    rd.close();
                } catch(IOException ex){
                }
                rd = null;
            }
        }

        if(response==null || response.length()==0)
            return "No Response. Some Network issue/Flushed. !!!";
        else
            return response.toString();
    }
}
