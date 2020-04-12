package alonbd.simpler.UI;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
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
    private static String TAG = "ThugAddTriggerActivity";
    private EditText mName;
    private Button mButton;
    private TaskBuilder mBuilder;
    private Button mOnlyOnceSwitch;
    private ImageView mPencilIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        mPencilIv = findViewById(R.id.pencil_iv);
        mBuilder = new TaskBuilder();
        mName = findViewById(R.id.name_et);
        mButton = findViewById(R.id.btn);
        mOnlyOnceSwitch = findViewById(R.id.only_once);

        mButton.setOnClickListener(v -> {
            if(mName.getText().length() == 0) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }else{
                mBuilder.setTaskName(mName.getText().toString());
                mBuilder.setOnlyOnce(((Checkable)mOnlyOnceSwitch).isChecked());
                Intent intent = new Intent(this,AddTriggerActivity.class);
                intent.putExtra(TaskBuilder.EXTRA_TAG,mBuilder);
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Animator.animatePencil(mPencilIv);
    }
}
