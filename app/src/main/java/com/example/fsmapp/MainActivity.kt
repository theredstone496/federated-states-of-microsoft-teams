package com.example.fsmapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.beust.klaxon.Klaxon

import com.example.fsmapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import okhttp3.Response
import okhttp3.RequestBody
import wu.seal.jsontokotlin.model.TargetJsonConverter


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var client: OkHttpClient
    private var result = ""
    public val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        client = OkHttpClient()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            print(result)
            result = run("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=micronesia&api-key=gexbmadHEPpVdjshvrGfyVGOtn4i2Flu")
            print("valls2")
            //var result2 = Klaxon().parse<NYTResult>(result)
            //print(result2!!.response)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun post(url: String?, json: String?, callback: Callback?): Call? {

        val body: RequestBody = RequestBody.create(JSON, json!!)
        val request: Request = Request.Builder()
            .url(url!!)
            .post(body)
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(callback!!)
        return call
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

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    result = response.body!!.string()
                    println(result)
                    var result2 = Gson().fromJson(result, NYTResult2.NYTResult3::class.java)
                    println(result2.response.docs)
                    viewModel.getDocData().postValue(result2.response.docs as ArrayList<NYTResult2.Doc>?)
                    println("valls")
                }
            }
        })
        return result
    }

}