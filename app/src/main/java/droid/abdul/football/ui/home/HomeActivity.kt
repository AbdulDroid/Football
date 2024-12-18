package droid.abdul.football.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import droid.abdul.football.R
import droid.abdul.football.databinding.ActivityHomeBinding
import droid.abdul.football.repository.api.dto.Competition
import droid.abdul.football.ui.details.DetailsActivity
import droid.abdul.football.ui.home.fragment.CompetitionsFragment
import droid.abdul.football.ui.home.fragment.TodayFixturesFragment

class HomeActivity : AppCompatActivity(), CompetitionsFragment.OnFragmentInteractionListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCompetitionClicked(competition: Competition) {
        Log.e(TAG, "we are on our way with code:${competition.code} and name:${competition.name}")
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", competition.id.toInt())
        intent.putExtra("code", competition.code.orEmpty())
        intent.putExtra("name", competition.name)
        intent.putExtra("start", competition.currentSeason?.startDate)
        intent.putExtra("end", competition.currentSeason?.endDate)
        startActivity(intent)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_fixtures -> {
                    binding.toolbar.title = getString(R.string.title_fixtures)
                    showFixtures()
                    currentView = "Fixtures"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_competition -> {
                    binding.toolbar.title = getString(R.string.title_competition)
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
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.navigation.selectedItemId = if (savedInstanceState != null && savedInstanceState.containsKey("current")) {
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
            .replace(binding.container.id, fr, fr::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("current", currentView)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentView = savedInstanceState.getString("current")!!
    }

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
    }
}
