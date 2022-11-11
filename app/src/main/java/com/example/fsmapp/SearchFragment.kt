package com.example.fsmapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fsmapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

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
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    activity.run("https://newsapi.org/v2/everything?q=" + query + "&apiKey=ac6a109a7e764a83bbc8836e8f79cb2b")
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
    }
}