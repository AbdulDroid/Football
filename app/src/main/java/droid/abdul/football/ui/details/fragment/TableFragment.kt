package droid.abdul.football.ui.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentTableBinding
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.ui.details.adapter.TableRecyclerViewAdapter
import droid.abdul.football.ui.models.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class TableFragment : Fragment() {

    private lateinit var mAdapter: TableRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var code = ""
    private var isDataFetched = false

    private var _binding: FragmentTableBinding? = null
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
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.tableList) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = TableRecyclerViewAdapter(ArrayList())
            adapter = mAdapter
        }
        binding.errorView.retryButton.setOnClickListener {
            binding.errorView.errorView.visibility = View.GONE
            getData()
        }
        model.tableUiData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.tableList.visibility = View.GONE
                    binding.errorView.errorView.visibility = View.GONE
                    binding.tableProgress.visibility = View.VISIBLE
                }
                is UiState.Error -> {
                    binding.errorView.errorMessage.text = state.message
                    binding.tableList.visibility = View.GONE
                    binding.tableProgress.visibility = View.GONE
                    binding.errorView.errorView.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    if (state.data.isNotEmpty()) {
                        mAdapter.setItems(state.data)
                        isDataFetched = true
                        binding.errorView.errorView.visibility = View.GONE
                        binding.tableList.visibility = View.VISIBLE
                    } else {
                        binding.errorView.errorMessage.text = "No data found"
                        binding.tableList.visibility = View.GONE
                        binding.errorView.errorView.visibility = View.VISIBLE
                    }
                    binding.tableProgress.visibility = View.GONE
                }
            }
        }
        getData()
    }

    private fun getData() {
        if (!isDataFetched)
            model.getTable(code)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(code: String) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putString("code", code)
                }
            }
    }
}
