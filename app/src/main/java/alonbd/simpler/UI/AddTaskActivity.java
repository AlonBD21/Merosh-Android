package alonbd.simpler.UI;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.Task;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.Trigger;

public class AddTaskActivity extends AppCompatActivity {
    EditText name;
    ImageButton button;
    TaskBuilder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        builder = new TaskBuilder();
        name = findViewById(R.id.name_et);
        button = findViewById(R.id.btn);

        button.setOnClickListener(v -> {
            if(name.getText().length() == 0) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }else{
                builder.setTaskName(name.getText().toString());
                Intent intent = new Intent(this,AddTriggerActivity.class);
                intent.putExtra(TaskBuilder.EXTRA_TAG,builder);
                startActivity(intent);
            }

        });
    }
}
