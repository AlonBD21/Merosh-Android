package alonbd.simpler.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.TaskBuilder;

public class AddActionActivity extends AppCompatActivity {
    private static final String ACTIONTYPE_EXTRA = "actionTypeExtra";
    //vars
    private Button mFinishBtn;
    private Button mAddActionBtn;
    private Action.ActionType mActionType;
    private TaskBuilder mBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaction);
        mFinishBtn = findViewById(R.id.next_btn);
        mAddActionBtn = findViewById(R.id.add_action_btn);
        //Data From Intent
        Intent cause = getIntent();
        mBuilder = (TaskBuilder)cause.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mActionType = (Action.ActionType) cause.getSerializableExtra(ACTIONTYPE_EXTRA);
        //Add Fragment
        switch(mActionType) {
            case Toast:
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ToastActionFragment()).commit();
                break;
            case Notification:
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new NotificationActionFragment()).commit();
                break;
        }



        mAddActionBtn.setOnClickListener((View v) -> {
            Action newAction = ((ActionFragment)getSupportFragmentManager().getFragments().get(0)).genAction();
            if(newAction == null) return;
            mBuilder.addAction(newAction);
            preDialog(this, mBuilder);

        });
        mFinishBtn.setOnClickListener((View v) -> {
            Action newAction = ((ActionFragment)getSupportFragmentManager().getFragments().get(0)).genAction();
            if(newAction == null) return;
            mBuilder.addAction(newAction);
            TasksManager.getInstance(this).addTask(mBuilder.build());



            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }


    private static Intent createIntent(Context context, Action.ActionType actionType, TaskBuilder builder) {
        Intent intent = new Intent(context, AddActionActivity.class);
        intent.putExtra(ACTIONTYPE_EXTRA, actionType);
        intent.putExtra(TaskBuilder.EXTRA_TAG,builder);
        return intent;
    }

    public static void preDialog(Context context, TaskBuilder builder) {
        String[] actionTypes = new String[Action.ActionType.values().length];
        for(int i = 0; i < actionTypes.length; i++)
            actionTypes[i] = Action.ActionType.values()[i].name();
        if(builder.getTrigger() == null) return;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = dialogBuilder.setTitle("Choose Task's Action Type")
                .setNegativeButton("Cancel", (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(true).setItems(actionTypes, (DialogInterface dialogInterface, int which) -> {
                    Intent intent = AddActionActivity.createIntent(context, Action.ActionType.values()[which],builder);
                    context.startActivity(intent);
                }).create();
        dialog.show();
    }
}
