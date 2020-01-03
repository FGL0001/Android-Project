package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView; //description
    EditText editText; //city name
    ImageView imageView;
    private RequestQueue requestQueue;

    //https://api.openweathermap.org/data/2.5/weather?q=Karnal&appid=c3877349917a6c6588314bc2cf83e8e5
    String baseURL="https://api.openweathermap.org/data/2.5/weather?q=";
    String API="&appid=c3877349917a6c6588314bc2cf83e8e5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button2);
        textView=findViewById(R.id.textView2);
        editText=findViewById(R.id.editText);
        requestQueue=VolleySingleton.getInstance(this).getmReqeustQueue();
        imageView=findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myURL= baseURL + editText.getText().toString() + API;
                Log.i("URL","URL:"+myURL);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSON","INFO:"+response);
                        try {
                             String info=response.getString("weather");
                             String info2=response.getString("main");
                             Log.i("INFO","INFO:"+info);

                             JSONArray ar= new JSONArray(info);
                             JSONObject obj=new JSONObject(info2);
                             for(int i=0;i<ar.length();i++){
                                 JSONObject parObj=ar.getJSONObject(i);
                                 String myWeather=parObj.getString("main");
                                 String Desc=parObj.getString("description");
                                 textView.setText(myWeather+"\n");
                                 textView.append("Description :"+Desc+"\n");
                                 Log.i("ID","ID:"+parObj.getString("id"));
                                 Log.i("MAIN","MAIN"+parObj.getString("main"));

                                 if(myWeather =="Clouds")
                                 {
                                     imageView.setImageResource(Integer.parseInt("@drawable/partlycloudy.png"));
                                 }
                                 if(myWeather =="Haze")
                                 {
                                     imageView.setImageResource(Integer.parseInt("@drawable/haze.png"));
                                 }
                             }
                             String temperature=String.valueOf(obj.getInt("temp")-273);
                             textView.append("Temperature :"+temperature+"°C");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error! Enter the correct city name",Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(request);
            }
        });


    }
}
