package utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import application.MyApplication;

/**
 * @author Wei.zhou
 * @date 2017年5月8日
 * @time 下午2:47:00
 * @desc 获取内外置Sd卡路径
 */
public class SDCardUtils {
	private static String[] extSdPathOrInnerSdPath = new String[2];
	//获取设备内置路径
	public static String EXTSDPATH = "";
	//获取外置SD卡路径
	public static String INNERSDPATH = "";
	static{
		try {
			extSdPathOrInnerSdPath = getStoragePath(MyApplication.getIntance().mContext);
			
			INNERSDPATH = extSdPathOrInnerSdPath[0];
			EXTSDPATH = extSdPathOrInnerSdPath[1];
		} catch (Exception e) {
		}
	}

	/**
	 * 获取设备存储路径（包含有SD路径）
	 * @param context
	 * @return
	 */
	private static String[] getStoragePath(Context context) {
		String[] volumePaths = null;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				StorageManager sm = (StorageManager) context
						.getSystemService(Context.STORAGE_SERVICE);
				try {
					volumePaths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[]{}).invoke(sm, new Object[]{});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return volumePaths;
	}

}
