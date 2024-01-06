package com.example.load_more_mo5

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.load_more_mo5.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var list: ArrayList<Data>
    lateinit var adapter: RecyclerViewAdapter
    var notLoading = true

    lateinit var layoutManager: LinearLayoutManager
    lateinit var api: DataAPI
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        adapter = RecyclerViewAdapter(list)

        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        retrofit = RetrofitInstance.getRetrofitInstance()
        api = retrofit.create(DataAPI::class.java)

        load(0)
        addScrollListener()
    }

    private fun addScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                    list.add(Data("progress"))
                    adapter.notifyItemInserted(list.size - 1)
                    notLoading = false

                    val call: Call<List<Data>> = api.getData(list.size - 1)
                    call.enqueue(object : Callback<List<Data>> {
                        override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                            list.removeAt(list.size - 1)
                            adapter.notifyItemRemoved(list.size)

                            if (response.body()!!.isNotEmpty()) {
                                response.body()?.let { list.addAll(it) }
                                adapter.notifyItemChanged(0)
//                                adapter.notifyDataSetChanged()
                                notLoading= true
                            }
                        }

                        override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                            Log.e("loadmore", t.toString() )                        }

                    })
                }
            }
        })
    }

    private fun load(i: Int) {
        val call: Call<List<Data>> = api.getData(i)

        call.enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
//                    response!!.body()?.let { list.addAll(it) }
                    response.body()?.let { list.addAll(it) }
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.e("load more", "onFailure: ")
            }

        })
    }
}