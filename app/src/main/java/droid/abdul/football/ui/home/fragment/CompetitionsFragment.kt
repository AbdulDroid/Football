package droid.abdul.football.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentCompetitionsBinding
import droid.abdul.football.di.viewmodels.HomeActivityViewModel
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.ui.home.adapter.CompetitionsRecyclerViewAdapter
import droid.abdul.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompetitionsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val model by viewModel<HomeActivityViewModel>()
    private lateinit var mAdapter: CompetitionsRecyclerViewAdapter
    private var isDataFetched = false
    private var disposable: Disposable? = null

    private var _binding: FragmentCompetitionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompetitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.event.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                binding.competitionProgress.visibility = View.VISIBLE
            } else {
                binding.competitionProgress.visibility = View.GONE
            }
        }
        model.compUiData.observe(viewLifecycleOwner) {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                binding.errorView.errorView.visibility = View.GONE
                binding.competitionList.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                binding.competitionList.visibility = View.GONE
                binding.errorView.errorMessage.text = it.errorMessage
                binding.errorView.errorView.visibility = View.VISIBLE
            }
        }
        binding.errorView.retryButton.setOnClickListener {
            binding.errorView.errorView.visibility = View.GONE
            getData()
        }
        with(binding.competitionList) {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.VERTICAL
                )
            )
            mAdapter = CompetitionsRecyclerViewAdapter(
                ArrayList(),
                listener
            )
            adapter = mAdapter
        }
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            model.getCompetitions()

        }.doOnError {
            binding.competitionList.visibility = View.GONE
            binding.errorView.errorMessage.text = ("No Internet Connection")
            binding.errorView.retryButton.isEnabled = true
            binding.errorView.errorView.visibility = View.VISIBLE
        }.subscribe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        if (disposable != null)
            disposable?.dispose()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onCompetitionClicked(competition: Competition)
    }
}
