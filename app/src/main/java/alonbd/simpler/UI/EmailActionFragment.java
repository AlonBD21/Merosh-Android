package alonbd.simpler.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

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
    private ImageView mPlaneIv;
    private TextInputLayout mToTil;
    private static final int REQ_PICK_EMAIL = 55;

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
        mPlaneIv = view.findViewById(R.id.plane_iv);
        mToTil = view.findViewById(R.id.to_til);
        Animator.animatePaperPlane(mPlaneIv);
        Intent intent = getActivity().getIntent();
        TaskBuilder builder = (TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();

        mToTil.setEndIconOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
            startActivityForResult(Intent.createChooser(intent1, "Choose Email address from..."), REQ_PICK_EMAIL);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PICK_EMAIL) {
            if(resultCode == Activity.RESULT_OK) {
                Uri contactUri = data.getData();
                Cursor cursor = getContext().getContentResolver().query(contactUri, null,
                        null, null, null);
                if(cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                    String email = cursor.getString(numberIndex);

                    if(mToEt.getText().toString().equals("")) {
                        mToEt.setText(email);
                    } else {
                        mToEt.setText(mToEt.getText()+", "+email);
                    }

                }
            }
        }
    }
}
