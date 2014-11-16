package com.movie.locations.application;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ViewPagerHelper extends ViewPager {

    public ViewPagerHelper(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        // we some the listner
        protected OnPageChangeListener listener;

        @Override
        public void setOnPageChangeListener(OnPageChangeListener listener) {
            super.setOnPageChangeListener(listener);
            this.listener = listener;
        }

        @Override
        public void setCurrentItem(int item) {
            // when you pass set current item to 0,
            // the listener won't be called so we call it on our own
            boolean invokeMeLater = false;

            if(super.getCurrentItem() == 0 && item == 0)
                invokeMeLater = true;

            super.setCurrentItem(item);

            if(invokeMeLater)
                listener.onPageSelected(0);
        }

}