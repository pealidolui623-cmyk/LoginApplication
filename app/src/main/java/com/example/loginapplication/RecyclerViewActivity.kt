package com.example.loginapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var friendRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycler_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        friendRecyclerView = findViewById(R.id.friendRecyclerView)

        val friendlist = listOf(
            Friend("Peali Dolui", "1h", "5 mutual friends"),
            Friend("Piu Dolui", "5h", "10 mutual friends"),
            Friend("Rakesh Dolui", "6h", "2 mutual friends"),
            Friend("Rohit Malik", "6h", "5 mutual friends"),
            Friend("Rupes Das", "8h", "3 mutual friends"),
            Friend("Rupom Das", "12h", "1 mutual friends"),
            Friend("Rupsha Das", "15h", "20 mutual friends")
        )

        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.adapter = FriendAdapter(friendlist)
    }
}