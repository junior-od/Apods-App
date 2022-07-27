package com.adyen.android.assignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.ActivityDetailBinding
import com.adyen.android.assignment.ui.commons.BaseActivity
import com.adyen.android.assignment.utils.Constants.IS_FAVOURITE
import com.adyen.android.assignment.utils.Constants.POD
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var pod: AstronomyPicture
    private var isPodFavourite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        enterFullScreen()
        setContentView(binding.root)

        binding.backViewLayout.setOnClickListener {
            onBackPressed()
        }

        intent.getStringExtra(POD).let {
            pod = Gson().fromJson(it, AstronomyPicture::class.java)
            pod.run {
                bindDataToView()
            }

        }

        intent.getBooleanExtra(IS_FAVOURITE, false).let {
            isPodFavourite = it
            isPodFavourite.run {
                binding.favourite
                    .setImageDrawable(
                        getDrawable(if (it) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite_border))

                binding.favourite.visibility = View.VISIBLE
            }
        }

        binding.favourite.setOnClickListener {
            isPodFavourite = !isPodFavourite
            isPodFavourite.let {
                binding.favourite
                    .setImageDrawable(
                        getDrawable(if (it) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite_border))

            }
        }

    }

    private fun bindDataToView() {
        binding.podTitle.text = pod.title
        binding.podDate.text = pod.date //todo add formatter for time here
        binding.podExplanation.text = pod.explanation
        pod.url.let { url ->
            Glide.with(this@DetailActivity)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.podImage)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}