package com.example.fsmapp

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
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

    val key = "ca3244e2e766418fbcdc3a6e391e3a33"
    private lateinit var binding: ActivityMainBinding
    private val textArray = arrayOf(R.string.history, R.string.search, R.string.history)
    public val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private lateinit var viewModel: MainViewModel
    private lateinit var client: OkHttpClient
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Database(this)
        client = OkHttpClient()
        getSources("https://newsapi.org/v2/sources?q=climate&sortBy=popularity&sources=abc-news&apiKey=" + key)
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
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Settings.apikey = (prefs.getString("api", "ca3244e2e766418fbcdc3a6e391e3a33"))!!
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.api_key -> {


                    var builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    var contentView: View = this.layoutInflater.inflate(R.layout.apikey_dialog, binding.mainmainView, false)
                    val apikey = Settings.apikey
                val apiTF = contentView.findViewById<EditText>(R.id.apiTF)
                val currApi = contentView.findViewById<TextView>(R.id.currApi)
                currApi.setText(Settings.apikey)
                    builder.setView(contentView)

                    builder.setTitle("Enter API Key")
                    builder.setPositiveButton("Ok") { dialogInterface, i ->
                        Settings.apikey = apiTF.text.toString()
                        var edit = prefs.edit()
                        edit.putString("api", apiTF.text.toString())
                        edit.commit()
                        getSources("https://newsapi.org/v2/sources?q=climate&sortBy=popularity&sources=abc-news&apiKey=" + Settings.apikey)
                    }
                builder.setNegativeButton("Cancel") { dialogInterface, i -> }
                builder.create().show()



                true
            }
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
                }
            }
        })
        return result
    }

    // call when user clicks on article
    fun historyUpdated(article: NewsResult.Article) {
        HistoryFragment.articleList.add(article)
        db.addArticle(article)
    }
}



