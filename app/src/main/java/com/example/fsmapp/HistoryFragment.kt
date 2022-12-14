package com.example.fsmapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fsmapp.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
    companion object {
        var articleList: ArrayList<NewsResult.Article> = ArrayList()
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val db = MainActivity.db
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        activity = requireActivity() as MainActivity
        super.onViewCreated(view, savedInstanceState)

        articleList = ArrayList(db.readArticles().reversed())
        binding.recyclerViewHist.adapter = RecyclerAdapter(articleList) //idk change adapter if needed
        binding.recyclerViewHist.layoutManager = LinearLayoutManager(requireContext())

        binding.clearHistBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setView(this.layoutInflater.inflate(R.layout.options_dialog, binding.mainView2, false))
            builder.setTitle("Clear History")
            builder.setPositiveButton("Ok", { dialogInterface, i ->
                db.clearHistory("articles")
                articleList.clear()
                binding.recyclerViewHist.adapter!!.notifyDataSetChanged()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewHist.adapter!!.notifyDataSetChanged()
    }
}