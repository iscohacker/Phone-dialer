package uz.iskandarbek.phone.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.iskandarbek.phone.R
import uz.iskandarbek.phone.models.CallHistoryManager

class DialerFragment : Fragment() {

    private lateinit var tvPhoneNumber: TextView
    private lateinit var btnCall: Button
    private lateinit var btnDelete: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        btnCall = view.findViewById(R.id.btnCall)
        btnDelete = view.findViewById(R.id.btnDelete)

        val numberButtons = listOf<Button>(
            view.findViewById(R.id.button1),
            view.findViewById(R.id.button2),
            view.findViewById(R.id.button3),
            view.findViewById(R.id.button4),
            view.findViewById(R.id.button5),
            view.findViewById(R.id.button6),
            view.findViewById(R.id.button7),
            view.findViewById(R.id.button8),
            view.findViewById(R.id.button9),
            view.findViewById(R.id.button0)
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                tvPhoneNumber.append((it as Button).text)
            }
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
                addToCallHistory(phoneNumber)  // Add this line
                makePhoneCall(phoneNumber)
            } else {
                Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
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
}