package alonbd.simpler.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.ToastAction;

public class ToastActionFragment extends ActionFragment implements View.OnClickListener {
    private ToggleButton mShorterToggle;
    private ToggleButton mLongerToggle;
    private Button mTryToastBtn;
    private EditText mToastTxtEt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_toast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShorterToggle = view.findViewById(R.id.length_short);
        mLongerToggle = view.findViewById(R.id.length_long);
        mTryToastBtn = view.findViewById(R.id.try_btn);
        mToastTxtEt = view.findViewById(R.id.toast_msg);

        mShorterToggle.setChecked(true);
        mShorterToggle.setOnClickListener(this);
        mLongerToggle.setOnClickListener(this);
        mTryToastBtn.setOnClickListener((View v) -> {
            genAction().onExecute(getContext());
        });

    }

    public Action genAction() {
        return new ToastAction(mToastTxtEt.getText().toString(), mShorterToggle.isChecked() ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {
        if(!((ToggleButton) v).isChecked()) {
            mShorterToggle.setChecked(true);
            mLongerToggle.setChecked(true);
            ((ToggleButton) v).setChecked(false);
        } else {
            mShorterToggle.setChecked(false);
            mLongerToggle.setChecked(false);
            ((ToggleButton) v).setChecked(true);
        }
    }
}
