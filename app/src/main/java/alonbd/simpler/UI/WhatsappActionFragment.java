package alonbd.simpler.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.WhatsappAction;

public class WhatsappActionFragment extends ActionFragment {
    private EditText mContentEt;
    private String mTaskName;
    private ImageView mHandIv;
    @Override
    public Action genAction() {
        return new WhatsappAction(TasksManager.NotificationIdGenerator.getNewId(getContext()),mTaskName,mContentEt.getText().toString()) ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_whatsapp,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentEt = view.findViewById(R.id.wa_content_et);
        mHandIv = view.findViewById(R.id.hand_iv);
        Intent intent = getActivity().getIntent();
        TaskBuilder builder =(TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();

        Animator.animateWavingHand(mHandIv);
    }

}
