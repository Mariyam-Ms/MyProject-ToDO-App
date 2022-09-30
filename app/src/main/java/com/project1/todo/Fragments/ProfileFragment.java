package com.project1.todo.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project1.todo.R;
import com.project1.todo.databinding.FragmentProfileBinding;
import com.project1.todo.utilsRecycle.DataClass;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private NavController navController;
    DatabaseReference databaseReference;
    //private static final int GALLERY_REQUEST_Code = 11;
    StorageReference storageReference;
    //Uri ImageUri;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        navController= Navigation.findNavController(view);
        storageReference = FirebaseStorage.getInstance().getReference("Images");


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                navController.navigate(R.id.action_profileFragment2_to_loginFragment);
            }
        });
        //What is object require no null
        binding.emailuser.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        binding.editProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetContent.launch("image/*");

            }
        });

    }

    private void uploadimage(Uri uri) {
        storageReference=FirebaseStorage.getInstance().getReference("images");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        DataClass imageUrl= new DataClass(uri.toString());
//                        String ImageUploadId = databaseReference.push().getKey();
//                        databaseReference.child(ImageUploadId).setValue(imageUrl);
                       // DataClass dataClass= new DataClass(uri.toString());
                       // String DataId = root.push().getKey();
                      //  root.child(DId).setValue(dataClass);

                        Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                binding.Profilepic.setImageURI(null);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Nope sorry",Toast.LENGTH_SHORT).show();

            }
        });
    }

    ActivityResultLauncher<String> GetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                   // ImageUri = data.getData();
                    binding.Profilepic.setImageURI(uri);
                    uploadimage(uri);

                }
            });


    }
