package com.zhilianxinke.schoolinhand.modules.autoupdate;

/**
 * Created by hh on 2015-02-02.
 */
public interface IDownload {
    void  onPreDownload(String... str);
    void  onUpdateDownload(Integer... progress);// currentProgress, max
    void  onPostDownload(boolean result);
}
