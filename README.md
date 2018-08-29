# 对话式列表

重写RecyclerView默认动画DefaultItemAnimator，修改其中添加动画的方法；

主要方法：  
---
  animateAdd(final ViewHolder holder);  
  animateAddImpl(final ViewHolder holder);  
 
修改如下：
---
```
@Override
public boolean animateAdd(final ViewHolder holder) {
    resetAnimation(holder);
    ViewCompat.setAlpha(holder.itemView, 0f);
    if(holder instanceof ConversationAdapter.ProblemTextHolder){
        ViewCompat.setTranslationX(holder.itemView, holder.itemView.getWidth());
    }else {
        ViewCompat.setScaleX(holder.itemView,0f);
        ViewCompat.setScaleY(holder.itemView,0f);
    }
    mPendingAdditions.add(holder);
    return true;
}    
```

```
void animateAddImpl(final ViewHolder holder) {
    final ContentEnum searchEnum;
    final View view = holder.itemView;
    final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
    mAddAnimations.add(holder);
    if(holder instanceof ConversationAdapter.ProblemTextHolder){
        animation.alpha(1f).translationX(1f).setDuration(getAddDuration()/2);
        searchEnum = ContentEnum.PROBLEM_TEXT;
    }else if(holder instanceof ConversationAdapter.AnswerLoadingHolder){
        animation.alpha(1f).scaleX(1f).scaleY(1f).setDuration(getAddDuration());
        searchEnum = ContentEnum.ANSWER_LOADING;
    }else {
        animation.alpha(1f).scaleX(1f).scaleY(1f).setDuration(getAddDuration());
        searchEnum = ContentEnum.ANSWER_TEXT;
    }
    animation.setListener(new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
            dispatchAddStarting(holder);
        }

        @Override
        public void onAnimationCancel(View view) {
            ViewCompat.setAlpha(view, 1f);
        }

        @Override
        public void onAnimationEnd(View view) {
            animation.setListener(null);
            dispatchAddFinished(holder);
            mAddAnimations.remove(holder);
            dispatchFinishedWhenDone();
            if(animationEndListener != null){
                animationEndListener.onAnimationEnd(searchEnum);
            }
        }
    });
    animation.start();
}
```
  
最终效果：  
---
<div align=center><img width="220" height="440" src="https://github.com/skymarginal/Conversation/blob/master/image/dialogue.gif"/></div>
  
有收获的朋友点个赞吧！


