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
import androidx.fragment.app.Fragment;

import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.ToastAction;

public class ToastActionFragment extends ActionFragment implements View.OnClickListener {
    ToggleButton shorter;
    ToggleButton longer;
    Button tryBtn;
    EditText msg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_toast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shorter = view.findViewById(R.id.length_short);
        longer = view.findViewById(R.id.length_long);
        tryBtn = view.findViewById(R.id.try_btn);
        msg = view.findViewById(R.id.toast_msg);

        shorter.setChecked(true);
        shorter.setOnClickListener(this);
        longer.setOnClickListener(this);
        tryBtn.setOnClickListener((View v) -> {
            genAction().onExecute(getContext());
        });

    }

    public Action genAction() {
        return new ToastAction(msg.getText().toString(), shorter.isChecked() ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {
        if(!((ToggleButton) v).isChecked()) {
            shorter.setChecked(true);
            longer.setChecked(true);
            ((ToggleButton) v).setChecked(false);
        } else {
            shorter.setChecked(false);
            longer.setChecked(false);
            ((ToggleButton) v).setChecked(true);
        }
    }
}
