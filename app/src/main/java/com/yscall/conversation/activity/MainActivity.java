package com.yscall.conversation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yscall.conversation.R;
import com.yscall.conversation.adapter.ConversationAdapter;
import com.yscall.conversation.animation.ConversationItemAnimator;
import com.yscall.conversation.bean.ContentEnum;
import com.yscall.conversation.bean.ContentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 你的样子 on 2018/5/18.
 * 主页面
 *
 * @author gerile
 */
public class MainActivity extends Activity implements ConversationItemAnimator.AnimationEndListener {

    private String[] answers = new String[]{"已为您找到30首歌曲..", "月亮之上", "右手 一个慢动作" +
            "右手 左手 慢动作重播\n" +
            "玺：这首歌 给你快乐" +
            "你有没有爱上我" +
            "源： 跟着我 鼻子" +
            "眼睛 动一动耳朵" +
            "装乖 耍帅 换不停风格\n" +
            "合：青春有太多" +
            "未知的猜测" +
            "成长的烦恼算什么"
            , "咚咚咚！咚咚咚！广州队！！"
            , "《唱起草原的歌》专辑是额尔古纳乐队自2009年成立了“古纳”独立厂牌后制作的首张大碟，全部工作流程都由古纳人独立完成。这是额尔古纳乐队作为“古纳”厂牌的第一次创业，也是“古纳”厂牌进军文化产业的第一步。"
            , "今天罗马赢了巴萨"
            , "今年欧冠冠军是皇家马德里。"};

    private String[] problems = new String[]{"最后我们没在一起", "天下最美的草原", "消愁", "Let's not fall in love", "马头琴诉说情长", "唱一首古老的琴歌恋曲"};
    private List<ContentInfo> list = new ArrayList<>();
    private List<ContentInfo> arr = new ArrayList<>();

    private RecyclerView mRecycler;
    private ConversationAdapter mAdapter;

    private RefreshHandler handler = new RefreshHandler();
    private Handler had = new Handler();
    private boolean isSearch = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Fresco.initialize(this);
        initView();
    }

    private void initView() {
        mRecycler = findViewById(R.id.recycler);
        mAdapter = new ConversationAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        ConversationItemAnimator itemAnimator = new ConversationItemAnimator();
        itemAnimator.setAnimationEndListener(this);
        itemAnimator.setAddDuration(600);
        mRecycler.setItemAnimator(itemAnimator);
        mRecycler.setAdapter(mAdapter);

//        final ContentInfo info = new ContentInfo();
//        info.setType(ContentEnum.ANSWER_TEXT);
//        info.setContent("欢迎来到小V世界~");
//        list.add(info);
//        mAdapter.insertData(info);

        findViewById(R.id.speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearch) {
                    if (mRecycler.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mRecycler.isComputingLayout()) {
                        if (isSlideToBottom(mRecycler)) {
                            ContentInfo info = new ContentInfo();
                            Random random = new Random();
                            int index = random.nextInt(5);
                            info.setType(ContentEnum.PROBLEM_TEXT);
                            info.setContent(problems[index]);
                            list.add(info);
                            mAdapter.insertData(info);
                            mRecycler.scrollToPosition(list.size() - 1);
                            isSearch = false;
                        } else {
                            mRecycler.scrollToPosition(list.size() - 1);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "过于频繁", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /** 有bug */
//                if (isSlideToBottom(mRecycler)) {
//                    if (isSearch) {
//                        ContentInfo info = new ContentInfo();
//                        Random random = new Random();
//                        int index = random.nextInt(5);
//                        info.setType(ContentEnum.PROBLEM_TEXT);
//                        info.setContent(problems[index]);
//                        list.add(info);
//                        mAdapter.insertData(info);
//                        had.postDelayed(runnable, 300);
//                        isSearch = false;
//                    } else if (arr.size() > 0) {
//                        addLoading();
//                        arr.clear();
//                    }
//                }
            }
        });
    }

    @Override
    public void onAnimationEnd(ContentEnum contentEnum) {
        if (contentEnum == ContentEnum.PROBLEM_TEXT) {
            addLoading();
        } else if (contentEnum == ContentEnum.ANSWER_LOADING) {
            Message message = new Message();
            message.what = 0;
            handler.handleMessage(message);
        } else if (contentEnum == ContentEnum.ANSWER_TEXT
                || contentEnum == ContentEnum.ANSWER_VIDEO) {
            Random random = new Random();
            int index = random.nextInt(3);
            if (index < 1) {
                isSearch = true;
            } else {
                addLoading();
            }
        }
    }

    private void addLoading() {
        if (!isSlideToBottom(mRecycler)) {
            isSearch = true;
            return;
        }
        ContentInfo info = new ContentInfo();
        info.setType(ContentEnum.ANSWER_LOADING);
        list.add(info);
        mAdapter.insertData(info);
        mRecycler.scrollToPosition(list.size() - 1);
    }

    private void addLift() {
        if (!isSlideToBottom(mRecycler)) {
            isSearch = true;
            return;
        }
        ContentInfo info = new ContentInfo();
        Random random = new Random();
        int index = random.nextInt(10);
        if (index > 6) {
            info.setType(ContentEnum.ANSWER_VIDEO);
        } else {
            info.setType(ContentEnum.ANSWER_TEXT);
            info.setContent(answers[index]);
        }
        list.add(info);
        mAdapter.insertData(info);
        mRecycler.scrollToPosition(list.size() - 1);
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        return recyclerView != null
                && recyclerView.computeVerticalScrollExtent()
                + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            mRecycler.scrollToPosition(list.size() - 1);
        }
    };

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int lastIndex = list.size();
            list.remove(lastIndex - 1);
            mAdapter.removeData();
            addLift();
        }
    }

}
