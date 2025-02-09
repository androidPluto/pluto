package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashBinding
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.utilities.extensions.asTimeElapsed
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class CrashItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash), actionListener) {

    private val binding = PlutoExcepItemCrashBinding.bind(itemView)
    private val timeElapsed = binding.timeElapsed

    override fun onBind(item: ListItem) {
        if (item is ExceptionEntity) {
            with(item.data.exception) {
                if (isANRException) {
                    binding.message.text =
                        context.getString(R.string.pluto_excep___anr_list_message, PlutoExceptions.mainThreadResponseThreshold)
                    binding.title.setSpan {
                        context.apply {
                            append(
                                fontColor(
                                    getString(R.string.pluto_excep___anr_list_title), color(com.pluto.plugin.R.color.pluto___text_dark_80)
                                )
                            )
                        }
                    }
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_excep___ic_anr_warning, 0, 0, 0)
                } else {
                    binding.message.setSpan {
                        append("${item.data.exception.file}\t\t")
                        append(
                            fontColor(
                                "line:${item.data.exception.lineNumber}",
                                context.color(com.pluto.plugin.R.color.pluto___text_dark_60)
                            )
                        )
                    }
                    binding.title.text = item.data.exception.name
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }
            timeElapsed.text = item.timestamp.asTimeElapsed()
            itemView.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
