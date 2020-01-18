package alonbd.merosh.UI;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alonbd.merosh.R;
import alonbd.merosh.TaskLogic.Task;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>{
    class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView typeTv;
        TextView nameTv;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.txt_name);
            typeTv = itemView.findViewById(R.id.txt_trigger_type);
        }
    }

    List<Task> tasks;

    public RecyclerViewAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_task,parent,false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task t = tasks.get(position);
        holder.typeTv.setText(t.triggerType());
        holder.nameTv.setText(t.getName());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
