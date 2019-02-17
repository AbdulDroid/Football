package com.sterlingbankng.football.ui.details.fragment

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
import com.sterlingbankng.football.di.viewmodels.DetailsActivityViewModel
import com.sterlingbankng.football.ui.home.adapter.FixturesRecyclerViewAdapter
import com.sterlingbankng.football.utils.getDate
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_competition_fixtures.*
import org.koin.androidx.viewmodel.ext.viewModel
import java.util.*

class CompetitionFixturesFragment : Fragment() {

    private var compId: Long = 0L
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var disposable: Disposable? = null
    private var isDataFetched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            compId = it.getLong("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_competition_fixtures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fixt_list) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = FixturesRecyclerViewAdapter(ArrayList())
            adapter = mAdapter
        }
        retryButton.setOnClickListener {
            error_view.visibility = View.GONE
            getData()
        }
        model.event.observe(this, Observer {
            if (it.isLoading) {
                fixt_progress.visibility = View.VISIBLE
            } else {
                fixt_progress.visibility = View.GONE
            }
        })
        model.fixtUiData.observe(this, Observer {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                error_view.visibility = View.GONE
                fixt_list.visibility = View.VISIBLE

            } else if (it.errorMessage.isNotEmpty()) {
                fixt_list.visibility = View.GONE
                error_message.text = it.errorMessage
                error_view.visibility = View.VISIBLE
            }
        })
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getFixtures(compId, getDate(Calendar.getInstance().time))
            } else {
                fixt_list.visibility = View.GONE
                error_message.text = ("No Internet Connection")
                retryButton.isEnabled = true
                error_view.visibility = View.VISIBLE
            }
        }.doOnError {
            fixt_list.visibility = View.GONE
            error_message.text = ("No Internet Connection")
            retryButton.isEnabled = true
            error_view.visibility = View.VISIBLE
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
        fun newInstance(compId: Long) =
            CompetitionFixturesFragment().apply {
                arguments = Bundle().apply {
                    putLong("id", compId)
                }
            }

        val TAG: String = CompetitionFixturesFragment::class.java.simpleName
    }
}
