package com.adyen.android.assignment.ui.ouruniverse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adyen.android.assignment.R
import com.adyen.android.assignment.data.remote.api.model.AstronomyPicture
import com.adyen.android.assignment.databinding.FragmentPodsBinding
import com.adyen.android.assignment.databinding.ReorderlistDialogBinding
import com.adyen.android.assignment.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodsFragment : Fragment(), PodsRecyclerAdapter.PodsListener,
    FavouritePodsRecyclerAdapter.PodsListener {
    private var _binding: FragmentPodsBinding? = null
    private val binding get() = _binding!!

    private val podsViewModel: PodsViewModel by activityViewModels()

    private var podsRecycleAdapter: PodsRecyclerAdapter? = null
    private var favouritePodsRecyclerAdapter: FavouritePodsRecyclerAdapter? = null

    private lateinit var bindingReorderDialog: ReorderlistDialogBinding
    private lateinit var reorderAlertDialog: AlertDialog

    private var hasFetchLatest = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPodsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupReorderListModal()
        retainFilterValues()

        setupRecyclerViews()
        initializeLatestPodsAdapter()

        if (!hasFetchLatest) {
            podsViewModel.fetchLatest()
            refreshPodsObserver()
        } else {
            podsViewModel.pinFavourite()
        }

        getLatestPods()


        binding.content.isNestedScrollingEnabled = true

        bindingReorderDialog.reset.setOnClickListener {
            setTitleChecked()
            applyFilter()
            reorderAlertDialog.dismiss()
        }

        bindingReorderDialog.applyBtn.setOnClickListener {
            applyFilter()
            reorderAlertDialog.dismiss()
        }

        bindingReorderDialog.title.setOnClickListener {
            setTitleChecked()
        }

        bindingReorderDialog.date.setOnClickListener {
            setDateChecked()
        }

        binding.reorderList.setOnClickListener {
            reorderAlertDialog.show()
        }
    }

    private fun retainFilterValues(){
        when(podsViewModel.getFilterBy()){
            Constants.PodsFilter.TITLE -> {
                setTitleChecked()
            }

            Constants.PodsFilter.DATE -> {
                setDateChecked()
            }

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

    private fun setTitleChecked() {
        bindingReorderDialog.title.isChecked = true
        bindingReorderDialog.date.isChecked = false
    }

    private fun setDateChecked() {
        bindingReorderDialog.title.isChecked = false
        bindingReorderDialog.date.isChecked = true
    }

    private fun setupReorderListModal() {
        bindingReorderDialog = ReorderlistDialogBinding.inflate(layoutInflater)
        //initialize builder
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alert.setView(bindingReorderDialog.root)
        reorderAlertDialog = alert.create()
    }

    private fun setupRecyclerViews() {
        val layoutManager = LinearLayoutManager(context)
        binding.latestRecyclerView.layoutManager = layoutManager

        val layoutManager1 = LinearLayoutManager(context)
        binding.favouriteRecyclerView.layoutManager = layoutManager1

    }

    private fun initializeLatestPodsAdapter() {
        podsRecycleAdapter = context?.let { PodsRecyclerAdapter(it, ArrayList(), this@PodsFragment) }
        binding.latestRecyclerView.adapter = podsRecycleAdapter

        favouritePodsRecyclerAdapter =
            context?.let { FavouritePodsRecyclerAdapter(it, ArrayList(), this@PodsFragment) }
        binding.favouriteRecyclerView.adapter = favouritePodsRecyclerAdapter
    }

    private fun showRefreshView(){
        findNavController().navigate(R.id.action_podsFragment_to_errorDialog)
    }

    private fun refreshPodsObserver(){
        lifecycleScope.launchWhenStarted {
            podsViewModel.refreshList.collect {
                when (it) {
                    is PodsViewModel.PodsEvent.Refresh -> {
                        podsViewModel.fetchLatest()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getLatestPods() {


        lifecycleScope.launchWhenStarted {
            podsViewModel.fetchLatestAndFavourites.collect{
                    event ->
                when (event) {
                    is PodsViewModel.PodsEvent.Loading -> {
                        binding.loadingBar.visibility = View.VISIBLE
                        hideViewsOnLoad()
                    }

                    is PodsViewModel.PodsEvent.Success -> {
                        hasFetchLatest = true
                        showViewsAfterSuccessfulLoad(event.favouritePods)
                        podsRecycleAdapter?.updateItems(event.latestPods)
                        favouritePodsRecyclerAdapter?.updateItems(event.favouritePods)

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

    private fun showViewsAfterSuccessfulLoad(favouritePods: List<AstronomyPicture>) {
        hideLoadingBar()
        binding.content.visibility = View.VISIBLE
        binding.favoritesLayout.visibility = if (favouritePods.isNotEmpty()) View.VISIBLE else View.GONE
        binding.reorderList.visibility = View.VISIBLE
    }

    private fun hideLoadingBar(){
        binding.loadingBar.visibility = View.GONE
    }

    private fun hideViewsOnLoad() {
        binding.content.visibility = View.GONE
        binding.favoritesLayout.visibility = View.GONE
        binding.reorderList.visibility = View.GONE
    }

    private fun goToPodDetails(isFavourite: Boolean, pod: AstronomyPicture) {
        val action =
            PodsFragmentDirections.actionPodsFragmentToPodsDetailsFragment(
                isFavourite,
                pod
            )
        findNavController().navigate(action)

    }

    override fun onFavoritePodClicked(pod: AstronomyPicture) {

        goToPodDetails(true, pod)
    }

    override fun onPodsClicked(pod: AstronomyPicture) {

        goToPodDetails(false, pod)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}