package com.example.firebasememo

// メモの保存・取得を行うための窓口
// interfaceを使うとテスト時のダミー実装に差し替えることもできる
interface MemoRepository {
    // メモを保存する処理
    suspend fun addMemo(memo: Memo)
}