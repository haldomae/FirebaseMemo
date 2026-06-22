package com.example.firebasememo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MemoListScreen(
    // 画面遷移を行えるようにする
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
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

        // ログアウトボタン
        Button(
            onClick = {
                // 1. Firebaseのログイン状態を破棄
                viewModel.logout()
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