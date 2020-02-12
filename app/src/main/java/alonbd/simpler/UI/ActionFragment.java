package alonbd.simpler.UI;

import androidx.fragment.app.Fragment;

import alonbd.simpler.TaskLogic.Action;

public abstract class ActionFragment extends Fragment {
    public abstract Action genAction();
}
