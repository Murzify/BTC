package ru.murzify.btc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Utils {
    // вибрация при копировании
    @Suppress("DEPRECATION")
    private fun vibratePhone(activity: FragmentActivity) {
        val v = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 31){
            // android 12+
            val vibratorManager = activity.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.CONTENTS_FILE_DESCRIPTOR))
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // android 8+
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.CONTENTS_FILE_DESCRIPTOR))
        } else {
            //deprecated in API 26
            v.vibrate(200)
        }
    }

    // копирование текста
    private fun copy(clipboard: ClipboardManager, text: String){
        val clip: ClipData = ClipData.newPlainText("data", text)
        clipboard.setPrimaryClip(clip)
    }

    // слушатель долгого нажатия для копирования
    fun onCopyClickListener(activity: FragmentActivity, clipboard: ClipboardManager) = View.OnLongClickListener{ v ->
        val text = activity.findViewById<TextView>(v.id).text.toString()
        copy(clipboard, text)
        vibratePhone(activity)
        true
    }

    fun validSearch(search: String): String {
        val addressRegex = Regex("^(bc1|[13])[a-zA-HJ-NP-Z\\d]{25,39}$")
        val transactionRegex = Regex("^[a-fA-F\\d]{64}\$") // также подходит для хэша блока
        val blockHashRegex = Regex("^0{8}[a-fA-F\\d]{56}\$")
        val blockHeightRegex = Regex("^(0|[1-9]\\d*)\$")

        return if (search.matches(addressRegex)){
            "address"
        } else if (search.matches(blockHashRegex) || search.matches(blockHeightRegex)){
            "block"
        } else if (search.matches(transactionRegex)){
            "transaction"
        } else {
            "invalid"
        }
    }

    fun timeFormat(time: Long): String{
        return Timestamp(time * 1000)
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
    }
}