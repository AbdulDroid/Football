package com.sterlingbankng.football.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.databinding.FragmentTodayFixturesBinding
import com.sterlingbankng.football.di.viewmodels.HomeActivityViewModel
import com.sterlingbankng.football.ui.home.adapter.FixturesRecyclerViewAdapter
import com.sterlingbankng.football.utils.getDate
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TodayFixturesFragment : Fragment() {

    private val model by viewModel<HomeActivityViewModel>()
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private var isDataFetched = false
    private var disposable: Disposable? = null

    private var _binding: FragmentTodayFixturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayFixturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = FixturesRecyclerViewAdapter(
                ArrayList()
            )
            adapter = mAdapter
        }
        binding.errorView.retryButton.setOnClickListener {
            binding.errorView.errorView.visibility = View.GONE
            getData()
        }
        model.event.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        }

        model.fixUiData.observe(viewLifecycleOwner) {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                binding.errorView.errorView.visibility = View.GONE
                binding.list.visibility = View.VISIBLE
            } else {
                binding.list.visibility = View.GONE
                binding.errorView.errorMessage.text = ("No Fixtures")
                binding.errorView.retryButton.isEnabled = false
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getTodayFixtures(getDate(Calendar.getInstance().time))
            } else {
                binding.list.visibility = View.GONE
                binding.errorView.errorMessage.text = ("No Internet Connection")
                binding.errorView.retryButton.isEnabled = true
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }.doOnError {
            binding.list.visibility = View.GONE
            binding.errorView.errorMessage.text = ("No Internet Connection")
            binding.errorView.retryButton.isEnabled = true
            binding.errorView.errorView.visibility = View.VISIBLE
        }.subscribe()
    }

    override fun onDetach() {
        super.onDetach()
        if (disposable != null)
            disposable?.dispose()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
