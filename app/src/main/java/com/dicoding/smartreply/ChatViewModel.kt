package com.dicoding.smartreply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class ChatViewModel : ViewModel() {

    private val anotherUserID = "101"

    private val _chatHistory = MutableLiveData<ArrayList<Message>>()
    val chatHistory: LiveData<ArrayList<Message>> = _chatHistory

    private val _pretendingAsAnotherUser = MutableLiveData<Boolean>()
    val pretendingAsAnotherUser: LiveData<Boolean> = _pretendingAsAnotherUser

    private val smartReply: SmartReplyGenerator = SmartReply.getClient()

    // MediatorLiveData adalah sebuah LiveData yang dapat kita gunakan untuk melakukan aksi observe pada objek LiveData lainnya
    // dan menghasilkan data baru jika terdapat perubahan nilai pada objek-objek tersebut.
    private val _smartReplyOptions = MediatorLiveData<List<SmartReplySuggestion>>()
    val smartReplyOptions: LiveData<List<SmartReplySuggestion>> = _smartReplyOptions

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        initSmartReplyOptionsGenerator()
        _pretendingAsAnotherUser.value = false
    }

    fun switchUser() {
        clearSmartReplyOptions() // biar opsi balasan jawaban tidak muncul ketika udah dipilih
        val value = _pretendingAsAnotherUser.value!!
        _pretendingAsAnotherUser.value = !value
    }

    fun setMessages(messages: ArrayList<Message>) {
        clearSmartReplyOptions()
        _chatHistory.value = messages
    }

    fun addMessage(message: String){

        val user = _pretendingAsAnotherUser.value!!

        var list: ArrayList<Message> = chatHistory.value ?: ArrayList()
        list.add(Message(message, !user, System.currentTimeMillis()))

        clearSmartReplyOptions()

        _chatHistory.value = list

    }

    private fun initSmartReplyOptionsGenerator(){
        _smartReplyOptions.addSource(pretendingAsAnotherUser){ isPretendingAsAnotherUser ->
            val list = chatHistory.value

            // jika aplikasi mendeteksi pesan baru ataupun pergantian pengguna aplikasi
            if (list.isNullOrEmpty()) {
                return@addSource
            } else {
                generateSmartReplyOptions(list, isPretendingAsAnotherUser)
                    .addOnSuccessListener { result ->
                        _smartReplyOptions.value = result
                    }
            }


        }

        _smartReplyOptions.addSource(chatHistory){ conversations ->
            val isPretendingAsAnotherUser = pretendingAsAnotherUser.value


            if (isPretendingAsAnotherUser != null && conversations.isNullOrEmpty()){
                return@addSource
            }else{
                generateSmartReplyOptions(conversations, isPretendingAsAnotherUser!!)
                    .addOnSuccessListener { result ->
                        _smartReplyOptions.value = result
                    }

            }

        }
    }

    // menghapus opsi balasan jawaban
    private fun clearSmartReplyOptions(){
        _smartReplyOptions.value = ArrayList()
    }

    //  memberikan nilai kembalian berupa opsi balasan jawaban
    private fun generateSmartReplyOptions(
        messages: List<Message>,
        isPretendingAsAnotherUser: Boolean
    ): Task<List<SmartReplySuggestion>> {
        val lastMessage = messages.last()

        // opsi jawaban balasan jika pesan terakhir berasal dari pengguna saat ini.
        if (lastMessage.isLocalUser != isPretendingAsAnotherUser){
            return Tasks.forException(Exception("Tidak menjalankan smart reply!"))
        }

        // mengubah bentuk data pesan dari Message ke TextMessage
        val chatConversations = ArrayList<TextMessage>()
        for (message in messages){
            if (message.isLocalUser != isPretendingAsAnotherUser){
                chatConversations.add(TextMessage.createForLocalUser(message.text, message.timestamp))
            } else {
                chatConversations.add(
                    TextMessage.createForRemoteUser(message.text, message.timestamp, anotherUserID)
                )
            }
        }

        // panggil metode suggestReplies untuk menghasilkan opsi balasan
        return smartReply
            .suggestReplies(chatConversations)
            .continueWith{ task ->
                val result = task.result
                when (result.status){
                    SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE ->
                        _errorMessage.value = "Unable to generate options due to a non-English language was used"
                    SmartReplySuggestionResult.STATUS_NO_REPLY ->
                        _errorMessage.value = "Unable to generate options due to no appropriate response found"
                }
                result.suggestions
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "An error has occurred on Smart Reply Instance"
            }
    }

    override fun onCleared() {
        super.onCleared()
        smartReply.close()
    }

    companion object {
        private const val TAG = "ChatViewModel"
    }

}
