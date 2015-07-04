package com.rims_new;

import java.text.AttributedCharacterIterator.Attribute;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.renderscript.Sampler.Value;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithText extends View 
{
    private int mColor=0xFF45C01A;
    private Bitmap mIconBitmap;
    private String mText="RIMS";
    private int mTextSize=(int)TypedValue.applyDimension(
    		TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
  
	private float mAlpha;
   
	private Rect mIconRect;
	private Rect mTextBound;
	
	private Paint mTextPaint;
    
    public ChangeColorIconWithText(Context context)
	{
		this(context,null);
		
	}

	public ChangeColorIconWithText(Context context, AttributeSet attr) {
		this(context,attr,0);
		
	}
    //��ȡ�Զ������Ե�ֵ
	public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.ChangeColorIconWithText);
		int n=a.getIndexCount();
		for(int i=0;i<n;i++)
		{
			int attr=a.getIndex(i);
			switch(attr)
			{
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable=(BitmapDrawable) a.getDrawable(attr);
				mIconBitmap=drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				mColor=a.getColor(attr, 0xFF45C01A);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText=a.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mTextSize=(int)a.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
				break;				
			}
		}
		a.recycle();
		mTextBound=new Rect();
		mTextPaint=new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff555555);		
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);	
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height());

		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth)
				/ 2;
		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
      
		int alpha = (int) Math.ceil(255 * mAlpha);
		//�ڴ�ȥ׼��mBitmap,setAlpha,��ɫ��xfermode,ͼ��
		setupTargetBitmap(alpha);
		//1.����ԭ�ı������Ʊ�ɫ���ı�
		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		
		canvas.drawBitmap(mBitmap, 0, 0, null);

	}
	
 
	/**
	 * ���Ʊ�ɫ���ı�
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);

	}

	/**
	 * ����ԭ�ı�
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255 - alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * ���ڴ��л��ƿɱ�ɫ��icon
	 */
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}
	public void setIconAlpha(float alpha)
	{
		this.mAlpha = alpha;
		invalidateView();
	}
 
	/**
	 * �ػ�
	 */
	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper())
		{
			invalidate();
		} else
		{
			postInvalidate();
		}
		
	}
}
