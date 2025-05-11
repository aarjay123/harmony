package com.nugget.hios.ui.preferences;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.R;
import com.nugget.hios.databinding.FragmentRestauranthelpBinding;

public class AppearanceFragment extends Fragment {

    private FragmentRestauranthelpBinding binding;
    private ValueCallback<Uri[]> fileChooserCallback;
    private final int FILE_CHOOSER_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRestauranthelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = (WebView)root.findViewById(R.id.webView);

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
                                   });


        /*if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration .UI_MODE_NIGHT_YES:
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebRestaurantCompat.setForceDark(webView.getRestaurant(), FORCE_DARK_ON);
                    break;
            }
        }*/

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //view.loadUrl(url);
                System.out.println("Never gonna give you up");
                return false;
            }
        });

        webView.loadUrl("https://thehighlandcafe.github.io/hioswebcore/activities/settingsActivity/settings_activities/appearance_activity.html");

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