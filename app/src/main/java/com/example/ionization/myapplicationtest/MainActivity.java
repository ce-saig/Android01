package com.example.ionization.myapplicationtest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToast = (Button) findViewById(R.id.btnToast);
        final EditText edtName = (EditText) findViewById(R.id.edtName);
        final EditText edtMsg = (EditText) findViewById(R.id.edtMsg);


        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String msg = edtMsg.getText().toString();

                SendData sd = new SendData("http://ziko.saig.kmitl.net",name,msg);
                sd.execute();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    class SendData extends AsyncTask{

        String url;
        String name;
        String comment;

        SendData(String url,String name,String comment){
            this.url    = url;
            this.name   = name;
            this.comment= comment;
        }

        @Override
        protected String doInBackground(Object[] params) {
            InputStream is;

            try {
                String stringUrl = String.format("http://ziko.saig.kmitl.net/api/create?" +
                                "name=%s&comment=%s",
                        URLEncoder.encode(name, "UTF-8"),
                        URLEncoder.encode(comment,"UTF-8"));

                URL url = new URL(stringUrl);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(10000 /* milliseconds */);
                con.setConnectTimeout(15000 /* milliseconds */);
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.connect();

                is =con.getInputStream();
                int conLength = con.getContentLength();

                String respond= readIt(is,conLength);
                return respond;


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected String readIt(InputStream is,int conLength){
            Reader rd = null;
            try {
                rd = new InputStreamReader(is, "UTF-8");
                char[] buffer = new char[conLength];
                rd.read(buffer);
                return new String(buffer);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }
    }

}














