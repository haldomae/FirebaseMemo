package com.example.firebasememo

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await


class FirestoreMemoRepository: MemoRepository {
    // FireStoreの入口を取得
    private val db = Firebase.firestore

    override suspend fun addMemo(memo: Memo) {
        // memosコレクションに新しいドキュメントを1件追加
        db.collection("memos").add(memo).await()
    }

    // 指定ユーザのメモ一覧をリアルタイムで監視する
    override fun observeMemos(userId: String): Flow<List<Memo>> {
        val listener = db.collection("memos")
            // ユーザIDで絞り込み
            .whereEqualTo(
                "userId",
                userId)
            // 並べ替え
            .orderBy(
                "createdAt",
                Query.Direction.DESCENDING)
            // 監視する関数を設定
            .addSnapshotListener { snapshots, error ->
                if(error != null){
                    Log.e(
                        "FirestoreMemoRepo",
                        "メモ一覧の取得でエラーが発生しました",
                        error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // snapshotがnull(ドキュメントが1件もない場合)
                if(snapshots == null){
                    // 空のリストを送る
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // FirestoreのドキュメントをMemoのListに変換
                // 変換する際にdata classとFirestoreのキーを合わせる
                val memos = snapshots.toObjects(Memo::class.java)
                trySend(memos)
            }
        // 後片付け
        // リスナーを解除して不要なデータ通信を止める
        awaitClose{
            listener.remove()
        }
    }
}