package com.mpset.pokerevents.Model

data class ViewProfileEventStatisticsModel(var id:Int, var eventType:String, var location: String, var duration: Long, var buyin: Long
                                           , var cashout: Double, var balance: Double,var date: String)