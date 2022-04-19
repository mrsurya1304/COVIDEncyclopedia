package com.example.androidproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class COVID19_Forecast extends AppCompatActivity {
    public Button predict;
    EditText place, date;
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
        date = findViewById(R.id.date);
        predict = findViewById(R.id.predict);
//        Result = findViewById(R.id.result);
        progressBar = findViewById(R.id.progressBar);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i1 = place.getText().toString();
                String i2 = date.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                String url = "https://covidencyclopedia1.herokuapp.com/?a=" + i1 + "&b=" + i2;
                RequestQueue queue = Volley.newRequestQueue(COVID19_Forecast.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("response", "onResponse: " + response.getString("prediction"));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dialog = new AlertDialog.Builder(COVID19_Forecast.this);
                                    //Replace the output down
                                    sound.speak("Predicted action is " + response.getString("prediction"), TextToSpeech.QUEUE_FLUSH, null);
                                    dialog.setMessage("Predicted action: " + response.getString("prediction"))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = dialog.create();
//                                    if (response.getString("prediction").startsWith("Patient is safe")) {
//                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
//                                    } else {
//                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
//                                    }
                                    //Setting the title manually
                                    alert.setTitle("*** PREDICTION RESULT ***");
                                    alert.show();
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
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