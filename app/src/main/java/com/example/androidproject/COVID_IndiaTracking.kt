package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.ArrayList

class COVID_IndiaTracking : AppCompatActivity() {
    lateinit var india_cases1:TextView
    lateinit var recovered1:TextView
    lateinit var deceased1:TextView
    lateinit var indiastates:RecyclerView
    lateinit var statesAdapter:StateAdapter
    lateinit var stateList:List<Statemodel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_india_tracking)
        india_cases1 = findViewById(R.id.india_cases)
        recovered1 = findViewById(R.id.recovered)
        deceased1 = findViewById(R.id.deceased)
        indiastates = findViewById(R.id.stateview)
        stateList = ArrayList<Statemodel>()
        getstateinfo()
    }

    private fun getstateinfo(){
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@COVID_IndiaTracking)
        val request =
                JsonObjectRequest(Request.Method.GET,url,null,{ response->
                    try {
                        val dataObj = response.getJSONObject("data")
                        val summaryObj = dataObj.getJSONObject("summary")
                        val cases:Int = summaryObj.getInt("total")
                        val recovered:Int = summaryObj.getInt("discharged")
                        val deaths:Int = summaryObj.getInt("deaths")

                        india_cases1.text = cases.toString()
                        recovered1.text = recovered.toString()
                        deceased1.text = deaths.toString()

                        val regionalArray = dataObj.getJSONArray("regional")
                        for(i in 0 until regionalArray.length()){
                            val regionalObj = regionalArray.getJSONObject(i)
                            val statename:String = regionalObj.getString("loc")
                            val  statecases:Int =  regionalObj.getInt("totalConfirmed")
                            val  staterecovered:Int =  regionalObj.getInt("discharged")
                            val  statedeaths:Int =  regionalObj.getInt("deaths")

                            val stateModel = Statemodel(statename,staterecovered,statedeaths,statecases)
                            stateList = stateList + stateModel
                        }
                        statesAdapter = StateAdapter(stateList)
                        indiastates.layoutManager = LinearLayoutManager(this)
                        indiastates.adapter = statesAdapter
                     }
                    catch (e:JSONException){
                        e.printStackTrace()
                    }
                },{ error->{
                    Toast.makeText(this,"Fail to get data",Toast.LENGTH_SHORT).show()
                }})
                queue.add(request)
    }
}