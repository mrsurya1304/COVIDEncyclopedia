package com.example.androidproject

data class CenterRVModel (
    val centername: String,
    val centeraddress: String,
    val centerfromtime: String,
    val centertotime: String,
    val feetype: String,
    val age: Int,
    var vaccinename: String,
    val availablecapacity: Int
)