package com.pluto.tool.selector

import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.pluto.R
import com.pluto.databinding.PlutoItemToolBinding
import com.pluto.tools.PlutoTool
import com.pluto.ui.selector.SelectorActivity.Companion.ANIMATION_DURATION
import com.pluto.ui.selector.loadAnimation
import com.pluto.utilities.extensions.color
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
    private val iconCard = binding.iconCard

    override fun onBind(item: ListItem) {
        if (item is PlutoTool) {
            icon.setImageResource(item.getConfig().icon)
            name.text = item.getConfig().name
            if (item.isEnabled()) {
                iconCard.setCardBackgroundColor(context.color(R.color.pluto___white))
                name.setTextColor(context.color(R.color.pluto___white))
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
            } else {
                iconCard.setCardBackgroundColor(context.color(R.color.pluto___white_40))
                name.setTextColor(context.color(R.color.pluto___white_60))
                binding.root.setOnDebounceClickListener(action = null)
            }
        }
    }
}
