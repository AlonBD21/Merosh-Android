package alonbd.simpler.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.Task;
import alonbd.simpler.TaskLogic.Trigger;

public class AddTaskActivity extends AppCompatActivity {
    EditText name;
    Button button;
    Trigger trigger;
    ArrayList<Action> actions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        name = findViewById(R.id.name_et);
        button = findViewById(R.id.btn);

        Intent cause = getIntent();
        trigger = (Trigger)cause.getSerializableExtra(AddTriggerActivity.TRIGGER_EXTRA);
        actions = (ArrayList<Action>)cause.getSerializableExtra(AddActionActivity.ACTIONS_EXTRA);

        button.setOnClickListener(v -> {
            if(name.getText().length() == 0) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }else{
                TasksManager.getInstance(this).addTask(new Task(trigger,name.getText().toString(),actions));



                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
    }
}
