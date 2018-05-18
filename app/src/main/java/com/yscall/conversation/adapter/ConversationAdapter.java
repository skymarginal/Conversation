package com.yscall.conversation.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yscall.conversation.R;
import com.yscall.conversation.bean.ContentEnum;
import com.yscall.conversation.bean.ContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 你的样子 on 2018/4/10.
 * 列表适配器
 *
 * @author gerile
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ContentInfo> data;

    public ConversationAdapter() {
        data = new ArrayList<>();
    }

    public void addData(List<ContentInfo> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void insertData(ContentInfo msg) {
        int lastIndex = data.size();
        data.add(msg);
        notifyItemInserted(lastIndex);
    }

    public void removeData() {
        int lastIndex = data.size();
        data.remove(lastIndex - 1);
        notifyItemRemoved(lastIndex - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == ContentEnum.ANSWER_VIDEO.ordinal()) {
            return new AnswerVideoHolder(LayoutInflater.from(context).inflate(R.layout.item_answer_video, parent, false));
        } else if (viewType == ContentEnum.PROBLEM_TEXT.ordinal()) {
            return new ProblemTextHolder(LayoutInflater.from(context).inflate(R.layout.item_problem_text, parent, false));
        } else if (viewType == ContentEnum.ANSWER_LOADING.ordinal()) {
            return new AnswerLoadingHolder(LayoutInflater.from(context).inflate(R.layout.item_answer_loading, parent, false));
        } else {
            return new AnswerTextHolder(LayoutInflater.from(context).inflate(R.layout.item_answer_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AnswerVideoHolder) {

        } else if (holder instanceof ProblemTextHolder) {
            ((ProblemTextHolder) holder).tv.setText(data.get(position).getContent());
        } else if (holder instanceof AnswerLoadingHolder) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true).setUri(Uri.parse("res://" + context.getPackageName() + "/" + R.drawable.loading)).build();
            ((AnswerLoadingHolder) holder).img.setController(controller);
        } else {
            ((AnswerTextHolder) holder).tv.setText(data.get(position).getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        ContentInfo msg = data.get(position);
        ContentEnum searchEnum = msg.getType();
        switch (searchEnum) {
            case ANSWER_TEXT:
                viewType = ContentEnum.ANSWER_TEXT.ordinal();
                break;
            case ANSWER_VIDEO:
                viewType = ContentEnum.ANSWER_VIDEO.ordinal();
                break;
            case ANSWER_LOADING:
                viewType = ContentEnum.ANSWER_LOADING.ordinal();
                break;
            case PROBLEM_TEXT:
                viewType = ContentEnum.PROBLEM_TEXT.ordinal();
                break;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AnswerTextHolder extends RecyclerView.ViewHolder {

        TextView tv;

        AnswerTextHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.answer_tv);
        }
    }

    public class AnswerVideoHolder extends RecyclerView.ViewHolder {

        AnswerVideoHolder(View itemView) {
            super(itemView);
        }
    }

    public class AnswerLoadingHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;

        AnswerLoadingHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.loading_img);
        }
    }

    public class ProblemTextHolder extends RecyclerView.ViewHolder {

        TextView tv;

        ProblemTextHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.problem_tv);
        }
    }

}
