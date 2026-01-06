package com.nugget.hios.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.R;

public class WebViewFragment extends Fragment {

    private static final String ARG_URL = "url";
    private static final int FILE_CHOOSER_REQUEST_CODE = 1001;

    private WebView webView;
    private View offlineOverlay;
    private ValueCallback<Uri[]> fileChooserCallback;

    public static WebViewFragment newInstance(String url) {
        WebViewFragment f = new WebViewFragment();
        Bundle b = new Bundle();
        b.putString(ARG_URL, url);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = root.findViewById(R.id.webView);
        offlineOverlay = root.findViewById(R.id.offline_overlay);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                setOfflineVisible(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    setOfflineVisible(true);
                    view.stopLoading();
                    view.loadUrl("about:blank");
                }
            }
        });

        String url = getArguments() != null ? getArguments().getString(ARG_URL) : null;
        if (url != null) webView.loadUrl(url);

        return root;
    }

    private void setOfflineVisible(boolean visible) {
        if (offlineOverlay != null) offlineOverlay.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (webView != null) webView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        offlineOverlay = null;
    }
}
