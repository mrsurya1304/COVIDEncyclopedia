package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class StateAdapter (private val statelist:List<Statemodel>) : RecyclerView.Adapter<StateAdapter.StateViewHolder>() {

    class StateViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        val statename:TextView = itemView.findViewById(R.id.india_states)
        val cases:TextView = itemView.findViewById(R.id.state_cases)
        val deceased:TextView = itemView.findViewById(R.id.state_deceased)
        val recovered:TextView = itemView.findViewById(R.id.state_recovered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stateitem,parent,false)
        return StateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val stateData = statelist[position]
        holder.cases.text = stateData.cases.toString()
        holder.statename.text = stateData.state
        holder.deceased.text = stateData.deaths.toString()
        holder.recovered.text = stateData.recovered.toString()

    }

    override fun getItemCount(): Int {
        return statelist.size
    }
}