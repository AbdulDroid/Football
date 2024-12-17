package droid.abdul.football.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import droid.abdul.football.databinding.PlayerListItemBinding
import droid.abdul.football.repository.api.Player

class PlayerListRecyclerViewAdapter(
    private val players: List<Player>
) : RecyclerView.Adapter<PlayerListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlayerListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position], position)
    }

    override fun getItemCount(): Int = players.size


    inner class ViewHolder(val view: PlayerListItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(player: Player, position: Int) {
            view.serialNo.text = ("${position + 1}")
            view.playerName.text = player.name
            view.playerRole.text = player.position

        }
    }
}