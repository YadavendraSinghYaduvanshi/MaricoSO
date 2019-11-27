package com.cpm.Marico_so.dailyEntry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpm.Marico_so.getterSetter.PromoterTargetMM;
import com.cpm.Marico_so.R;
import com.cpm.Marico_so.database.MaricoSODatabase;

import java.util.ArrayList;

public class MonthlyTargetActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MonthlyTargetAdapter adapter;
    MaricoSODatabase db;
    ArrayList<PromoterTargetMM> promoterTargets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_target);

        db = new MaricoSODatabase(getApplicationContext());
        db.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Monthly Target");

        recyclerView = (RecyclerView)findViewById(R.id.sales_view);

        db.open();
        promoterTargets = db.getMonthlyTargetData();

        if(promoterTargets.size() > 0){
            adapter = new MonthlyTargetAdapter(this,promoterTargets);
            recyclerView.setHasFixedSize(true);
            // set a GridLayoutManager with default vertical orientation and 2 number of columns
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        }

    }

    private class MonthlyTargetAdapter extends RecyclerView.Adapter<MonthlyTargetAdapter.ViewHolder>{
       Context context;
        ArrayList<PromoterTargetMM> promoterTargets;

        public MonthlyTargetAdapter(Context context, ArrayList<PromoterTargetMM> promoterTargets) {
            this.context = context;
            this.promoterTargets = promoterTargets;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_target_report_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
            viewHolder.store_name.setText(promoterTargets.get(pos).getStore_name() + "(ID - " + promoterTargets.get(pos).getStoreId() + ")");
//            viewHolder.mtdVolume.setText(promoterTargets.get(pos).getMTDVolumeTarget()+"");
//            viewHolder.mtdValue.setText(promoterTargets.get(pos).getMTDValueTarget()+"");
//            viewHolder.dailyVolume.setText(promoterTargets.get(pos).getDailyVolumeTarget()+"");
//           viewHolder.dailyValue.setText(promoterTargets.get(pos).getDailyValueTarget()+"");

            viewHolder.target_val.setText(promoterTargets.get(pos).getMTDValueTarget()+"");
            viewHolder.target_vol.setText(promoterTargets.get(pos).getMTDVolumeTarget()+"");

            viewHolder.achieve_val.setText(promoterTargets.get(pos).getMTDValueAchievement ()+"");
            viewHolder.achieve_vol.setText(promoterTargets.get(pos).getMTDVolumeAchievement()+"");

            viewHolder.per_val.setText(promoterTargets.get(pos).getMTDValPer()+"");
            viewHolder.per_vol.setText(promoterTargets.get(pos).getMTDVolPer()+"");
        }

        @Override
        public int getItemCount() {
            return promoterTargets.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView target_val,target_vol,achieve_val,achieve_vol,per_val,per_vol,store_name;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                target_val = (TextView)itemView.findViewById(R.id.val_target);
                target_vol = (TextView)itemView.findViewById(R.id.vol_target);
                achieve_val = (TextView)itemView.findViewById(R.id.val_achieve);
                achieve_vol = (TextView)itemView.findViewById(R.id.vol_achieve);
                per_val = (TextView)itemView.findViewById(R.id.val_per);
                per_vol = (TextView)itemView.findViewById(R.id.vol_per);

//                mtdVolume = (TextView)itemView.findViewById(R.id.mtd_volume);
//                mtdValue = (TextView)itemView.findViewById(R.id.mtd_value);
//                dailyVolume = (TextView)itemView.findViewById(R.id.daily_volume);
//                dailyValue = (TextView)itemView.findViewById(R.id.daily_value);
                store_name = (TextView)itemView.findViewById(R.id.store_name);
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
