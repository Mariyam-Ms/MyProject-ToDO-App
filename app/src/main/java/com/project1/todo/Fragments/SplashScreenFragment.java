package com.project1.todo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.project1.todo.R;
import com.project1.todo.databinding.FragmentSplashScreenBinding;


public class SplashScreenFragment extends Fragment {

   private FragmentSplashScreenBinding binding;
   private FirebaseAuth auth;
   private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentSplashScreenBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController  = Navigation.findNavController(view);
        auth = FirebaseAuth.getInstance();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null){
                    navController.navigate(R.id.action_splashScreenFragment_to_homeFragment);
                }else{
                    navController.navigate(R.id.action_splashScreenFragment_to_loginFragment);
                }
            }
        }, 1000);
    }
}