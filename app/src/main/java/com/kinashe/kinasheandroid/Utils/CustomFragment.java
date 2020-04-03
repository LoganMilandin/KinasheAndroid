package com.kinashe.kinasheandroid.Utils;

import androidx.fragment.app.Fragment;

public class CustomFragment extends Fragment {

    private CustomFragment parent;
    private CustomFragment child;
    private int navbarIndex;

    public CustomFragment getParent(){return parent;}
    public CustomFragment getChild(){return child;}

    public void setParent(CustomFragment parent){
        this.parent = parent;
    };

    public void setChild(CustomFragment child){
        this.child = child;
    };

    public int getNavbarIndex() {
        return navbarIndex;
    }
    public void setNavbarIndex(int index) {
        this.navbarIndex = index;
    }
}
