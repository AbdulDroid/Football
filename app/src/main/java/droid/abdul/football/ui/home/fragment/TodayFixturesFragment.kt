package droid.abdul.football.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentTodayFixturesBinding
import droid.abdul.football.di.viewmodels.HomeActivityViewModel
import droid.abdul.football.ui.home.adapter.FixturesRecyclerViewAdapter
import droid.abdul.football.ui.models.UiState
import droid.abdul.football.utils.getDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


class TodayFixturesFragment : Fragment() {

    private val model by viewModel<HomeActivityViewModel>()
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private var isDataFetched = false

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

        model.fixUiData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progress.visibility = View.GONE
                    mAdapter.setItems(state.data)
                    isDataFetched = true
                    binding.errorView.errorView.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                }

                is UiState.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.list.visibility = View.GONE
                    binding.errorView.errorMessage.text = state.message.ifEmpty { "No Fixtures" }
                    binding.errorView.retryButton.isEnabled = false
                    binding.errorView.errorView.visibility = View.VISIBLE
                }
            }
        }
        getData()
    }

    private fun getData() {
        if (!isDataFetched)
            model.getTodayFixtures(getDate(Calendar.getInstance().time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
