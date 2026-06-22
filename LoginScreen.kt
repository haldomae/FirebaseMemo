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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    // NavHostControllerを受け取れるようにする
    navController: NavHostController,
    // viewModel()を使用することによって
    // この画面専用のAuthViewModelインスタンスが
    // 自動的に作成・管理される
    viewModel: AuthViewModel = viewModel()
){
    // メールアドレスとパスワードを取得
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // uiStateをComposableで使えるようにする
    // Stateの状態が変わったときに画面を再描画する
    val uiState by viewModel.uiState.collectAsState()

    // uiStateの変化を監視し、ログイン成功時に画面遷移する
    LaunchedEffect(uiState) {
        // ログイン成功
        if(uiState is AuthUiState.Success){
            navController.navigate(Routes.MEMO_LIST){
                // ログイン画面・新規登録画面をバックスタックから削除
                popUpTo(0)
            }
            viewModel.resetState()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ログイン",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // メールアドレスt、パスワード入力欄
        OutlinedTextField(
            value = email,
            onValueChange = {
                // テキストフィールドが変更される度に実行
                email = it
            },
            label = {
                Text(
                    text = "メールアドレス"
                )
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        // パスワード
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(
                    text = "パスワード"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ログインボタン
        Button(
            onClick = {
                viewModel.login(
                    email,
                    password
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ログイン"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 新規登録へのリンク
        // TextButton()
        // -> リンクのようなボタンになる
        TextButton(
            onClick = {
                // 新規登録画面に遷移
                navController.navigate(Routes.REGISTER)
            }
        ) {
            Text(
                text = "アカウントをお持ちでない方はこちら(新規登録)"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 現在の状態(uiState)によって表示を切り替える
        // 4つの状態を網羅しないとエラーになる
        when(val state = uiState){
            is AuthUiState.Idle -> {
                // 初期状態では何もしない
            }
            is AuthUiState.Loading -> {
                // 通信中ではローディング
                CircularProgressIndicator()
            }
            is AuthUiState.Success -> {
                // 成功時
                Text(
                    text = "ログイン成功 : ${state.uid}"
                )
            }
            is AuthUiState.Error -> {
                // 失敗時
                Text(
                    text = "エラー : ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}