package com.cpm.Marico_so.dailyEntry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpm.Marico_so.database.MaricoSODatabase;
import com.cpm.Marico_so.utilities.CountDownTimer;
import com.cpm.Marico_so.R;
import com.cpm.Marico_so.getterSetter.AuditDataGetterSetter;
import com.cpm.Marico_so.getterSetter.AuditQuestionMaster;
import com.cpm.Marico_so.getterSetter.JourneyPlan;
import com.cpm.Marico_so.utilities.AlertandMessages;
import com.cpm.Marico_so.utilities.CommonFunctions;
import com.cpm.Marico_so.utilities.CommonString;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    List<Integer> checkHeaderArray = new ArrayList<>();
    List<Integer> checkGroupArray = new ArrayList<>();
    boolean checkflag = true;
    private SharedPreferences preferences;
    String pathforcheck = "", _path = "", str, img_str = "";
    String visit_date, username, intime, date;
    MaricoSODatabase db;
    TextView txtTimerCounter;
    ArrayList<AuditDataGetterSetter> question_list, childListData;
    HashMap<AuditDataGetterSetter, ArrayList<AuditDataGetterSetter>> hashMapAnsListChildData;
    int child_position = -1,time_count;
    String error_msg = "",timeCounter;
    Toolbar toolbar;
    Context context;
    CountDownTimer myCountDownTimer;
    JourneyPlan jcpGetset;
    FloatingActionButton fab;
    ExpandableListView expandableListView;
    ExpandableListAdapter adapter;
    ArrayList<AuditQuestionMaster> listDataHeader;
    HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild;
    // Calculate left seconds.
    long seconds;//convert to seconds
    long minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Intent data
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset  = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
            time_count = jcpGetset.getTimePeriod();
        }

        decalartionUI();
        //Header
        listDataHeader = db.getAuditQuestionCategoryData(jcpGetset);
        question_list = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (int i = 0; i < listDataHeader.size(); i++) {
            question_list = db.getAfterSaveAuditQuestionAnswerData(jcpGetset.getStoreId().toString());
            if (question_list.size() == 0) {
                question_list = db.getAuditCategoryWise(jcpGetset, listDataHeader.get(i).getQuestionCategoryId());
                if (question_list.size() > 0) {
                    String select = getString(R.string.title_activity_select_dropdown);
                    // Adding child data
                    for (int j = 0; j < question_list.size(); j++) {
                        childListData = db.getAuditAnswerData(question_list.get(j), select);
                        ArrayList<AuditDataGetterSetter> answerList = new ArrayList<>();
                        for (int k = 0; k < childListData.size(); k++) {
                            answerList.add(childListData.get(k));
                        }
                        hashMapAnsListChildData.put(question_list.get(j), answerList); // Header, Child data
                    }
                }
            }else {
                if (question_list.size() > 0) {
                    String select = getString(R.string.title_activity_select_dropdown);
                    // Adding child data
                    for (int j = 0; j < question_list.size(); j++) {
                        childListData = db.getAuditAnswerData(question_list.get(j), select);
                        ArrayList<AuditDataGetterSetter> answerList = new ArrayList<>();
                        for (int k = 0; k < childListData.size(); k++) {
                            answerList.add(childListData.get(k));
                        }
                        hashMapAnsListChildData.put(question_list.get(j), answerList); // Header, Child data
                    }
                }
            }
            listDataChild.put(listDataHeader.get(i), question_list);
        }

        adapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableListView.clearFocus();
                // questionAdapter.notifyDataSetChanged();
                if (validateData(listDataHeader, listDataChild)) {
                    myCountDownTimer.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.title_activity_Want_save)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (db.saveAuditQuestionAnswerData(listDataHeader, listDataChild, jcpGetset) >0 ) {
                                        if(db.saveAuditCountDownTimerData(jcpGetset,date,time_count,timeCounter) > 0) {
                                            finish();
                                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                        }
                                    } else {
                                        AlertandMessages.showToastMsg(context, "Error in data saving");
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    myCountDownTimer = new CountDownTimer(minutes * 60 * 1000 + seconds * 1000, 1000);
                                    // Set count down timer source activity.
                                    myCountDownTimer.setSourceActivity(QuizActivity.this);
                                    myCountDownTimer.start();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Snackbar.make(expandableListView, error_msg, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               // expandableListView.invalidate();

                int lastItem = firstVisibleItem + visibleItemCount;

                if (firstVisibleItem == 0) {
                    fab.show();//.setVisibility(View.VISIBLE);
                } else if (lastItem == totalItemCount) {
                    fab.hide();//setVisibility(View.INVISIBLE);
                } else {
                    fab.show();//setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                //expandableListView.invalidateViews();

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }


    private void decalartionUI() {
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        toolbar.setTitle("Quiz - " + date);
        hashMapAnsListChildData = new HashMap<>();
        childListData = new ArrayList<>();
        str = CommonString.FILE_PATH;
        expandableListView = (ExpandableListView) findViewById(R.id.list);
        txtTimerCounter = (TextView)findViewById(R.id.txtTimer);

        db = new MaricoSODatabase(context);
        db.open();

        myCountDownTimer = new CountDownTimer(time_count * 60 * 1000, 1000);
        // Set count down timer source activity.
        myCountDownTimer.setSourceActivity(this);
        myCountDownTimer.start();

//        auditTimerData = db.getAuditCountDownTimeData(jcpGetset.getStoreId());
//
//        if(auditTimerData.getStart_time().equalsIgnoreCase("")) {
//            // Create a count down timer which will count 60 seconds and invoke the timer object onTick() method every second.
//            myCountDownTimer = new CountDownTimer(time_count * 60 * 1000, 1000);
//            // Set count down timer source activity.
//            myCountDownTimer.setSourceActivity(this);
//            myCountDownTimer.start();
//        } else{
//            String[] timeVal = auditTimerData.getEnd_time().split(":");
//            hour = Integer.parseInt(timeVal[0]);
//            min  = Integer.parseInt(timeVal[1]);
//            sec  = Integer.parseInt(timeVal[2]);
//            if(min !=0 && sec!=0){
//                // Create a count down timer which will count 60 seconds and invoke the timer object onTick() method every second.
//                myCountDownTimer = new CountDownTimer(min * sec * 1000, 1000);
//            } else if(sec !=0){
//                // Create a count down timer which will count 60 seconds and invoke the timer object onTick() method every second.
//                myCountDownTimer = new CountDownTimer( sec * 1000, 1000);
//            }
//            // Set count down timer source activity.
//            myCountDownTimer.setSourceActivity(this);
//            myCountDownTimer.start();
//        }
    }

    /* This method will be invoked when CountDownTimer finish. */
    public void onCountDownTimerFinishEvent()
    {
        if (db.saveAuditQuestionAnswerData(listDataHeader, listDataChild, jcpGetset) > 0) {
            if(db.saveAuditCountDownTimerData(jcpGetset,date,time_count,timeCounter) > 0) {
                AlertandMessages.showAlert(this,"Time over! filled quiz data will be saved.",true);
            }
        } else {
            AlertandMessages.showToastMsg(context, "Error in data saving");
        }
    }

    /* This method will be invoked when CountDownTimer tick event happened.*/
    public void onCountDownTimerTickEvent(long millisUntilFinished)
    {
        // Calculate left seconds.
        seconds = millisUntilFinished/1000;//convert to seconds
        minutes = seconds / 60;//convert to minutes
        long hours = minutes / 60;//convert to hours

        if(minutes > 0)//if we have minutes, then there might be some remainder seconds
            seconds = seconds % 60;//seconds can be between 0-60, so we use the % operator to get the remainder
        if(hours > 0)
            minutes = minutes % 60;//similar to seconds

        timeCounter = formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                formatNumber(seconds);

        if(minutes == 0 && seconds == 0)
        {
            // Show left seconds in send button.
            txtTimerCounter.setText( "Timer Over !");
            Log.d("Timing-> ",timeCounter);
        }else{
            // Show left seconds in send button.
            txtTimerCounter.setText("Time Left:" + timeCounter);
            Log.d("Timing-> ",timeCounter);
        }


    }

    private String formatNumber(long value){
        if(value < 10)
            return "0" + value;
        return value + "";
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Parinaam");
//            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
//                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            finish();
//                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private ArrayList<AuditQuestionMaster> listDataHeader; // header titles
        private HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild;

        public ExpandableListAdapter(Context context, ArrayList<AuditQuestionMaster> listDataHeader, HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild) {
            this._context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listDataChild;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("NewApi")
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            AuditDataGetterSetter checkList = (AuditDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.audit_question_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.data = checkList;
            holder.txt_question.setText(holder.data.getQUESTION());

            final ArrayList<AuditDataGetterSetter> ans_list = hashMapAnsListChildData.get(holder.data);

            if (holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Single_Choice") || holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Multi_Choice")) {
                holder.sp_auditAnswer.setVisibility(View.VISIBLE);
                holder.edt_answer.setVisibility(View.GONE);
            } else {
                holder.edt_answer.setVisibility(View.VISIBLE);
                holder.sp_auditAnswer.setVisibility(View.GONE);
            }

            if (ans_list.size() > 0) {
                holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter(context, R.layout.custom_spinner_item, ans_list));
            }

            final ArrayList<AuditDataGetterSetter> finalAns_list = ans_list;
            final ViewHolder finalHolder = holder;

            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    AuditDataGetterSetter ans = finalAns_list.get(position);
                    finalHolder.data.setANSWER_ID(ans.getANSWER_ID());
                    finalHolder.data.setANSWER(ans.getANSWER());
                    finalHolder.data.setCAMERA_ALLOW(ans.getCAMERA_ALLOW());

                    if (finalHolder.data.getCAMERA_ALLOW().equals("1")) {
                        finalHolder.parent_cam_layout.setVisibility(View.VISIBLE);
                    } else {
                        finalHolder.parent_cam_layout.setVisibility(View.GONE);
                        if (finalHolder.data.getCAM_IMAGE() != null && finalHolder.data.getCAM_IMAGE().equals("")) {
                            if (new File(str + finalHolder.data.getCAM_IMAGE()).exists()) {
                                new File(str + finalHolder.data.getCAM_IMAGE()).delete();
                            }
                        }
                        finalHolder.data.setCAM_IMAGE("");
                        finalHolder.img_cam.setBackgroundResource(R.mipmap.camera_orange);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.img_cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathforcheck = jcpGetset.getStoreId().toString() + "_AuditAnsPic-" + date.replace("/", "") + CommonFunctions.getCurrentTimeHHMMSS().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + pathforcheck;
                    CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                    child_position = childPosition;
                    //startCameraActivity(0);
                }
            });

            final ViewHolder finalHolder1 = holder;
            final ViewHolder finalHolder2 = holder;
            holder.edt_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        finalHolder1.data.setANSWER_ID("0");
                        finalHolder1.data.setANSWER(((EditText) v).getText().toString());
                     //   ans_list.get(0).setANSWER(((EditText) v).getText().toString());
                    }else{
                        if(finalHolder2.data.getANSWER_TYPE().equalsIgnoreCase("Number")){
                            finalHolder2.edt_answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                            finalHolder2.edt_answer.setFilters(new InputFilter[]{
                                    new InputFilter.LengthFilter(5)
                            });
                        }else{
                            finalHolder2.edt_answer.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }
                }
            });

            if (ans_list.size() > 0) {
                if ((holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Single_Choice") || holder.data.getANSWER_TYPE().equalsIgnoreCase("List_Multi_Choice")))
                {
                    for (int i = 0; i < ans_list.size(); i++) {
                        if (ans_list.get(i).getANSWER_ID().equals(holder.data.getANSWER_ID())) {
                            holder.sp_auditAnswer.setSelection(i);
                            break;
                        }
                    }
                }else{
                    holder.edt_answer.setText(holder.data.getANSWER());
                }
            }else{
                holder.edt_answer.setText(holder.data.getANSWER());
            }


            if (!img_str.equals("")) {
                if (child_position == childPosition) {
                    holder.data.setCAM_IMAGE(img_str);
                    img_str = "";
                    child_position = -1;
                }
            }

            if (holder.data.getCAM_IMAGE().equals("")) {
                holder.img_cam.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                holder.img_cam.setBackgroundResource(R.mipmap.camera_green);
            }

            if (!checkflag) {
                if (checkGroupArray.contains(groupPosition) && checkHeaderArray.contains(childPosition)) {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_header_expandable_quiz, parent, false);
            }
            final AuditQuestionMaster headerTitle = (AuditQuestionMaster) getGroup(groupPosition);

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_header);
            CardView cardView = (CardView) convertView.findViewById(R.id.cardview_exists);
            lblListHeader.setText(headerTitle.getQuestionCategory());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableListView.isGroupExpanded(groupPosition)) {
                        expandableListView.collapseGroup(groupPosition);
                    } else {
                        expandableListView.expandGroup(groupPosition);
                    }
                }
            });

            if (!checkflag) {
                if (checkGroupArray.contains(groupPosition)) {
                    cardView.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    cardView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txt_question;
            public final Spinner sp_auditAnswer;
            public final EditText edt_answer;
            public final ImageView img_cam;
            public final RelativeLayout parent_cam_layout;
            CardView card_view;
            AuditDataGetterSetter data;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                txt_question = (TextView) view.findViewById(R.id.txt_question);
                sp_auditAnswer = (Spinner) view.findViewById(R.id.sp_auditAnswer);
                card_view = (CardView) view.findViewById(R.id.card_view);
                edt_answer = (EditText) view.findViewById(R.id.edt_answer);
                img_cam = (ImageView) view.findViewById(R.id.img_cam);
                parent_cam_layout = (RelativeLayout) view.findViewById(R.id.parent_cam_layout);
            }
        }

    }


    public class AnswerSpinnerAdapter extends ArrayAdapter<AuditDataGetterSetter> {
        List<AuditDataGetterSetter> list;
        Context context;
        int resourceId;

        public AnswerSpinnerAdapter(Context context, int resourceId, ArrayList<AuditDataGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            AuditDataGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_ans);
            txt_spinner.setText(list.get(position).getANSWER());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            AuditDataGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.tv_ans);
            txt_spinner.setText(cm.getANSWER());

            return view;
        }
    }

    boolean validateData(ArrayList<AuditQuestionMaster> listDataHeader, HashMap<AuditQuestionMaster, ArrayList<AuditDataGetterSetter>> listDataChild) {
        //boolean flag = true;
        checkHeaderArray.clear();
        checkGroupArray.clear();
        loop:
        for (int j = 0; j < listDataHeader.size(); j++) {
            ArrayList<AuditDataGetterSetter> data = listDataChild.get(listDataHeader.get(j));
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getANSWER_TYPE().equalsIgnoreCase("Text") || data.get(i).getANSWER_TYPE().equalsIgnoreCase("Number")) {
                    if (data.get(i).getANSWER() == null || data.get(i).getANSWER().equalsIgnoreCase("")) {
                        error_msg = getString(R.string.pls_answer_all_qns);
                        checkflag = false;
                    } else {
                        checkflag = true;
                    }
                } else {
                    if (data.get(i).getANSWER_ID() == null || data.get(i).getANSWER_ID().equalsIgnoreCase("0")) {
                        error_msg = getString(R.string.pls_answer_all_qns);
                        checkflag = false;
                    } else if (data.get(i).getCAMERA_ALLOW().equals("1") && data.get(i).getCAM_IMAGE().equals("")) {
                        error_msg = getString(R.string.click_image);
                        checkflag = false;
                    } else {
                        checkflag = true;
                    }
                }
                if (checkflag == false) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                        checkGroupArray.add(j);
                    }
                    break loop;
                }
            }
        }
        adapter.notifyDataSetChanged();
        return checkflag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (pathforcheck != null && !pathforcheck.equals("")) {
                    if (new File(str + pathforcheck).exists()) {
                        //if (requestCode == 0) {
                        img_str = pathforcheck;
                        // }
                         /*else {
                            img_str2 = pathforcheck;
                        }*/
                        pathforcheck = "";
                        adapter.notifyDataSetChanged();
                    }
                }

        }
    }
}
