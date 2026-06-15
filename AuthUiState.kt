package com.example.firebasememo

// 認証処理の「今の状態」を表すための型
// sealed interface
// -> 4つ以外の状態は存在しない事を保証する
sealed interface AuthUiState {
    // まだ操作していない状態
    data object  Idle : AuthUiState

    // Firebaseと通信中
    // ボタンを押した直後 ~ 結果が返ってくるまで
    data object Loading: AuthUiState

    // 認証成功
    // 成功したユーザーを識別するためのIDを保持
    data class Success(
        val uid: String
    ): AuthUiState

    // 認証失敗
    // エラーメッセージを格納しておく
    data class Error(
        val message: String
    ): AuthUiState
}