package com.sterlingbankng.football.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Player
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.player_list_item.*

class PlayerListRecyclerViewAdapter(
    private val players: List<Player>
) : RecyclerView.Adapter<PlayerListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position], position)
    }

    override fun getItemCount(): Int = players.size


    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(player: Player, position: Int) {
            serial_no.text = ("${position + 1}")
            player_name.text = player.name
            player_role.text = when (player.role) {

                "PLAYER" -> {
                    player.position
                }

                else -> {
                    getRole(player.role)
                }
            }
        }

        private fun getRole(value: String): String {
            return value.replace("_", " ").toLowerCase().capitalize()
        }
    }
}