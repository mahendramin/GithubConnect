package com.example.githubconnect.ui.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubconnect.ui.detail.DetailUserFragmentDirections
import com.example.githubconnect.ui.home.UserAdapter
import com.example.githubconnect.data.remote.response.ItemsItem
import com.example.githubconnect.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel: FollowViewModel by viewModels()

    private var position = 0
    private var username = String()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION, 0)
            username = it.getString(ARG_USERNAME, username)
        }
        if (position == 1) {
            followViewModel.getFollowers(username)
        } else {
            followViewModel.getFollowing(username)
        }

        followViewModel.followersList.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }

        followViewModel.followingList.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
    }

    private fun setupRecyclerView(searchResult: List<ItemsItem>) {
        val rvLayoutManager = LinearLayoutManager(requireContext())
        val userAdapter = UserAdapter(searchResult) {
            findNavController().navigate(
                DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                    it.username
                )
            )
        }
        binding.rvFollow.apply {
            adapter = userAdapter
            layoutManager = rvLayoutManager
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
