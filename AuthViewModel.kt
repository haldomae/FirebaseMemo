package com.example.firebasememo

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    // Firebaseのインスタンスを取得
    // Firebase　Authenticationの入口になる
    // このインスタンスを通じて、ログイン、ログアウト、新規登録の操作を行う
    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    // 画面に公開する状態(UI State)
    // _付きはViewModel内部だけで書き換え可能
    private val _uiState =
        MutableStateFlow<AuthUiState>(
        AuthUiState.Idle
        )
    // _無しは画面に公開する
    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    // 新規登録
    fun register(email: String, password: String){
        // 入力チェック
        if(email.isBlank() || password.isBlank()){
            _uiState.value = AuthUiState.Error(
                "メールアドレスとパスワードを入力してください"
            )
            return
        }

        // 通信開始
        _uiState.value = AuthUiState.Loading

        // Firebaseにメールアドレスとパスワードで新規登録の依頼をする
        // この処理は非同期処理で行われる
        auth.createUserWithEmailAndPassword(
            email,
            password
        )
        // 結果はaddOnSuccessListenerで後から受け取る
        .addOnSuccessListener { result ->
            // 登録成功時
            // 作成されたユーザーのUID(一意のID)を取得
            val uid = result.user?.uid ?: ""
            _uiState.value = AuthUiState.Success(uid)
        }
        .addOnFailureListener { exception ->
            // 登録失敗時
            _uiState.value = AuthUiState.Error(
                exception.message ?: "登録に失敗しました"
            )
        }
    }

    // ログイン処理
    fun login(email: String, password: String){
        // 入力チェック
        if(email.isBlank() || password.isBlank()){
            _uiState.value = AuthUiState.Error(
                "メールアドレスとパスワードを入力してください"
            )
            return
        }

        _uiState.value = AuthUiState.Loading

        // Firebaseにメールアドレスとパスワードでログインするように依頼
        auth.signInWithEmailAndPassword(
            email,
            password
        )
        .addOnSuccessListener { result ->
            // ログイン成功時
            val uid = result.user?.uid ?: ""
            _uiState.value = AuthUiState.Success(uid)
        }
        .addOnFailureListener { exception ->
            // ログイン失敗時
            _uiState.value = AuthUiState.Error(
                exception.message ?: "ログインに失敗しました"
            )
        }
    }

    // 状態のリセット
    // 画面遷移後に前の結果(Success/Error)が残らないようにする
    fun resetState(){
        _uiState.value = AuthUiState.Idle
    }

    // ログアウト
    fun logout(){
        auth.signOut()
    }
}