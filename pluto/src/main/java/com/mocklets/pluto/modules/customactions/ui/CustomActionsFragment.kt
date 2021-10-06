package com.mocklets.pluto.modules.customactions.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoLayoutCustomActionsBinding
import com.mocklets.pluto.modules.customactions.CustomAction

internal class CustomActionsFragment : Fragment(R.layout.pluto___layout_custom_actions) {
    private val binding by viewBinding(PlutoLayoutCustomActionsBinding::bind)
    private val actionsAdapter: BaseAdapter by lazy { CustomActionAdapter(onActionListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }

        binding.recyclerView.adapter = actionsAdapter
        actionsAdapter.list = Pluto.customActions
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is CustomAction) {
                data.clickListener.onClick()
                if (data.shouldClosePluto) {
                    requireActivity().finish()
                }
            }
        }
    }
}
