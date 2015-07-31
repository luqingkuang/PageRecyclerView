# PageRecyclerView
A RecyclerView make pagination easier,
and also can add a header and a footer esayly,
support LinerLayoutManager and StaggeredGridLayoutManager now .

![image](https://github.com/kingty/PageRecyclerView/blob/master/images/screenshot.gif)![image](https://github.com/kingty/PageRecyclerView/blob/master/images/screenshot2.gif)

# Usages
##dependencies
```xml
dependencies {
    ...
    compile 'com.kingty.pageRecyclerView:library:1.0.2'
    ...
}
```

##add in your xml

```xml
<com.kingty.library.PageRecycleView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

## add in your activity
```
pageRecycleView = (PageRecycleView) this.findViewById(R.id.recyclerView);
        pageRecycleView.setCallBack(new PageRecycleView.CallBack() {
            @Override
            public void firstLoadData(final int pagesize) {

                handler.postDelayed(new Runnable() {//模拟网络延时
                    @Override
                    public void run() {
                        List<Integer> list = new ArrayList<Integer>();
                        for(int i = 0;i<pagesize;i++){
                            list.add(count++);
                        }
                        pageRecycleView.initData(list);
                    }
                },2000);

            }

            @Override
            public void refreshData(final int pagesize) {

                count = 0;
                handler.postDelayed(new Runnable() {//模拟网络延时
                    @Override
                    public void run() {
                        List<Integer> list = new ArrayList<Integer>();
                        for (int i = 0; i < pagesize; i++) {
                            list.add(count++);
                        }
                        pageRecycleView.initData(list);
                    }
                }, 2000);
            }

            @Override
            public void lodeNextPage(int cursor, final int pagesize) {
                handler.postDelayed(new Runnable() {//模拟网络延时
                    @Override
                    public void run() {
                        List<Integer> list = new ArrayList<Integer>();
                        for (int i = 0; i < pagesize; i++) {
                            list.add(count++);
                        }
                        if(count>200){
                            pageRecycleView.HasNextPage(false);
                        }
                        pageRecycleView.initData(list);
                    }
                }, 2000);
            }

            @Override
            public void onRecyclerViewScrollStateChanged() {

            }

            @Override
            public void onRecyclerViewScrolled() {

            }
        });

        pageRecycleView.setAdapterCallBack(new PageRecycleView.AdapterCallBack() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

                View v = null;
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
                return new ItemViewHolder(v);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List items) {

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                itemViewHolder.text.setText((items.get(position)) + "");

            }
        });

        pageRecycleView.setHeadOrFooterCallBack(new PageRecycleView.HeadOrFooterCallBack() {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            @Override
            public View addHeader() {

                TextView header = (TextView) inflater.inflate(R.layout.item2, pageRecycleView, false);
                header.setText("this is a header!");
                return header;
            }

            @Override
            public View addFooter() {
                TextView footer = (TextView) inflater.inflate(R.layout.item2, pageRecycleView, false);
                footer.setText("there is no more data,this is a footer!");
                return footer;
            }
        });
```


