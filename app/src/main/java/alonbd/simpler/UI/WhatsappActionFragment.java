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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alonbd.simpler.BackgroundAndroid.TasksManager;
import alonbd.simpler.R;
import alonbd.simpler.TaskLogic.Action;
import alonbd.simpler.TaskLogic.TaskBuilder;
import alonbd.simpler.TaskLogic.WhatsappAction;

public class WhatsappActionFragment extends ActionFragment {
    private EditText mContentEt;
    private EditText mToEt;
    private String mTaskName;
    private ImageView mHandIv;
    private TextInputLayout mToTil;
    private static final int REQ_PICK_PHONE = 66;

    @Override
    public Action genAction() {
        return new WhatsappAction(TasksManager.NotificationIdGenerator.getNewId(getContext()),mTaskName,mContentEt.getText().toString(),formatPhone(mToEt.getText().toString())) ;
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
        mToTil = view.findViewById(R.id.to_til);
        mToEt = view.findViewById(R.id.to_et);
        mHandIv = view.findViewById(R.id.hand_iv);
        Intent intent = getActivity().getIntent();
        TaskBuilder builder =(TaskBuilder) intent.getSerializableExtra(TaskBuilder.EXTRA_TAG);
        mTaskName = builder.getTaskName();

        Animator.animateWavingHand(mHandIv);

        mToTil.setEndIconOnClickListener((View.OnClickListener) v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(Intent.createChooser(intent1, "Choose Phone number from..."), REQ_PICK_PHONE);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PICK_PHONE) {
            if(resultCode == Activity.RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
                Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                        null, null, null);
                if(cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String num = cursor.getString(numberIndex);
                    num = formatPhone(num);
                        mToEt.setText(num);
                }
            }
        }
    }
    public static String formatPhone(String s) {
        if(s.length() == 0 ) return s;
        String out = "";
        Pattern p = Pattern.compile("[+\\d]+");
        Matcher m = p.matcher(s);
        while (m.find())
            out += m.group();

        if (out.contains("+"))
            return out;

        else {
            if (out.charAt(0) == '0')
                out = out.substring(1);

            if (out.length() >= 12)
                return "+" + out;
            else
                return "+972" + out;
        }
    }
}
