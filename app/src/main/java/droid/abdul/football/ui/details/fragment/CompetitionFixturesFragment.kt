package droid.abdul.football.ui.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentCompetitionFixturesBinding
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.ui.home.adapter.FixturesRecyclerViewAdapter
import droid.abdul.football.utils.getDate
import droid.abdul.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CompetitionFixturesFragment : Fragment() {

    private var code: String = ""
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var disposable: Disposable? = null
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
        model.event.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                binding.fixtProgress.visibility = View.VISIBLE
            } else {
                binding.fixtProgress.visibility = View.GONE
            }
        }
        model.fixtUiData.observe(viewLifecycleOwner) {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                binding.errorView.errorView.visibility = View.GONE
                binding.fixtList.visibility = View.VISIBLE

            } else if (it.errorMessage.isNotEmpty()) {
                binding.fixtList.visibility = View.GONE
                binding.errorView.errorMessage.text = it.errorMessage
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getFixtures(code, getDate(Calendar.getInstance().time))
            } else {
                binding.fixtList.visibility = View.GONE
                binding.errorView.errorMessage.text = ("No Internet Connection")
                binding.errorView.retryButton.isEnabled = true
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }.doOnError {
            binding.fixtList.visibility = View.GONE
            binding.errorView.errorMessage.text = ("No Internet Connection")
            binding.errorView.retryButton.isEnabled = true
            binding.errorView.errorView.visibility = View.VISIBLE
        }.subscribe()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         *@param compId ID of the competition to fetch fixtures for
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
