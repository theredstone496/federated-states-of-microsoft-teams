package com.example.fsmapp

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.fsmapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment(), MultiSelectionSpinnerAdapter.MultipleSelectionCallBack {

    private var _binding: FragmentSearchBinding? = null
    val items = ArrayList<String>()
    lateinit var multiSpinner: Spinner
    lateinit var  multiSelectionSpinnerAdapter : MultiSelectionSpinnerAdapter
    private var selectedItemsList: java.util.ArrayList<String> = java.util.ArrayList()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    private lateinit var activity: MainActivity
    private var articleList: ArrayList<ArrayList<String>> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchView = _binding!!.searchView
        for (source in Settings.sources) {
            items.add(source.name)
        }
        multiSpinner = _binding!!.multiSpinner as Spinner
        multiSelectionSpinnerAdapter = MultiSelectionSpinnerAdapter(requireContext(), items, this)
        multiSpinner.adapter = multiSelectionSpinnerAdapter
        viewModel.getSourceData().observe(this, { newValue ->
            for (source in viewModel.getSources()!!) {
                items.add(source.name)
            }
            multiSpinner = _binding!!.multiSpinner as Spinner
            multiSelectionSpinnerAdapter = MultiSelectionSpinnerAdapter(requireContext(), items, this)
            multiSpinner.adapter = multiSelectionSpinnerAdapter
            multiSelectionSpinnerAdapter.notifyDataSetChanged()
        })
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
        activity = requireActivity() as MainActivity
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = RecyclerAdapter(articleList) //idk change adapter if needed
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getDocData().observe(viewLifecycleOwner
        ) { newValue ->
            articleList.clear()
            var articles = viewModel.getDocs()!!
            for (article: NewsResult.Article in articles) {
                var articleinfo = ArrayList<String>()
                articleinfo.add(article.title)
                articleinfo.add(article.author)
                articleinfo.add(article.source.name)
                articleinfo.add(article.urlToImage)
                articleinfo.add(article.publishedAt)
                articleList.add(articleinfo)
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
        activity.run("https://newsapi.org/v2/everything?q=" + Settings.query + "&sortBy=" + Settings.sortBy + "&apiKey=ac6a109a7e764a83bbc8836e8f79cb2b")
    }
    override fun onItemSelect(isSelect: Boolean, selectedItemText: String, position: Int) {
        if(isSelect){
            selectedItemsList.add(Settings.sources[position].id)
        }else{
            selectedItemsList.remove(Settings.sources[position].id)
        }
        multiSelectionSpinnerAdapter.setSelectedItemsList(selectedItemsList)

    }
}