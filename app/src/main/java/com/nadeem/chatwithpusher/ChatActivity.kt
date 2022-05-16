package com.nadeem.chatwithpusher

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nadeem.chatwithpusher.model.Message
import com.nadeem.chatwithpusher.model.MessageRequest
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PusherEvent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val TAG = "ChatActivity"
private const val PUSHER_APP_CLUSTER = "ap1"
private const val PUSHER_APP_KEY = "19fd8908950da6443574"
private const val PUSHER_TOPIC = "2"
private const val PUSHER_EVENT = "chat-event"
private const val TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZTYyMWFlYWU0ZjM5YTMzOTRlNDY1YzI4NTQwMDg4Y2FiNzE0OWViYjBlYTlkZDBlZWJlOTJjZTBhYjU3MjBjOTI2OWNiMDU4Y2MzMzlhN2UiLCJpYXQiOjE2NTI0MzA0MDMuNTY4NTE5LCJuYmYiOjE2NTI0MzA0MDMuNTY4NTIyLCJleHAiOjE2ODM5NjY0MDMuNTY3NDE4LCJzdWIiOiIyNyIsInNjb3BlcyI6W119.Y6ehaMJuPW4aASd0PKX18Fh_xXO5Z8DjiU34f8vS34TFU6mA14n1sAIK218DLutVSlnxFEgUYjqenl0swxuDL4IB9uTj5bhYt6KJ4oO-WoglN1Nb1PaVv3lEKu_FPPf6_ArRnD22rqa3WZZpn4LxA2VxhAi2QVg_c3QpYWJOVKmuMfIO50Rs1HabdCTEJnsJ2ZPfAJjBLmQr435HeiMQ9yFHbSeaiIqFu12QiZApHVFEHAH0BRoiJU4QBSlbHmxPZ4hnMNAEe5YYXisMQ8Jb9MRxoN_-oVsTBqWaWI9j8KihQxD28uzB0SXT97Ph-oUFfjMf04m2Nkq38U_BOmVU3759u5lD4emXu-iV7BCMG95j2u9-rQUnbtLM5hsxGucSAaVe6X4QZEWsJxl4Wir82975tFCS9De0SGb1V8Nj0ReB47FusRaALn9HUp_sfhX592Dx9iLzCRYDP8cLnI8GE2ju8TtKWnUXjT8WpRCy1IRiIlVYemKf10jW8Bgbbq6zlMyhA9mzPzibl76c-3Fmr2redDlCvEugc7ZUOp3XnxmpgguVkezRtZGZhzpVa3XTK6OiRjyEyhLaFoFjO3diF_KGh2OQeyaDY0f4LfhstBSTpt1acaG9ShVE3fnme_vn0jxzmu41A9eQsjwkAeKaguGL4T-Nmk1T_EGlrN0yJ9M"
class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var txtMessage : EditText
    private lateinit var messagesRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagesRecyclerView = findViewById<RecyclerView>(R.id.messageList)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this)
        messagesRecyclerView.adapter = adapter
        setupPusher()
        txtMessage = findViewById(R.id.txtMessage)

        findViewById<Button>(R.id.btnSend).setOnClickListener {
            if(txtMessage.text.isNotEmpty()) {
                val message = MessageRequest(
                    conversationId = "2",
                    receiverId = "27",
                    message = "test message"
                )

                val call = ChatService.create().postMessage(token = TOKEN, body = message)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        resetInput()
                        if (!response.isSuccessful) {
                            Log.e(TAG, response.code().toString());
                            Toast.makeText(applicationContext,"Response was not successful", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        resetInput()
                        Log.e(TAG, t.toString());
                        Toast.makeText(applicationContext,"Error when calling the service", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext,"Message should not be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        val inputManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster(PUSHER_APP_CLUSTER)

        val pusher = Pusher(PUSHER_APP_KEY, options)
        pusher.connect()
        val channel = pusher.subscribe(PUSHER_TOPIC)

        channel.bind(PUSHER_EVENT) { pusherEvent: PusherEvent ->
            val jsonObject = JSONObject(pusherEvent.data)

            val message = Message(
                message = jsonObject["message"].toString()
            )

            runOnUiThread {
                adapter.addMessage(message)
                // scroll the RecyclerView to the last added element
                messagesRecyclerView.scrollToPosition(adapter.itemCount - 1);
            }

        }

     /*   channel.bind(PUSHER_EVENT, SubscriptionEventListener { pusherEvent: PusherEvent ->
            pusherEvent.data
        })
        */
        pusher.connect()
    }
}