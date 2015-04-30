package com.zhilianxinke.schoolinhand.base;


import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;

/**
 * Created by hh on 2015-03-26.
 */
public abstract class BaseApiActivity extends BaseActivity implements ApiCallback {

    public abstract void onCallApiSuccess(AbstractHttpRequest request, Object obj);

    public abstract void onCallApiFailure(AbstractHttpRequest request, BaseException e);

    @Override
    public void onComplete(final AbstractHttpRequest request, final Object obj) {

        runOnUiThread(new Runnable() {
            public void run() {
                onCallApiSuccess(request, obj);
            }
        });

    }

    @Override
    public void onFailure(final AbstractHttpRequest request, final BaseException e) {

        runOnUiThread(new Runnable() {
            public void run() {
                onCallApiFailure(request, e);
            }
        });

    }

}
