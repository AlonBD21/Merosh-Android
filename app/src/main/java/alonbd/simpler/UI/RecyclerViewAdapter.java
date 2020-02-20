package alonbd.simpler.UI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.BluetoothTrigger;
import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.Task;
import alonbd.simpler.TaskLogic.Trigger;
import alonbd.simpler.TaskLogic.WifiTrigger;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder> {
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView mTypeIv;
        private TextView mTaskNameTv;
        private TextView mOnlyOnceTv;
        private TextView mUsedTv;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskNameTv = itemView.findViewById(R.id.txt_name);
            mTypeIv = itemView.findViewById(R.id.txt_trigger_icon);
            mOnlyOnceTv = itemView.findViewById(R.id.txt_only_once);
            mUsedTv = itemView.findViewById(R.id.txt_used);
        }
    }

    private List<Task> mTasks;

    public RecyclerViewAdapter(ArrayList<Task> mTasks) {
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Task t = mTasks.get(position);
        holder.mTaskNameTv.setText(t.getName());
        if(t.isOnceOnly()) {
            holder.mOnlyOnceTv.setText("One Time Action");
            if(t.isTriggerUsed()) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                holder.mUsedTv.setText("Used Already");
            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                holder.mUsedTv.setText("Ready");
            }
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
            holder.mOnlyOnceTv.setText("Reusable Action");
            holder.mUsedTv.setText("Ready");
        }

        Class c = mTasks.get(position).getTriggerClass();
        if(c == BluetoothTrigger.class) {
            holder.mTypeIv.setImageResource(R.drawable.ic_bluetooth_black);
        } else if(c == LocationTrigger.class) {
            holder.mTypeIv.setImageResource(R.drawable.ic_location_black);
        } else if(c == WifiTrigger.class) {
            holder.mTypeIv.setImageResource(R.drawable.ic_wifi_black_);
        } else {
            holder.mTypeIv.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
