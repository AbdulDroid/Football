package droid.abdul.football.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import droid.abdul.football.R
import droid.abdul.football.databinding.TeamGridItemBinding
import droid.abdul.football.repository.api.Team
import droid.abdul.football.ui.details.fragment.TeamFragment.OnFragmentInteractionListener


class TeamsRecyclerViewAdapter(
    private var teams: List<Team>,
    private val listener: OnFragmentInteractionListener?
) :
    RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TeamGridItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount(): Int = teams.size

    fun setItems(data: List<Team>) {
        teams = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: TeamGridItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(team: Team) {
            if (team.crestUrl.isNotEmpty()) {
                val context = view.teamCrest.context

                view.teamCrest.load(team.crestUrl, builder = {
                    ImageRequest.Builder(context)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .build()
                })
            } else {
                view.teamCrest.setImageResource(R.drawable.ic_soccer)
            }
            view.teamName.text = team.name
            view.root.setOnClickListener {
                listener?.onTeamClicked(team.id)
            }
        }
    }
}