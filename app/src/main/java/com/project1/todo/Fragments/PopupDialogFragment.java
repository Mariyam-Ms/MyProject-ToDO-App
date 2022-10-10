package com.project1.todo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.project1.todo.R;
import com.project1.todo.databinding.FragmentPopupDialogBinding;


public class PopupDialogFragment extends DialogFragment {
    private FragmentPopupDialogBinding binding;
    public static final String TAG = "PopupDialogFragment";
    private DailogListner listener;
    private String taskid = "";
    public PopupDialogFragment(DailogListner listener){
        this.listener=listener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPopupDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null){
            String updatetask = getArguments().getString("task");
            taskid = getArguments().getString("taskid");
            binding.Tasktodo.setText(updatetask);
        }
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = binding.Tasktodo.getText().toString();

                if (!todo.isEmpty()){
                    if (getArguments() != null){
                        listener.updateTaskinRDb(todo ,  taskid   , binding.Tasktodo);
                    }else{
                        listener.addTasktoRDb(todo , binding.Tasktodo);
                    }

                }

                
            }

        });


    }
    public interface DailogListner{
        void updateTaskinRDb(String todo,String taskid, EditText edittext);
        void addTasktoRDb(String todo,EditText edittext);
    }
}