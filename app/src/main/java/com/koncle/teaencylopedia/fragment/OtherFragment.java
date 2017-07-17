package com.koncle.teaencylopedia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koncle.teaencylopedia.R;
import com.koncle.teaencylopedia.adapter.MyRecycleViewAdapter;
import com.koncle.teaencylopedia.bean.TeaJson;
import com.koncle.teaencylopedia.data.DataApi;
import com.koncle.teaencylopedia.data.HandlerType;

/**
 * Created by 10976 on 2017/7/16.
 */

public class OtherFragment extends Fragment implements HasName {
    private String name;
    private String url;

    public static Fragment newInstance(String name, String url) {
        OtherFragment otherFragment = new OtherFragment();
        otherFragment.setName(name);
        otherFragment.setUrl(url);
        return otherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycle);
        MyRecycleViewAdapter myRecycleViewAdapter = new MyRecycleViewAdapter(this.getContext(), 0, url);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(myRecycleViewAdapter);

        DataApi.getData(myRecycleViewAdapter.getTeaHandler(), this.url , TeaJson.class, HandlerType.TEA_MSG);
        return view;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
