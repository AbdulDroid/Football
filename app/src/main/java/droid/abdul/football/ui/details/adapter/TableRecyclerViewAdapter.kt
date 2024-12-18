package droid.abdul.football.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import droid.abdul.football.R
import droid.abdul.football.databinding.TableListItemBinding
import droid.abdul.football.repository.api.dto.Table

class TableRecyclerViewAdapter(private var tables: List<Table>) :
    RecyclerView.Adapter<TableRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TableListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    override fun getItemCount(): Int = tables.size


    fun setItems(data: List<Table>) {
        tables = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: TableListItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(table: Table) {
            view.pos.text = ("${table.position}")
            if (table.team.crestUrl.isNotEmpty()) {
                val context = view.crest.context
                view.crest.load(table.team.crestUrl, builder = {
                    ImageRequest.Builder(context)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .build()
                })
            } else {
                view.crest.setImageResource(R.drawable.ic_soccer)
            }
            view.teamName.text = table.team.name
            view.played.text = ("${table.played}")
            view.difference.text = ("${table.difference}")
            view.points.text = ("${table.points}")
        }
    }
}