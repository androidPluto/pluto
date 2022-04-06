package com.pluto.plugins.list

import android.content.Context
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.annotation.AnimRes
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginBinding
import com.pluto.plugins.Plugin
import com.pluto.plugins.utilities.extensions.inflate
import com.pluto.plugins.utilities.extensions.setListener
import com.pluto.plugins.utilities.list.DiffAwareAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem
import com.pluto.plugins.utilities.setDebounceClickListener

internal class PluginItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin), actionListener) {

    private val binding = PlutoItemPluginBinding.bind(itemView)
    private val name = binding.name
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is Plugin) {
            icon.setImageResource(item.getConfig().icon)
            name.text = item.getConfig().name
            binding.root.setDebounceClickListener(haptic = true) {
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

            binding.root.setOnLongClickListener {
                onAction("long_click")
                return@setOnLongClickListener true
            }
        }
    }

    private companion object {
        const val ANIMATION_DURATION = 250L
    }
}

fun Context.loadAnimation(@AnimRes id: Int): Animation {
    return AnimationUtils.loadAnimation(this, id)
}
