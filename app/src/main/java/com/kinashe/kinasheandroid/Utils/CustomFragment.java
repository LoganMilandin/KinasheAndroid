package com.kinashe.kinasheandroid.Utils;

import androidx.fragment.app.Fragment;

/**
 * my attempt to implement my own navigation. This represents
 * a fragment that also acts as a doubly linked tree node, with
 * parent and child fragments to determine nav heirarchy
 */
public class CustomFragment extends Fragment {

    private CustomFragment parent;
    private CustomFragment child;
    //parents pass their navbar index to children when one is created
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
