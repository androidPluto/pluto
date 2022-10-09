package com.pluto.tool.selector

import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.pluto.R
import com.pluto.databinding.PlutoItemToolBinding
import com.pluto.tools.PlutoTool
import com.pluto.ui.selector.SelectorActivity.Companion.ANIMATION_DURATION
import com.pluto.ui.selector.loadAnimation
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.setListener
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class ToolItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_tool), actionListener) {

    private val binding = PlutoItemToolBinding.bind(itemView)
    private val name = binding.name
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is PlutoTool) {
            icon.setImageResource(item.getConfig().icon)
            name.text = item.getConfig().name
            binding.root.setOnDebounceClickListener(haptic = true) {
                val scale = context.loadAnimation(R.anim.pluto___click_bounce)
                scale.duration = ANIMATION_DURATION
                scale.interpolator = OvershootInterpolator()
                scale.setListener {
                    onAnimationStart {
                    }
                    onAnimationEnd {
                        onAction("click")
                    }
                }
                it.startAnimation(scale)
            }
        }
    }
}
