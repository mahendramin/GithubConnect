package com.example.githubconnect.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubconnect.R
import com.example.githubconnect.data.remote.response.ItemsItem
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
        setupMenu()
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
            it.getContentIfNotHandled()?.let { isResultEmpty ->
                isSearchResultEmpty(isResultEmpty, binding.searchBar.text.toString())
            }
        }
    }

    private fun setupRecyclerView(searchResult: List<ItemsItem>) {
        val rvLayoutManager = LinearLayoutManager(requireContext())
        val userAdapter = UserAdapter(searchResult) {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailUserFragment(
                    it.username
                )
            )
        }
        binding.rvSearch.apply {
            adapter = userAdapter
            layoutManager = rvLayoutManager
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvSearch.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun isSearchResultEmpty(isEmpty: Boolean, searchInput: String) {
        if (isEmpty) {
            val alertBuilder = AlertDialog.Builder(requireContext()).apply {
                setTitle(resources.getString(R.string.empty_search_result))
                setCancelable(false)
                setMessage("$searchInput Not Found")
                setPositiveButton(getString(android.R.string.ok), null)
            }
            alertBuilder.show()
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
                        true
                    }
                    R.id.favorite -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
