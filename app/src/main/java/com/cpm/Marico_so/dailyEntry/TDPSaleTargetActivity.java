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

import com.cpm.Marico_so.database.MaricoSODatabase;
import com.cpm.Marico_so.getterSetter.PromoterTDPSaleTarget;
import com.cpm.Marico_so.R;

import java.util.ArrayList;

public class TDPSaleTargetActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TDPSaleTargetAdapter adapter;
    MaricoSODatabase db;
    ArrayList<PromoterTDPSaleTarget> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdpsale_target);


        db = new MaricoSODatabase(getApplicationContext());
        db.open();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("TDP Sale Target");

        recyclerView = (RecyclerView)findViewById(R.id.sales_view);

        db.open();
        list = db.getTDPSaleTargetData();

        if(list.size() > 0){
            adapter = new TDPSaleTargetAdapter(this,list);
            recyclerView.setHasFixedSize(true);
            // set a GridLayoutManager with default vertical orientation and 2 number of columns
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        }

    }

    private class TDPSaleTargetAdapter extends RecyclerView.Adapter<TDPSaleTargetAdapter.ViewHolder>{

        private Context context;
        ArrayList<PromoterTDPSaleTarget> list;

        public TDPSaleTargetAdapter(Context context, ArrayList<PromoterTDPSaleTarget> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public TDPSaleTargetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tdp_sale_target_report_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TDPSaleTargetAdapter.ViewHolder viewHolder, int pos) {
            viewHolder.store_name.setText(list.get(pos).getStore_name() + "(ID - " + list.get(pos).getStoreId() + ")");
            viewHolder.timePeroid.setText(list.get(pos).getTimePeriod());
            viewHolder.target_val.setText(list.get(pos).getTDPValueTarget()+"");
            viewHolder.target_vol.setText(list.get(pos).getTDPVolumeTarget()+"");

            viewHolder.achieve_val.setText(list.get(pos).getTDPValueAchievement()+"");
            viewHolder.achieve_vol.setText(list.get(pos).getTDPVolumeAchievement()+"");

            viewHolder.per_val.setText(list.get(pos).getTDPValPer()+"");
            viewHolder.per_vol.setText(list.get(pos).getTDPVolPer()+"");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView timePeroid,store_name,target_val,target_vol,achieve_val,achieve_vol,per_val,per_vol;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                timePeroid = (TextView)itemView.findViewById(R.id.time_period);
                store_name = (TextView)itemView.findViewById(R.id.store_name);
                target_val = (TextView)itemView.findViewById(R.id.val_target);
                target_vol = (TextView)itemView.findViewById(R.id.vol_target);
                achieve_val = (TextView)itemView.findViewById(R.id.val_achieve);
                achieve_vol = (TextView)itemView.findViewById(R.id.vol_achieve);
                per_val = (TextView)itemView.findViewById(R.id.val_per);
                per_vol = (TextView)itemView.findViewById(R.id.vol_per);
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
