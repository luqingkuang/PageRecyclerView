package com.kingty.samples;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kingty.library.PageRecycleView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    int count = 0;
    PageRecycleView pageRecycleView;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageRecycleView = (PageRecycleView) this.findViewById(R.id.recyclerView);
        pageRecycleView.setPageSize(50);
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
                Toast.makeText(MainActivity.this,""+cursor,Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {//模拟网络延时
                    @Override
                    public void run() {
                        List<Integer> list = new ArrayList<Integer>();
                        for (int i = 0; i < pagesize; i++) {
                            list.add(count++);
                        }
                        if(count>100){
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
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
