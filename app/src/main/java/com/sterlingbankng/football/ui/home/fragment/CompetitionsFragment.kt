package com.sterlingbankng.football.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.di.viewmodels.HomeActivityViewModel
import com.sterlingbankng.football.repository.api.Competition
import com.sterlingbankng.football.ui.home.adapter.CompetitionsRecyclerViewAdapter
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_competitions.*
import org.koin.androidx.viewmodel.ext.viewModel

class CompetitionsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val model by viewModel<HomeActivityViewModel>()
    private lateinit var mAdapter: CompetitionsRecyclerViewAdapter
    private var isDataFetched = false
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competitions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.event.observe(this, Observer {
            if (it.isLoading) {
                competition_progress.visibility = View.VISIBLE
            } else {
                competition_progress.visibility = View.GONE
            }
        })
        model.compUiData.observe(this, Observer {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                error_view.visibility = View.GONE
                competition_list.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                competition_list.visibility = View.GONE
                error_message.text = it.errorMessage
                error_view.visibility = View.VISIBLE
            }
        })
        retryButton.setOnClickListener {
            error_view.visibility = View.GONE
            getData()
        }
        with(competition_list) {
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
            competition_list.visibility = View.GONE
            error_message.text = ("No Internet Connection")
            retryButton.isEnabled = true
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

    companion object {
        val TAG: String = CompetitionsFragment::class.java.simpleName
    }
}
