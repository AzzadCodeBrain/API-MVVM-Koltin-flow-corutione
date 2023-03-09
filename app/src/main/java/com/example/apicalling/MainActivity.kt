package com.example.apicalling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apicalling.model.ItemsModel
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.viewModel.ItemViewModel
import com.example.apicalling.viewModel.ItemViewModelFactory
import com.example.apicalling.repository.ItemRepository

class MainActivity : AppCompatActivity() {

    private var result: ItemsModel? = null

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this, ItemViewModelFactory(ItemRepository()))[ItemViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        itemViewModel = ViewModelProvider(this, ItemViewModelFactory(ItemRepository()))[ItemViewModel::class.java]


        itemViewModel.getFlowerList("2")

        //collect
        collect()

    }

    private fun collect() {
        lifecycleScope.launchWhenCreated {

            itemViewModel.wMessage.collect {

                when (it) {
                    is ApiState.Loading -> {
                        Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Success -> {
                        result = it as ItemsModel


                    }

                    is ApiState.Failure -> {
                        Toast.makeText(this@MainActivity, "Faild"+it.e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Empty -> {
                        Toast.makeText(this@MainActivity, "Empaty ", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}

