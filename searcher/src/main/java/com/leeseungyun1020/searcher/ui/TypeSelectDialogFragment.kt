package com.leeseungyun1020.searcher.ui

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.leeseungyun1020.searcher.utilities.Type

class TypeSelectDialogFragment(
    private val types: Set<Type>,
    @StringRes private val titleId: Int,
    @StringRes private val emptyMessageId: Int,
    private val onSelect: (type: Type) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            var checkedItem = 0
            val builder = AlertDialog.Builder(it)
            val supportedTypes = types.toList()

            if (supportedTypes.isEmpty()) {
                builder.setTitle(titleId)
                    .setMessage(emptyMessageId)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
            } else {
                builder.setTitle(titleId)
                    .setSingleChoiceItems(
                        supportedTypes.map { type -> type.name }.toTypedArray(), checkedItem
                    ) { _, which ->
                        checkedItem = which
                    }
                    .setPositiveButton(titleId) { _, _ ->
                        onSelect(supportedTypes[checkedItem])
                    }
                    .create()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}