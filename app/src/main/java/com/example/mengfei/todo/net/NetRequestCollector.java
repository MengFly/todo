package com.example.mengfei.todo.net;

import com.example.todolib.net.CommonOkHttpClient;
import com.example.todolib.net.listener.DisposeDataHandle;
import com.example.todolib.net.listener.DisposeDataListener;
import com.example.todolib.net.request.CommonRequest;
import com.google.gson.Gson;

/**
 * Created by mengfei on 2017/3/17.
 */
public class NetRequestCollector {
    public static  final String ONE_WORDS_URL =  "http://open.iciba.com/dsapi/";

    public static <T>  void requestQuest(final String requestURL, final ResultHandler<T> resultHandler, final Class<T> tClass) {
        new Thread(){
            @Override
            public void run() {
                CommonOkHttpClient.get(CommonRequest.createGetRequest(requestURL, null), new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        T t = new Gson().fromJson(responseObj.toString(), tClass);
                        resultHandler.successResult(t);
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        resultHandler.errorResult(reasonObj);
                    }
                }));
            }
        }.start();
    }

    public static interface ResultHandler<T> {
        public void successResult(T t);
        public void errorResult(Object resultMessage);
    }
}
