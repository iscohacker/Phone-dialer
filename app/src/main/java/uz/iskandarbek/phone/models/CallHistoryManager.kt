package uz.iskandarbek.phone.models

import android.content.Context
import android.content.SharedPreferences

class CallHistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CallHistoryPrefs", Context.MODE_PRIVATE)

    fun addCallToHistory(phoneNumber: String) {
        val history = getCallHistory().toMutableList()
        history.add(phoneNumber)
        sharedPreferences.edit().putStringSet("history", history.toSet()).apply()
    }

    fun getCallHistory(): List<String> {
        return sharedPreferences.getStringSet("history", emptySet())?.toList() ?: emptyList()
    }

    fun removeCallFromHistory(phoneNumber: String) {
        val history = getCallHistory().toMutableList()
        history.remove(phoneNumber)
        sharedPreferences.edit().putStringSet("history", history.toSet()).apply()
    }

}