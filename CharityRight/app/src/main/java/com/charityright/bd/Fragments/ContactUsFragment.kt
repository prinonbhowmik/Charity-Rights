package com.charityright.bd.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charityright.bd.databinding.FragmentContactUsBinding
import android.content.Intent
import android.net.Uri
import java.lang.Exception
import android.content.ActivityNotFoundException

class ContactUsFragment : Fragment() {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater,container,false)

        binding.facebookBtn.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/charity.right.bangladesh"))
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/charity.right.bangladesh"))
                startActivity(intent)
            }
        }

        binding.whatsAppBtn.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+8801675226740"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.linkedinBtn.setOnClickListener {
            val uri = Uri.parse("https://www.linkedin.com/company/charity-right-bangladesh/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.linkedin.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/company/charity-right-bangladesh/")
                    )
                )
            }
        }

        binding.youtubeBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setPackage("com.google.android.youtube")
            intent.data = Uri.parse("https://youtu.be/I9bOGCZHWxE")
            startActivity(intent)

        }

        binding.emailTV.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:info@charityright.com.bd")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }

        binding.phoneTV1.setOnClickListener {
            val phone = "+8801756354800"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }

        binding.phoneTV2.setOnClickListener {
            val phone = "+8801675226740"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}