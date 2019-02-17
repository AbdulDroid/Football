package com.sterlingbankng.football.ui.details.adapter

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Team
import com.sterlingbankng.football.ui.details.fragment.TeamFragment.OnFragmentInteractionListener
import com.sterlingbankng.football.utils.glide.GlideApp
import com.sterlingbankng.football.utils.glide.SvgSoftwareLayerSetter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.team_grid_item.*


class TeamsRecyclerViewAdapter(
    private var teams: List<Team>,
    private val listener: OnFragmentInteractionListener?
):
    RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_grid_item, parent, false)
        return ViewHolder(v)
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
    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(team: Team) {
            if (!team.crestUrl.isNullOrEmpty()) {
                if (team.crestUrl.endsWith(".svg")) {
                    GlideApp.with(team_crest)
                        .`as`(PictureDrawable::class.java)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(SvgSoftwareLayerSetter())
                        .load(Uri.parse(team.crestUrl))
                        .into(team_crest)
                } else {
                    GlideApp.with(team_crest)
                        .load(team.crestUrl)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .into(team_crest)
                }
            } else {
                team_crest.setImageResource(R.drawable.ic_soccer)
            }
            team_name.text = team.name
            containerView.setOnClickListener {
                listener?.onTeamClicked(team.id)
            }
        }
    }
}