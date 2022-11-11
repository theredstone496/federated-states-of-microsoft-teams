package com.example.fsmapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fsmapp.databinding.FragmentSearchBinding
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    val items = ArrayList<String>()
    lateinit var multiSpinner: Spinner
    lateinit var  multiSelectionSpinnerAdapter : MultiSelectionSpinnerAdapter
    private var selectedItemsList: java.util.ArrayList<String> = java.util.ArrayList()
    // This property is only valid between onCreateView and
    // onDestroyView.
    var selectedLanguage: BooleanArray? = null
    var langList: ArrayList<Int> = ArrayList()
    var langArray = arrayOf<String>()
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    private lateinit var activity: MainActivity
    private var articleList: ArrayList<NewsResult.Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchView = _binding!!.searchView
        for (source in Settings.sources) {
            items.add(source.name)
        }
        selectedLanguage = BooleanArray(14, init = {i -> true})


        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Settings.query = query
                    search()
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {

                    return false
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            for (source in viewModel.getSources()!!) {
                if (!langArray.contains(source.language)) {
                    langArray = langArray.plus(source.language)
                }
            }
            println(langArray.size)
            _binding!!.sourceButton.setOnClickListener(View.OnClickListener { // Initialize alert dialog
                val builder = AlertDialog.Builder(requireContext())

                // set title
                builder.setTitle("Select Language")

                // set dialog non cancelable
                builder.setCancelable(false)

                builder.setMultiChoiceItems(langArray, selectedLanguage,
                    OnMultiChoiceClickListener { dialogInterface, i, b ->
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i)
                            Settings.langs[i] = true
                            // Sort array list
                            Collections.sort(langList)
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            Settings.langs[i] = false
                            langList.remove(Integer.valueOf(i))
                        }
                    })
                builder.setPositiveButton(
                    "OK"
                ) { dialogInterface, i -> // Initialize string builder
                    val stringBuilder = StringBuilder()
                    // use for loop
                    for (j in 0 until langList.size) {
                        // concat array value
                        stringBuilder.append(langArray.get(langList.get(j)))
                        // check condition
                        if (j != langList.size - 1) {
                            // When j value  not equal
                            // to lang list size - 1
                            // add comma
                            stringBuilder.append(", ")
                        }
                    }
                    // set text on textView

                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialogInterface, i -> // dismiss dialog
                    dialogInterface.dismiss()
                }

                // show dialog
                builder.show()
            })


        activity = requireActivity() as MainActivity
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = RecyclerAdapter(articleList) //idk change adapter if needed
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getDocData().observe(viewLifecycleOwner
        ) { newValue ->
            articleList.clear()
            var articles = viewModel.getDocs()!!
            for (article: NewsResult.Article in articles) {
                articleList.add(article)
            }
            var adapter = binding.recyclerView.adapter
            adapter!!.notifyDataSetChanged()
        }

        binding.sortButton.setOnClickListener { view ->
            var builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
            var contentView: View = this.layoutInflater.inflate(R.layout.sort_dialog, binding.mainView, false)
            val sortBtnR: RadioButton = contentView.findViewById(R.id.sortBtnR)
            val sortBtnP: RadioButton = contentView.findViewById(R.id.sortBtnP)
            val sortBtnL: RadioButton = contentView.findViewById(R.id.sortBtnL)
            sortBtnR.isChecked = Settings.sortBy == "relevancy"
            sortBtnP.isChecked = Settings.sortBy == "popularity"
            sortBtnL.isChecked = Settings.sortBy == "publishedAt"
            sortBtnR.setOnClickListener { view ->
                Settings.sortBy = "relevancy"
            }
            sortBtnP.setOnClickListener { view ->
                Settings.sortBy = "popularity"
            }
            sortBtnL.setOnClickListener { view ->
                Settings.sortBy = "publishedAt"
            }
            builder.setView(contentView)
            builder.setTitle("Sort By:")
            builder.setPositiveButton("Ok",{ dialogInterface, i ->  search()})


            builder.create().show()
        }
    }
    fun search() {
        var langlist = ArrayList<String>()
        var sourcelist = ""
        for (i in 0..13) {
            if (Settings.langs[i]) langlist.add(Settings.existentlangs[i])
        }
        for ( source: Source.SourceItem in Settings.sources) {
            if (source.language in langlist) sourcelist += (source.id + ",")
        }
        sourcelist = sourcelist.substring(0,sourcelist.length-2)
        print("https://newsapi.org/v2/everything?q=" + Settings.query + "&sortBy=" + Settings.sortBy + "&sources=" + sourcelist + "&apiKey=ac6a109a7e764a83bbc8836e8f79cb2b")
        activity.run("https://newsapi.org/v2/everything?q=" + Settings.query + "&sortBy=" + Settings.sortBy + "&sources=" + sourcelist + "&apiKey=ac6a109a7e764a83bbc8836e8f79cb2b")
    }

    // call when user clicks on article
    fun historyUpdated(article: NewsResult.Article) {
        articleList.add(article)
        val adapter = requireView().findViewById<RecyclerView>(R.id.recyclerViewHist).adapter
        adapter!!.notifyItemInserted(-1)
        MainActivity.db.addArticle(article)
    }
}