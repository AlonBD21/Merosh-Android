package alonbd.simpler.TaskLogic;

import android.content.Intent;

import java.io.Serializable;

public abstract class Trigger implements Serializable {
    public abstract boolean matchIntent(Intent intent);
}
