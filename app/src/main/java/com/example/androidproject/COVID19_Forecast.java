package com.example.androidproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class COVID19_Forecast extends AppCompatActivity {
    public Button predict;
    final Calendar myCalendar= Calendar.getInstance();
    EditText place, date_et;
    ProgressBar progressBar;
    AlertDialog.Builder dialog;
    TextToSpeech sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_forecast);
        sound = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                sound.setLanguage(Locale.UK);
            }
        });
        place = findViewById(R.id.place);
        date_et =(EditText) findViewById(R.id.date);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }

        };
        date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(COVID19_Forecast.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        predict = findViewById(R.id.predict);
//        Result = findViewById(R.id.result);
        progressBar = findViewById(R.id.progressBar);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i1 = place.getText().toString();
                String i2 = date_et.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                String url = "https://covidencyclopedia1.herokuapp.com/?a=" + i1 + "&b=" + i2;
                RequestQueue queue = Volley.newRequestQueue(COVID19_Forecast.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int size = response.getString("prediction").length();
                                    String[] risk_arr = response.getString("prediction").substring(2,size-3).split(",");
                                    String risk = risk_arr[1].substring(2);
                                    float risk_percent = Float.parseFloat(risk_arr[0].substring(0,risk_arr[0].length()-2));
                                    risk_percent  = (float) (Math.round(risk_percent * 100.0) / 100.0);
                                    Log.d("response", "onResponse: " + risk + " " +String.valueOf(risk_percent));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dialog = new AlertDialog.Builder(COVID19_Forecast.this);
                                    //Replace the output down
                                    sound.speak("Predicted risk percentage: " + risk_percent+"%"+"\nRisk Category: "+risk, TextToSpeech.QUEUE_FLUSH, null);
                                    dialog.setMessage("Predicted risk percentage: " + risk_percent+"%"+"\nRisk Category: "+risk)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = dialog.create();
                                    if (risk.equals("Very High")||risk.equals("High")) {
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                                    } else if(risk.equals("Medium")) {
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                                    } else{
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                                    }
                                    //Setting the title manually
                                    alert.setTitle("RISK PREDICTION RESULT");
                                    alert.show();
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                    dialog.setMessage("Invalid Date!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                    AlertDialog alert = dialog.create();
                                    alert.setTitle("RISK PREDICTION RESULT");
                                    alert.show();
//                                    Result.setText("Error! please check!!!");
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("response", "received that error");

                            }

                        });
                queue.add(jsonObjectRequest);

            }
        });
    }
    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        date_et.setText(dateFormat.format(myCalendar.getTime()));
    }
}
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class COVID19_Forecast extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_covid19_forecast);
//    }
//}