package com.nugget.hios.screens

import android.content.res.Configuration
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.nugget.hios.R

@Composable
fun HomeScreen(navController: NavHostController) {
    webView(url = "https://thehighlandcafe.github.io/hioswebcore/welcome.html")
}

@Composable
fun webView(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true

                setBackgroundColor(ContextCompat.getColor(context, R.color.black))

                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}