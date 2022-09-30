package com.project1.todo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project1.todo.R;
import com.project1.todo.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private FirebaseAuth auth;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSignUpBinding.inflate(inflater , container , false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailadd = binding.emailSignup.getText().toString().trim();
                String password = binding.passSignup.getText().toString();
                String Conformpass = binding.ConfirmpassSignup.getText().toString();

                if (!emailadd.isEmpty() && !password.isEmpty() && !Conformpass.isEmpty()) {
                    if (password.equals(Conformpass)) {
                        auth.createUserWithEmailAndPassword(emailadd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Successfully Register ", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_signUpFragment_to_homeFragment);
                                } else {
                                    Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Please type correct password", Toast.LENGTH_SHORT).show();


                    }

                } else {
                    Toast.makeText(getContext(), "Please give valid email and password", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}