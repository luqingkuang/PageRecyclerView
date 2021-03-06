package com.kingty.library;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PageRecycleView extends RelativeLayout {
    RecyclerView page_recyclerView;
    ImageView page_top;
    CustomSwipeToRefresh page_swipeLayout;
    RelativeLayout page_load_more;
    private Context context;
    private View view;
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    private boolean mIsOnCreate = true;
    private boolean mIsLoadingMore = true;
    private boolean isNeedTopBunton = true;
    private boolean isNeedLoadMore = true;

    private int pageSize = 20;
    private int cursor = 0;//start page index
    List items = new ArrayList();
    private boolean hasNextPage = true;
    PageRecyclerViewAdapter pageRecyclerViewAdapter;
    private Bookends<PageRecyclerViewAdapter> mBookends;
    //top buttun animation
    AlphaAnimation mAlphaAnimationToMiss;
    AlphaAnimation mAlphaAnimationToShow;

    //init the animation
    private void initAnima() {
        mAlphaAnimationToMiss = new AlphaAnimation(1.0f, 0f);
        mAlphaAnimationToMiss.setDuration(1000);
        mAlphaAnimationToShow = new AlphaAnimation(0f, 1f);
        mAlphaAnimationToShow.setDuration(1000);
    }

    public void notityAdapter() {
        if (mBookends != null)
            mBookends.notifyDataSetChanged();
    }
    public PageRecycleView(Context context) {
        super(context);
    }

    public PageRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public PageRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.page_recyclerview, this, true);
        page_recyclerView = (RecyclerView) view.findViewById(R.id.page_recyclerView);
        page_top = (ImageView) view.findViewById(R.id.page_top);
        page_swipeLayout = (CustomSwipeToRefresh) view.findViewById(R.id.page_swipeLayout);
        page_load_more = (RelativeLayout) view.findViewById(R.id.page_load_more);
        page_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                page_recyclerView.smoothScrollToPosition(0);
            }
        });
        page_recyclerView.setLayoutManager(linearLayoutManager);
        page_recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        page_swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        page_swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cursor = 0;
                mIsOnCreate = true;
                hasNextPage = true;
                callBack.refreshData(pageSize);
            }
        });
        page_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://stop scroll
                        if (!mIsOnCreate) {
                            if (null == page_top)
                                return;
                            page_top.setAnimation(mAlphaAnimationToShow);
                            mAlphaAnimationToShow.start();
                            page_top.setAlpha(1f);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (!mIsOnCreate) {
                            if (null == page_top)
                                return;
                            page_top.setAlpha(0f);
                            page_top.setAnimation(mAlphaAnimationToMiss);
                            mAlphaAnimationToMiss.start();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        if (!mIsOnCreate) {
                            if (null == page_top)
                                return;
                            page_top.setAlpha(0f);
                        }
                        break;
                }
                callBack.onRecyclerViewScrollStateChanged();
            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//                // dy>0 表示向下滑动
//
//                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
//                    if (mIsLoadingMore) {
//                        //加载操作
//                        callBack.lodeNextPage(cursor, pageSize);
//                        //防止加载两次的情况
//                        mIsLoadingMore = false;
//                    }
//                }
//                callBack.onRecyclerViewScrolled();
//            }
        });
        pageRecyclerViewAdapter = new PageRecyclerViewAdapter();
        mBookends = new Bookends<>(pageRecyclerViewAdapter);
    }

    public void LoadData() {
        if (mIsOnCreate) {
            callBack.firstLoadData(pageSize);
        }
    }

    public void setIsNeedRefresh(boolean isNeedRefresh) {
        page_swipeLayout.setNeedRefresh(isNeedRefresh);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        page_recyclerView.setLayoutManager(manager);
    }

    private void addHeader(View header) {
        if (mBookends != null) {
            mBookends.addHeader(header);
        }
    }

    private void addFooter(View footer) {
        if (mBookends != null) {
            mBookends.addFooter(footer);
        }
    }

    public List getItems() {
        return items;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setIsNeedTopBunton(boolean isNeedTopBunton) {
        this.isNeedTopBunton = isNeedTopBunton;
    }

    public void setIsNeedLoadMore(boolean isNeedLoadMore) {
        this.isNeedLoadMore = isNeedLoadMore;
    }

    public void HasNextPage(boolean yes) {
        this.hasNextPage = yes;
    }

    //this mothed need init  in the host
    public <T> void initData(List<T> getItems) {

        initAnima();
        if (mIsOnCreate) {
            if (items.size() == 0) {
                //first time init adapter
                items = getItems;

                if (headOrFooterCallBack != null && headOrFooterCallBack.addHeader() != null) {
                    mBookends.addHeader(headOrFooterCallBack.addHeader());
                }
                if (headOrFooterCallBack != null && headOrFooterCallBack.addFooter() != null ) {
                    mBookends.addFooter(headOrFooterCallBack.addFooter());
                }
                if (mBookends.getFooter(0) != null) {
                    mBookends.setFooterVisibility(false);
                }
                page_recyclerView.setAdapter(mBookends);
                page_recyclerView.post(new Runnable() {
                                           @Override
                                           public void run() {
                                               mBookends.notifyDataSetChanged();
                                           }
                                       }
                );
                if (isNeedLoadMore) {
                    page_recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if(page_recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                                        int lastPosition = page_recyclerView.getAdapter().getItemCount() - 1;
                                        int into[] = ((StaggeredGridLayoutManager)page_recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPositions(null);
                                        Arrays.sort(into);

                                        if (null != into && into.length > 0 && lastPosition == into[into.length - 1] && mIsLoadingMore) {
                                            //load more
                                            callBack.lodeNextPage(cursor, pageSize);
                                            page_load_more.setVisibility(View.VISIBLE);
                                            //in case of load again
                                            mIsLoadingMore = false;
                                        }
                                    }else {
                                        int lastPosition = page_recyclerView.getAdapter().getItemCount() - 1;
                                        int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                                        if (lastVisiblePosition == lastPosition && mIsLoadingMore) {
                                            //load more
                                            callBack.lodeNextPage(cursor, pageSize);
                                            page_load_more.setVisibility(View.VISIBLE);
                                            //in case of load again
                                            mIsLoadingMore = false;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                }
            } else {
                //refresh

                items = getItems;
                mBookends.notifyDataSetChanged();
                page_swipeLayout.setRefreshing(false);
                if (mBookends.getFooter(0) != null) {
                    mBookends.setFooterVisibility(false);
                }
            }
            mIsOnCreate = false;
            cursor += pageSize;
        } else {
            if (isNeedTopBunton) {
                page_top.setVisibility(View.VISIBLE);
            }
            //load more
            items.addAll(getItems);
            mBookends.notifyDataSetChanged();
            cursor += pageSize;
            page_load_more.setVisibility(View.GONE);
        }
        if (!hasNextPage) {
            mIsLoadingMore = false;
            mBookends.setFooterVisibility(true);

        } else {
            //in case of load again,set it back
            mIsLoadingMore = true;
        }


    }


    class PageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return adapterCallBack.onCreateViewHolder(viewGroup, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            adapterCallBack.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }
    }


    /**
     * callback method
     */
    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
        LoadData();
    }

    public static interface CallBack {
        void initRecyclerView(RecyclerView recyclerView);

        void firstLoadData(int pagesize);

        void refreshData(int pagesize);

        void lodeNextPage(int cursor, int pagesize);

        void onRecyclerViewScrollStateChanged();

        void onRecyclerViewScrolled();
    }


    public AdapterCallBack adapterCallBack;

    public void setAdapterCallBack(AdapterCallBack adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    public static interface AdapterCallBack {
        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType);

        void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);
    }

    public HeadOrFooterCallBack headOrFooterCallBack;

    public void setHeadOrFooterCallBack(HeadOrFooterCallBack headOrFooterCallBack) {
        this.headOrFooterCallBack = headOrFooterCallBack;
    }

    public static interface HeadOrFooterCallBack {
        View addHeader();

        View addFooter();
    }



}
