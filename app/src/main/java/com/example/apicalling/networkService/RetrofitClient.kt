package com.example.apicalling.networkService

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.BufferedSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private const val TIME_OUT: Long = 120

    private val gson = GsonBuilder().setLenient().create()

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptorLogPrintServerDetails) // log print request body
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            /*.addInterceptor(object : Interceptor {

                override fun intercept(chain: Interceptor.Chain): Response {

                    return try {
                        val resp = chain.proceed(chain.request()) // .newBuilder().build()
                        // Deal with the response code
                        if (resp.code == 200) {
                            try {
                                val myJson = resp.peekBody(2048).string() // peekBody() will not close the response
                               // println(myJson)
                            } catch (e: Exception) {
                                 e.printStackTrace()
                                //Log.e("TAG", "intercept: "+e.printStackTrace() )
                                println("Error parse json from intercept..............")
                            }
                        } else {
                           // println(resp)
                        }
                        return resp

                    } catch (e: IOException) {
                        e.printStackTrace()
                            okhttp3.Response.Builder()
                                .code(418)
                                .request(chain.request())
                                .body(object : ResponseBody() {
                                    override fun contentLength() = 0L
                                    override fun contentType(): MediaType? = null
                                    override fun source(): BufferedSource = Buffer()
                                })
                                .message(e.message ?: e.toString())
                                .protocol(Protocol.HTTP_1_1)
                                .build()
                    }

                }

            })*/
            .build()


    val retrofit: RetrofitInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(AllApi.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(RetrofitInterface::class.java)
    }

    private val interceptorLogPrintServerDetails = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }
    }



}
