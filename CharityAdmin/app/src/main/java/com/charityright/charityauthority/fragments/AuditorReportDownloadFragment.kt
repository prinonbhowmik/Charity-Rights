package com.charityright.charityauthority.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.charityright.charityauthority.R
import com.charityright.charityauthority.databinding.FragmentAuditorReportDownloadBinding
import kotlinx.coroutines.launch
import java.lang.Exception
import android.widget.Toast
import android.content.Context.DOWNLOAD_SERVICE
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


class AuditorReportDownloadFragment : Fragment() {

    private var _binding: FragmentAuditorReportDownloadBinding? = null
    private val binding get() = _binding!!

    private var downloadUrl = ""
    private var previewUrl = ""
    private var tileData = ""

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(requireContext(),"Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission request was denied.
                Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuditorReportDownloadBinding.inflate(inflater,container,false)

        try {
            val downloadData = requireArguments().getString("url","")
            val previewData = requireArguments().getString("preview_url","")
            tileData = requireArguments().getString("title","")
            downloadUrl =  resources.getString(R.string.base_url) + downloadData
            previewUrl =  resources.getString(R.string.base_url) + previewData
            binding.webView.loadUrl(previewUrl)
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webChromeClient = object: WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)

                    binding.progressBar.isVisible = newProgress < 100
                    binding.progressBar.progress = newProgress
                }
            }
        }catch (e: Exception){
            Log.wtf("AuditorReportDownload", e.message)
        }

        binding.downloadBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                lifecycleScope.launch {
                    DownloadFromUrl(downloadUrl,tileData)
                }

            } else {
                requestPermission()
            }

        }

        return binding.root
    }

    fun DownloadFromUrl(downloadLink: String, tileData: Any?) {

        val request = DownloadManager.Request(
            Uri.parse(downloadLink)
        )

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$tileData.pdf")
        val dm = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
        dm!!.enqueue(request)
        Toast.makeText(requireContext(), "Downloading File", Toast.LENGTH_LONG).show()

    }

    private fun requestPermission() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}