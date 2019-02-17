package com.sterlingbankng.football.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Competition
import com.sterlingbankng.football.ui.details.DetailsActivity
import com.sterlingbankng.football.ui.home.fragment.CompetitionsFragment
import com.sterlingbankng.football.ui.home.fragment.TodayFixturesFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), CompetitionsFragment.OnFragmentInteractionListener {
    override fun onCompetitionClicked(competition: Competition) {
        Log.e(TAG, "we are on our way with id:${competition.id} and name:${competition.name}")
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", competition.id.toInt())
        intent.putExtra("name", competition.name)
        intent.putExtra("start", competition.currentSeason.startDate)
        intent.putExtra("end", competition.currentSeason.endDate)
        startActivity(intent)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_fixtures -> {
                    toolbar.title = getString(R.string.title_fixtures)
                    showFixtures()
                    currentView = "Fixtures"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_competition -> {
                    toolbar.title = getString(R.string.title_competition)
                    showCompetition()
                    currentView = "Competitions"
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    private var todayFixturesFragment: TodayFixturesFragment? = null
    private var competitionsFragment: CompetitionsFragment? = null
    private var currentView = "Fixtures"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = if (savedInstanceState != null && savedInstanceState.containsKey("current")) {
            if (savedInstanceState.getString("current").equals("Fixtures", true)) {
                R.id.action_fixtures
            } else {
                R.id.action_competition
            }
        } else
            R.id.action_fixtures
    }

    private fun showFixtures() {
        if (todayFixturesFragment == null)
            todayFixturesFragment = TodayFixturesFragment()
        loadFragments(todayFixturesFragment!!)
    }

    private fun showCompetition() {
        if (competitionsFragment == null)
            competitionsFragment = CompetitionsFragment()
        loadFragments(competitionsFragment!!)
    }

    private fun loadFragments(fr: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(container.id, fr, fr::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("current", currentView)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null)
            currentView = savedInstanceState.getString("current")!!
    }

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
    }
}
