package com.example.apicalling.networkService


object AllApi {

    private external fun baseUrlFromJNI(): String

//    https://reqres.in/api/users?page=2
    val BASE_URL = " https://reqres.in/"


    const val users = "api/users"

}
