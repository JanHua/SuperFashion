package com.sf.test.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sf.R;
import com.sf.fresco.FrescoImageView;
import com.sf.mvp.view.ViewImpl;
import com.sf.widget.recycler.RecyclerBaseAdapter;
import com.sf.widget.recycler.RecyclerBaseView;
import com.sf.widget.recycler.RecyclerLayoutView;

import java.util.ArrayList;
import java.util.List;

/**
 * MVP测试用例 View层
 */
public class MvpTestView extends ViewImpl {

    private RecyclerLayoutView recyclerLayoutView;
    private List<String> dt;
    private String name = "%";
    private TestAdapter testAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.test_fresco_iamge;
    }

    @Override
    public void onInit() {
       /* Button button = (Button) findViewById(R.id.bt);
        FrescoImageView fi = (FrescoImageView) findViewById(R.id.fi);
        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        fi.setImageURI(uri);
        clickEvent(button, fi);*/
        recyclerLayoutView = (RecyclerLayoutView) findViewById(R.id.list_view);
        recyclerLayoutView.setRecyclerBackImp(new RecyclerBaseView.RecyclerMoreImp() {
            @Override
            public void onLoadMore() {
                //addData();
                dt.clear();
                name = "@";
                testAdapter.setData(dt);

                //recyclerLayoutView.setRefreshEnabled(true);
            }
        }, new RecyclerBaseView.RecyclerRefreshImp() {
            @Override
            public void onRefresh() {
                recyclerLayoutView.setState(RecyclerBaseView.STATE_CONTENT);
                //testAdapter.setTextView("无数据了");
                //testAdapter.showFooter();
                /*
                dt.clear();
                name = "@";
               testAdapter.setData(dt);*/
                addData();
                recyclerLayoutView.stopRefresh();

            }
        });

        testAdapter = new TestAdapter(iPresenter.getActivity(), dt);
        testAdapter.showProgressBar();
        recyclerLayoutView.setRefreshEnabled(true);
        recyclerLayoutView.setAdapter(testAdapter);
        addData();
    }

    public void addData() {
        if (dt == null) {
            dt = new ArrayList<String>();
        }
        name = name + name;
        for (int i = 0; i < 15; i++) {
            dt.add(name + i);
        }
        testAdapter.setData(dt);
    }

    public class TestAdapter extends RecyclerBaseAdapter<java.lang.String> {

        public TestAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, int position) {
            String s = data.get(position);
            ((CommentViewHolder) holder).textView.setText("" + s);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            View commentView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_images_test_item, parent, false);
            return new CommentViewHolder(commentView);
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            public SimpleDraweeView simpleDraweeView;
            private TextView textView;

            public CommentViewHolder(View itemView) {
                super(itemView);
                simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_image);
                textView = (TextView) itemView.findViewById(R.id.item_tv);
            }
        }

    }

}
