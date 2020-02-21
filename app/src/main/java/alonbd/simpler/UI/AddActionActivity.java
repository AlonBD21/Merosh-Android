package alonbd.simpler.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.EmailAction;
import alonbd.simpler.TaskLogic.NotificationAction;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.ToastAction;
import alonbd.simpler.TaskLogic.WazeAction;
import alonbd.simpler.TaskLogic.WhatsappAction;

public class AddActionActivity extends AppCompatActivity {
    public static final String NEW_TASK_NAME_EXTRA_STRING = "newTaskNameExtraString";
    private static final String TAG = "ThugAddActionActivity";
    //vars
    private Button mFinishBtn;
    private Button mAddActionBtn;
    private TaskBuilder mBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaction);
        mFinishBtn = findViewById(R.id.next_btn);
        mAddActionBtn = findViewById(R.id.add_action_btn);
        //Data From Intent
        Intent cause = getIntent();
        mBuilder = (TaskBuilder) cause.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        Log.d(TAG, "onCreate: mBuilder is null:" + (mBuilder == null));
        Class actionClass = ((Class) cause.getSerializableExtra(Action.ACTION_EXTRA_CLASS));
        //Add Fragment
        if(actionClass == ToastAction.class)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ToastActionFragment()).commit();
        else if(actionClass == NotificationAction.class)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotificationActionFragment()).commit();
        else if(actionClass == EmailAction.class){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new EmailActionFragment()).commit();
        }else if(actionClass == WhatsappAction.class){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new WhatsappActionFragment()).commit();
        }else if(actionClass == WazeAction.class){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new WazeActionFragment()).commit();
        }
        else
            throw new IllegalStateException("Unexpected value: " + actionClass.getName());


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
            TasksManager.getInstance(this).addTask(mBuilder.build());


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NEW_TASK_NAME_EXTRA_STRING,mBuilder.getTaskName());
            startActivity(intent);
        });
    }

    private static Intent createIntent(Context context, Class actionClass, TaskBuilder builder) {
        Intent intent = new Intent(context, AddActionActivity.class);
        intent.putExtra(Action.ACTION_EXTRA_CLASS, actionClass);
        intent.putExtra(TaskBuilder.EXTRA_TAG, builder);
        return intent;
    }

    public static void preDialog(Context context, TaskBuilder builder) {
        if(builder.getTrigger() == null) return;
        String[] actionNames = {"Display Toast","Push Notification","Send Email","Whatsapp Message","Waze Navigation"};
        Class[] actionClasses = {ToastAction.class,NotificationAction.class, EmailAction.class, WhatsappAction.class, WazeAction.class};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog = dialogBuilder.setTitle("Choose Task's Action Type")
                .setNegativeButton("Cancel", (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(true).setItems(actionNames, (DialogInterface dialogInterface, int which) -> {
                    Intent intent = AddActionActivity.createIntent(context, actionClasses[which], builder);
                    context.startActivity(intent);
                }).create();
        dialog.show();
    }
}
