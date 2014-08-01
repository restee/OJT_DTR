package com.example.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

public class FontAndToastUtils {
	
	Context context;
	
	public FontAndToastUtils(Context context) {
		
		this.context = context;
		
	}
	
	public Typeface getTtfFont(String fontName) {
		
		return Typeface.createFromAsset(context.getAssets(),
				fontName);
	}
	
	public void promptUser(String message) {

		Toast.makeText(context, message, Toast.LENGTH_LONG).show();

	}

}
