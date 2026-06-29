package com.example.firebasememo


import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// FirebaseのTimeStampを画面表示用の文字列にする
fun formatTimestamp(timestamp: Timestamp?) : String{
    if(timestamp == null){
        return ""
    }
    val sdf = SimpleDateFormat(
        "yyyy/MM/dd HH:mm",
        Locale.JAPAN)
    return sdf.format(timestamp.toDate())
}