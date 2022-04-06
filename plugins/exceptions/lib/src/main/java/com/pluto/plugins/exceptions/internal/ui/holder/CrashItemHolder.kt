package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.asTimeElapsed
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashBinding
import com.pluto.plugins.exceptions.internal.dao.ExceptionEntity

internal class CrashItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash), actionListener) {

    private val binding = PlutoExcepItemCrashBinding.bind(itemView)
    private val timeElapsed = binding.timeElapsed

    override fun onBind(item: ListItem) {
        if (item is ExceptionEntity) {
//            with(item.data.exception) {
//                if (isANRException) {
//                    binding.message.text =
//                        context.getString(R.string.pluto___anr_list_message, MAIN_THREAD_RESPONSE_THRESHOLD)
//                    binding.title.setSpan {
//                        context.apply {
//                            append(
//                                fontColor(
//                                    getString(R.string.pluto___anr_list_title), color(com.pluto.plugin.R.color.pluto___text_dark_80)
//                                )
//                            )
//                        }
//                    }
//                    binding.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto___ic_anr_warning, 0, 0, 0)
//                } else {
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
//                }
//            }
            timeElapsed.text = item.timestamp.asTimeElapsed()
            itemView.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
