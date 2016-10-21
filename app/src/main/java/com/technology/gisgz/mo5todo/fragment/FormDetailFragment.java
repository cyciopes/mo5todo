package com.technology.gisgz.mo5todo.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.FormDetailPagerActivity;
import com.technology.gisgz.mo5todo.adapter.FormDetailAdapter;
import com.technology.gisgz.mo5todo.adapter.decoration.DividerItemDecoration;
import com.technology.gisgz.mo5todo.utils.MyConfig;

/**
 * Created by Jim.Lee on 2016/5/12.
 */
public class FormDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private FormDetailAdapter adapter;
    private FormDetailPagerActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(MyConfig.TAG,"FormDetailFragment onCreateView !!!!!!!!!!!! ");
        View view = inflater.inflate(R.layout.activity_form_detail_fragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.formdetail_recyclerview);
        activity = (FormDetailPagerActivity)getActivity();
        handleRecylcer();
        return view;
    }
    private void handleRecylcer(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

        adapter = new FormDetailAdapter(this.activity.formDetailPOJO,getActivity());
        recyclerView.setAdapter(adapter);

    }
    public void makeChange(boolean isHidenEmpty){
        adapter.prepareList(this.activity.formDetailPOJO,isHidenEmpty);
        adapter.notifyDataSetChanged();
    }
    public void hideEmptyRow(){

    }

}
