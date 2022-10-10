package com.project1.todo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project1.todo.R;
import com.project1.todo.databinding.FragmentHomeBinding;
import com.project1.todo.utilsRecycle.DataClass;
import com.project1.todo.utilsRecycle.TODOAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements PopupDialogFragment.DailogListner , TODOAdapter.TaskClickedListeners{
    public static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private NavController navController;
    private DatabaseReference databaseReference;
    private  StorageReference storageReference;
    private PopupDialogFragment popup;
    private List<DataClass> tododatalist;
    private TODOAdapter todoAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        todoAdapter = new TODOAdapter(this);
        tododatalist = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("").child(auth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        getProfilePic();
        binding.Notes.setHasFixedSize(true);
        binding.Notes.setLayoutManager(new LinearLayoutManager(getContext()));

        getTaskfromRDb();


        binding.profileinHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_profileFragment2);

            }});

        binding.addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popup != null){
                    getChildFragmentManager().beginTransaction().remove(popup).commit();
                }else {
                    popup = new PopupDialogFragment(HomeFragment.this);
                    popup.show(getChildFragmentManager(), PopupDialogFragment.TAG);
                }
            }
        });

    }

    private void getProfilePic() {
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String urlImage = snapshot.child("profilePic").getValue(String.class);
                Picasso.get().load(urlImage).into(binding.profileinHome);
              //  Log.d("TAG", "onDataChange: "+urlImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "There is Trouble in retriving", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getTaskfromRDb() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tododatalist.clear();

                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    DataClass todotask = new DataClass ();
                    todotask.setTodo(taskSnapshot.getValue(String.class));
                    todotask.setTaskid(taskSnapshot.getKey());

                    tododatalist.add(todotask);
                }
                Log.d(TAG, "onDataChange: "+tododatalist);
                todoAdapter.setTodolist(tododatalist);
                binding.Notes.setAdapter(todoAdapter);
                todoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void addTasktoRDb(String task, EditText edittext) {

        databaseReference.push().setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
                    edittext.setText("");
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                popup.dismiss();
            }
        });
    }
    @Override
    public void updateTaskinRDb(String task, String taskid, EditText edittext) {
        HashMap<String , Object> updateMap = new HashMap<>();
        updateMap.put(taskid , task);


        databaseReference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Successfull", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getContext(), task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
                popup.dismiss();
            }
        });
    }

    @Override
    public void Edittask(DataClass todotask) {
        String taskvalue = todotask.getTodo();
        if (popup != null){
            getChildFragmentManager().beginTransaction().remove(popup).commit();
        }
        popup = new PopupDialogFragment(this);
        Bundle bundle = new Bundle();
        bundle.putString("task" , taskvalue);
        bundle.putString("taskid" , todotask.getTaskid());
        popup.setArguments(bundle);
        popup.show(getChildFragmentManager() , PopupDialogFragment.TAG);

    }


    @Override
    public void Deletetask(DataClass todotask) {

        databaseReference.child(todotask.getTaskid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), " Task Deleted ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}