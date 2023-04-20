package com.pluto.plugins.rooms.db.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.plugins.rooms.db.R
import com.pluto.plugins.rooms.db.databinding.PlutoRoomsFragmentQueryErrorBinding
import com.pluto.utilities.extensions.color
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding

internal class QueryErrorFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoRoomsFragmentQueryErrorBinding::bind)
    private val errorMessage: String?
        get() = arguments?.getString(ERROR_MESSAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto_rooms___fragment_query_error, container, false)

    override fun getTheme(): Int = R.style.PlutoRoomsDBBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnDebounceClickListener {
            dismiss()
        }

        binding.description.setSpan {
            errorMessage?.let {
                val errorSplit = it.split(": ")
                if (errorSplit.size == 1) {
                    append(fontColor(errorSplit[0], context.color(R.color.pluto___text_dark_60)))
                }
                if (errorSplit.size > 1) {
                    append(fontColor(semiBold("${errorSplit[0]} : "), context.color(R.color.pluto___text_dark_80)))
                    append(fontColor(errorSplit[1], context.color(R.color.pluto___text_dark_60)))
                }
            } ?: run {
                append(fontColor(GENERIC_ERROR, context.color(R.color.pluto___text_dark_60)))
            }
        }
    }

    companion object {
        const val ERROR_MESSAGE = "error_message"
        const val GENERIC_ERROR = "Something went wrong"
    }
}
