package com.example.githubconnect.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubconnect.R
import com.example.githubconnect.data.database.FavoriteUser
import com.example.githubconnect.data.remote.response.DetailUserResponse
import com.example.githubconnect.databinding.FragmentDetailUserBinding
import com.example.githubconnect.ui.favorite.FavoriteViewModel
import com.example.githubconnect.utils.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username = String()

    private val detailUserViewModel: DetailUserViewModel by viewModels()

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var favoriteUser: FavoriteUser

//    private val favoriteViewModel by viewModels<FavoriteViewModel> {
//        FavoriteViewModelFactory.getInstance((requireActivity()).application)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        favoriteViewModel = obtainViewModel(activity as AppCompatActivity)
        username = DetailUserFragmentArgs.fromBundle(arguments as Bundle).username

        if (savedInstanceState == null) {
            detailUserViewModel.getDetailUserResponse(username)
        }

        detailUserViewModel.detailUserResponse.observe(viewLifecycleOwner) {
            setUserData(it)
            setFollowData(it)
            favoriteUser = FavoriteUser(it.username, it.avatarUrl, it.type)
        }

        detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        favoriteViewModel.getFavoriteUser(username).observe(viewLifecycleOwner) { favoriteUser ->
            if (favoriteUser != null) {
                binding.fabFavorite.apply {
                    setOnClickListener { favoriteViewModel.delete(favoriteUser) }
                    setImageResource(R.drawable.baseline_favorite_24)
                }
            } else {
                binding.fabFavorite.apply {
                    setOnClickListener {
                        favoriteViewModel.insert(
                            this@DetailUserFragment.favoriteUser
                        )
                    }
                    setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
    }

    private fun setUserData(data: DetailUserResponse) {
        val listInformation = mutableListOf<String>()
        data.location?.let {
            listInformation.add(resources.getString(R.string.detail_live, data.location))
        } ?: listInformation.add(resources.getString(R.string.no_place_attached))
        data.company?.let {
            listInformation.add(resources.getString(R.string.detail_work, data.company))
        } ?: listInformation.add(resources.getString(R.string.no_company_attached))
        listInformation.add(
            resources.getString(
                R.string.detail_total_public_repository,
                data.publicRepos
            )
        )
        binding.personalInformation.apply {
            tvUsername.text = data.username
            tvInformationPlaceholder.text = resources.getString(
                R.string.little_information_placeholder,
                data.name ?: data.username
            )
            Glide.with(requireContext())
                .load(data.avatarUrl)
                .circleCrop()
                .into(imgAvatar)
            data.name?.let {
                tvName.text = it
            } ?: run {
                tvName.visibility = View.GONE
            }
            data.bio?.let {
                tvBio.text = it
            } ?: run {
                tvBio.visibility = View.GONE
            }
            tvInformation.text = listInformation.joinToString("\n")
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

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
                        shareUserInformation()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun shareUserInformation() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                resources.getString(R.string.share_user_information, username.lowercase())
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
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
