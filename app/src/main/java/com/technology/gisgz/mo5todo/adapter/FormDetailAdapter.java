package com.technology.gisgz.mo5todo.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.technology.gisgz.mo5todo.R;
import com.technology.gisgz.mo5todo.activity.DataGridActivity;
import com.technology.gisgz.mo5todo.activity.FormDetailPagerActivity;
import com.technology.gisgz.mo5todo.activity.MyBaseActivity;
import com.technology.gisgz.mo5todo.activity.RTFContentActivity;
import com.technology.gisgz.mo5todo.model.BasePOJO;
import com.technology.gisgz.mo5todo.model.FormDetailPOJO;
import com.technology.gisgz.mo5todo.utils.MyConfig;
import com.technology.gisgz.mo5todo.utils.MyHttpService;
import com.technology.gisgz.mo5todo.utils.common.SpannableUtils;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.apmem.tools.layouts.FlowLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jim.Lee on 2016/4/29.
 */
public class FormDetailAdapter extends RecyclerView.Adapter<FormDetailAdapter.FormDetailBaseViewHolder> {
    private FormDetailPOJO formDetailPOJO;
    private List<FormDetailPOJO.FormRows> rowList = new ArrayList<>();
    private Context context;
    // lastRow is for display last row data when scroll to bottom
    private int lastRow = 1;
    private int attachmentRow=1;

    public FormDetailAdapter(FormDetailPOJO fd, Context context){
        this.formDetailPOJO = fd;
        this.context = context;
        prepareList(formDetailPOJO,false);
    }
    public void prepareList(FormDetailPOJO formDetailPOJO,boolean isHidenEmpty){
        this.formDetailPOJO = formDetailPOJO;
        rowList.clear();
        if(formDetailPOJO.getInfoRow()!=null){
            rowList.addAll(formDetailPOJO.getInfoRow());
        }
        if(isHidenEmpty){
            for (FormDetailPOJO.FormRows fr:formDetailPOJO.getRows()
                 ){
                boolean rowIsEmpty = true;
                for (FormDetailPOJO.FormField field :
                        fr.getFormFields()) {
                    int type = field.getFieldType();
                    if(isInputControl(type)){
                        if(checkIsEmptyField(field)){
                            continue;
                        }else{
                            rowIsEmpty = false;
                            break;
                        }
                    }
                }
                if(!rowIsEmpty){rowList.add(fr);}
            }
        }else {
            if(formDetailPOJO.getRows()!=null){
                rowList.addAll(formDetailPOJO.getRows());
            }
        }
    }

    private boolean checkIsEmptyField(FormDetailPOJO.FormField field) {
        boolean result = true;
        int type = field.getFieldType();
        String value = field.getFieldValue();
        if(type== MyConfig.CONTROL_TYPE_TEXT
                || type== MyConfig.CONTROL_TYPE_TEXTAREA
                || type== MyConfig.CONTROL_TYPE_SELECT
                || type== MyConfig.CONTROL_TYPE_SELECTWITHSEARCH
                || type== MyConfig.CONTROL_TYPE_CALENDAR
            // TODO, others
                ){
            if(TextUtils.isEmpty(value)){
                result = true;
            }else{
                result = false;
            }
        }else if(type== MyConfig.CONTROL_TYPE_RADIO
                || type== MyConfig.CONTROL_TYPE_CHECKBOX
            // TODO, others
                ){
            if(TextUtils.isEmpty(value)){
                result = true;
            }else{
                if(value.contains("Yes")){
                    result = false;
                }else{
                    result = true;
                }
            }
        }else if(type== MyConfig.CONTROL_TYPE_DATAGRID
            // TODO, others
                ){
            result = false;
        }
        return result;
    }

