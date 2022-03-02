package com.example.barcodescan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btnScan;
    EditText txtResultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btnScan);
        txtResultado = findViewById(R.id.txtResultado);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IntentIntegrator integrador = new IntentIntegrator(MainActivity.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if(result != null) {
            if(result.getContents() == null){

                Toast.makeText(this,"Leitura cancelada",Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                //txtResultado.setText(result.getContents());
                LeerWs();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void LeerWs(){
        String url= "https://jsonplaceholder.typicode.com/posts/1";
        StringRequest postRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtResultado.setText(jsonObject.getString("title"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        Volley.newRequestQueue(this).add(postRequest);
    }
}