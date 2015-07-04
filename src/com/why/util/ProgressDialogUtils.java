package com.why.util;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View.OnClickListener;

public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;

	/**
	 * œ‘ æProgressDialog
	 * @param context
	 * @param message
	 */
	public static void showProgressDialog(Context context, CharSequence message){
		if(mProgressDialog == null){
			mProgressDialog = ProgressDialog.show(context, "", message);
		}else{
			mProgressDialog.show();
		}
	}
	
	/**
	 * πÿ±’ProgressDialog
	 */
	public static void dismissProgressDialog(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}


}
