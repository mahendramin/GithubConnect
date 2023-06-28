package com.example.githubconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubconnect.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { searchViewText, _, _ ->
                    if (searchViewText.text.isNotBlank()) {
                        searchBar.text = searchViewText.text
                        searchView.hide()
                        homeViewModel.searchUser(searchView.text.toString())
                    } else Snackbar.make(
                        binding.root,
                        resources.getString(R.string.search_minimum_one_char),
                        Snackbar.LENGTH_SHORT
                    ).setTextColor(resources.getColor(R.color.white, null))
                        .setBackgroundTint(resources.getColor(R.color.red_error, null))
                        .show()
                    false
                }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.searchResult.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }

        homeViewModel.isSearchResultEmpty.observe(viewLifecycleOwner) {
            isSearchResultEmpty(it)
        }
    }

    private fun setupRecyclerView(searchResult: List<ItemsItem>) {
        val rvLayoutManager = LinearLayoutManager(requireContext())
        val userAdapter = UserAdapter(searchResult)
        binding.rvSearch.apply {
            adapter = userAdapter
            layoutManager = rvLayoutManager
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvSearch.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun isSearchResultEmpty(isEmpty: Boolean) {
        binding.rvSearch.visibility = if (isEmpty) View.GONE else View.VISIBLE
        if (isEmpty) {
            Snackbar.make(
                binding.root,
                resources.getString(R.string.search_not_found, binding.searchBar.text.toString()),
                Snackbar.LENGTH_SHORT
            ).setTextColor(resources.getColor(R.color.white, null))
                .setBackgroundTint(resources.getColor(R.color.red_error, null))
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
