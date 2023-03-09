package com.example.apicalling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apicalling.model.ItemsModel
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.repository.ItemRepository
import com.example.apicalling.viewModel.ItemViewModel
import com.example.apicalling.viewModel.ItemViewModelFactory
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {


    private var result: ItemsModel? = null

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this, ItemViewModelFactory(ItemRepository()))[ItemViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        itemViewModel.getFlowerList("2")

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
                        val myObj = it.data as ItemsModel

                        Toast.makeText(this@MainActivity, ""+myObj.total.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Failure -> {
                        Toast.makeText(this@MainActivity, "Faild$it", Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Empty ->{
                        Toast.makeText(this@MainActivity, "Empaty $it", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}

