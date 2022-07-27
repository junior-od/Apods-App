package com.adyen.android.assignment.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.SinglePodLayoutBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class FavouritePodsRecyclerAdapter (
    private val context: Context,
    pods: List<AstronomyPicture>,
    listener : PodsListener,
):  RecyclerView.Adapter<FavouritePodsRecyclerAdapter.FavouritePodsViewHolder>() {

    private val listener: PodsListener?
    private var podsList: List<AstronomyPicture>

    init {
        this.listener = listener
        this.podsList = pods
    }

    interface PodsListener {
        fun onFavoritePodClicked(pod: AstronomyPicture)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(podsList: List<AstronomyPicture>) {
        this.podsList = podsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritePodsViewHolder {
        return FavouritePodsViewHolder(
            SinglePodLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int {
        return podsList.size
    }

    override fun onBindViewHolder(
        holder: FavouritePodsViewHolder,
        position: Int
    ) {
        val podModel = podsList[position]
        holder.bind(podModel, position)
    }

    private fun selectedPodClicked(position: Int) {
        val podModel = podsList[position]
        listener?.onFavoritePodClicked(podModel)
    }

    inner class FavouritePodsViewHolder (
        private val binding: SinglePodLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(podModel: AstronomyPicture, position: Int) {
            binding.main.setOnClickListener {
                selectedPodClicked(position)
            }

            podModel.url.let {
                Glide.with(context)
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.podImage)
            }

            podModel.title.let {
                binding.podTitle.text = it
            }

            podModel.date.let {
                binding.podDate.text = it //todo add formatter for time here
            }

        }
    }


}