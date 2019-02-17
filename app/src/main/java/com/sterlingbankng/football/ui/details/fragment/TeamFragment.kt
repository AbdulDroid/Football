package com.sterlingbankng.football.ui.details.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.di.viewmodels.DetailsActivityViewModel
import com.sterlingbankng.football.ui.details.adapter.TeamsRecyclerViewAdapter
import com.sterlingbankng.football.utils.getYear
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_team.*
import org.koin.androidx.viewmodel.ext.viewModel

class TeamFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mAdapter: TeamsRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var teamId = 0L;
    private var date: String = ""
    private var isDataFetched = false
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamId = it.getLong("id")
            date = it.getString("date")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(team_list) {
            layoutManager = GridLayoutManager(context!!, 3, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            mAdapter = TeamsRecyclerViewAdapter(ArrayList(), listener)
            adapter = mAdapter
        }
        retryButton.setOnClickListener {
            error_view.visibility = View.GONE
            getData()
        }
        model.event.observe(this, Observer {
            if (it.isLoading) {
                team_progress.visibility = View.VISIBLE
            } else {
                team_progress.visibility = View.GONE
            }
        })
        model.teamUiData.observe(this, Observer {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                error_view.visibility = View.GONE
                team_list.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                team_list.visibility = View.GONE
                if (it.errorMessage.equals("HTTP 403 ", true))
                    error_message.text = ("Access Denied, Required paid plan")
                else
                    error_message.text = it.errorMessage
                error_view.visibility = View.VISIBLE
                Log.e(TAG, it.errorMessage)
            }
        })
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getTeams(teamId, getYear(date))
            } else {
                team_list.visibility = View.GONE
                error_message.text = ("No Internet Connection")
                error_view.visibility = View.VISIBLE
            }
        }.doOnError {
            team_list.visibility = View.GONE
            error_message.text = ("No Internet Connection")
            error_view.visibility = View.VISIBLE
        }.subscribe()
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
        if (disposable != null)
            disposable?.dispose()
    }

    interface OnFragmentInteractionListener {
        fun onTeamClicked(id: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(teamId: Long, startYear: String) =
            TeamFragment().apply {
                arguments = Bundle().apply {
                    putLong("id", teamId)
                    putString("date", startYear)
                }
            }

        val TAG: String = TeamFragment::class.java.simpleName
    }
}
