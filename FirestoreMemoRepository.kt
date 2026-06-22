package com.example.firebasememo

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


interface FirestoreMemoRepository: MemoRepository {
    // FireStoreの入口を取得
    private val db = Firebase.firestore

    override suspend fun addMemo(memo: Memo) {
        // memosコレクションに新しいドキュメントを1件追加
        db.collection("memos").add(memo).await()
    }
}