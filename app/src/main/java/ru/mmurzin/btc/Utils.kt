package ru.mmurzin.btc

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
    fun copy(clipboard: ClipboardManager, text: String){
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
}