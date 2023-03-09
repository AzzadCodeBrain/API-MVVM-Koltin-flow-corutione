package com.example.apicalling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.apicalling.databinding.ActivityMainBinding
import com.example.apicalling.model.ItemsModel
import com.example.apicalling.networkService.ApiState
import com.example.apicalling.repository.ItemRepository
import com.example.apicalling.utils.viewBinding
import com.example.apicalling.viewModel.ItemViewModel
import com.example.apicalling.viewModel.ItemViewModelFactory
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {


    private var result: ItemsModel? = null

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this, ItemViewModelFactory(ItemRepository()))[ItemViewModel::class.java]
    }
    private val binding by viewBinding(ActivityMainBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



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
                        result = it.data as ItemsModel
                        binding.textview.text = result?.data.toString()
                        Toast.makeText(this@MainActivity, ""+result?.total.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Failure -> {
                        binding.textview.text = it.e.message
                        Toast.makeText(this@MainActivity, "Faild  -> ${it.e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }

                    is ApiState.Empty ->{
                        binding.textview.text = it.toString()
                        Toast.makeText(this@MainActivity, "Empaty $it", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}

