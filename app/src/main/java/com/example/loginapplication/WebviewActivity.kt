package com.example.loginapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WebviewActivity : AppCompatActivity() {

    private lateinit var webview: WebView
    private lateinit var swiperefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_webview)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webview = findViewById<WebView>(R.id.webview)
        swiperefresh = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.loadUrl("https://www.debracollege.ac.in")

        swiperefresh.setOnRefreshListener {
            webview.reload()
        }

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swiperefresh.isRefreshing = false
            }
        }
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
