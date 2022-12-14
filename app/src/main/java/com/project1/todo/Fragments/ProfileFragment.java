package com.project1.todo.Fragments;

import static com.project1.todo.Fragments.HomeFragment.TAG;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project1.todo.R;
import com.project1.todo.databinding.FragmentProfileBinding;
import com.project1.todo.utilsRecycle.DataClass;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private NavController navController;
    private DatabaseReference databaseReference;
    //private static final int GALLERY_REQUEST_Code = 11;
    private StorageReference storageReference;
    //Uri ImageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        getProfilePic();
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                navController.navigate(R.id.action_profileFragment2_to_loginFragment);
            }
        });

        binding.emailuser.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());

        binding.editProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
                Log.d(TAG, "onClick: ");
            }
        });
    }




    private void getProfilePic() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String urlImage = null;
                for (DataSnapshot profileSnapshot : snapshot.getChildren()) {
                    String pushKey = profileSnapshot.getKey();
                    if (pushKey != null)
                        urlImage = snapshot.child(pushKey).child("imageUrl").getValue(String.class);
                }


                //  Log.d("TAG", "onDataChange: "+urlImage);
                if (urlImage == null) {
                    binding.Profilepic.setImageResource(R.drawable.ic_baseline_person_24);
                } else {
                    Picasso.get().load(urlImage).into(binding.Profilepic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "There is Trouble in retriving", Toast.LENGTH_SHORT).show();

            }
        });
      ;
    }


    private void uploadImage(Uri uri) {
        binding.profileProgressBar.setVisibility(View.VISIBLE);
        StorageReference filereff = storageReference.child(System.currentTimeMillis() + "." +getFileExtention(uri));
        filereff.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filereff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        binding.profileProgressBar.setVisibility(View.INVISIBLE);

                        DataClass dataClass = new DataClass(uri.toString());
                        String imageUrl = databaseReference.push().getKey();
                        databaseReference.child(imageUrl).setValue(dataClass)


                                // databaseReference.child("profilePic").setValue(uri.toString()).
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            binding.profileProgressBar.setVisibility(View.VISIBLE);
                                        }

                                    }


                                });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Nope sorry", Toast.LENGTH_SHORT).show();
                        binding.profileProgressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }


        });

    }






    private String getFileExtention(Uri muri){
        ContentResolver cr= requireContext().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));


    }
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    binding.Profilepic.setImageURI(uri);
                    uploadImage(uri);
                }
            });


}


