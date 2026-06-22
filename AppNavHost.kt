package com.example.firebasememo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(){
    // NaVControllerを生成
    val navController = rememberNavController()

    // FirebaseAuthの現在のユーザ(CurrentUser)を確認
    // ログイン状態によって画面を切り替える
    // ログイン済
    // -> memo_list
    // 未ログイン
    // -> login
    // この判定は「Composable」が最初に呼ばれたタイミングで1度だけ行う
    var startDestination = if (FirebaseAuth.getInstance().currentUser != null){
        // ログイン済
        Routes.MEMO_LIST
    } else {
        // 未ログイン
        Routes.LOGIN
    }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        // ログイン画面
        composable(Routes.LOGIN){
            LoginScreen(
                navController = navController
            )
        }

        // 新規登録
        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController
            )
        }

        // メモ一覧画面
        composable(Routes.MEMO_LIST) {
            MemoListScreen(
                navController = navController
            )
        }
    }


}