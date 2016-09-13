package com.beiing.customview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.beiing.customview.R;

/**
 * Created by chenliu on 2016/9/13 0013<br/>.
 * 描述：
 */
public class PtRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View header;
    private int headerHeight;

    private int firstVisibleItem;

    private int scrollState;

    private boolean isRemark;//当前是在最顶端按下的
    private float startY;//按下时的y坐标

    private int state;//滑动状态

    public static final int NONE = 0;

    public static final int PULL = 1;

    public static final int RELEASE = 2;

    public static final int REFRESHING = 3;

    public PtRefreshListView(Context context) {
        this(context, null, 0);
    }

    public PtRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        header = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null);
        this.addHeaderView(header);
        header.post(new Runnable() {
            @Override
            public void run() {
                headerHeight = header.getMeasuredHeight();
                topPadding(-headerHeight);
            }
        });

        this.setOnScrollListener(this);
    }

    private void topPadding(int topPadding){
        topPadding = Math.min(topPadding, 500);
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(firstVisibleItem == 0){
                    isRemark = true;
                    startY = ev.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;

            case MotionEvent.ACTION_UP:
                if(state == RELEASE){
                    state = REFRESHING;

                } else if(state == PULL){
                    state = NONE;
                    isRemark = false;
                }
                break;

        }
        return true;
    }


    private void onMove(MotionEvent ev){
        if(isRemark){
            float nowY = ev.getY();
            int dy = (int) (nowY - startY);
            int topPadding = (dy - headerHeight) / 2;
            switch (state){
                case NONE:
                    if(dy > 0){
                        state = PULL;
                    }
                    refreshHeader();
                    break;

                case PULL:
                    topPadding(topPadding);
                    if(dy > headerHeight + 50 && scrollState == SCROLL_STATE_TOUCH_SCROLL){
                        state = RELEASE;
                    }
                    refreshHeader();
                    break;

                case RELEASE:
                    if(dy < headerHeight + 50){
                        state = PULL;
                    } else if(dy <= 0){
                        state = NONE;
                        isRemark = false;
                    }
                    refreshHeader();
                    break;

                case REFRESHING:
                    refreshHeader();
                    break;

            }
        }
    }

    private void refreshHeader(){
        TextView tvHeader = (TextView) header.findViewById(R.id.tv_header);
        switch (state){
            case NONE:
                topPadding(-headerHeight);
                break;

            case PULL:
                tvHeader.setText("下拉刷新");
                break;

            case RELEASE:
                tvHeader.setText("松开刷新");
                break;

            case REFRESHING:
                topPadding(50);
                tvHeader.setText("刷新中...");
                break;

        }
    }

}














