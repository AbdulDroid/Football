package droid.abdul.football.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.CompetitionListItemBinding
import droid.abdul.football.ui.home.fragment.CompetitionsFragment.OnFragmentInteractionListener
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.utils.getYear
import droid.abdul.football.utils.getYearShort

class CompetitionsRecyclerViewAdapter(
    private var competitions: List<Competition>,
    private val listener: OnFragmentInteractionListener?
) : RecyclerView.Adapter<CompetitionsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CompetitionListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    inner class ViewHolder(val view: CompetitionListItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(competition: Competition) {
            view.competitionName.text =
                competition.currentSeason?.let {
                    if (it.endDate.isNotEmpty() && it.startDate.isNotEmpty())
                        if (!getYear(it.startDate).equals(
                                getYear(it.endDate),
                                true
                            )
                        ) {

                            ("${competition.name} ${
                                getYear(
                                    it.startDate
                                )
                            }/${
                                getYearShort(
                                    it.endDate
                                )
                            }")
                        } else {
                            ("${competition.name} ${getYear(it.startDate)}")
                        }
                    else competition.name
                } ?: competition.name
            view.root.setOnClickListener {
                listener?.onCompetitionClicked(competition)
            }
        }
    }
}