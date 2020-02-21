package alonbd.simpler.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.EmailAction;
import alonbd.simpler.TaskLogic.TaskBuilder;

public class EmailActionFragment extends ActionFragment {
    private EditText mToEt;
    private EditText mSubjectEt;
    private EditText mContentEt;
    private String mTaskName;

    @Override
    public Action genAction() {
        return new EmailAction(TasksManager.NotificationIdGenerator.getNewId(getContext()), mTaskName
                , mToEt.getText().toString(), mSubjectEt.getText().toString(), mContentEt.getText().toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_email, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToEt = view.findViewById(R.id.to_et);
        mSubjectEt = view.findViewById(R.id.subject_et);
        mContentEt = view.findViewById(R.id.content_et);
        Intent intent = getActivity().getIntent();
        TaskBuilder builder = (TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();


    }
}
