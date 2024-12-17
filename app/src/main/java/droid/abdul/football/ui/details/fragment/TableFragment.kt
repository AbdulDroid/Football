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
import droid.abdul.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class TableFragment : Fragment() {

    private lateinit var mAdapter: TableRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var code = ""
    private var isDataFetched = false
    private var disposable: Disposable? = null

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
        model.event.observe(viewLifecycleOwner) {
            if (it.isLoading)
                binding.tableProgress.visibility = View.VISIBLE
            else
                binding.tableProgress.visibility = View.GONE
        }
        model.tableUiData.observe(viewLifecycleOwner) {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                binding.errorView.errorView.visibility = View.GONE
                binding.tableList.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                binding.errorView.errorMessage.text = it.errorMessage
                binding.tableList.visibility = View.GONE
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getTable(code)
            } else {
                binding.errorView.errorMessage.text = ("No Internet Connection")
                binding.tableList.visibility = View.GONE
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }.doOnError {
            binding.errorView.errorMessage.text = ("No Internet Connection")
            binding.tableList.visibility = View.GONE
            binding.errorView.errorView.visibility = View.VISIBLE
        }.subscribe()
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
