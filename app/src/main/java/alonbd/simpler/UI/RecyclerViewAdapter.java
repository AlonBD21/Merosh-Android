package alonbd.simpler.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.BluetoothTrigger;
import alonbd.simpler.TaskLogic.LocationTrigger;
import alonbd.simpler.TaskLogic.Task;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder> {
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView mTypeIv;
        private TextView mTaskNameTv;
        private TextView mOnlyOnceTv;
        private TextView mUsedTv;
        private CardView mRoot;
        private TextView mLongPressTv;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskNameTv = itemView.findViewById(R.id.txt_name);
            mTypeIv = itemView.findViewById(R.id.trigger_icon);
            mOnlyOnceTv = itemView.findViewById(R.id.txt_only_once);
            mUsedTv = itemView.findViewById(R.id.txt_used);
            mRoot = itemView.findViewById(R.id.root_cardview);
            mLongPressTv = itemView.findViewById(R.id.txt_longpress);
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
        //RESETS
        Context context = holder.itemView.getContext();
        Task t = mTasks.get(position);
        holder.mLongPressTv.setVisibility(View.GONE);
        holder.mRoot.setOnLongClickListener(null);
        //GENERAL INFO
        holder.mTaskNameTv.setText(t.getName());
        holder.mRoot.setOnClickListener((v -> {
            ViewTaskActivity.startActivity(context, t);
        }));
        holder.mLongPressTv.setText(t.getNotReadyMessage(context));
        //IMAGE VIEW
        Class c = mTasks.get(position).getTriggerClass();
        if(c == BluetoothTrigger.class) {
            holder.mTypeIv.setImageResource(R.drawable.ic_task_bluetooth);
        } else if(c == LocationTrigger.class) {
            holder.mTypeIv.setImageResource(R.drawable.ic_task_location);
        } else {
            holder.mTypeIv.setImageBitmap(null);
        }
        //OTHER FIELDS
        if(t.isTriggerSingleUse()) {//SingleUse
            holder.mOnlyOnceTv.setText(context.getString(R.string.prop_once_only));
            if(!t.isTriggerReady()) {//Done
                holder.mRoot.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                holder.mUsedTv.setText(context.getString(R.string.prop_done));
                holder.mRoot.setOnLongClickListener(v -> {
                    t.setTriggerNotUsed();
                    notifyDataSetChanged();
                   return true;
                });
            } else {//Ready
                holder.mRoot.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                holder.mUsedTv.setText(context.getString(R.string.prop_ready));
            }
        } else {//MultiUse
            holder.mOnlyOnceTv.setText(context.getString(R.string.prop_reusable));
            if(c == LocationTrigger.class && !t.isTriggerReady()) {
                holder.mUsedTv.setText(context.getString(R.string.prop_cooldown));
                holder.mRoot.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));

            } else {
                holder.mUsedTv.setText(context.getString(R.string.prop_ready));
                holder.mRoot.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
            }

        }

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
