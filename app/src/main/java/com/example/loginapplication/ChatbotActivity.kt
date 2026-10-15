package com.example.loginapplication

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class ChatbotActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etmessage: EditText
    private lateinit var btnsend: CardView
    private lateinit var btnvoice: CardView

    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<ChatModel>()

    private val REQUEST_CODE_SPEECH_INPUT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        recyclerView = findViewById(R.id.recyclerView)
        etmessage = findViewById(R.id.etmessage)
        btnsend = findViewById(R.id.btnsend)
        btnvoice = findViewById(R.id.btnvoice)

        chatAdapter = ChatAdapter(chatList, this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        btnsend.setOnClickListener {
            val message = etmessage.text.toString().trim()

            if (message.isNotEmpty()) {
                chatList.add(ChatModel(message, true))
                chatAdapter.notifyItemInserted(chatList.size - 1)
                recyclerView.scrollToPosition(chatList.size - 1)

                etmessage.setText("")
                apicall(message)
            }
        }

        btnvoice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak your message"
            )

            try {
                startActivityForResult(
                    intent,
                    REQUEST_CODE_SPEECH_INPUT
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Voice input is not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT &&
            resultCode == RESULT_OK &&
            data != null
        ) {
            val result = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )

            if (!result.isNullOrEmpty()) {
                etmessage.setText(result[0])
            }
        }
    }

    private fun apicall(prompt: String) {

        val url = "https://api.groq.com/openai/v1/chat/completions"

        val GROQ_API_KEY = "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"

        val jsonBody = JSONObject().apply {
            put("model", "openai/gpt-oss-120b")

            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put(
                        "content",
                        "You are a friendly AI chatbot. " +
                                "Talk naturally like a real conversation partner. " +
                                "If the user says hello, greet them warmly. " +
                                "Give clear and helpful answers in simple language."
                    )
                })

                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })

            put("temperature", 0.7)
            put("max_completion_tokens", 1024)
            put("stream", false)
        }

        val request = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,

            { response ->
                try {
                    val choices = response.getJSONArray("choices")
                    val message = choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")

                    chatList.add(ChatModel(message.trim(), false))
                    chatAdapter.notifyItemInserted(chatList.size - 1)
                    recyclerView.scrollToPosition(chatList.size - 1)

                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Could not read the AI response",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            { error ->

                val statusCode = error.networkResponse?.statusCode
                val errorDetails = error.networkResponse?.data?.let {
                    String(it, Charsets.UTF_8)
                }

                val errorMessage = if (statusCode != null) {
                    "Error $statusCode: $errorDetails"
                } else {
                    "Network error: ${error.message ?: error.toString()}"
                }

                chatList.add(ChatModel(errorMessage, false))
                chatAdapter.notifyItemInserted(chatList.size - 1)
                recyclerView.scrollToPosition(chatList.size - 1)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to "Bearer $GROQ_API_KEY"
                )
            }
        }

        request.retryPolicy = DefaultRetryPolicy(
            20000,
            1,
            1.0f
        )

        Volley.newRequestQueue(this).add(request)
    }
}
