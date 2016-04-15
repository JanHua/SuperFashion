package com.sf.test.moudle;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wjh on 16/2/17.
 */
public class MVPTestPrinting {

    private String printContent;

    public MVPTestPrinting(String printContent) {
        this.printContent = printContent;
    }

    public void printContent() {
        Log.e("MVP-Print", printContent);
    }

    public void toastContent(Activity activity) {
        Toast.makeText(activity, printContent, Toast.LENGTH_SHORT).show();
    }
}
