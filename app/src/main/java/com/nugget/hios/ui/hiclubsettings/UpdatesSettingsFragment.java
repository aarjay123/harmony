package com.nugget.hios.ui.hiclubsettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugget.hios.R;

public class UpdatesSettingsFragment extends Fragment {

    public UpdatesSettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Replace with the actual filename of the XML you pasted (e.g. fragment_updates.xml)
        return inflater.inflate(R.layout.fragment_updates_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnManualUpdate = view.findViewById(R.id.btn_manual_update);
        btnManualUpdate.setOnClickListener(v -> {
            // TODO: put your update logic here
            Toast.makeText(requireContext(), "Implementation coming soon...", Toast.LENGTH_SHORT).show();
        });
    }
}
