package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdapter(private val centerList: List<CenterRVModel>) : RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val centername: TextView= itemView.findViewById(R.id.tv_centername);
        val centeraddress: TextView= itemView.findViewById(R.id.tv_centerlocation);
        val centertiming: TextView= itemView.findViewById(R.id.tv_centertiming);
        val vaccinename: TextView= itemView.findViewById(R.id.tv_vaccinename);
        val vaccinefee: TextView= itemView.findViewById(R.id.tv_fee);
        val age: TextView= itemView.findViewById(R.id.tv_age);
        val vaccineavailability: TextView= itemView.findViewById(R.id.tv_availability);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item,parent,false)
        return CenterRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]
        holder.centername.text = center.centername
        holder.centeraddress.text = center.centeraddress
        holder.centertiming.text = ("From : "+center.centerfromtime+" To : "+center.centertotime)
        holder.vaccinename.text = center.vaccinename
        holder.vaccinefee.text = center.feetype
        holder.age.text = ("Age Limit : "+center.age.toString())
        holder.vaccineavailability.text = ("Availability : "+center.availablecapacity.toString())
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

}