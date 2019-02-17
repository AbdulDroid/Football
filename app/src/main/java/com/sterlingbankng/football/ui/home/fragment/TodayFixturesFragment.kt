package com.sterlingbankng.football.ui.home.fragment

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
import com.sterlingbankng.football.ui.home.adapter.FixturesRecyclerViewAdapter
import com.sterlingbankng.football.utils.getDate
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_today_fixtures.*
import org.koin.androidx.viewmodel.ext.viewModel
import java.util.*


class TodayFixturesFragment : Fragment() {

    private val model by viewModel<HomeActivityViewModel>()
    private lateinit var mAdapter: FixturesRecyclerViewAdapter
    private var isDataFetched = false
    private var disposable: Disposable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today_fixtures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(list) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = FixturesRecyclerViewAdapter(
                ArrayList()
            )
            adapter = mAdapter
        }
        retryButton.setOnClickListener {
            error_view.visibility = View.GONE
            getData()
        }
        model.event.observe(this, Observer {
            if (it.isLoading) {
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        })

        model.fixUiData.observe(this, Observer {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                error_view.visibility = View.GONE
                list.visibility = View.VISIBLE
            } else {
                list.visibility = View.GONE
                error_message.text = ("No Fixtures")
                retryButton.isEnabled = false
                error_view.visibility = View.VISIBLE
            }
        })
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getTodayFixtures(getDate(Calendar.getInstance().time))
            } else {
                list.visibility = View.GONE
                error_message.text = ("No Internet Connection")
                retryButton.isEnabled = true
                error_view.visibility = View.VISIBLE
            }
        }.doOnError {
            list.visibility = View.GONE
            error_message.text = ("No Internet Connection")
            retryButton.isEnabled = true
            error_view.visibility = View.VISIBLE
        }.subscribe()
    }

    override fun onDetach() {
        super.onDetach()
        if (disposable != null)
            disposable?.dispose()
    }

    companion object {
        val TAG: String = TodayFixturesFragment::class.java.simpleName
    }
}
