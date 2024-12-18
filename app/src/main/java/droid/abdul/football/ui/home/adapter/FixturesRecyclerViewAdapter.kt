package droid.abdul.football.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.R
import droid.abdul.football.databinding.FixtureListItemBinding
import droid.abdul.football.repository.api.dto.Match
import droid.abdul.football.utils.getMatchTime
import droid.abdul.football.utils.getTime

class FixturesRecyclerViewAdapter(
    private var mValues: List<Match>
) : RecyclerView.Adapter<FixturesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FixtureListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    inner class ViewHolder(val view: FixtureListItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(match: Match) {
            view.status.text = match.status
            view.time.text = getTime(match.utcDate)
            view.mDay.text = String.format(view.root.context?.getString(R.string.matchday)!!, match.matchDay)
            view.home.text = match.homeTeam.name
            view.away.text = match.awayTeam.name
            when (match.status) {
                "IN_PLAY" -> {
                    view.duration.text = if (match.score.halfTime?.away != null || match.score.halfTime?.home != null) {
                        ("${getMatchTime(match.utcDate, -15)}\'")
                    } else ("${getMatchTime(match.utcDate, 0)}\'")
                }
                "PAUSED" -> {
                    view.duration.text = ("HT")
                }
                "FINISHED" -> {
                    view.duration.text = ("FT")
                }
                else -> {
                    view.duration.text = ("")
                }
            }
            view.homeScore.text = ("${match.score.fullTime.home ?: 0}")
            view.awayScore.text = ("${match.score.fullTime.away ?: 0}")
        }
    }
}
