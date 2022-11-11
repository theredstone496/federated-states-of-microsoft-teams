package com.example.fsmapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fsmapp.databinding.FragmentFirstBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var client: OkHttpClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        client = OkHttpClient()




        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            var docs = viewModel.getDocs()
            binding.textviewFirst.text = docs.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        super.onActivityCreated(savedInstanceState)

    }
}