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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel()
){
    // メールアドレスとパスワードを取得
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // uiStateをComposableで使えるようにする
    // Stateの状態が変わったときに画面を再描画する
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "新規登録",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // メールアドレス、パスワード入力欄
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
                    text = "パスワード(6文字以上)"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 新規登録ボタン
        Button(
            onClick = {
                viewModel.register(
                    email,
                    password
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "新規登録"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "登録成功 : ${state.uid}"
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