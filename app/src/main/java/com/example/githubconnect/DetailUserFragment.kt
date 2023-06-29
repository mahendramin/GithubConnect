package com.example.githubconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.githubconnect.databinding.FragmentDetailUserBinding

class DetailUserFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

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
        val username = DetailUserFragmentArgs.fromBundle(arguments as Bundle).username

        if (savedInstanceState == null) {
            detailUserViewModel.getDetailUserResponse(username)
        }

        detailUserViewModel.detailUserResponse.observe(viewLifecycleOwner) {
            setUserData(it)
        }

        detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setUserData(data: DetailUserResponse) {
        val listInformation = mutableListOf<String>()
        data.location?.let {
            listInformation.add("Tinggal di $it")
        }
        data.company?.let {
            listInformation.add("Kerja di $it")
        }
        listInformation.add("jml public repo: ${data.publicRepos}")
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
            tvGithubUrl.text = "https://github.com/${data.username}"
            tvInformation.text = listInformation.joinToString()
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
}
