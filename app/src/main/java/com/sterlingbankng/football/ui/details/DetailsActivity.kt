package com.sterlingbankng.football.ui.details

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.sterlingbankng.football.R
import com.sterlingbankng.football.di.viewmodels.DetailsActivityViewModel
import com.sterlingbankng.football.ui.details.fragment.CompetitionFixturesFragment
import com.sterlingbankng.football.ui.details.fragment.TableFragment
import com.sterlingbankng.football.ui.details.fragment.TeamBottomSheetFragment
import com.sterlingbankng.football.ui.details.fragment.TeamFragment
import com.sterlingbankng.football.utils.getYear
import com.sterlingbankng.football.utils.getYearShort
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.viewModel

class DetailsActivity : AppCompatActivity(), TeamFragment.OnFragmentInteractionListener {

    override fun onTeamClicked(id: Int) {
        viewModel.detailEvent.observe(this, Observer {
            if (it.isLoading) {
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        })
        viewModel.detailUiData.observe(this, Observer {
            Log.e(TAG, "Fired")
            if (it.result != null) {
                if (tBottomSheetFragment == null) {
                    tBottomSheetFragment = TeamBottomSheetFragment.newInstance(it.result!!)
                    tBottomSheetFragment?.isCancelable = false
                    tBottomSheetFragment?.show(supportFragmentManager, tBottomSheetFragment?.tag ?: "")
                } else {
                    tBottomSheetFragment?.updateData(it.result!!)
                }
            } else if (it.errorMessage.isNotEmpty()) {
                Snackbar.make(main_content, it.errorMessage, Snackbar.LENGTH_LONG).show()
            }
        })
        viewModel.getTeam(id.toLong())
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var name = ""
    private var start = ""
    private var end = ""
    private var id = 0
    private var tableFragment: TableFragment? = null
    private var teamFragment: TeamFragment? = null
    private var cFixturesFragment: CompetitionFixturesFragment? = null
    private var tBottomSheetFragment: TeamBottomSheetFragment? = null
    private val viewModel by viewModel<DetailsActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0)
            name = intent.getStringExtra("name")
            start = intent.getStringExtra("start")
            end = intent.getStringExtra("end")
            toolbar.title = if (!getYear(start).equals(getYear(end), true))
                ("$name ${getYear(start)}/${getYearShort(end)}")
            else
                ("$name ${getYear(start)}")
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTableFragment(): TableFragment {
        if (tableFragment == null)
            tableFragment = TableFragment.newInstance(id.toLong())
        return tableFragment!!
    }

    private fun getTeamFragment(): TeamFragment {
        if (teamFragment == null)
            teamFragment = TeamFragment.newInstance(id.toLong(), start)
        return teamFragment!!
    }

    private fun getCFixturesFragment(): CompetitionFixturesFragment {
        if (cFixturesFragment == null)
            cFixturesFragment = CompetitionFixturesFragment.newInstance(id.toLong())
        return cFixturesFragment!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("id", id)
        outState.putString("name", name)
        outState.putString("start", start)
        outState.putString("end", end)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt("id")
            name = savedInstanceState.getString("name")!!
            start = savedInstanceState.getString("start")!!
            end = savedInstanceState.getString("end")!!
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            return when (position) {
                1 -> {
                    getCFixturesFragment()
                }
                2 -> {
                    getTeamFragment()
                }
                else -> {
                    getTableFragment()
                }
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    companion object {
        private val TAG: String = DetailsActivity::class.java.simpleName
    }
}
