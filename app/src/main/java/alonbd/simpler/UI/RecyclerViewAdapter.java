package alonbd.simpler.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Task;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>{
    class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView mTypeTv;
        private TextView mTaskNameTv;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskNameTv = itemView.findViewById(R.id.txt_name);
            mTypeTv = itemView.findViewById(R.id.txt_trigger_type);
        }
    }

    private List<Task> mTasks;


    public RecyclerViewAdapter(Context context) {
        mTasks = TasksManager.getInstance(context).loadData();
    }
    public void RefreshData(Context context){
        mTasks = TasksManager.getInstance(context).loadData();
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
        holder.mTypeTv.setText(t.triggerType());
        holder.mTaskNameTv.setText(t.getName());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
