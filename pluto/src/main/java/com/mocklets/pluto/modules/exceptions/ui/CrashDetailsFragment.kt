package com.mocklets.pluto.modules.exceptions.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.core.extensions.capitalizeText
import com.mocklets.pluto.core.extensions.delayedLaunchWhenResumed
import com.mocklets.pluto.core.extensions.lazyParcelExtra
import com.mocklets.pluto.core.extensions.share
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.viewBinding
import com.mocklets.pluto.databinding.PlutoFragmentCrashDetailsBinding
import com.mocklets.pluto.modules.exceptions.ExceptionAllData
import com.mocklets.pluto.modules.exceptions.ExceptionRepo
import com.mocklets.pluto.modules.exceptions.ReportData
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity
import java.net.URLEncoder
import java.util.Locale
import kotlinx.android.parcel.Parcelize

internal class CrashDetailsFragment : Fragment(R.layout.pluto___fragment_crash_details) {

    private val binding by viewBinding(PlutoFragmentCrashDetailsBinding::bind)
    private val viewModel: CrashesViewModel by viewModels()
    private val crashAdapter: BaseAdapter by lazy { CrashesAdapter(onActionListener) }
    private val arguments by lazyParcelExtra<Data>()
    private val exceptionCipher by lazy { getCipheredException() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.id?.let { viewModel.fetch(it) }
        binding.list.apply {
            adapter = crashAdapter
        }
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }

        binding.delete.setDebounceClickListener {
            viewModel.currentException.value?.id?.let { id ->
                lifecycleScope.delayedLaunchWhenResumed(SCREEN_CLOSE_DELAY) {
                    viewModel.delete(id)
                    activity?.onBackPressed()
                    context?.toast("Crash logs deleted.")
                }
            }
        }

        binding.share.setDebounceClickListener {
            viewModel.currentException.value?.let {
                context?.share(
                    message = it.data.toShareText(),
                    title = "Share Crash Report",
                    subject = "Crash Report from Pluto"
                )
            }
        }

        viewModel.currentException.removeObservers(viewLifecycleOwner)
        viewModel.currentException.observe(viewLifecycleOwner, exceptionObserver)
    }

    private fun getCipheredException(): String? {
        viewModel.currentException.value?.data?.exception?.let {
            val reportData = ReportData(
                name = it.name,
                message = it.message,
                stackTrace = it.stackTrace.take(STACK_TRACE_SHORT_LENGTH) as ArrayList<String>,
                client = Pluto.appContext?.packageName
            )
            val exceptionString = Gson().toJson(reportData)

            val encodedByteArray = exceptionString.toByteArray(Charsets.UTF_8)
            val encodedString = encodeToString(encodedByteArray, DEFAULT)

//            val decodedByteArray = decode(encodedString, DEFAULT)
//            val decodedString = String(decodedByteArray, Charsets.UTF_8)

            return URLEncoder.encode(encodedString, "utf-8")
        }
        return null
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (action == "report_crash") {
                exceptionCipher?.let {
                    val url = "https://pluto.mocklets.com/exception/$it/a0bbe9cd-2f02-4a12-b7b7-36fce61a6b48"
                    DebugLog.d("Prateek", url)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }
        }
    }

    private val exceptionObserver = Observer<ExceptionEntity> {
        val list = arrayListOf<ListItem>()
        list.add(it.data.exception)
        it.data.threadStateList?.let { states -> list.add(states) }
        it.data.thread?.let { thread -> list.add(thread) }
        list.add(it.data.device)

        crashAdapter.list = list
    }

    @Parcelize
    data class Data(val id: Int) : Parcelable

    private companion object {
        const val SCREEN_CLOSE_DELAY = 200L
        private const val STACK_TRACE_SHORT_LENGTH = 15
    }
}

private const val STACK_TRACE_LENGTH = 25
private fun ExceptionAllData.toShareText(): String {
    val text = StringBuilder()
    text.append("EXCEPTION : \n")
    text.append("${this.exception.name}: ${this.exception.message}\n")
    this.exception.stackTrace.take(STACK_TRACE_LENGTH).forEach {
        text.append("\t at $it\n")
    }
    if (this.exception.stackTrace.size - STACK_TRACE_LENGTH > 0) {
        text.append("\t + ${this.exception.stackTrace.size - STACK_TRACE_LENGTH} more lines\n\n")
    }

    this.thread?.let {
        text.append("Thread : ")
        text.append("${it.name.uppercase(Locale.getDefault())} (")
        text.append("id : ${it.id},  ")
        text.append("priority : ${ExceptionRepo.getPriorityString(it.priority)},  ")
        text.append("is_Daemon : ${it.isDaemon},  ")
        text.append("state : ${it.state}")
        text.append(")")

        text.append("\n\n==================\n\n")
    }

    text.append("APP STATE : \n")
    this.device.software.appVersion?.let {
        text.append("App Version : ${it.name} (${it.code})\n")
    }
    text.append("Android  (OS : ${this.device.software.androidOs}, API_Level : ${this.device.software.androidAPILevel})\n")
    text.append("Orientation : ${this.device.software.orientation}\n")
    text.append("is_Rooted : ${this.device.isRooted}")

    text.append("\n\n==================\n\n")

    text.append("DEVICE INFO : \n")
    text.append("Model : ${this.device.build.brand?.capitalizeText()} ${this.device.build.model}\n")
    text.append(
        "Screen : { height : ${this.device.screen.height}, width : ${this.device.screen.height}, " +
            "density :  ${this.device.screen.density}, size :  ${this.device.screen.size} }"
    )

    text.append("\n\n-----\nreport powered by Pluto https://pluto.mocklets.com")

    return text.toString()
}
