package com.example.apicalling

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import org.json.JSONObject
import retrofit2.HttpException

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

        binding.refresh.setOnClickListener {
            itemViewModel.getFlowerList(binding.etNo.text.toString())
        }



        collect()

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
                        binding.textview.text = result?.data.toString()
                        Toast.makeText(this@MainActivity, ""+result?.total.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Failure -> {

                        if (it.e is HttpException && (it.e.code() == 400 || it.e.code()==404|| it.e.code()==500)){

                            val responseBody = it.e.response()?.errorBody()?.string()
                            val jsonObject = responseBody?.trim() ?.let { it1 -> JSONObject(it1) }

                            val message = jsonObject?.getString("message")
                            binding.textview.text = message

                        }



                        /* val exception = it.e as HttpException
                         if (exception.response()!=null) {
                             when (exception.code()) {
                                 401 -> {
                                     binding.textview.text = exception.response()?.raw()?.code.toString() + "  -> "+exception.response()?.raw()?.message.toString()
                                 }
                                 500 -> {
                                     binding.textview.text = exception.response()?.code().toString() +"   - > "+exception.response()?.message().toString()
                                     binding.textview.text = exception.response()?.code().toString() +"   - > "+exception.response().errorBody()
                                 }
                                 else -> {
                                     Toast.makeText(this@MainActivity, "Something worng", Toast.LENGTH_SHORT).show()
                                 }
                             }
                         }*/

                    }


                    is ApiState.Empty ->{
                        binding.textview.text = it.toString() // this code
                    }

                }
            }
        }
    }
}

