package com.example.firebasememo

// メモ投稿処理状態を表す
sealed interface MemoUiState {
    // 初期状態
    data object Idle: MemoUiState
    // 投稿処理
    data object Loading: MemoUiState
    // 投稿成功
    data object Success: MemoUiState
    // 投稿失敗
    data class Error(
        val message: String
    ): MemoUiState
}