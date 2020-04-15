package alonbd.simpler.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.simpler.TaskLogic.Task;

public class ViewTaskActivity extends AppCompatActivity {
    private static final String EXTRA_TASK = "extraTaskExtra";
    private Task mTask;

    public static void startActivity(Context context, Task task) {
        Intent intent = new Intent(context, ViewTaskActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTask = (Task) getIntent().getSerializableExtra(EXTRA_TASK);
        setContentView(mTask.getDescriptiveView(this));

    }
}
