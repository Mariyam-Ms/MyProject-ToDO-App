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
import com.project1.todo.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private FirebaseAuth auth;
    private NavController navController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        binding.loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.emaill.getText().toString().trim();
                String password = binding.passwordl.getText().toString();

                if (!email.isEmpty() && !password.isEmpty() ) {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "YOU Logged in Successfully /n Congrajulation", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.action_loginFragment_to_homeFragment2);


                            }else{
                                Toast.makeText(getContext(), "User Credentails are not valid /n Plss Try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else{
                    Toast.makeText(getContext(), " Password Incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}