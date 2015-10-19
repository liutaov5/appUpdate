package com.lt.demo_appupdate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TextImageView extends ImageView {

	private Paint mPaint;
	private Context mContext;
	private String mText;	

	public TextImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		int center = getWidth() / 2;
//		mPaint.setColor(Color.GRAY);
//		mPaint.setStyle(Style.FILL);
//		canvas.drawCircle(center, center, dip2px(mContext, center/2), mPaint);
		mPaint.setColor(Color.WHITE);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTextSize(40);
		FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
		int baseline =  (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		canvas.drawText(mText, center, baseline, mPaint);
		super.onDraw(canvas);
	}
	
	public void setText(String text){
		mText=text;
		invalidate();
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
