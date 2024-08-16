package uz.iskandarbek.phone.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.iskandarbek.phone.R
import uz.iskandarbek.phone.models.CallHistoryManager

class DialerFragment : Fragment() {
    private lateinit var tvPhoneNumber: TextView
    private lateinit var btnCall: ImageView
    private lateinit var btnDelete: ImageView
    private lateinit var nol: CardView
    private lateinit var soundPool: SoundPool
    private var clickSoundId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        btnCall = view.findViewById(R.id.btnCal)
        btnDelete = view.findViewById(R.id.btnDelete)
        nol = view.findViewById(R.id.btn_0)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()

        clickSoundId = soundPool.load(requireContext(), R.raw.click, 1)

        val numberButtons = listOf<CardView>(
            view.findViewById(R.id.btn_1),
            view.findViewById(R.id.btn_2),
            view.findViewById(R.id.btn_3),
            view.findViewById(R.id.btn_4),
            view.findViewById(R.id.btn_5),
            view.findViewById(R.id.btn_6),
            view.findViewById(R.id.btn_7),
            view.findViewById(R.id.btn_8),
            view.findViewById(R.id.btn_9),
            view.findViewById(R.id.btn_0)
        )

        for (cardView in numberButtons) {
            cardView.setOnClickListener {
                if (tvPhoneNumber.length() == 14) {
                    Toast.makeText(requireContext(), "Raqamlar limitga yetdi", Toast.LENGTH_SHORT).show()
            } else {
                    soundPool.play(clickSoundId, 1f, 1f, 0, 0, 1f)
                    val textView = cardView.findViewById<TextView>(R.id.tv)
                    val text = textView.text.toString()
                    tvPhoneNumber.append(text)
                }
            }
        }

        nol.setOnLongClickListener {
            val currentText = tvPhoneNumber.text.toString()
            if (!currentText.contains("+")) {
                tvPhoneNumber.append("+")
            }
            true
        }

        btnDelete.setOnClickListener {
            val currentText = tvPhoneNumber.text.toString()
            if (currentText.isNotEmpty()) {
                tvPhoneNumber.text = currentText.dropLast(1)
            }
        }

        btnCall.setOnClickListener {
            val phoneNumber = tvPhoneNumber.text.toString()
            if (phoneNumber.isNotEmpty()) {
                addToCallHistory(phoneNumber)
                makePhoneCall(phoneNumber)
            } else {
                Toast.makeText(requireContext(), "Iltimos, telefon raqam yozing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(dialIntent)
        }
    }

    private fun addToCallHistory(phoneNumber: String) {
        val callHistoryManager = CallHistoryManager(requireContext())
        callHistoryManager.addCallToHistory(phoneNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}