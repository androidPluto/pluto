package com.pluto.plugins.logger.internal.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.logger.R
import com.pluto.plugins.logger.databinding.PlutoLoggerFragmentDetailsBinding
import com.pluto.plugins.logger.internal.LogData
import com.pluto.plugins.logger.internal.LogsViewModel
import com.pluto.plugins.logger.internal.asExceptionData
import com.pluto.plugins.logger.internal.beautifyAttributes
import com.pluto.plugins.logger.internal.ui.DetailsFragment.Companion.MAX_STACK_TRACE_LINES

internal class DetailsFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoLoggerFragmentDetailsBinding::bind)
    private val viewModel: LogsViewModel by activityViewModels()
    private val contentSharer by lazyContentSharer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_logger___fragment_details, container, false)

    override fun getTheme(): Int = R.style.PlutoLoggerBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        viewModel.current.removeObserver(detailsObserver)
        viewModel.current.observe(viewLifecycleOwner, detailsObserver)
    }

    private val detailsObserver = Observer<LogData> { data ->
        binding.title.setSpan {
            append("${context.getString(R.string.pluto_logger___log_details)}  ")
            append(italic(fontColor(data.level.label.uppercase(), context.color(data.level.textColor))))
        }

        binding.cta.setDebounceClickListener {
            context?.let {
                contentSharer.share(Shareable(title = "Share Log details", content = data.toShareText(it)))
            }
        }

        binding.tag.text = data.tag
        binding.filename.setSpan {
            append(fontColor("called from\n", context.color(R.color.pluto___text_dark_40)))
            append(data.stackTraceElement.methodName)
            append(fontColor(" (", context.color(R.color.pluto___text_dark_40)))
            append(data.stackTraceElement.fileName)
            append(fontColor(", line:", context.color(R.color.pluto___text_dark_60)))
            append(fontColor("${data.stackTraceElement.lineNumber}", context.color(R.color.pluto___text_dark_80)))
            append(fontColor(")", context.color(R.color.pluto___text_dark_40)))
        }
        binding.message.text = data.message
        binding.stackTraceContainer.visibility = View.GONE
        data.tr?.asExceptionData()?.let {
            binding.stackTraceContainer.visibility = View.VISIBLE
            binding.stackTrace.setSpan {
                append(fontColor("${it.name}: ${it.message}", context.color(R.color.pluto___text_dark_80)))
                it.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                    append("\n\t\t\t")
                    append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                    append(it)
                }
                val extraTrace = it.stackTrace.size - MAX_STACK_TRACE_LINES
                if (extraTrace > 0) {
                    append(
                        fontColor(
                            "\n\t\t\t + $extraTrace more lines", context.color(R.color.pluto___text_dark_40)
                        )
                    )
                }
            }
        }

        if (!data.eventAttributes.isNullOrEmpty()) {
            binding.stackTraceContainer.visibility = View.VISIBLE
            binding.stackTraceTitle.setSpan {
                append(context.getString(R.string.pluto_logger___event_attributes))
                append(fontColor(" (${data.eventAttributes.size})", context.color(R.color.pluto___text_dark_40)))
            }
            binding.stackTrace.text = context.beautifyAttributes(data.eventAttributes)
        }
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 15
    }
}

private fun LogData.toShareText(context: Context): String {
    val text = StringBuilder()
    text.append("$tag : $message\n")

    tr?.asExceptionData()?.let {
        text.append("\n${it.name}: ${it.message}\n")
        it.stackTrace.take(MAX_STACK_TRACE_LINES).forEach { trace ->
            text.append("\t at $trace\n")
        }
        if (it.stackTrace.size - MAX_STACK_TRACE_LINES > 0) {
            text.append("\t + ${it.stackTrace.size - MAX_STACK_TRACE_LINES} more lines\n\n")
        }
    }

    if (!eventAttributes.isNullOrEmpty()) {
        text.append("\n${context.getString(R.string.pluto_logger___event_attributes).lowercase()} - ")
        eventAttributes.entries.forEach {
            text.append("\n\t ${it.key} : ${it.value}")
        }
    }
    return text.toString()
}
