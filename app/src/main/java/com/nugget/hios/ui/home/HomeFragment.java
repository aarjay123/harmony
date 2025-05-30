package com.nugget.hios.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.MainActivity;
import com.nugget.hios.R;
import com.nugget.hios.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ValueCallback<Uri[]> fileChooserCallback;
    private final int FILE_CHOOSER_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = (WebView) root.findViewById(R.id.webView);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        // Set the WebChromeClient only once
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webview, ValueCallback<Uri[]> filePathCallBack, FileChooserParams fileChooserParams) {
                fileChooserCallback = filePathCallBack;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    if (newProgress == 100) {
                        mainActivity.hideProgressBar();
                    } else {
                        mainActivity.showProgressBar();
                        mainActivity.setTheProgress(newProgress);
                    }
                }
            }
        });

        webView.loadUrl("https://thehighlandcafe.github.io/hioswebcore/welcome.html");

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (fileChooserCallback == null) {
                return;
            }
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    Uri[] results = null;
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                    fileChooserCallback.onReceiveValue(results);
                } else {
                    fileChooserCallback.onReceiveValue(null);
                }
            } else {
                fileChooserCallback.onReceiveValue(null);
            }
            fileChooserCallback = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}