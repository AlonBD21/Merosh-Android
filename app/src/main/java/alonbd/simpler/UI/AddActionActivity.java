package alonbd.simpler.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.TaskBuilder;

public class AddActionActivity extends AppCompatActivity {
    public static final String NEW_TASK_NAME_EXTRA_STRING = "newTaskNameExtraString";
    private static final String TAG = "ThugAddActionActivity";
    //vars
    private Button mFinishBtn;
    private Button mAddActionBtn;
    private TaskBuilder mBuilder;
    private ActionFragment mActionFragment;

    private static Intent createIntent(Context context, String actionType, TaskBuilder builder) {
        Intent intent = new Intent(context, AddActionActivity.class);
        intent.putExtra(Action.ACTION_EXTRA_CLASS_TYPE, actionType);
        intent.putExtra(TaskBuilder.EXTRA_TAG, builder);
        return intent;
    }

    public static void preDialog(Context context, TaskBuilder builder) {
        if(builder.getTrigger() == null) return;
        String[] actionTypes = {context.getString(R.string.action_toast), context.getString(R.string.action_notification),
                context.getString(R.string.action_email), context.getString(R.string.action_whatsapp), context.getString(R.string.action_waze)};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = dialogBuilder.setTitle(context.getString(R.string.action_type_title))
                .setNegativeButton(context.getString(R.string.main_cancel), (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(true).setItems(actionTypes, (DialogInterface dialogInterface, int which) -> {
                    Intent intent = AddActionActivity.createIntent(context, actionTypes[which], builder);
                    context.startActivity(intent);
                }).create();
        dialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaction);
        mFinishBtn = findViewById(R.id.next_btn);
        mAddActionBtn = findViewById(R.id.add_action_btn);
        //Data From Intent
        Intent cause = getIntent();
        mBuilder = (TaskBuilder) cause.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        String actionType = cause.getStringExtra(Action.ACTION_EXTRA_CLASS_TYPE);
        //Add Fragment
        if(actionType.equals(getString(R.string.action_toast)))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ToastActionFragment()).commit();
        else if(actionType.equals(getString(R.string.action_notification)))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotificationActionFragment()).commit();
        else if(actionType.equals(getString(R.string.action_email)))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new EmailActionFragment()).commit();
        else if(actionType.equals(getString(R.string.action_whatsapp)))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new WhatsappActionFragment()).commit();
        else if(actionType.equals(getString(R.string.action_waze)))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new WazeActionFragment()).commit();
        else
            throw new RuntimeException("Unexpected type of action: " + actionType);


        mAddActionBtn.setOnClickListener((View v) -> {
            Action newAction = ((ActionFragment) getSupportFragmentManager().getFragments().get(0)).genAction();
            if(newAction == null) return;
            mBuilder.addAction(newAction);
            preDialog(this, mBuilder);

        });
        mFinishBtn.setOnClickListener((View v) -> {
            Action newAction = ((ActionFragment) getSupportFragmentManager().getFragments().get(0)).genAction();
            if(newAction == null) return;
            mBuilder.addAction(newAction);
            TasksManager.getInstance(this).getData().add(mBuilder.build());
            TasksManager.getInstance(this).saveData();


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NEW_TASK_NAME_EXTRA_STRING,mBuilder.getTaskName());
            startActivity(intent);
        });
    }
}
