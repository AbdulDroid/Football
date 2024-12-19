package droid.abdul.football.ui.details.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.FragmentTeamBinding
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.ui.details.adapter.TeamsRecyclerViewAdapter
import droid.abdul.football.ui.models.UiState
import droid.abdul.football.utils.getYear
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeamFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mAdapter: TeamsRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var teamCode = ""
    private var date: String = ""
    private var isDataFetched = false

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamCode = it.getString("code")!!
            date = it.getString("date")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.teamList) {
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            mAdapter = TeamsRecyclerViewAdapter(ArrayList(), listener)
            adapter = mAdapter
        }
        binding.errorView.retryButton.setOnClickListener {
            binding.errorView.errorView.visibility = View.GONE
            getData()
        }
        model.teamUiData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.teamProgress.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    if (state.data.isNotEmpty()) {
                        mAdapter.setItems(state.data)
                        isDataFetched = true
                        binding.errorView.errorView.visibility = View.GONE
                        binding.teamList.visibility = View.VISIBLE
                    } else {
                        binding.teamList.visibility = View.GONE
                        binding.errorView.errorMessage.text = ("No Teams")
                        binding.errorView.errorView.visibility = View.VISIBLE
                    }
                    binding.teamProgress.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.teamList.visibility = View.GONE
                    if (state.message.equals("HTTP 403 ", true)) {
                        binding.errorView.errorMessage.text = ("Access Denied, Required paid plan")
                    } else {
                        binding.errorView.errorMessage.text = state.message
                    }
                    binding.errorView.errorView.visibility = View.VISIBLE
                    Log.e(TAG, state.message)
                }
            }
        }
        getData()
    }

    private fun getData() {
        if (!isDataFetched)
            model.getTeams(teamCode, getYear(date))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onTeamClicked(id: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(code: String, startYear: String) =
            TeamFragment().apply {
                arguments = Bundle().apply {
                    putString("code", code)
                    putString("date", startYear)
                }
            }

        val TAG: String = TeamFragment::class.java.simpleName
    }
}
