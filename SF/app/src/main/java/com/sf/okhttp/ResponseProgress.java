package com.sf.okhttp;

import com.sf.okhttp.util.OtherUtils;
import com.squareup.okhttp.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 后台响应数据进度更新
 * @author wjh
 */
public class ResponseProgress {

	public String handleEntity(ResponseBody responseBod, String charset, CallBackResult callBackResult) throws IOException {
		if (responseBod == null)
			return null;

		long current = 0;
		long total = responseBod.contentLength();

		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();
		try {
			inputStream = responseBod.byteStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
				current += OtherUtils.sizeOfString(line, charset);
				if (callBackResult != null) {
					callBackResult.onProgressUpdate(current, total, false);
				}
			}

			if (callBackResult != null) {
				callBackResult.onProgressUpdate(total, current, true);
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (Throwable e) {
				}
			}
		}
		return sb.toString().trim();
	}
}
