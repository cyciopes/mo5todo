package com.technology.gisgz.mo5todo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.FormDetailPagerActivity;
import com.technology.gisgz.mo5todo.adapter.FormDetailAdapter;
import com.technology.gisgz.mo5todo.model.FormRoutingPOJO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jim.Lee on 2016/5/12.
 */
public class FormRoutingFragment extends Fragment {

    private FormDetailAdapter adapter;
    private FormDetailPagerActivity activity;
    private ViewGroup formdetail_routing_container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_form_routing_fragment, container, false);
        formdetail_routing_container = (ViewGroup)view.findViewById(R.id.formdetail_routing_container);
        activity = (FormDetailPagerActivity)getActivity();
//        TextView textview = (TextView)view.findViewById(R.id.texttest);
//
//        String string = "这是一个测试的字符串";
//        SpannableString span = SpannableUtils.setTextSize(string,0,3,12);
//
//        textview.setText(span);
        return view;
    }

    public void makeChange(){
        removeFirst();
        renderRoutingPath();
        renderRoutingHistory();
        View lastBlank = LayoutInflater.from(activity).inflate(R.layout.formdetail_lastblank_viewholder,formdetail_routing_container,false);
        formdetail_routing_container.addView(lastBlank);
    }

    private void renderRoutingHistory() {
        ViewGroup HistoryContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.formdetail_routinghistory_view,formdetail_routing_container,false);
        for (FormRoutingPOJO.ApprovalHisotyRow row :
                activity.formRoutingPOJO.getApprovalHisotyRowList()) {
            ViewGroup historyRowContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.formdetail_routinghistory_view_part1, HistoryContainer,false);
            TextView updatedBy = (TextView)historyRowContainer.findViewById(R.id.updatedBy);
            TextView updatedAt = (TextView)historyRowContainer.findViewById(R.id.updatedAt);
            TextView actionType = (TextView)historyRowContainer.findViewById(R.id.actionType);
            TextView logInfo = (TextView)historyRowContainer.findViewById(R.id.logInfo);
            TextView isCausedLevelChange = (TextView)historyRowContainer.findViewById(R.id.isCausedLevelChange);
            updatedBy.setText(row.getUpdatedBy());
            updatedAt.setText(row.getUpdatedAt());
            actionType.setText(row.getActionType());
            logInfo.setText(row.getLogInfo());
            isCausedLevelChange.setText(row.isCausedLevelChange()?"Yes":"No");
            HistoryContainer.addView(historyRowContainer);
        }
        formdetail_routing_container.addView(HistoryContainer);
    }

    private void renderRoutingPath() {
        boolean isAlreadyShowPathTitle = false;
        List<List<FormRoutingPOJO.ApprovalPathRow>> list = groupByLevel(activity.formRoutingPOJO);
        sortListByLevel(list);
        for (List<FormRoutingPOJO.ApprovalPathRow> levelList:list
             ) {
            ViewGroup levelContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.formdetail_routingpath_view,formdetail_routing_container,false);
            if(!isAlreadyShowPathTitle) {
                levelContainer.findViewById(R.id.formdetail_routingpath_title).setVisibility(View.VISIBLE);
                isAlreadyShowPathTitle = true;
            }
            TextView levelDes = (TextView)levelContainer.findViewById(R.id.formdetail_routing_leveldescription);
            levelDes.setText(levelList.get(0).getSequence()+" "+levelList.get(0).getLevelDescription());
            for (FormRoutingPOJO.ApprovalPathRow vo :
                    levelList) {
                ViewGroup approverContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.formdetail_routingpath_view_part1, levelContainer,false);
                TextView user = (TextView)approverContainer.findViewById(R.id.User);
                TextView action = (TextView)approverContainer.findViewById(R.id.action);
                TextView actionDate = (TextView)approverContainer.findViewById(R.id.action_date);
                TextView comment = (TextView)approverContainer.findViewById(R.id.comment);
                user.setText(vo.getUserName());
                action.setText(vo.getActionType());
                actionDate.setText(vo.getActionDateTime().startsWith("1900")?"":vo.getActionDateTime());
                comment.setText(vo.getComment());
                levelContainer.addView(approverContainer);
            }
            formdetail_routing_container.addView(levelContainer);
        }
    }

    private void removeFirst() {
        formdetail_routing_container.removeAllViews();
    }

    private void sortListByLevel(List<List<FormRoutingPOJO.ApprovalPathRow>> list) {
        Collections.sort(list, new Comparator<List<FormRoutingPOJO.ApprovalPathRow>>() {
            @Override
            public int compare(List<FormRoutingPOJO.ApprovalPathRow> t1, List<FormRoutingPOJO.ApprovalPathRow> t2) {
                if(t1.get(0).getSequence() < t2.get(0).getSequence()){
                    return -1;
                }else if(t1.get(0).getSequence() > t2.get(0).getSequence()){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
    }

    private List<List<FormRoutingPOJO.ApprovalPathRow>> groupByLevel(FormRoutingPOJO formRoutingPOJO){
        Map<String,List<FormRoutingPOJO.ApprovalPathRow>> map = new HashMap<>();
        for (FormRoutingPOJO.ApprovalPathRow vo:formRoutingPOJO.getApprovalPathRowList()) {
            String key = vo.getSequence()+vo.getLevelDescription();
            if(map.containsKey(key)){
                map.get(key).add(vo);
            }else{
                List<FormRoutingPOJO.ApprovalPathRow> list = new ArrayList<>();
                list.add(vo);
                map.put(key,list);
            }
        }
        return new ArrayList<>(map.values());
    }


}
