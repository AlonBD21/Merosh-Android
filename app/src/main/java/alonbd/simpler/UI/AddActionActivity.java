package alonbd.simpler.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.Task;
import alonbd.simpler.TaskLogic.Trigger;

public class AddActionActivity extends AppCompatActivity {
    final static String ACTIONS_EXTRA = "actionsExtra";
    Trigger trigger;
    LinearLayout fragmentContainer;
    Button next;
    Button addActionBtn;
    Action.ActionType actionType;
    ArrayList<Action> actions;
    String taskName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaction);
        next = findViewById(R.id.next_btn);
        addActionBtn = findViewById(R.id.add_action_btn);
        //Data From Intent
        Intent cause = getIntent();
        trigger = (Trigger) cause.getSerializableExtra(AddTriggerActivity.TRIGGER_EXTRA);
        taskName = cause.getStringExtra(AddTaskActivity.EXTRA_TASKNAME);
        fragmentContainer = findViewById(R.id.fragment_container);
        actionType = (Action.ActionType) cause.getSerializableExtra(AddTriggerActivity.ACTIONTYPE_EXTRA);
        actions = (ArrayList<Action>) cause.getSerializableExtra(ACTIONS_EXTRA);
        if(actions == null) actions = new ArrayList<>();
        //Add Fragment
        switch(actionType) {
            case Toast:
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ToastActionFragment()).commit();
                break;
            case Notification:
                break;
        }

        addActionBtn.setOnClickListener((View v) -> {
            Action newAction = genAction();
            if(newAction == null) return;
            actions.add(newAction);
            preDialog(this,trigger,taskName, actions);

        });
        next.setOnClickListener((View v) -> {
            Action newAction = genAction();
            if(newAction == null) return;
            actions.add(newAction);
            TasksManager.getInstance(this).addTask(new Task(trigger,taskName,actions));



            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private Action genAction() {
        switch(actionType) {
            case Toast:
                return ((ToastActionFragment) getSupportFragmentManager().getFragments().get(0)).genToast();
            case Notification:
                break;
        }
        return null;
    }

    private static Intent createIntent(Context context, Action.ActionType actionType, Trigger trigger,String taskName, ArrayList<Action> actions) {
        Intent intent = new Intent(context, AddActionActivity.class);
        intent.putExtra(AddTriggerActivity.ACTIONTYPE_EXTRA, actionType);
        intent.putExtra(AddTriggerActivity.TRIGGER_EXTRA, trigger);
        intent.putExtra(AddTaskActivity.EXTRA_TASKNAME,taskName);
        if(actions == null) actions = new ArrayList<>();
        if(actions.size() > 0) {
            intent.putExtra(ACTIONS_EXTRA, actions);
        }
        return intent;
    }

    public static void preDialog(Context context, Trigger trigger,String taskName ,ArrayList<Action> actions) {
        String[] actionTypes = new String[Action.ActionType.values().length];
        for(int i = 0; i < actionTypes.length; i++)
            actionTypes[i] = Action.ActionType.values()[i].name();
        if(trigger == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle("Choose Task's Action Type")
                .setNegativeButton("Cancel", (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(true).setItems(actionTypes, (DialogInterface dialogInterface, int which) -> {
                    Intent intent = AddActionActivity.createIntent(context, Action.ActionType.values()[which], trigger,taskName, actions);
                    context.startActivity(intent);
                }).create();
        dialog.show();
    }
}
