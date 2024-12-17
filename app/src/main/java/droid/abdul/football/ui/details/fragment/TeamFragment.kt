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
        model.event.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                binding.teamProgress.visibility = View.VISIBLE
            } else {
                binding.teamProgress.visibility = View.GONE
            }
        }
        model.teamUiData.observe(viewLifecycleOwner) {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                binding.errorView.errorView.visibility = View.GONE
                binding.teamList.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                binding.teamList.visibility = View.GONE
                if (it.errorMessage.equals("HTTP 403 ", true))
                    binding.errorView.errorMessage.text = ("Access Denied, Required paid plan")
                else
                    binding.errorView.errorMessage.text = it.errorMessage
                binding.errorView.errorView.visibility = View.VISIBLE
                Log.e(TAG, it.errorMessage)
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
