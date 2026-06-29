package com.example.firebasememo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MemoListScreen(
    // 画面遷移を行えるようにする
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    // メモ投稿用のモデル
    memoViewModel: MemoViewModel = viewModel()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // メモ投稿フォームに入力中のテキストを保持する変数
        var memoText by remember { mutableStateOf("") }

        // 投稿の処理状態(idle,Loading,Success, Error)
        val memoUiState by memoViewModel.uiState.collectAsState()

        // 投稿成功時の処理
        // 投稿が成功(Success)したら、入力欄を空にする
        LaunchedEffect(memoUiState) {
            if(memoUiState is MemoUiState.Success){
                memoText = ""
                memoViewModel.resetState()
            }
        }

        Text(
            text = "メモ一覧",
            style = MaterialTheme
                .typography
                .headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 現在ログイン中のユーザ情報
        // FirebaseAuth.getInstance().currentUser
        // -> 現在ログイン中のユーザ情報
        val currentEmail = FirebaseAuth
            .getInstance().currentUser?.email ?: "不明"
        Text(text = "ログイン中 : ${currentEmail}")

        Spacer(modifier = Modifier.height(16.dp))

        // メモ入力欄
        OutlinedTextField(
            value = memoText,
            onValueChange = {
                memoText = it
            },
            label = {
                Text(text = "一言メモ")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 投稿ボタン
        Button(
            onClick = {
                memoViewModel.postMemo(memoText)
            },
            // 投稿処理中にボタンを押せないようにする
            enabled = memoUiState !is MemoUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "投稿する")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 投稿処理の状態表示
        when(val state = memoUiState){
            is MemoUiState.Idle -> {
                // 何もしない
            }
            is MemoUiState.Loading -> CircularProgressIndicator()
            is MemoUiState.Success -> {
                // 入力欄クリア後にすぐにIdleに戻る
            }
            is MemoUiState.Error -> {
                Text(
                    text = "エラー : ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // ログアウトボタン
        Button(
            onClick = {
                // 1. Firebaseのログイン状態を破棄
                authViewModel.logout()
                // 2. ログイン画面へ遷移
                navController.navigate(Routes.LOGIN){
                    // 3. メモ一覧をバックスタックから削除
                    // ログアウト後の戻るでメモ一覧に戻らないようにする
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "ログアウト")
        }
    }
}