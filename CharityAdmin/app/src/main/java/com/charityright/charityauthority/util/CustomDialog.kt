package com.charityright.charityauthority.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.charityright.charityauthority.R

object CustomDialog {

    private lateinit var progressDialog: Dialog

    fun init(ctx: Context?) {
        progressDialog = Dialog(ctx!!)
        progressDialog.setContentView(R.layout.progress_loader)
        progressDialog.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
    }

    fun show() {
        try {
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }
        } catch (e: Exception) {

        }
    }

    fun dismiss() {
        try {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
        }
    }

}