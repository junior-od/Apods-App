package com.adyen.android.assignment.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.FragmentPodsDetailsBinding
import com.adyen.android.assignment.ui.viewmodels.PodsDetailsViewModel
import com.adyen.android.assignment.utils.DateConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodsDetailsFragment : Fragment() {

    private var _binding: FragmentPodsDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PodsDetailsFragmentArgs>()

    private val detailsViewModel: PodsDetailsViewModel by activityViewModels()
    private var isPodFavourite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPodsDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isPodFavourite = args.favourite

        bindDataToView()

        binding.backViewLayout.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.favourite.setOnClickListener { onFavouriteClicked() }
    }

    private fun onFavouriteClicked(){
        if (isPodFavourite) {
            detailsViewModel.removeFavorite(args.pod)
        } else {
            detailsViewModel.addFavorite(args.pod)
        }

        isPodFavourite = !isPodFavourite
        isPodFavourite.let {
            binding.favourite
                .setImageDrawable(
                    getDrawable(requireContext(), if (it) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border))

        }
    }

    private fun bindDataToView() {
        binding.podTitle.text = args.pod.title
        binding.podDate.text = DateConverter.formatDateToDdMmYyyy(args.pod.date)
        binding.podExplanation.text = args.pod.explanation
        args.pod.url.let { url ->
            Glide.with(this@PodsDetailsFragment)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.podImage)
        }

        binding.favourite
            .setImageDrawable(
                getDrawable(requireContext(), if (args.favourite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border))

        binding.favourite.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}