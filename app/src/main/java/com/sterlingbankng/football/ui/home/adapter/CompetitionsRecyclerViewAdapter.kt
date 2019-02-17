package com.sterlingbankng.football.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.ui.home.fragment.CompetitionsFragment.OnFragmentInteractionListener
import com.sterlingbankng.football.repository.api.Competition
import com.sterlingbankng.football.utils.getYear
import com.sterlingbankng.football.utils.getYearShort
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.competition_list_item.*

class CompetitionsRecyclerViewAdapter(
    private var competitions: List<Competition>,
    private val listener: OnFragmentInteractionListener?
) : RecyclerView.Adapter<CompetitionsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.competition_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val competition = competitions[position]
        holder.bind(competition)
    }

    override fun getItemCount(): Int = competitions.size

    fun setItems(data: List<Competition>) {
        this.competitions = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(competition: Competition) {
            competition_name.text =
                    if (competition.currentSeason.endDate.isNotEmpty() && competition.currentSeason.startDate.isNotEmpty())
                        if (!getYear(competition.currentSeason.startDate).equals(
                                getYear(competition.currentSeason.endDate),
                                true
                            )
                        ) {

                            ("${competition.name} ${getYear(
                                competition.currentSeason.startDate
                            )}/${getYearShort(
                                competition.currentSeason.endDate
                            )}")
                        } else {
                            ("${competition.name} ${getYear(competition.currentSeason.startDate)}")
                        }
                    else
                        competition.name
            containerView.setOnClickListener {
                listener?.onCompetitionClicked(competition)
            }
        }
    }
}