package com.example.firebasememo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// メモ投稿機能を扱うViewModel

class MemoViewModel(
    private val repository:
    FirestoreMemoRepository
    = FirestoreMemoRepository()
): ViewModel() {
    // 外部向け、内部向けで別で作成
    private val _uiState
    = MutableStateFlow<MemoUiState>(MemoUiState.Idle)
    val uiState: StateFlow<MemoUiState>
    = _uiState.asStateFlow()

    // 現在ログイン中のユーザ
    private val currentUserId: String
    = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // メモをリアルタイムで受け取るStateFlow
    val memos: StateFlow<List<Memo>>
    = repository.observeMemos(currentUserId)
        .stateIn(
            // 常に最新の値を持つようにする
            scope = viewModelScope,
            // このViewModelが生きている間だけFlowを購読
            // 画面が閉じたら自動的に読むことをやめる
            // StateFlowを読んでいるComposeが1つもなくなってから
            // 5秒後にFlowの収集を停止する
            started = SharingStarted
                .WhileSubscribed(5000),
            // Firebaseから最初のデータが届く前に画面が表示される瞬間の初期値
            initialValue = emptyList()
        )

    // メモ投稿
    fun postMemo(text: String){
        // 入力チェック(空欄チェック)
        if(text.isBlank()){
            _uiState.value = MemoUiState.Error("メモを入力してください")
            return
        }

        // 現在ログイン中のユーザIDを取得
        // メモ投稿時には必ずログインされているはずだが、念のためチェック
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null){
            _uiState.value = MemoUiState.Error("ログイン情報が取得できません")
            return
        }
        _uiState.value = MemoUiState.Loading

        // コルーチン起動
        // viewModelScope.launch
        // -> ViewModelで非同期処理を行うために必要
        viewModelScope.launch {
            try{
                // メモ作成
                val memo = Memo(
                    userId = uid,
                    text = text,
                    createdAt = Timestamp.now()
                )
                // データ登録の関数を実行する
                repository.addMemo(memo)

                // 状態を変更
                _uiState.value = MemoUiState.Success
            }catch (e: Exception){
                _uiState.value = MemoUiState.Error(
                    e.message ?: "投稿に失敗しました"
                )
            }
        }
    }

    // 状態を初期化する関数
    // 投稿成功時、入力欄をクリアして次の投稿に備える
    fun resetState(){
        _uiState.value = MemoUiState.Idle
    }
}