package com.example.githubconnect.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubconnect.databinding.FragmentFavoriteBinding
import com.example.githubconnect.utils.FavoriteViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance((requireActivity()).application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = FavoriteAdapter {
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailUserFragment(
                    it.username
                )
            )
        }
        favoriteViewModel.getAllListFavoriteUser().observe(viewLifecycleOwner) {
            if (it != null) {
                favoriteAdapter.setListFavoriteUser(it)
            }
        }
        setupRecyclerView(favoriteAdapter)
    }

    private fun setupRecyclerView(favoriteAdapter: FavoriteAdapter) {
        val rvLayoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = rvLayoutManager
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
