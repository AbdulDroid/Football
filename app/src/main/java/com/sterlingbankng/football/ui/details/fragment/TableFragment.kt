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
import com.sterlingbankng.football.ui.details.adapter.TableRecyclerViewAdapter
import com.sterlingbankng.football.utils.hasInternetConnection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_table.*
import org.koin.androidx.viewmodel.ext.viewModel

class TableFragment : Fragment() {

    private lateinit var mAdapter: TableRecyclerViewAdapter
    private val model by viewModel<DetailsActivityViewModel>()
    private var compId = 0
    private var isDataFetched = false
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            compId = it.getLong("id").toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(table_list) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            mAdapter = TableRecyclerViewAdapter(ArrayList())
            adapter = mAdapter
        }
        retryButton.setOnClickListener {
            error_view.visibility = View.GONE
            getData()
        }
        model.event.observe(this, Observer {
            if (it.isLoading)
                table_progress.visibility = View.VISIBLE
            else
                table_progress.visibility = View.GONE
        })
        model.tableUiData.observe(this, Observer {
            if (it.result.isNotEmpty()) {
                mAdapter.setItems(it.result)
                isDataFetched = true
                error_view.visibility = View.GONE
                table_list.visibility = View.VISIBLE
            } else if (it.errorMessage.isNotEmpty()) {
                error_message.text = it.errorMessage
                table_list.visibility = View.GONE
                error_view.visibility = View.VISIBLE
            }
        })
        getData()
    }

    private fun getData() {
        disposable = hasInternetConnection().doOnSuccess {
            if (it) {
                if (!isDataFetched)
                    model.getTable(compId.toLong())
            } else {
                error_message.text = ("No Internet Connection")
                table_list.visibility = View.GONE
                error_view.visibility = View.VISIBLE
            }
        }.doOnError {
            error_message.text = ("No Internet Connection")
            table_list.visibility = View.GONE
            error_view.visibility = View.VISIBLE
        }.subscribe()
    }

    companion object {

        @JvmStatic
        fun newInstance(compId: Long) =
            TableFragment().apply {
                arguments = Bundle().apply {
                    putLong("id", compId)
                }
            }
    }
}
