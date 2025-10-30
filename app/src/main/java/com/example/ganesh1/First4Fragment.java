package com.example.ganesh1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ganesh1.databinding.FragmentFirst4Binding;

public class First4Fragment extends Fragment {

    private FragmentFirst4Binding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirst4Binding.inflate(inflater, container, false);
        return binding.btnNext.getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ Correct button click listener (use your button ID from XML)
        binding.btnNext.setOnClickListener(v ->
                NavHostFragment.findNavController(First4Fragment.this)
                        .navigate(R.id.action_First4Fragment_to_Second4Fragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
