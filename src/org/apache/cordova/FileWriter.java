package org.apache.cordova;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

public class FileWriter extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if ("cordovaSetFileContents".equals(action)) {
			try {
				JSONObject parameters = args.getJSONObject(0);
				if (parameters != null) {
					setFileContents(parameters);
				}
			} catch (Exception e) {
				LOG.e("FileWriter", "Error parsing parameters: " + e.toString());
			}
			callbackContext.success();
			return true;
		}
		return false;
	}
	
	private void setFileContents(JSONObject parameters) {
		String directory = null;
		String fileName = null;
		byte[] byteArray = null;
		File f;
		FileOutputStream fos;
		try {
			directory = Environment.getExternalStorageDirectory().toString();
			fileName = parameters.getString("fileName");
			JSONArray byteJSONArray = parameters.getJSONArray("byteArray");
			byteArray = new byte[byteJSONArray.length()];
		    for (int i = 0; i < byteJSONArray.length(); i++) {
		    	byteArray[i] = (byte) byteJSONArray.getInt(i);
		    }
			
			File file = new File(fileName);
			String directoryFileName = file.getParent();
			String simpleFileName = file.getName();
			
			f = new File(directory + directoryFileName);
			if (!f.exists()) {
				f.mkdirs();
			}
			f.setWritable(true);
			
			f = new File(f, simpleFileName);
			f.createNewFile();
			f.setWritable(true);
			
			fos = new FileOutputStream(f);
			fos.write(byteArray);
			fos.close();
		} catch (Exception e) {
			LOG.e("FileWriter", "Error writing into file: " + e.toString());
		}
	}
}
