# 对话式列表

修改RecyclerView的默认动画DefaultItemAnimator达到对话式效果

主要修改方法：  
  animateAdd(final ViewHolder holder);  
  animateAddImpl(final ViewHolder holder);  

顾名思义就是添加数据动画，而数据需一条一条添加  
修改入选：
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

<div align=center><img width="220" height="440" src="https://github.com/skymarginal/Conversation/blob/master/image/dialogue.gif"/></div>


