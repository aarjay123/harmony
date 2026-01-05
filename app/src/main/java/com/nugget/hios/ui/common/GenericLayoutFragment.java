package com.nugget.hios.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GenericLayoutFragment extends Fragment {

    private static final String ARG_LAYOUT = "arg_layout";
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_SCREEN = "arg_screen";

    public static GenericLayoutFragment newInstance(
            @LayoutRes int layoutResId,
            @NonNull String title,
            @NonNull String screenKey
    ) {
        GenericLayoutFragment fragment = new GenericLayoutFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_LAYOUT, layoutResId);
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_SCREEN, screenKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        int layoutRes = requireArguments().getInt(ARG_LAYOUT);
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String screenKey = requireArguments().getString(ARG_SCREEN, "");

        ScreenBinder.bind(screenKey, view, requireActivity());
    }
}
