package alonbd.simpler.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.NotificationAction;
import alonbd.simpler.TaskLogic.TaskBuilder;

public class NotificationActionFragment extends ActionFragment {

    private EditText mContentEt;
    private String mTaskName;
    @Override
    public Action genAction() {
        return new NotificationAction(mContentEt.getText().toString(),mTaskName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_action_notification,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentEt = view.findViewById(R.id.content);
        Activity activity = getActivity();
        Intent intent = activity.getIntent();
        TaskBuilder builder =(TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();
    }
}
