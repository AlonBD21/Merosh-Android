package alonbd.merosh.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import alonbd.merosh.R;
import alonbd.merosh.TaskLogic.Action;
import alonbd.merosh.TaskLogic.Trigger;

public class AddActionActivity extends AppCompatActivity {
Trigger trigger;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_addaction);
    Intent cause = getIntent();
    trigger = (Trigger)cause.getSerializableExtra(AddTriggerActivity.TRIGGER_EXTRA);
    Action.ActionType actionType = (Action.ActionType) cause.getSerializableExtra(AddTriggerActivity.ACTION_EXTRA);

    TextView actionTv = findViewById(R.id.action);
    TextView triggerTv = findViewById(R.id.trigger);

    actionTv.setText(actionType.toString());
    triggerTv.setText(trigger.getClass().getSimpleName());
}
}
