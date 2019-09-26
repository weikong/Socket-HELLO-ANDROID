package com.king.chat.socket.util;

import android.app.Activity;

import com.king.chat.socket.App;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Activity栈管理
 *
 * @author luomin
 */
public class FilterTimeOutManager {

    public final int TimeOut = 10;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
    private Map<String,Future<String>> futureMap = new HashMap<>();

    private static class FilterTimeOutManagerHolder{
        private static final FilterTimeOutManager INSTANCE = new FilterTimeOutManager();
    }

    /**
     * 单一实例
     */
    public static final FilterTimeOutManager getInstance(){
        return FilterTimeOutManagerHolder.INSTANCE;
    }

    public void scheduledFuture(final ChatRecordData baseBean){
        if (baseBean == null)
            return;
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Logger.e("FilterTimeOutManager",baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
                    TimeUnit.SECONDS.sleep(TimeOut+2);
                } catch (InterruptedException e) {
                    Logger.e("FilterTimeOutManager","任务被中断"+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
                }
                Logger.e("FilterTimeOutManager","scheduledFuture"+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
                return  "TimeOut";
            }
        });
        try {
            futureMap.put(baseBean.getMessageid(),future);
            String result = future.get(TimeOut, TimeUnit.SECONDS);
            Logger.e("FilterTimeOutManager","result = "+result+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
        } catch (InterruptedException |ExecutionException | TimeoutException| CancellationException e) {
            if (futureMap.containsKey(baseBean.getMessageid()) && futureMap.get(baseBean.getMessageid()) != null){
                try {
                    Future<String> future2 = futureMap.get(baseBean.getMessageid());
                    if (future2 != null)
                        future2.cancel(true);
                } catch (Exception e2){
                    e2.printStackTrace();
                }
                //任务超时，更新数据为“未发送成功”
                if (baseBean.getMessagetype() == 9){ //聊天消息
                    baseBean.setMessagestate(9);
                    DBChatRecordImpl.getInstance().updateChatRecord(baseBean);
                    BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_UPDATE_MESSAGE,baseBean);
                } else if (baseBean.getMessagetype() == 4){//登录状态
                    BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_DISCONNECT);
                }
            }
            if (future != null){
                future.cancel(true);
            }
            Logger.e("FilterTimeOutManager","任务超时"+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
        }finally {
            Logger.e("FilterTimeOutManager","清理资源"+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
            if (futureMap.containsKey(baseBean.getMessageid())){
                futureMap.remove(baseBean.getMessageid());
            }
        }

    }

    public void scheduledNotTimeOut(ChatRecordData baseBean){
        Logger.e("FilterTimeOutManager","NotTimeOut ");
        if (baseBean == null)
            return;
        if (futureMap.containsKey(baseBean.getMessageid())){
            try {
                Future<String> future = futureMap.get(baseBean.getMessageid());
                futureMap.remove(baseBean.getMessageid());
                future.cancel(true);
                future = null;
                Logger.e("FilterTimeOutManager","NotTimeOut 任务超时"+"   "+baseBean.getMessageid()+"   "+baseBean.getMessagecontent());
            } catch (Exception e){
                e.printStackTrace();
            }finally {
            }
        }
    }
}
