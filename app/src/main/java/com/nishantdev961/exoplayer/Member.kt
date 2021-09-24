package com.nishantdev961.exoplayer

data class Member(
    val name: String,
    val videoUrl:String,
    val search: String
){

    constructor():this ("", "","")
}