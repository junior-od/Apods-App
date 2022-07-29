package com.adyen.android.assignment.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.SinglePodLayoutBinding
import com.adyen.android.assignment.ui.util.PodsDiffCallback
import com.adyen.android.assignment.utils.DateConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PodsRecyclerAdapter (
    private val context: Context,
    pods: MutableList<AstronomyPicture>,
    listener : PodsListener,
        ):  RecyclerView.Adapter<PodsRecyclerAdapter.PodsViewHolder>() {

    private val listener: PodsListener?
    private var podsList: MutableList<AstronomyPicture>

    init {
        this.listener = listener
        this.podsList = pods
    }

    interface PodsListener {
        fun onPodsClicked(pod: AstronomyPicture)
    }

    fun updateItems(podsList: List<AstronomyPicture>) {

        val diffCallback = PodsDiffCallback(this.podsList, podsList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.podsList.clear()
        this.podsList.addAll(podsList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PodsViewHolder {
        return PodsViewHolder(
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
        holder: PodsViewHolder,
        position: Int
    ) {
        val podModel = podsList[position]
        holder.bind(podModel)
    }

    inner class PodsViewHolder (
        private val binding: SinglePodLayoutBinding
            ): RecyclerView.ViewHolder(binding.root) {

                fun bind(podModel: AstronomyPicture) {
                    binding.main.setOnClickListener {
                        listener?.onPodsClicked(podModel)
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
                        binding.podDate.text = DateConverter.formatDateToDdMmYyyy(it)
                    }

                }
            }


}