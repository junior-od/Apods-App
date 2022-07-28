package com.adyen.android.assignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyen.android.assignment.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.ActivityPodsBinding
import com.adyen.android.assignment.databinding.ReorderlistDialogBinding
import com.adyen.android.assignment.ui.adapters.FavouritePodsRecyclerAdapter
import com.adyen.android.assignment.ui.adapters.PodsRecyclerAdapter
import com.adyen.android.assignment.ui.dialogs.ErrorDialog
import com.adyen.android.assignment.ui.interfaces.RefreshCallbackListener
import com.adyen.android.assignment.ui.viewmodels.PodsViewModel
import com.adyen.android.assignment.utils.Constants
import com.adyen.android.assignment.utils.Constants.IS_FAVOURITE
import com.adyen.android.assignment.utils.Constants.POD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodsActivity : AppCompatActivity(), PodsRecyclerAdapter.PodsListener,
    FavouritePodsRecyclerAdapter.PodsListener, RefreshCallbackListener {
    private lateinit var binding: ActivityPodsBinding
    private val podsViewModel: PodsViewModel by viewModels()

    private var podsRecycleAdapter: PodsRecyclerAdapter? = null
    private var favouritePodsRecyclerAdapter: FavouritePodsRecyclerAdapter? = null

    private lateinit var refreshResult: ActivityResultLauncher<Intent>
    private lateinit var detailBackResult: ActivityResultLauncher<Intent>

    private lateinit var bindingReorderDialog: ReorderlistDialogBinding
    private lateinit var reorderAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPodsBinding.inflate(layoutInflater)


        setContentView(binding.root)

        setupReorderListModal()

        setupRecyclerViews()

        getLatestPods()

        observeFilteredData()

        contractCallbackForRefreshClicked()

        contractCallbackForDetailBackClicked()

        bindingReorderDialog.reset.setOnClickListener {
            resetFilterValues()
            applyFilter()
            reorderAlertDialog.dismiss()
        }

        bindingReorderDialog.applyBtn.setOnClickListener {
            applyFilter()
            reorderAlertDialog.dismiss()
        }

        bindingReorderDialog.title.setOnClickListener {
            resetFilterValues()
        }

        bindingReorderDialog.date.setOnClickListener {
            bindingReorderDialog.title.isChecked = false
            bindingReorderDialog.date.isChecked = true
        }

        binding.reorderList.setOnClickListener {
            reorderAlertDialog.show()
        }

    }

    private fun applyFilter() {
        var filterBy: Constants.PodsFilter = Constants.PodsFilter.TITLE
        if (bindingReorderDialog.title.isChecked) {
            filterBy = Constants.PodsFilter.TITLE
        }

        if (bindingReorderDialog.date.isChecked) {
            filterBy = Constants.PodsFilter.DATE
        }

        podsViewModel.applyFilter(filterBy)
    }

    private fun observeFilteredData() {
        lifecycleScope.launchWhenStarted {
            podsViewModel.filterLatestAndFavourites.collect{
                event -> when (event) {
                    is  PodsViewModel.PodsEvent.Success -> {
                        podsRecycleAdapter?.updateItems(event.latestPods)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun resetFilterValues() {
        bindingReorderDialog.title.isChecked = true
        bindingReorderDialog.date.isChecked = false
    }

    private fun setupReorderListModal() {
        bindingReorderDialog = ReorderlistDialogBinding.inflate(layoutInflater)
        //initialize builder
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setView(bindingReorderDialog.root)
        reorderAlertDialog = alert.create()
    }

    private fun getLatestPods() {
        podsViewModel.fetchLatest()

        lifecycleScope.launchWhenStarted {
            podsViewModel.fetchLatestAndFavourites.collect{
                    event ->
                when (event) {
                    is PodsViewModel.PodsEvent.Loading -> {
                        binding.loadingBar.visibility = View.VISIBLE
                        hideViewsOnLoad()
                    }

                    is PodsViewModel.PodsEvent.Success -> {
                        showViewsAfterSuccessfulLoad(event.favouritePods)
                        initializeLatestPodsAdapter(event.latestPods, event.favouritePods)

                    }

                    is PodsViewModel.PodsEvent.Error -> {
                        hideLoadingBar()
                        hideViewsOnLoad()

                        showRefreshView()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun contractCallbackForRefreshClicked() {
        /* contract callback to receive on refresh result clicked
        here from the refresh activity screen */
        refreshResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {
            it?.let {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { _ ->
                        podsViewModel.fetchLatest()
                    }
                }

            }
        }
    }

    private fun contractCallbackForDetailBackClicked(){
        /* contract callback to check and update list if favourite was set/unset
           on details back button clicked
         here from the detail activity screen */
        detailBackResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {
            it?.let {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { _ ->
                        //todo function to update list and display latest and favourites if any
                    }
                }
            }
        }


    }

    private fun showRefreshView() {
//        val intent = Intent(this, ErrorActivity::class.java)
//        refreshResult.launch(intent)
        val dialogFragment = ErrorDialog(this)
        dialogFragment.show(supportFragmentManager, "signature")

    }

    private fun hideLoadingBar(){
        binding.loadingBar.visibility = View.GONE
    }

    private fun hideViewsOnLoad() {
        binding.content.visibility = View.GONE
        binding.favoritesLayout.visibility = View.GONE
        binding.reorderList.visibility = View.GONE
    }

    private fun showViewsAfterSuccessfulLoad(favouritePods: List<AstronomyPicture>) {
        hideLoadingBar()
        binding.content.visibility = View.VISIBLE
        binding.favoritesLayout.visibility = if (favouritePods.isNotEmpty()) View.VISIBLE else View.GONE
        binding.reorderList.visibility = View.VISIBLE
    }

    private fun setupRecyclerViews() {
        val layoutManager = LinearLayoutManager(this)
        binding.latestRecyclerView.layoutManager = layoutManager

        val layoutManager1 = LinearLayoutManager(this)
        binding.favouriteRecyclerView.layoutManager = layoutManager1
    }

    private fun initializeLatestPodsAdapter(latestPods: List<AstronomyPicture>, favouritePods: List<AstronomyPicture>) {
        podsRecycleAdapter = PodsRecyclerAdapter(this@PodsActivity, latestPods, this@PodsActivity)
        binding.latestRecyclerView.adapter = podsRecycleAdapter

        favouritePodsRecyclerAdapter = FavouritePodsRecyclerAdapter(this@PodsActivity, favouritePods, this@PodsActivity)
        binding.favouriteRecyclerView.adapter = favouritePodsRecyclerAdapter
    }

    private fun viewPodDetails(pod: AstronomyPicture, isFavourite: Boolean) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(POD, pod)
        intent.putExtra(IS_FAVOURITE, isFavourite)
        detailBackResult.launch(intent)
    }

    override fun onPodsClicked(pod: AstronomyPicture) {
        viewPodDetails(pod, false)
    }

    override fun onFavoritePodClicked(pod: AstronomyPicture) {
        viewPodDetails(pod, true)
    }

    override fun onRefresh() {
        podsViewModel.fetchLatest()
    }

}