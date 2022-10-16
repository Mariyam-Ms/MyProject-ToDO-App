package com.project1.todo.utilsRecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project1.todo.databinding.EachitemBinding;

import java.util.List;

public class TODOAdapter extends RecyclerView.Adapter<TODOAdapter.TODOViewholder> {
    private List<DataClass> todolist;
    private EachitemBinding binding;
    private TaskClickedListeners taskclick;

    public TODOAdapter(TaskClickedListeners taskclick){
        this.taskclick= taskclick;
    }

    public void setTodolist(List<DataClass> todolist) {
        this.todolist = todolist;
    }

    @NonNull
    @Override
    public TODOViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EachitemBinding.inflate(LayoutInflater.from(parent.getContext()) , parent ,false);
        return new TODOViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TODOViewholder holder, int position) {
        DataClass todotask=todolist.get(position);
        binding.Task.setText(todotask.getTodo());

        binding.EditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskclick.Edittask(todotask);
            }
        });

        binding.DeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskclick.Deletetask(todotask);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(todolist==null){
            return 0;

        }else {
            return todolist.size();
        }
    }
    public class TODOViewholder extends RecyclerView.ViewHolder{

        public TODOViewholder(@NonNull EachitemBinding itemView) {
            super(binding.getRoot());
            binding=itemView;
        }
    }
    public interface TaskClickedListeners{
        void Edittask(DataClass todotask);
        void Deletetask(DataClass todotask);
    }
}





