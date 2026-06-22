package com.example.firebasememo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebasememo.ui.theme.FirebaseMemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseMemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 個別に画面を表示するのではなく、
                    // NavHostを呼び出し、画面遷移は任せる
                    AppNavHost()
                }
            }
        }
    }
}