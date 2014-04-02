package com.unrulyrecursion.partkeeprconnector.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerTitleStrip;
import android.util.AttributeSet;
import android.widget.TextView;

public class PkprPagerTitle extends PagerTitleStrip {

	public PkprPagerTitle(Context context) {
		super(context);
	}
	public PkprPagerTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	// Grabbed from stack overflow
	// http://stackoverflow.com/questions/12882746/how-to-change-the-font-of-a-pagertitlestrip/17371290#17371290
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);// Load Font Face
		String fontPath = "fonts/Ubuntu/Ubuntu-BoldItalic.ttf";
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        for (int i=0; i<this.getChildCount(); i++) {
            if (this.getChildAt(i) instanceof TextView) {
                ((TextView)this.getChildAt(i)).setTypeface(tf);
            }
        }
    }
}