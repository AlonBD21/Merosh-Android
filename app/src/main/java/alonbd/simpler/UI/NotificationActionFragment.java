package alonbd.simpler.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.NotificationAction;
import alonbd.simpler.TaskLogic.TaskBuilder;

public class NotificationActionFragment extends ActionFragment {

    private EditText mContentEt;
    private ImageView mStarIv;
    private String mTaskName;
    private LinearLayout mColorsLl;
    private int[] mColors;
    private int mSelectedColor;
    private ColorOption[] mColorOptions;

    @Override
    public Action genAction() {
        if(mSelectedColor != 0)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return new NotificationAction(TasksManager.NotificationIdGenerator.getNewId(getContext()), mContentEt.getText().toString(), mTaskName, mSelectedColor);
            } else return null;
        else {
            Toast.makeText(getContext(), getString(R.string.notif_choose_color), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_action_notification, container, false);

        mColorsLl = v.findViewById(R.id.colors_ll);
        mColors = new int[]{Color.parseColor("#448aff"), Color.parseColor("#e53935"), Color.parseColor("#ef6191"),
                Color.parseColor("#ffeb3b"), Color.parseColor("#4caf50"), Color.parseColor("#fb8c00"), Color.parseColor("#ab47bc"), Color.parseColor("#607d8b")};
        mColorOptions = new ColorOption[mColors.length];

        int dp = 120 * (int) getContext().getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp, dp);
        lp.setMargins(dp / 3, dp / 8, dp / 3, dp / 10);

        mSelectedColor = 0;
        ColorOptionClickListener listener = new ColorOptionClickListener();
        for(int i = 0; i < mColorOptions.length; i++) {
            mColorOptions[i] = new ColorOption(getContext(), mColors[i]);
            mColorsLl.addView(mColorOptions[i], lp);
            mColorOptions[i].setOnClickListener(listener);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentEt = view.findViewById(R.id.content);
        mStarIv = view.findViewById(R.id.star_iv);
        ((AnimationDrawable) mStarIv.getDrawable()).start();
        Activity activity = getActivity();
        Intent intent = activity.getIntent();
        TaskBuilder builder = (TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();


    }

    private class ColorOptionClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ColorOption checked = (ColorOption) v;
            mSelectedColor = checked.getColor();
            for(ColorOption c : mColorOptions) {
                c.setChecked(false);
            }
            checked.setChecked(true);
        }
    }
}
