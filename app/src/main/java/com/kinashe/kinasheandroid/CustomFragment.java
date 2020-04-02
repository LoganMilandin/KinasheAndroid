package com.kinashe.kinasheandroid;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class CustomFragment extends Fragment {

    private CustomFragment parent;
    private CustomFragment child;

    public CustomFragment getParent(){return parent;}
    public CustomFragment getChild(){return child;
    }

    public void setParent(CustomFragment parent){
        this.parent = parent;
    };

    public void setChild(CustomFragment child){
        this.child = child;
    };
}
