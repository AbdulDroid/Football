package com.sterlingbankng.football.ui.details.adapter

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Table
import com.sterlingbankng.football.utils.glide.GlideApp
import com.sterlingbankng.football.utils.glide.SvgSoftwareLayerSetter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.table_list_item.*

class TableRecyclerViewAdapter(private var tables: List<Table>) :
    RecyclerView.Adapter<TableRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    override fun getItemCount(): Int = tables.size


    fun setItems(data: List<Table>) {
        tables = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(table: Table) {
            pos.text = ("${table.position}")
            if (!table.team.crestUrl.isNullOrEmpty()) {
                if (table.team.crestUrl.endsWith(".svg")) {
                    GlideApp.with(crest)
                        .`as`(PictureDrawable::class.java)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(SvgSoftwareLayerSetter())
                        .load(Uri.parse(table.team.crestUrl))
                        .into(crest)
                } else {
                    GlideApp.with(crest)
                        .load(table.team.crestUrl)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .into(crest)
                }
            } else {
                crest.setImageResource(R.drawable.ic_soccer)
            }
            team_name.text = table.team.name
            played.text = ("${table.played}")
            difference.text = ("${table.difference}")
            points.text = ("${table.points}")
        }
    }
}