package com.example.githubconnect.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.githubconnect.R
import com.example.githubconnect.data.remote.response.DetailUserResponse
import com.example.githubconnect.databinding.FragmentDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username = String()

    private val detailUserViewModel: DetailUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = DetailUserFragmentArgs.fromBundle(arguments as Bundle).username

        if (savedInstanceState == null) {
            detailUserViewModel.getDetailUserResponse(username)
        }

        detailUserViewModel.detailUserResponse.observe(viewLifecycleOwner) {
            setUserData(it)
            setFollowData(it)
        }

        detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setUserData(data: DetailUserResponse) {
        val listInformation = mutableListOf<String>()
        data.location?.let {
            listInformation.add(resources.getString(R.string.detail_live, data.location))
        }
        data.company?.let {
            listInformation.add(resources.getString(R.string.detail_work, data.company))
        }
        listInformation.add(
            resources.getString(
                R.string.detail_total_public_repository,
                data.publicRepos
            )
        )
        binding.apply {
            tvUsername.text = data.username
            Glide.with(requireContext())
                .load(data.avatarUrl)
                .into(imgAvatar)
            data.name?.let {
                tvName.text = it
            } ?: run {
                binding.tvName.visibility = View.GONE
            }
            tvGithubUrl.text = resources.getString(R.string.detail_github_url, data.username)
            tvInformation.text = listInformation.joinToString()
        }
    }

    private fun setFollowData(userDetail: DetailUserResponse) {
        val tabTitlesValue = intArrayOf(userDetail.followers, userDetail.following)
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        sectionsPagerAdapter.username = username
        binding.apply {
            vpFollow.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabFollow, vpFollow) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position], tabTitlesValue[position])
            }.attach()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.group.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }
}
