package droid.abdul.football.ui.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentCompetitionFixturesBinding
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.ui.home.adapter.FixturesRecyclerViewAdapter
import droid.abdul.football.ui.models.UiState
import droid.abdul.football.utils.getDate
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CompetitionFixturesFragment : Fragment() {

    private var code: String = ""
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var isDataFetched = false
    private var _binding: FragmentCompetitionFixturesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getString("code")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompetitionFixturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.fixtList) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = FixturesRecyclerViewAdapter(ArrayList())
            adapter = mAdapter
        }
        binding.errorView.retryButton.setOnClickListener {
            binding.errorView.errorView.visibility = View.GONE
            getData()
        }
        model.fixtUiData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.fixtList.visibility = View.GONE
                    binding.errorView.errorView.visibility = View.GONE
                    binding.fixtProgress.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.fixtProgress.visibility = View.GONE
                    if (state.data.isNotEmpty()) {
                        mAdapter.setItems(state.data)
                        isDataFetched = true
                        binding.errorView.errorView.visibility = View.GONE
                        binding.fixtList.visibility = View.VISIBLE
                    } else {
                        binding.fixtList.visibility = View.GONE
                        binding.errorView.errorMessage.text = "No Fixtures"
                        binding.errorView.errorView.visibility = View.VISIBLE
                    }
                    binding.fixtProgress.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.fixtProgress.visibility = View.GONE
                    binding.fixtList.visibility = View.GONE
                    binding.errorView.errorMessage.text = state.message
                    binding.errorView.errorView.visibility = View.VISIBLE
                }
            }
        }
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        lifecycleScope.launch {
            if (!isDataFetched)
                model.getFixtures(code, getDate(Calendar.getInstance().time))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         *@param code ID of the competition to fetch fixtures for
         * @return A new instance of fragment CompetitionFixturesFragment.
         */
        @JvmStatic
        fun newInstance(code: String) =
            CompetitionFixturesFragment().apply {
                arguments = Bundle().apply {
                    putString("code", code)
                }
            }
    }
}
