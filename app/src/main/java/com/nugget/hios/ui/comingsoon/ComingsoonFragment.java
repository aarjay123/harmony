package com.nugget.hios.ui.comingsoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.R;
import com.nugget.hios.databinding.FragmentSupportBinding;

public class ComingsoonFragment extends Fragment {

    private FragmentSupportBinding binding;

    public static ComingsoonFragment newInstance() {
        return new ComingsoonFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) { //TODO: FINISH ADDING SUPPORT WEBPAGE!!
        binding = FragmentSupportBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        WebView webView = (WebView)root.findViewById(R.id.webView);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        /*if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration .UI_MODE_NIGHT_YES:
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebSettingsCompat.setForceDark(webView.getSettings(), FORCE_DARK_ON);
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

        webView.loadUrl("https://sites.google.com/view/x-by-thc-comingsoon");

        return root;
    }

}