package com.nugget.hios.ui.hiclubsettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.HiClubSettingsActivity;
import com.nugget.hios.R;

public class HomeSettingsFragment extends Fragment {

    public HomeSettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // This should be the layout file that contains your big "Preferences" page UI
        // (the XML you originally had inside settings_container).
        return inflater.inflate(R.layout.fragment_home_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Appearance ---
        View btnAppearance = view.findViewById(R.id.btn_appearance);
        if (btnAppearance != null) {
            btnAppearance.setOnClickListener(v -> {
                if (getActivity() instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) getActivity()).openAppearance();
                }
            });
        }

        // --- Updates ---
        View btnUpdates = view.findViewById(R.id.btn_updates);
        if (btnUpdates != null) {
            btnUpdates.setOnClickListener(v -> {
                if (getActivity() instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) getActivity()).openUpdates();
                }
            });
        }

        // --- About ---
        View btnAbout = view.findViewById(R.id.btn_about);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> {
                if (getActivity() instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) getActivity()).openAbout();
                }
            });
        }

        // --- Privacy Policy ---
        View btnPrivacyPolicy = view.findViewById(R.id.btn_privacypolicy);
        if (btnPrivacyPolicy != null) {
            btnPrivacyPolicy.setOnClickListener(v -> {
                if (getActivity() instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) getActivity()).openPrivacyPolicy();
                }
            });
        }

        // --- Websites (placeholder) ---
        View btnWebsites = view.findViewById(R.id.btn_websites);
        if (btnWebsites != null) {
            btnWebsites.setOnClickListener(v ->
                    Toast.makeText(requireContext(), "Opening Websites...", Toast.LENGTH_SHORT).show()
            );
        }

        // --- Apps (placeholder) ---
        View btnApps = view.findViewById(R.id.btn_apps);
        if (btnApps != null) {
            btnApps.setOnClickListener(v ->
                    Toast.makeText(requireContext(), "Opening Apps...", Toast.LENGTH_SHORT).show()
            );
        }

        // --- Socials (placeholder) ---
        View btnSocials = view.findViewById(R.id.btn_socials);
        if (btnSocials != null) {
            btnSocials.setOnClickListener(v ->
                    Toast.makeText(requireContext(), "Opening Socials...", Toast.LENGTH_SHORT).show()
            );
        }
    }
}