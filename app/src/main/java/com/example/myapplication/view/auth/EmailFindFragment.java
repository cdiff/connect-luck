package com.example.myapplication.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentEmailFindBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmailFindFragment extends Fragment {

    FragmentEmailFindBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmailFindBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}