package com.sterlingbankng.football.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Match
import com.sterlingbankng.football.utils.getMatchTime
import com.sterlingbankng.football.utils.getTime
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fixture_list_item.*

class FixturesRecyclerViewAdapter(
    private var mValues: List<Match>
) : RecyclerView.Adapter<FixturesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fixture_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mValues.size

    fun setItems(matches: List<Match>) {
        this.mValues = matches
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(match: Match) {
            status.text = match.status
            time.text = getTime(match.utcDate)
            mDay.text = String.format(mDay.context?.getString(R.string.matchday)!!, match.matchDay)
            home.text = match.homeTeam.name
            away.text = match.awayTeam.name
            when (match.status) {
                "IN_PLAY" -> {
                    duration.text = if (match.score.halfTime?.away != null || match.score.halfTime?.home != null) {
                        ("${getMatchTime(match.utcDate, -15)}\'")
                    } else ("${getMatchTime(match.utcDate, 0)}\'")
                }
                "PAUSED" -> {
                    duration.text = ("HT")
                }
                "FINISHED" -> {
                    duration.text = ("FT")
                }
                else -> {
                    duration.text = ("00")
                }
            }
            homeScore.text = ("${match.score.fullTime.home ?: 0}")
            awayScore.text = ("${match.score.fullTime.away ?: 0}")
        }
    }
}
