package alonbd.simpler.UI;

import androidx.fragment.app.Fragment;

import alonbd.simpler.TaskLogic.Trigger;

public abstract class TriggerFragment extends Fragment {
    public abstract Trigger genTrigger(boolean singleUse);
}
