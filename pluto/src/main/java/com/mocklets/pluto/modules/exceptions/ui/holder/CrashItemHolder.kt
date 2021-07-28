package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.asTimeElapsed
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemCrashBinding
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.MAIN_THREAD_RESPONSE_THRESHOLD
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity

internal class CrashItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_crash), actionListener) {

    private val binding = PlutoItemCrashBinding.bind(itemView)
    private val timeElapsed = binding.timeElapsed

    override fun onBind(item: ListItem) {
        if (item is ExceptionEntity) {
            with(item.data.exception) {
                if (isANRException) {
                    binding.message.text =
                        context.getString(R.string.pluto___anr_list_message, MAIN_THREAD_RESPONSE_THRESHOLD)
                    binding.title.setSpan {
                        context.apply {
                            append(
                                fontColor(
                                    getString(R.string.pluto___anr_list_title), color(R.color.pluto___text_dark_80)
                                )
                            )
                        }
                    }
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto___ic_anr_warning, 0, 0, 0)
                } else {
                    binding.message.setSpan {
                        append("${item.data.exception.file}\t\t")
                        append(
                            fontColor(
                                "line:${item.data.exception.lineNumber}",
                                context.color(R.color.pluto___text_dark_60)
                            )
                        )
                    }
                    binding.title.text = item.data.exception.name
                    binding.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }
            timeElapsed.text = item.timestamp.asTimeElapsed()
            itemView.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
