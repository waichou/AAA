package com.example.fragmentweixin.state_recover.intefaces;

import android.os.Bundle;

/**
 * Created by YoKey on 17/6/23.
 */

public interface ISupportFragment {

    // ResultCode
    int RESULT_CANCELED = 0;
    int RESULT_OK = -1;

    
    void onFragmentResult(int requestCode, int resultCode, Bundle data);

}
