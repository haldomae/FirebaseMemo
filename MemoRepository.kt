package com.example.firebasememo

import kotlinx.coroutines.flow.Flow

// メモの保存・取得を行うための窓口
// interfaceを使うとテスト時のダミー実装に差し替えることもできる
interface MemoRepository {
    // メモを保存する処理
    suspend fun addMemo(memo: Memo)

    // 指定したユーザのメモ一覧をリアルタイムで監視
    // suspend fun(1度だけ値を返す)ではなく、Flow(継続的)に監視する
    // FireStoreの値が変化する度に新しい値が流れる
    fun observeMemos(userId: String): Flow<List<Memo>>
}