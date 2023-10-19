package com.pluto.plugins.exceptions.internal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.share.Shareable
import com.pluto.plugin.share.lazyContentSharer
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepFragmentDetailsBinding
import com.pluto.plugins.exceptions.internal.ReportData
import com.pluto.plugins.exceptions.internal.persistence.ExceptionEntity
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.extensions.capitalizeText
import com.pluto.utilities.extensions.delayedLaunchWhenResumed
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.net.URLEncoder

internal class DetailsFragment : Fragment(R.layout.pluto_excep___fragment_details) {

    private val binding by viewBinding(PlutoExcepFragmentDetailsBinding::bind)
    private val viewModel: CrashesViewModel by activityViewModels()
    private val crashAdapter: BaseAdapter by autoClearInitializer { CrashesAdapter(onActionListener) }
    private val exceptionCipher by lazy { getCipheredException() }
    private val contentSharer by lazyContentSharer()

    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<ReportData> = moshi.adapter(ReportData::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        binding.list.apply {
            adapter = crashAdapter
        }
        binding.close.setOnDebounceClickListener {
            activity?.onBackPressed()
        }

        binding.delete.setOnDebounceClickListener {
            viewModel.currentException.value?.id?.let { id ->
                lifecycleScope.delayedLaunchWhenResumed(SCREEN_CLOSE_DELAY) {
                    viewModel.delete(id)
                    activity?.onBackPressed()
                    context?.toast("Crash logs deleted.")
                }
            }
        }

        binding.share.setOnDebounceClickListener {
            viewModel.currentException.value?.let {
                contentSharer.share(Shareable(title = "Share Crash Report", content = it.toShareText(), fileName = "Crash Report from Pluto"))
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
                client = PlutoExceptions.appPackageName
            )
            val exceptionString = moshiAdapter.toJson(reportData)

            val encodedByteArray = exceptionString.toByteArray(Charsets.UTF_8)
            val encodedString = encodeToString(encodedByteArray, DEFAULT)

            return URLEncoder.encode(encodedString, "utf-8")
        }
        return null
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            when (action) {
                "report_crash" -> exceptionCipher?.let {
                    val url = "https://androidpluto.com/exception/$it/a0bbe9cd-2f02-4a12-b7b7-36fce61a6b48"
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
                "thread_stack_trace" -> findNavController().navigate(R.id.openStackTrace)
            }
        }
    }

    private val exceptionObserver = Observer<ExceptionEntity> {
        val list = arrayListOf<ListItem>()
        list.add(it.data.exception)
        it.data.threadStateList?.let { states -> list.add(states) }
        it.data.thread?.let { thread -> list.add(thread) }
        list.add(it.device)

        crashAdapter.list = list
    }

    private companion object {
        const val SCREEN_CLOSE_DELAY = 200L
        private const val STACK_TRACE_SHORT_LENGTH = 15
    }
}

private const val STACK_TRACE_LENGTH = 25
private const val SHARE_SECTION_DIVIDER = "\n\n==================\n\n"
private fun ExceptionEntity.toShareText(): String {
    val text = StringBuilder()
    text.append("EXCEPTION : \n")
    text.append("${this.data.exception.name}: ${this.data.exception.message}\n")
    this.data.exception.stackTrace.take(STACK_TRACE_LENGTH).forEach {
        text.append("\t at $it\n")
    }
    if (this.data.exception.stackTrace.size - STACK_TRACE_LENGTH > 0) {
        text.append("\t + ${this.data.exception.stackTrace.size - STACK_TRACE_LENGTH} more lines\n\n")
    }

    text.append(SHARE_SECTION_DIVIDER)

    this.data.thread?.let {
        text.append("Thread : ")
        text.append("${it.name.uppercase()} (")
        text.append("id : ${it.id},  ")
        text.append("priority : ${it.priorityString},  ")
        text.append("is_Daemon : ${it.isDaemon},  ")
        text.append("state : ${it.state}")
        text.append(")")

        text.append(SHARE_SECTION_DIVIDER)
    }

    text.append("APP STATE : \n")
    this.device.appVersionName.let {
        text.append("App Version : $it (${this.device.appVersionCode})\n")
    }
    text.append("Android  (OS : ${this.device.androidOs}, API_Level : ${this.device.androidAPILevel})\n")
    text.append("Orientation : ${this.device.screenOrientation}\n")
    text.append("is_Rooted : ${this.device.isRooted}")

    text.append(SHARE_SECTION_DIVIDER)

    text.append("DEVICE INFO : \n")
    text.append("Model : ${this.device.buildBrand?.capitalizeText()} ${this.device.buildModel}\n")
    text.append(
        "Screen : { height : ${this.device.screenHeightPx}px, width : ${this.device.screenWidthPx}px, " +
            "density :  ${this.device.screenDensityDpi}, size :  ${this.device.screenSizeInch} inches }"
    )
    return text.toString()
}