    @Override
    public FormDetailBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == MyConfig.NORMAL_ROW) {

//            Log.i("MO5Todo","In Normal");
            return new FormDetail_Normal_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_normal_viewholder, parent, false));
        }
        else if (viewType == MyConfig.CHECKBOX_ROW){
//            Log.i("MO5Todo","In check box");
            return new FormDetail_CheckBox_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_checkbox_viewholder, parent, false));
        }
        else if (viewType == MyConfig.RADIO_ROW){
//            Log.i("MO5Todo","In Raido");
            return new FormDetail_Radio_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_radio_viewholder, parent, false));
        }else if (viewType == MyConfig.PUREMARK_ROW){
//            Log.i("MO5Todo","In PUREMARK_ROW");
            return new FormDetail_PureRemark_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_pureremark_viewholder, parent, false));
        }else if (viewType == MyConfig.MORETHAN2_ROW){
//            Log.i("MO5Todo","In MORETHAN2_ROW");
            return new FormDetail_MoreThan2Control_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_morethan2cotrol_viewholder, parent, false));
        }else if (viewType == MyConfig.LAST_BLANK_ROW){
//            Log.i("MO5Todo","In MORETHAN2_ROW");
            return new FormDetail_LastBlankRow_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_lastblank_viewholder, parent, false));
        }else if (viewType == MyConfig.DATAGRID_ROW){
            return new FormDetail_DataGrid_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_datagrid_viewholder, parent, false));
        }else if (viewType == MyConfig.LEAVE_YEARDATES_ROW){
            return new FormDetail_LeaveDates_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_yearleavedates_viewholder, parent, false));
        }else if(viewType == MyConfig.ATTACHMENT_ROW){
            return new FormDetail_Attachment_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_attachment_viewholder, parent, false));
        }else if(viewType == MyConfig.RTF_ROW){
            return new FormDetail_RTF_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_rtf_viewholder, parent, false));
        }
        else {
//            Log.i("MO5Todo","In else");
            return new FormDetail_Normal_ViewHolder(LayoutInflater.from(context).inflate(R.layout.formdetail_normal_viewholder, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(FormDetailAdapter.FormDetailBaseViewHolder holder, int position) {
        // last -1 row, attachment row
        if(position==getItemCount()-2){holder.bind(); return;}

        // last row
        if(position==getItemCount()-1){return;}
//        Log.i("MO5Todo","error position ~~~~~~~~~ "+position);
        holder.bind(rowList.get(position));
    }

    @Override
    public void onViewRecycled(FormDetailAdapter.FormDetailBaseViewHolder holder){
        holder.unBind();
    }

    @Override
    public int getItemCount() {
        return rowList.size()+lastRow+attachmentRow;
    }

    @Override
    public int getItemViewType(int position){
        // last -1 row, attachment row
        if(position==getItemCount()-2){return MyConfig.ATTACHMENT_ROW;}
        // last row
        if(position==getItemCount()-1){return MyConfig.LAST_BLANK_ROW;}

        FormDetailPOJO.FormRows fd = rowList.get(position);
        // counts of the noe-label\remark controls
        int nonLabelRemarkCounts=0;
        FormDetailPOJO.FormField oneField = null;
        for (FormDetailPOJO.FormField field:fd.getFormFields()) {
            int type = field.getFieldType();
            if(isInputControl(type)){
                nonLabelRemarkCounts ++;
                oneField=  field;
            }
        }
        if (nonLabelRemarkCounts==1){
            if(oneField.getFieldType()==(MyConfig.CONTROL_TYPE_RADIO)){
                return MyConfig.RADIO_ROW;
            }else if(oneField.getFieldType()==MyConfig.CONTROL_TYPE_CHECKBOX || oneField.getFieldType()==MyConfig.CONTROL_TYPE_CHECKBOX_2){
                return MyConfig.CHECKBOX_ROW;
            }else if(oneField.getFieldType()==(MyConfig.CONTROL_TYPE_DATAGRID)){
                return MyConfig.DATAGRID_ROW;
            }else if(oneField.getFieldType()==(MyConfig.CONTROL_TYPE_LEAVE_YEARDATES)){
                return MyConfig.LEAVE_YEARDATES_ROW;
            }else if(oneField.getFieldType()==(MyConfig.CONTROL_TYPE_RTFTEXT)){
                return MyConfig.RTF_ROW;
            }
            return MyConfig.NORMAL_ROW;
        }else if(nonLabelRemarkCounts==0){
            return MyConfig.PUREMARK_ROW;
        }else if(nonLabelRemarkCounts>1){
            return MyConfig.MORETHAN2_ROW;
        }else {
            return MyConfig.NORMAL_ROW;
        }
    }
    private boolean isInputControl(int type){
        if(type== MyConfig.CONTROL_TYPE_TEXT
                || type== MyConfig.CONTROL_TYPE_TEXTAREA
                || type== MyConfig.CONTROL_TYPE_SELECT
                || type== MyConfig.CONTROL_TYPE_RADIO
                || type== MyConfig.CONTROL_TYPE_CHECKBOX
                || type== MyConfig.CONTROL_TYPE_CHECKBOX_2
                || type== MyConfig.CONTROL_TYPE_SELECTWITHSEARCH
                || type== MyConfig.CONTROL_TYPE_CALENDAR
                || type== MyConfig.CONTROL_TYPE_DATAGRID
                || type== MyConfig.CONTROL_TYPE_TIME
                || type== MyConfig.CONTROL_TYPE_LEAVE_YEARDATES
                || type== MyConfig.CONTROL_TYPE_RTFTEXT
                || type== MyConfig.CONTROL_TYPE_JOBLIST
            // TODO, others
                ){
            return true;
        }else{
            return false;
        }
    }

    //自定义ViewHolder类
    public class FormDetailBaseViewHolder extends RecyclerView.ViewHolder{
        View view;
        public FormDetailBaseViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
        }
        public void bind(FormDetailPOJO.FormRows fr){

        }
        public void bind(){

        }
        public void unBind(){

        }
        protected int getInputControlPosition(List<FormDetailPOJO.FormField> list) {
            for (int i=0;i<list.size();i++) {
                if (isInputControl(list.get(i).getFieldType())) {
                    return i;
                }
            }
            return -1;
        }

        protected boolean isExists(List<FormDetailPOJO.FormField> list, int position) {
            boolean result;
            if (position<0 || position>=list.size()){
                result = false;
            }else{
                result = true;
            }
            return result;
        }
        protected String convertLabelValue(String labelValue){
            return labelValue.replace("&amp;","&").replace("<BR>","\n").replace("<br>","\n").replace("<BR/>","\n").replace("<br/>","\n").replace("<br />","\n").replace("<BR />","\n");
        }
        protected String convertRemarkValue(String remarkValue){
            return remarkValue.replace("<BR>","\n").replace("<br>","\n").replace("<BR/>","\n").replace("<br/>","\n").replace("<br />","\n").replace("<BR />","\n").replaceAll("<[^>]*>","");
        }
        protected String convertRemarkValueWithoutNewLine(String remarkValue){
            return remarkValue.replace("<BR>","").replace("<br>","").replace("<BR/>","").replace("<br/>","").replace("<br />","").replace("<BR />","").replaceAll("<[^>]*>","");
        }
    }

    public class FormDetail_Normal_ViewHolder extends FormDetailBaseViewHolder{
            public FormDetail_Normal_ViewHolder(final View itemView) {
                super(itemView);
            }
            @Override
            public void bind(FormDetailPOJO.FormRows fr){
                List<FormDetailPOJO.FormField> list = fr.getFormFields();
                int controlPosition = super.getInputControlPosition(list);
                int remarkPosition = controlPosition + 1;
                int labelPosition = controlPosition -1;
                int starPosition =  controlPosition -2;

                String star = "";
                if(super.isExists(list,starPosition)){
                    star = list.get(starPosition).getFieldValue();
                }
                if(super.isExists(list,labelPosition)){
                    FormDetailPOJO.FormField labelField = list.get(labelPosition);
                    TextView labelView = (TextView)super.view.findViewById(R.id.label);
                    labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
                }
                if(super.isExists(list,controlPosition)){
                    FormDetailPOJO.FormField controlField = list.get(controlPosition);
                    TextView controlView = (TextView)super.view.findViewById(R.id.value);
                    controlView.setText(controlField.getFieldValue());
                }
                if(super.isExists(list,remarkPosition)){
                    FormDetailPOJO.FormField remarkField = list.get(remarkPosition);
                    TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
                    remarkView.setText(convertRemarkValue(remarkField.getFieldValue()));
                    super.view.findViewById(R.id.remark).setVisibility(View.VISIBLE);
                }else{
                    super.view.findViewById(R.id.remark).setVisibility(View.GONE);
                }
            }


        @Override
        public void unBind(){
            TextView labelView = (TextView)super.view.findViewById(R.id.label);
            labelView.setText("");
            TextView controlView = (TextView)super.view.findViewById(R.id.value);
            controlView.setText("");
            TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
            remarkView.setText("");
        }
    }
    public class FormDetail_Radio_ViewHolder extends FormDetailBaseViewHolder{

        public FormDetail_Radio_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            int controlPosition = super.getInputControlPosition(list);
            int remarkPosition = controlPosition + 1;
            int labelPosition = controlPosition -1;
            int starPosition =  controlPosition -2;

            String star = "";
            if(super.isExists(list,starPosition)){
                star = list.get(starPosition).getFieldValue();
            }
            if(super.isExists(list,labelPosition)){
                FormDetailPOJO.FormField labelField = list.get(labelPosition);
                TextView labelView = (TextView)super.view.findViewById(R.id.label);
                labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
            }
            if(super.isExists(list,controlPosition)){
                FormDetailPOJO.FormField controlField = list.get(controlPosition);
                // radio
                FlowLayout radioContainer = (FlowLayout)super.view.findViewById(R.id.radiaoContainer);

                for (String radios :
                        controlField.getFieldValue().split("\\|\\|\\|")) {
                    View view = LayoutInflater.from(context).inflate(R.layout.formdetail_radio_view,radioContainer,false);
                    RadioButton radio = (RadioButton)view.findViewById(R.id.radio1);
                    // skip unchecked
                    if( TextUtils.isEmpty(radios) || radios.split(":")[0].equalsIgnoreCase("false")){
                        continue;
                    }
                    if(radios.split(":").length>1){

                        radio.setText(radios.split(":")[1]);
                        radio.setChecked(radios.split(":")[0].equalsIgnoreCase("true")?true:false);
                    }else{
                        if(TextUtils.isEmpty(radios)){ continue;}
                        radio.setText(radios);
                        radio.setChecked(true);
                    }

                   // radio.setEnabled(false);
                    radioContainer.addView(radio);
                }
            }
            if(super.isExists(list,remarkPosition)){
                FormDetailPOJO.FormField remarkField = list.get(remarkPosition);
                TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
                remarkView.setText(convertRemarkValue(remarkField.getFieldValue()));
                super.view.findViewById(R.id.remark).setVisibility(View.VISIBLE);
            }else{
                super.view.findViewById(R.id.remark).setVisibility(View.GONE);
            }


        }
        @Override
        public void unBind(){
            TextView labelView = (TextView)super.view.findViewById(R.id.label);
            labelView.setText("");
            ViewGroup radioContainer = (ViewGroup)super.view.findViewById(R.id.radiaoContainer);
            radioContainer.removeAllViews();
            TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
            remarkView.setText("");
        }
    }

    public class FormDetail_CheckBox_ViewHolder extends FormDetailBaseViewHolder{

        public FormDetail_CheckBox_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            int controlPosition = super.getInputControlPosition(list);
            int remarkPosition = controlPosition + 1;
            int labelPosition = controlPosition -1;
            int starPosition =  controlPosition -2;

            String star = "";
            if(super.isExists(list,starPosition)){
                star = list.get(starPosition).getFieldValue();
            }
            if(super.isExists(list,labelPosition)){
                FormDetailPOJO.FormField labelField = list.get(labelPosition);
                TextView labelView = (TextView)super.view.findViewById(R.id.label);
                labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
            }
            if(super.isExists(list,controlPosition)){
                FormDetailPOJO.FormField controlField = list.get(controlPosition);
                FlowLayout checkBoxContainer = (FlowLayout)super.view.findViewById(R.id.checkBoxContainer);
                for (String checkboxString :
                        controlField.getFieldValue().split("\\|\\|\\|")) {
                    View view = LayoutInflater.from(context).inflate(R.layout.formdetail_checkbox_view,checkBoxContainer,false);
                    CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox1);

                    // skip unchecked
                    if(TextUtils.isEmpty(checkboxString) || checkboxString.split(":")[0].equalsIgnoreCase("false")){
                        continue;
                    }
                    if(checkboxString.split(":").length>1){

                        checkBox.setText(checkboxString.split(":")[1]);
                        checkBox.setChecked(checkboxString.split(":")[0].equalsIgnoreCase("true")?true:false);
                    }else{
                        checkBox.setText("");
                        //checkBox.setChecked(false);
                    }
                    //checkBox.setEnabled(false);
                    checkBoxContainer.addView(checkBox);
                }
            }
            if(super.isExists(list,remarkPosition)){
                FormDetailPOJO.FormField remarkField = list.get(remarkPosition);
                TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
                remarkView.setText(convertRemarkValue(remarkField.getFieldValue()));
                super.view.findViewById(R.id.remark).setVisibility(View.VISIBLE);
            }else{
                super.view.findViewById(R.id.remark).setVisibility(View.GONE);
            }
            /////////////////////////////////////////

        }
        @Override
        public void unBind(){
            TextView labelView = (TextView)super.view.findViewById(R.id.label);
            labelView.setText("");
            ViewGroup checkBoxContainer = (ViewGroup)super.view.findViewById(R.id.checkBoxContainer);
            checkBoxContainer.removeAllViews();
            TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
            remarkView.setText("");
        }
    }


    public class FormDetail_PureRemark_ViewHolder extends FormDetailBaseViewHolder{

        public FormDetail_PureRemark_ViewHolder(final View itemView) {
            super(itemView);
        }

        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            // get label
//            FormDetailPOJO.FormField labelField = list.get()
            // radio
            String remarkValue = "";
            for (FormDetailPOJO.FormField ff:list) {
                remarkValue += ff.getFieldValue();
            }

            TextView remark = (TextView)super.view.findViewById(R.id.remarkvalue);
            remark.setText(convertRemarkValue(remarkValue));
        }
        @Override
        public void unBind(){
            TextView remark = (TextView)super.view.findViewById(R.id.remarkvalue);
            remark.setText("");
        }
    }
    public class FormDetail_MoreThan2Control_ViewHolder extends FormDetailBaseViewHolder{

        public FormDetail_MoreThan2Control_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            int controlPosition = super.getInputControlPosition(list);
            int labelPosition = controlPosition -1;
            int starPosition =  controlPosition -2;

            String star = "";
            if(super.isExists(list,starPosition)){
                star = list.get(starPosition).getFieldValue();
            }
            if(super.isExists(list,labelPosition)){
                FormDetailPOJO.FormField labelField = list.get(labelPosition);
                TextView labelView = (TextView)super.view.findViewById(R.id.label);
                labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
            }
            if(super.isExists(list,controlPosition)){
                String valueString = "";

                for (int i=controlPosition;i<list.size();i++){
                    FormDetailPOJO.FormField ff = list.get(i);
                    if(ff.getFieldType()==MyConfig.CONTROL_TYPE_RADIO || ff.getFieldType()==MyConfig.CONTROL_TYPE_CHECKBOX|| ff.getFieldType()==MyConfig.CONTROL_TYPE_CHECKBOX_2){
                        String radioValue = ff.getFieldValue();
                        for (String temp :
                                radioValue.split("\\|\\|\\|")) {
                            if(temp.split(":")[0].equalsIgnoreCase("True")){
                                ff.setFieldValue(temp.split(":")[1].trim() + " ");
                                break;
                            }
                        }
                    }else {
                        if (TextUtils.isEmpty(ff.getFieldValue())) {
                            ff.setFieldValue("  ");
                        } else {
                            ff.setFieldValue(convertRemarkValueWithoutNewLine(ff.getFieldValue().trim()) + " ");
                        }
                    }
                    valueString += ff.getFieldValue();
                }
                SpannableString spanString = new SpannableString(valueString);
                int lengthFlag = 0;
                for (int i=controlPosition;i<list.size();i++){
                    FormDetailPOJO.FormField ff = list.get(i);
                    if(isInputControl(ff.getFieldType())){

                    }else{
                        spanString = SpannableUtils.setTextSize(spanString,lengthFlag,lengthFlag+ff.getFieldValue().length(),12);
                        spanString = SpannableUtils.setTextForeground(spanString,lengthFlag,lengthFlag+ff.getFieldValue().length(), Color.parseColor("#0d5b9f"));
                    }
                    lengthFlag += ff.getFieldValue().length();
                }
                TextView controlView = (TextView)super.view.findViewById(R.id.value);
                controlView.setText(spanString);
            }

        }
        @Override
        public void unBind(){
            TextView labelView = (TextView)super.view.findViewById(R.id.label);
            labelView.setText("");
            TextView controlView = (TextView)super.view.findViewById(R.id.value);
            controlView.setText(new SpannableString(""));
        }
    }
    public class FormDetail_LastBlankRow_ViewHolder extends FormDetailBaseViewHolder{
        public FormDetail_LastBlankRow_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){

        }
        @Override
        public void unBind(){

        }
    }
    public class FormDetail_DataGrid_ViewHolder extends FormDetailBaseViewHolder{
        public FormDetail_DataGrid_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            int controlPosition = super.getInputControlPosition(list);
//            int remarkPosition = controlPosition + 1;
            int labelPosition = controlPosition -1;
            int starPosition =  controlPosition -2;

            String star = "";
            if(super.isExists(list,starPosition)){
                star = list.get(starPosition).getFieldValue();
            }
            if(super.isExists(list,labelPosition)){
                FormDetailPOJO.FormField labelField = list.get(labelPosition);
                TextView labelView = (TextView)super.view.findViewById(R.id.label);
                labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
            }

            if(super.isExists(list,controlPosition)) {
                TextView text = (TextView) view.findViewById(R.id.value);
                text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

                int formTemplateDetailId = list.get(controlPosition).getFieldId();
//                for (FormDetailPOJO.FormField ff :
//                        fr.getFormFields()) {
//                    if (ff.getFieldType() == MyConfig.CONTROL_TYPE_DATAGRID) {
//                        formTemplateDetailId = ff.getFieldId();
//                        break;
//                    }
//                }

                final int finalFormTemplateDetailId = formTemplateDetailId;
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DataGridActivity.class);
                        intent.putExtra("formId", formDetailPOJO.getFormId());
                        intent.putExtra("formTemplateDetailId", finalFormTemplateDetailId);
                        context.startActivity(intent);
                    }
                });
            }
        }
        @Override
        public void unBind(){

        }
    }
    public class FormDetail_LeaveDates_ViewHolder extends FormDetailBaseViewHolder{
        public FormDetail_LeaveDates_ViewHolder(final View itemView) {
            super(itemView);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            String datesStr = "";
            for (FormDetailPOJO.FormField ff :
                    fr.getFormFields()) {
                if (ff.getFieldType()==MyConfig.CONTROL_TYPE_LEAVE_YEARDATES){
                    datesStr = ff.getFieldValue();
                    break;
                }
            }
            String[] dates = datesStr.split("\\|\\|\\|");
            ViewGroup datesContainer = (ViewGroup)view.findViewById(R.id.leaveDatesContainer);
            for (int i = 0; i < dates.length; i=i+2) {
                ViewGroup dateLine = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.formdetail_yearleavedates_view,datesContainer,false);
                ((TextView)dateLine.findViewById(R.id.date1)).setText(dates[i]);
                if(i+1<dates.length) {
                    ((TextView) dateLine.findViewById(R.id.date2)).setText(dates[i+1]);
                }
                datesContainer.addView(dateLine);
            }
        }
        @Override
        public void unBind(){
            ViewGroup datesContainer = (ViewGroup)view.findViewById(R.id.leaveDatesContainer);
            datesContainer.removeAllViews();
        }
    }
    public class FormDetail_Attachment_ViewHolder extends FormDetailBaseViewHolder{
        public FormDetail_Attachment_ViewHolder(final View itemView) {
            super(itemView);
        }

        public void bind(){
            ViewGroup container = (ViewGroup)view.findViewById(R.id.container);
            if ( formDetailPOJO.getAttachment().size()==0){
                unBind();
                ViewGroup dataLine = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.formdetail_attachment_view,container,false);
                MaterialIconView iconView  = (MaterialIconView)dataLine.findViewById(R.id.icon);
                iconView.setVisibility(View.GONE);
                TextView textView  = (TextView)dataLine.findViewById(R.id.filename);
                textView.setText( rowList.size()==0?"":"No Attachment.");
                container.addView(dataLine);
                return;
            }

            container.setVisibility(View.VISIBLE);
            for (FormDetailPOJO.AttachmentFile file:
                    formDetailPOJO.getAttachment()
                 ) {
                final int fileId = file.getId();
                String fileExt = file.getExt();
                final String fileName = file.getFileName()+file.getExt();
                BigDecimal b = new BigDecimal(file.getSize());
                final String fileSize = b.divide(new BigDecimal(1024)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+""; ;

                MaterialDrawableBuilder.IconValue icon = MaterialDrawableBuilder.IconValue.FILE;
                if (fileExt.toLowerCase().endsWith(".doc") || fileExt.toLowerCase().endsWith(".docx")){
                    icon = MaterialDrawableBuilder.IconValue.FILE_WORD;
                }else if (fileExt.toLowerCase().endsWith(".xls") || fileExt.toLowerCase().endsWith(".xlsx")|| fileExt.toLowerCase().endsWith(".xlsm")){
                    icon = MaterialDrawableBuilder.IconValue.FILE_EXCEL;
                }else if (fileExt.toLowerCase().endsWith(".pdf")){
                    icon = MaterialDrawableBuilder.IconValue.FILE_PDF;
                }else if (fileExt.toLowerCase().endsWith(".jpg") || fileExt.toLowerCase().endsWith(".png")||fileExt.toLowerCase().endsWith(".jpeg") ){
                    icon = MaterialDrawableBuilder.IconValue.IMAGE;
                }else{
                    icon = MaterialDrawableBuilder.IconValue.FILE;
                }
                ViewGroup dataLine = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.formdetail_attachment_view,container,false);
                MaterialIconView iconView  = (MaterialIconView)dataLine.findViewById(R.id.icon);
                iconView.setIcon(icon);
                TextView textView  = (TextView)dataLine.findViewById(R.id.filename);
                textView.setText(fileName);

                dataLine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     downLoadFile(fileId,fileName, fileSize);
                    }
                });
                container.addView(dataLine);
            }
        }

        private boolean downLoadFile(final int fileId, final String fileName, final String fileSize) {
            MyHttpService.CommonInterface apiService =  MyHttpService.instanceRetrofit().create( MyHttpService.CommonInterface.class);
            try {
                Call<BasePOJO> call = apiService.checkFileIfExists(((MyBaseActivity) context).myConfig.getUserLoginPOJO().getUserId(),fileId, Base64.encodeToString(fileName.trim().getBytes("utf-8"),Base64.URL_SAFE).trim());
                //Response<BasePOJO> response = call.execute();
                call.enqueue(new Callback<BasePOJO>() {
                    @Override
                    public void onResponse(Call<BasePOJO> call, Response<BasePOJO> response) {
                        if(response.isSuccessful()){
                            BasePOJO pojo = response.body();
                            if(pojo.getErrorMsg().getCode()==0){
                                //创建builder对象
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                //设置标题
                                builder.setTitle("Confrim download");
                                //设置对话框信息
                                //builder.setMessage(pojo.getNewVersionName() +" are ready.Size is "+pojo.getApkSize()+ "M."+""+"Update now?");
                                View viewAlert = LayoutInflater.from(context).inflate(R.layout.download_file_alert, null);
                                ((TextView) viewAlert.findViewById(R.id.filename)).setText(fileName);
                                ((TextView) viewAlert.findViewById(R.id.size)).setText(fileSize + "M");
                                builder.setView(viewAlert);
                                //设置确定按钮
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        final DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                        String dowloadPath = String.format(MyConfig.BASE_URL + MyHttpService.CommonInterface.getFileURL, ((MyBaseActivity) context).myConfig.getUserLoginPOJO().getUserId(), fileId, 2);
                                        Uri uri = Uri.parse(dowloadPath);
                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        request.setDestinationInExternalPublicDir("download", fileName);
                                        request.setTitle(fileName);
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        // 设置为可被媒体扫描器找到
                                        request.allowScanningByMediaScanner();
                                        // 设置为可见和可管理
                                        request.setVisibleInDownloadsUi(true);
                                        long refernece = dManager.enqueue(request);
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        // 点击"取消"按钮之后退出程序
                                        dialog.dismiss();
                                    }
                                }).create().show();
                            }else{
                                Snackbar.make(((FormDetailPagerActivity)context).findViewById(R.id.main_content),"Attachment doesn't exist.",Snackbar.LENGTH_LONG).show();
                            }
                        }else{
                            Toast toast = Toast.makeText(context,R.string.login_toast_mo5_service_failed,Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<BasePOJO> call, Throwable t) {
                        Snackbar.make(((FormDetailPagerActivity)context).findViewById(R.id.main_content),"Check file failed.",Snackbar.LENGTH_LONG).show();
                    }
                });
                return false;
            } catch (Exception e) {
                Snackbar.make(((FormDetailPagerActivity)context).findViewById(R.id.main_content),"Check file failed.",Snackbar.LENGTH_LONG).show();
                return false;
            }
        }

        @Override
        public void unBind(){
            ViewGroup container = (ViewGroup)view.findViewById(R.id.container);
            container.removeAllViews();
        }
    }

    public class FormDetail_RTF_ViewHolder extends FormDetailBaseViewHolder {
        public FormDetail_RTF_ViewHolder(View view) {
            super(view);
        }
        @Override
        public void bind(FormDetailPOJO.FormRows fr){
            List<FormDetailPOJO.FormField> list = fr.getFormFields();
            int controlPosition = super.getInputControlPosition(list);
            int remarkPosition = controlPosition + 1;
            int labelPosition = controlPosition -1;
            int starPosition =  controlPosition -2;

            String star = "";
            if(super.isExists(list,starPosition)){
                star = list.get(starPosition).getFieldValue();
            }
            if(super.isExists(list,labelPosition)){
                FormDetailPOJO.FormField labelField = list.get(labelPosition);
                TextView labelView = (TextView)super.view.findViewById(R.id.label);
                labelView.setText(convertLabelValue(star+labelField.getFieldValue()));
            }
            if(super.isExists(list,controlPosition)){
                FormDetailPOJO.FormField controlField = list.get(controlPosition);
                String RTFStr = controlField.getFieldValue();
                ViewGroup container = (ViewGroup)view.findViewById(R.id.container);
                RTFStr = RTFStr.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&");
                View childView;
                if((RTFStr.contains("<") && RTFStr.contains(">")) || (RTFStr.contains("<img")) || (RTFStr.contains("&amp;") || RTFStr.contains("&quot;") ||RTFStr.contains("&apos;"))){
                    View view1 = LayoutInflater.from(context).inflate(R.layout.formdetail_rtf_view_html, null);
                    WebView webView = (WebView) view1.findViewById(R.id.webView);
                    WebSettings webSettings =   webView.getSettings();
                    webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setDefaultTextEncodingName("UTF-8");
                    //WebView加载web资源
                    //webView.loadUrl("http://baidu.com");
                    //String customHtml="<html>dd<br>dd<br><font color=\"#0090FF\">ddd<br><font color=\"#0000C0\">ddd<br><span><img src=\"http://144.210.190.84/MyOffice_mbwAP_siT//MyAsset/JobBag/Popup/AJB_File_Download_Popup.aspx?filePath=MTQ0LjIxMC4xOTAuODQ=/GIS/{ECE065AC-8096-44A6-AC88-FCBC18757BD8}.png&amp;amp;isFolder=false\"></span><br></font></font></html>";
                    //webView.loadData(RTFStr, "text/html", "UTF-8");
                    webView.loadDataWithBaseURL("", RTFStr, "text/html", "UTF-8","");

                    //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                    webView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            // TODO Auto-generated method stub
                            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    final String finalRTFStr = RTFStr;
                    webView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Log.i(MyConfig.TAG,"web long click......");
                            Intent intent=new Intent(context,RTFContentActivity.class);
                            //intent.putExtra("RTFStr", finalRTFStr);
                            MyConfig.RTF_TEMP = finalRTFStr;
                            context.startActivity(intent);
                            return true;
                        }
                    });
                    webView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i(MyConfig.TAG,"web click......");
                            Intent intent=new Intent(context,RTFContentActivity.class);
                            intent.putExtra("RTFStr", finalRTFStr);
                            context.startActivity(intent);
                        }
                    });
                    childView = webView;
                }else{
                    View view1 = LayoutInflater.from(context).inflate(R.layout.formdetail_rtf_view_nohtml, null);
                    TextView textView = (TextView) view1.findViewById(R.id.value);
                    textView.setText(RTFStr);
                    childView = textView;
                    //view1
                }
                container.addView(childView);
            }
            if(super.isExists(list,remarkPosition)){
                FormDetailPOJO.FormField remarkField = list.get(remarkPosition);
                TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
                remarkView.setText(convertRemarkValue(remarkField.getFieldValue()));
                super.view.findViewById(R.id.remark).setVisibility(View.VISIBLE);
            }else{
                super.view.findViewById(R.id.remark).setVisibility(View.GONE);
            }
        }
        @Override
        public void unBind(){
            ViewGroup container = (ViewGroup)view.findViewById(R.id.container);
            container.removeAllViews();
            TextView labelView = (TextView)super.view.findViewById(R.id.label);
            labelView.setText("");
            TextView remarkView = (TextView)super.view.findViewById(R.id.remarkvalue);
            remarkView.setText("");
        }
    }
}
