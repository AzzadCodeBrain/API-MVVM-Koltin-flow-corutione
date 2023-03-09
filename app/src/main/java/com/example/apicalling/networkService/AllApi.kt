package com.example.apicalling.networkService


object AllApi {

    private external fun baseUrlFromJNI(): String

//    https://reqres.in/api/users?page=2  https://sanatan.coddedbrain.in/api/mock/users
    val BASE_URL = " https://sanatan.coddedbrain.in/"


    const val users = "api/mock/users"

}
