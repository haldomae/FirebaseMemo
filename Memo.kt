package com.example.firebasememo

import com.google.firebase.Timestamp

data class Memo(
    // メモを投稿したユーザID(UID(一意なID))
    val userId: String = "",
    // メモの本文
    val text: String = "",
    // 投稿日時
    // Timestamp
    // -> Firebase専用の日時型
    val createdAt: Timestamp? = null
)
