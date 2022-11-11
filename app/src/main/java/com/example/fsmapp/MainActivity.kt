package com.example.fsmapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.fsmapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var db: Database
    }

    private lateinit var binding: ActivityMainBinding
    private val textArray = arrayOf(R.string.history, R.string.search, R.string.history)
    public val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private lateinit var viewModel: MainViewModel
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = OkHttpClient()
        getSources("https://newsapi.org/v2/sources?q=climate&sortBy=popularity&sources=abc-news&apiKey=ac6a109a7e764a83bbc8836e8f79cb2b")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.whateverbutton.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
           //     run("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=micronesia&api-key=gexbmadHEPpVdjshvrGfyVGOtn4i2Flu")
         //   print("valls2")
            //var result2 = Klaxon().parse<NYTResult>(result)
            //print(result2!!.response)
        //}
        setSupportActionBar(binding.toolbar)
        client = OkHttpClient()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getSourceData().observe(this, { newValue ->
            var sources = viewModel.getSources()
            Settings.sources = sources!!
        })
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(textArray[position])
            //tab.icon = getDrawable(iconArray[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dark_mode -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun run(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()
        var result: String = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")


                        result = response.body!!.string()
                        println(result)
                        var result2 = Gson().fromJson(result, NewsResult.NewsResult2::class.java)
                        println(result2.articles)
                        viewModel.getDocData()
                            .postValue(result2.articles as ArrayList<NewsResult.Article>?)
                        println("valls")



                }

            }
        })
        return result
    }
    fun getSources(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()
        var result: String = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")


                    result = response.body!!.string()
                    println(result)
                    var result2 = Gson().fromJson(result, Source.Source::class.java)

                    viewModel.getSourceData()
                        .postValue(result2.sources)
                    println("valls")



                }

            }
        })
        return result
    }
}



