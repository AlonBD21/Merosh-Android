package alonbd.simpler.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Task;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>{
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView mTypeTv;
        private TextView mTaskNameTv;
        private TextView mOnlyOnce;
        private TextView mConsumed;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskNameTv = itemView.findViewById(R.id.txt_name);
            mTypeTv = itemView.findViewById(R.id.txt_trigger_type);
            mOnlyOnce = itemView.findViewById(R.id.txt_only_once);
            mConsumed = itemView.findViewById(R.id.txt_trigger_consumed);
        }
    }

    private List<Task> mTasks;


    public RecyclerViewAdapter(ArrayList<Task> mTasks) {
        this.mTasks = mTasks;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_task,parent,false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task t = mTasks.get(position);
        holder.mTypeTv.setText(t.getTriggerClass().getSimpleName());
        holder.mTaskNameTv.setText(t.getName());
        holder.mOnlyOnce.setText("Action Only Once: "+t.isOnceOnly());
        holder.mConsumed.setText("Used Once Already: "+t.isTriggerUsed());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
