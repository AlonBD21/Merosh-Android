package alonbd.simpler.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.TaskBuilder;

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
                Toast.makeText(this, getString(R.string.no_name_err), Toast.LENGTH_SHORT).show();
                return;
            }else{
                mBuilder.setTaskName(mName.getText().toString());
                mBuilder.setSingleUse(((Checkable) mOnlyOnceSwitch).isChecked());
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
