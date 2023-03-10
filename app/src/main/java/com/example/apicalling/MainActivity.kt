package com.example.apicalling

import android.annotation.SuppressLint
import android.net.InetAddresses
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apicalling.databinding.ActivityMainBinding
import com.example.apicalling.model.ItemsModel
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.repository.ItemRepository
import com.example.apicalling.utils.viewBinding
import com.example.apicalling.viewModel.ItemViewModel
import com.example.apicalling.viewModel.ItemViewModelFactory
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {


    private var result: ItemsModel? = null

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this, ItemViewModelFactory(ItemRepository()))[ItemViewModel::class.java]
    }
    private val binding by viewBinding(ActivityMainBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        itemViewModel.getFlowerList(binding.etNo.text.toString())
//        binding.textview.text = getIPAddress(true)
        binding.refresh.setOnClickListener {
            itemViewModel.getFlowerList(binding.etNo.text.toString())


            /* Ipfy.getInstance().getPublicIpObserver().observe(this) { ipData ->
                 ipData.currentIpAddress // this is a value which is your current public IP address, null if no/lost internet connection
                 ipData.lastStoredIpAddress // this is a previous IP address while network lost/reconnected and current IP address assigned to null/new one

                 binding.textview.text = ipData.currentIpAddress
             }*/
            myFunction()

//            Toast.makeText(this, ""+getLocalIpAddress().toString(), Toast.LENGTH_SHORT).show()
        }



        collect()

    }

    private suspend fun getMyPublicIpAsync(): Deferred<String> =
        coroutineScope {
            async(Dispatchers.IO) {
                var result = ""
                result = try {
                    val url = URL("https://api.ipify.org")
                    val httpsURLConnection = url.openConnection()
                    val iStream = httpsURLConnection.getInputStream()
                    val buff = ByteArray(1024)
                    val read = iStream.read(buff)
                    String(buff, 0, read)
                } catch (e: Exception) {
                    "error : $e"
                }
                return@async result
            }
        }


    private fun myFunction() {
        CoroutineScope(Dispatchers.Main).launch {
            val myPublicIp = getMyPublicIpAsync().await()

            if (isIpValid(myPublicIp))
//                binding.textview.text = myPublicIp
            else
                Toast.makeText(this@MainActivity, myPublicIp, Toast.LENGTH_LONG).show()
        }

    }

    fun isIpValid(ip: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InetAddresses.isNumericAddress(ip)
        } else {
            Patterns.IP_ADDRESS.matcher(ip).matches()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun collect() {
        lifecycleScope.launchWhenCreated {

            itemViewModel.wMessage.collect {
                when (it) {
                    is ApiState.Loading -> {
                        Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Success -> {
                        result = it.data as ItemsModel
                        binding.textview.text = result?.ip
                        /*Toast.makeText(
                            this@MainActivity,
                            "" + result?.total.toString(),
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }

                    is ApiState.Failure -> {

                        if (it.e is HttpException && (it.e.code() == 400 || it.e.code() == 404 || it.e.code() == 500)) {

                            val responseBody = it.e.response()?.errorBody()?.string()
                            val jsonObject = responseBody?.trim()?.let { it1 -> JSONObject(it1) }

                            val message = jsonObject?.getString("message")
                            val error = jsonObject?.getString("error")
                            val status = jsonObject?.getString("status")
                            val statusCode = jsonObject?.getString("statusCode")

                            binding.textview.text = "$message \n$error \n $status \n $statusCode"

                        }
                    }


                    is ApiState.Empty -> {
                        binding.textview.text = it.toString() // this code
                    }

                }
            }
        }
    }
}

