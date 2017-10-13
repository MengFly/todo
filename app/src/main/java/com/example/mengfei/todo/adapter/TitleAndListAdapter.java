package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TitleAndListAdapter extends RecyclerView.Adapter<TitleAndListAdapter.TitleAndListHolder> {

    private List<ShowModel> showModels;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public TitleAndListAdapter(Context context, List<ShowModel> showModels) {
        this.layoutInflater = LayoutInflater.from(context);
        this.showModels = showModels;
    }

    @Override
    public TitleAndListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TitleAndListHolder holder = new TitleAndListHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, (Integer) v.getTag());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TitleAndListHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.bindView(showModels.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return showModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return showModels.get(position).getType();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class TitleAndListHolder extends RecyclerView.ViewHolder {

        private TextView showTextTV;

        private TitleAndListHolder(View itemView) {
            super(itemView);
            showTextTV = (TextView) itemView.findViewById(android.R.id.text1);
        }

        private void bindView(ShowModel showModels) {
            switch (showModels.getType()) {
                case ShowModel.TYPE_CONTENT:
                    break;
                case ShowModel.TYPE_TITLE:
                    showTextTV.setTextSize(14.0f);
                    showTextTV.setTextColor(Color.parseColor("#E91E63"));
                    break;
            }
            showTextTV.setText(showModels.getShowText());
        }
    }

}

