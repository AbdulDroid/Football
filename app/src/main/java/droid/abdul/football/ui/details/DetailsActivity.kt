package droid.abdul.football.ui.details

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import droid.abdul.football.R
import droid.abdul.football.databinding.ActivityDetailsBinding
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.ui.details.fragment.CompetitionFixturesFragment
import droid.abdul.football.ui.details.fragment.TableFragment
import droid.abdul.football.ui.details.fragment.TeamBottomSheetFragment
import droid.abdul.football.ui.details.fragment.TeamFragment
import droid.abdul.football.utils.getYear
import droid.abdul.football.utils.getYearShort
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity(), TeamFragment.OnFragmentInteractionListener {

    private lateinit var binding: ActivityDetailsBinding

    override fun onTeamClicked(id: Int) {
        viewModel.detailEvent.observe(this) {
            if (it.isLoading) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
            }
        }
        viewModel.detailUiData.observe(this) {
            Log.e(TAG, "Fired")
            if (it.result != null) {
                if (tBottomSheetFragment == null) {
                    tBottomSheetFragment = TeamBottomSheetFragment.newInstance(it.result!!)
                    tBottomSheetFragment?.isCancelable = false
                    tBottomSheetFragment?.show(
                        supportFragmentManager,
                        tBottomSheetFragment?.tag ?: ""
                    )
                } else {
                    tBottomSheetFragment?.updateData(it.result!!)
                }
            } else if (it.errorMessage.isNotEmpty()) {
                Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }
        viewModel.getTeam(id.toLong())
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var name = ""
    private var start = ""
    private var end = ""
    private var id = 0
    private var code = ""
    private var tableFragment: TableFragment? = null
    private var teamFragment: TeamFragment? = null
    private var cFixturesFragment: CompetitionFixturesFragment? = null
    private var tBottomSheetFragment: TeamBottomSheetFragment? = null
    private val viewModel by viewModel<DetailsActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0)
            code = intent.getStringExtra("code").orEmpty()
            name = intent?.getStringExtra("name").orEmpty()
            start = intent?.getStringExtra("start").orEmpty()
            end = intent?.getStringExtra("end").orEmpty()
            binding.toolbar.title = if (!getYear(start).equals(getYear(end), true))
                ("$name ${getYear(start)}/${getYearShort(end)}")
            else
                ("$name ${getYear(start)}")
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = mSectionsPagerAdapter

        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.container))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTableFragment(): TableFragment {
        if (tableFragment == null)
            tableFragment = TableFragment.newInstance(code)
        return tableFragment!!
    }

    private fun getTeamFragment(): TeamFragment {
        if (teamFragment == null)
            teamFragment = TeamFragment.newInstance(code, start)
        return teamFragment!!
    }

    private fun getCFixturesFragment(): CompetitionFixturesFragment {
        if (cFixturesFragment == null)
            cFixturesFragment = CompetitionFixturesFragment.newInstance(code)
        return cFixturesFragment!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("id", id)
        outState.putString("code", code)
        outState.putString("name", name)
        outState.putString("start", start)
        outState.putString("end", end)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        id = savedInstanceState.getInt("id")
        code = savedInstanceState.getString("code")!!
        name = savedInstanceState.getString("name")!!
        start = savedInstanceState.getString("start")!!
        end = savedInstanceState.getString("end")!!
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
