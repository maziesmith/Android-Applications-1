package com.assignment.rahulhosmani.stockwatch;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{
    private List<Stock>  stockList;
    private MainActivity mainActivity;
    private DecimalFormat df = new DecimalFormat("##.##");

    public StockAdapter(List<Stock> stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mConstraintLayout;
        public TextView symbolTextView;
        public TextView companyTextView;
        public TextView priceTextView;
        public TextView changedPriceTextView;
        public ImageView changeImageView;

        public ViewHolder(ConstraintLayout itemView) {
            super(itemView);
            mConstraintLayout = itemView;
            symbolTextView = itemView.findViewById(R.id.textViewSymbol);
            companyTextView = itemView.findViewById(R.id.textViewCompanyName);
            priceTextView = itemView.findViewById(R.id.textViewPrice);
            changedPriceTextView= itemView.findViewById(R.id.textViewPriceChange);
            changeImageView= (ImageView) itemView.getViewById(R.id.changeImage);

        }
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ConstraintLayout v = (ConstraintLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_row_layout, parent, false);
        v.setOnClickListener(mainActivity);
        v.setOnLongClickListener(mainActivity);
        ViewHolder viewhold = new ViewHolder(v);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Stock stock = stockList.get(i);

        if(Math.abs(stock.getPriceChange())==stock.getPriceChange()){
            viewHolder.symbolTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorGreenDark));
            viewHolder.companyTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorGreenDark));
            viewHolder.priceTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorGreenDark));
            viewHolder.changedPriceTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorGreenDark));
            viewHolder.changeImageView.setBackgroundResource(R.drawable.icon_up);

        }else{
            viewHolder.symbolTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorRedDark));
            viewHolder.companyTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorRedDark));
            viewHolder.priceTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorRedDark));
            viewHolder.changedPriceTextView.setTextColor(mainActivity.getResources().getColor(R.color.colorRedDark));
            viewHolder.changeImageView.setBackgroundResource(R.drawable.icon_down);
        }

        viewHolder.symbolTextView.setText(stock.getStockSymbol());
        viewHolder.companyTextView.setText(stock.getCompanyName());
        viewHolder.priceTextView.setText(""+stock.getPrice());
        viewHolder.changedPriceTextView.setText(df.format(stock.getPriceChange()) + "(" + df.format(stock.getPercentageChange()) + "%)");
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }




}
