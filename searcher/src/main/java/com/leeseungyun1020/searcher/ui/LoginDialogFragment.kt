package com.leeseungyun1020.searcher.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.leeseungyun1020.searcher.R
import com.leeseungyun1020.searcher.utilities.Type

class LoginDialogFragment(
    private val loginTypes: Set<Type>,
    private val onSelect: (type: Type) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            var checkedItem = 0
            val builder = AlertDialog.Builder(it)
            val supportedTypes = loginTypes.toList()

            if (supportedTypes.isEmpty()) {
                builder.setTitle(R.string.login)
                    .setMessage(R.string.login_unsupported_error)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
            } else {
                builder.setTitle(R.string.login)
                    .setSingleChoiceItems(
                        supportedTypes.map { type -> type.name }.toTypedArray(), checkedItem
                    ) { _, which ->
                        checkedItem = which
                    }
                    .setPositiveButton(R.string.login) { _, _ ->
                        onSelect(supportedTypes[checkedItem])
                    }
                    .create()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}