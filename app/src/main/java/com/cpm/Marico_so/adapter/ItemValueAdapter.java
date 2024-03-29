package com.cpm.Marico_so.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpm.Marico_so.database.MaricoSODatabase;
import com.cpm.Marico_so.getterSetter.NavGetterSetter;
import com.cpm.Marico_so.getterSetter.PromoterSkuwiseSale;
import com.cpm.Marico_so.getterSetter.PromoterTDPSaleTarget;
import com.cpm.Marico_so.getterSetter.PromoterTargetMM;
import com.cpm.Marico_so.utilities.AlertandMessages;
import com.cpm.Marico_so.R;
import com.cpm.Marico_so.dailyEntry.MonthlyTargetActivity;
import com.cpm.Marico_so.dailyEntry.SkuWiseSaleActivity;
import com.cpm.Marico_so.dailyEntry.TDPSaleTargetActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemValueAdapter  extends RecyclerView.Adapter<ItemValueAdapter.ViewHolder> {
    Context context;
    List<NavGetterSetter> list;
    MaricoSODatabase db;
    ArrayList<PromoterTargetMM> promoterTargets;
    ArrayList<PromoterTDPSaleTarget> TDPSaleList;
    ArrayList<PromoterSkuwiseSale> promoterSkuwiseSales;

    public ItemValueAdapter(Context context, List<NavGetterSetter> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {
        viewHolder.txt.setText(list.get(pos).getReport_name());
        db = new MaricoSODatabase(context);
        db.open();

        viewHolder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos == 0){
                    db.open();
                    promoterTargets = db.getMonthlyTargetData();
                    if(promoterTargets.size()>0) {
                        Intent intent = new Intent(context, MonthlyTargetActivity.class);
                        context.startActivity(intent);
                    }else{
                        AlertandMessages.showToastMsg(context,"No data found");
                    }
                }else if(pos == 1){
                    db.open();
                    TDPSaleList = db.getTDPSaleTargetData();
                    if(TDPSaleList.size() >0 ){
                        Intent intent = new Intent(context, TDPSaleTargetActivity.class);
                        context.startActivity(intent);
                    }else{
                        AlertandMessages.showToastMsg(context,"No data found");
                    }
                } else if(pos == 2){
                    db.open();
                    promoterSkuwiseSales = db.getSkuWiseSaleTarget();
                    if(promoterSkuwiseSales.size() >0){
                        Intent intent = new Intent(context, SkuWiseSaleActivity.class);
                        context.startActivity(intent);
                    }else{
                        AlertandMessages.showToastMsg(context,"No data found");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout lay_menu;
        TextView txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lay_menu = (LinearLayout)itemView.findViewById(R.id.layout_menu);
            cardView = (CardView)itemView.findViewById(R.id.card_view_layout);
            txt = (TextView)itemView.findViewById(R.id.txtDynamic);
        }
    }
}
