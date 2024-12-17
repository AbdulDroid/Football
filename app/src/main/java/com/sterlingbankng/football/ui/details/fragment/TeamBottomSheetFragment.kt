package com.sterlingbankng.football.ui.details.fragment

import android.app.Dialog
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
import coil3.load
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sterlingbankng.football.R
import com.sterlingbankng.football.databinding.TeamBottomViewBinding
import com.sterlingbankng.football.repository.api.Player
import com.sterlingbankng.football.repository.api.TeamPlayerResponse
import com.sterlingbankng.football.ui.details.adapter.PlayerListRecyclerViewAdapter

class TeamBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>

    private var _binding: TeamBottomViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(sheet)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        dialog.setOnDismissListener {
            if (!::behavior.isInitialized) {
                val d = it as BottomSheetDialog
                val sheet =
                    d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
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
    ): View {
        _binding = TeamBottomViewBinding.inflate(inflater, container, false)
        return binding.root
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
            binding.teamSheetName.text = name
            if (url.isNotEmpty()) {
                binding.teamSheetCrest.load(url, builder = {
                    ImageRequest.Builder(requireContext())
                        .placeholder(R.drawable.ic_soccer)
                        .error(R.drawable.ic_soccer)
                        .build()
                })
            } else {
                binding.teamSheetCrest.setImageResource(R.drawable.ic_soccer)
            }
            if (players.isNotEmpty()) {
                with(binding.playerList) {
                    layoutManager = LinearLayoutManager(
                        context,
                        RecyclerView.VERTICAL,
                        false
                    )
                    adapter = PlayerListRecyclerViewAdapter(players)
                }
            }
        }
        binding.closeButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            dialog.hide()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateData(data: TeamPlayerResponse): Boolean {
        if (context != null) {
            binding.teamSheetName.text = data.name
            if (data.crestUrl.isNotEmpty()) {
                if (data.crestUrl.endsWith(".svg")) {
                    binding.teamSheetCrest.load(Uri.parse(data.crestUrl), builder = {
                        ImageRequest.Builder(requireContext())
                            .placeholder(R.drawable.ic_soccer)
                            .error(R.drawable.ic_soccer)
                            .build()
                    })
                } else {
                    binding.teamSheetCrest.load(data.crestUrl, builder = {
                        ImageRequest.Builder(requireContext())
                            .placeholder(R.drawable.ic_soccer)
                            .error(R.drawable.ic_soccer)
                            .build()
                    })
                }
            } else {
                binding.teamSheetCrest.setImageResource(R.drawable.ic_soccer)
            }
            if (data.squad.isNotEmpty()) {
                with(binding.playerList) {
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