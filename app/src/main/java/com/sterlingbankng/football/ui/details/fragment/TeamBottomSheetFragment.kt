package com.sterlingbankng.football.ui.details.fragment

import android.app.Dialog
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sterlingbankng.football.R
import com.sterlingbankng.football.repository.api.Player
import com.sterlingbankng.football.repository.api.TeamPlayerResponse
import com.sterlingbankng.football.ui.details.adapter.PlayerListRecyclerViewAdapter
import com.sterlingbankng.football.utils.glide.GlideApp
import com.sterlingbankng.football.utils.glide.SvgSoftwareLayerSetter
import kotlinx.android.synthetic.main.team_bottom_view.*

class TeamBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(sheet)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        dialog.setOnDismissListener {
            if (!::behavior.isInitialized) {
                val d = it as BottomSheetDialog
                val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                behavior = BottomSheetBehavior.from(sheet)
            }
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_bottom_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name: String
        val url: String
        var players: List<Player>
        arguments?.let {
            name = it.getString("name") ?: ""
            url = it.getString("url") ?: ""
            players = it.getParcelableArrayList("players") ?: ArrayList()
            team_sheet_name.text = name
            if (url.isNotEmpty()) {
                if (url.endsWith(".svg")) {
                    GlideApp.with(this)
                        .`as`(PictureDrawable::class.java)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .transition(withCrossFade())
                        .listener(SvgSoftwareLayerSetter())
                        .load(Uri.parse(url))
                        .into(team_sheet_crest)
                } else {
                    GlideApp.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .into(team_sheet_crest)
                }
            } else {
                team_sheet_crest.setImageResource(R.drawable.ic_soccer)
            }
            if (players.isNotEmpty()) {
                with(player_list) {
                    layoutManager = LinearLayoutManager(
                        context,
                        RecyclerView.VERTICAL,
                        false
                    )
                    adapter = PlayerListRecyclerViewAdapter(players)
                }
            }
        }
        close_button.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            dialog.hide()
        }

    }

    fun updateData(data: TeamPlayerResponse): Boolean {
        if (context != null) {
            team_sheet_name?.text = data.name
            if (!data.crestUrl.isNullOrEmpty()) {
                if (data.crestUrl.endsWith(".svg")) {
                    GlideApp.with(this)
                        .`as`(PictureDrawable::class.java)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .transition(withCrossFade())
                        .listener(SvgSoftwareLayerSetter())
                        .load(Uri.parse(data.crestUrl))
                        .into(team_sheet_crest)
                } else {
                    GlideApp.with(this)
                        .load(data.crestUrl)
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .into(team_sheet_crest)
                }
            } else {
                team_sheet_crest.setImageResource(R.drawable.ic_soccer)
            }
            if (data.squad.isNotEmpty()) {
                with(player_list) {
                    layoutManager = LinearLayoutManager(
                        context,
                        RecyclerView.VERTICAL,
                        false
                    )
                    setHasFixedSize(true)
                    addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                    adapter = PlayerListRecyclerViewAdapter(data.squad)
                }
            }
            dialog.show()
            return true
        } else {
            Log.e("Bottom Sheet", "View has been nullified")
            Log.e("Bottom Sheet", arguments.toString())
            dialog.show()
            return false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(data: TeamPlayerResponse) =
            TeamBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("name", data.name)
                    putString("url", data.crestUrl)
                    putParcelableArrayList("players", ArrayList(data.squad))
                }
            }
    }
}