package com.example.piyush.recyclerwithscroll.Behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.piyush.recyclerwithscroll.R;

import edmt.dev.advancednestedscrollview.MaxHeightRecyclerView;
import edmt.dev.advancednestedscrollview.MyViewGroupUtils;

public class CustomBehavior extends CoordinatorLayout.Behavior<NestedScrollView>
{
    public CustomBehavior() {
    }

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, @NonNull View dependency) {
        return dependency.getId()==R.id.toolbar_container;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);

        int fabHalfHeight = child.findViewById(R.id.fab).getHeight()/2;
        setTopMargin(child.findViewById(R.id.card_view),fabHalfHeight);
        int rvMaxheight =child.getHeight() - fabHalfHeight -child.findViewById(R.id.card_title).getHeight()
                -child.findViewById(R.id.card_sub_title).getHeight();

        MaxHeightRecyclerView rv=child.findViewById(R.id.card_recycler_view);
        rv.setMaxHeight(rvMaxheight);

        View cardContainer=child.findViewById(R.id.card_container);
        int toolbarContainerHeight=parent.getDependencies(child).get(0).getHeight();
        setPaddingTop(cardContainer,rvMaxheight-toolbarContainerHeight);
        ViewCompat.offsetTopAndBottom(child,toolbarContainerHeight);
        setPaddingBottom(rv,toolbarContainerHeight);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, @NonNull MotionEvent ev) {
        return ev.getActionMasked()==MotionEvent.ACTION_DOWN &&
                isTouchChildBounds(parent,child,ev)
                && !isTouchChildBounds(parent,child.findViewById(R.id.card_view),ev)
                && !isTouchChildBounds(parent,child.findViewById(R.id.fab),ev);

    }

    private boolean isTouchChildBounds(ViewGroup parent, View child, MotionEvent ev) {
        return MyViewGroupUtils.isPointInChildBounds(parent,child,(int)ev.getX(),(int)ev.getY());
    }

    private void setPaddingBottom(View view, int bottom) {
        if(view.getPaddingBottom()!=bottom)
        {
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),bottom);
        }
    }

    private void setPaddingTop(View view, int top) {
        if(view.getPaddingTop()!=top)
        {
            view.setPadding(view.getPaddingLeft(),top,view.getPaddingRight(),view.getPaddingBottom());
        }
    }

    private void setTopMargin(View view, int top) {
        ViewGroup.MarginLayoutParams lp=(ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if(lp.topMargin!=top)
        {
            lp.topMargin=top;
            view.setLayoutParams(lp);
        }

    }
}
