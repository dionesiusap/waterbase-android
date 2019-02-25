package com.example.waterbase.pbd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class LightbulbListAdapter extends
        RecyclerView.Adapter<LightbulbListAdapter.LightbulbViewHolder>{
    private final LinkedList<String> mLightbulbList;
    private LayoutInflater mInflater;

    public LightbulbListAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mLightbulbList = wordList;
    }
    @NonNull
    @Override
    public LightbulbListAdapter.LightbulbViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View mItemView = mInflater.inflate(R.layout.lightbulblist_item,
                parent, false);
        return new LightbulbViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(LightbulbViewHolder holder, int position) {
        String mCurrent = mLightbulbList.get(position);
        holder.lightbulpItemView.setText(mCurrent);
        if(mCurrent.contains("A/C")){
            holder.mImageView.setImageResource(R.drawable.acmati);
        }
    }
    @Override
    public int getItemCount() {
        return mLightbulbList.size();
    }



    class LightbulbViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView lightbulpItemView;
        final LightbulbListAdapter mAdapter;
        private ImageView mImageView;
        public LightbulbViewHolder(View itemView, LightbulbListAdapter adapter) {
            super(itemView);
            lightbulpItemView = itemView.findViewById(R.id.word);
            this.mAdapter = adapter;
            mImageView = (ImageView) itemView.findViewById(R.id.lightbulb_image);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(lightbulpItemView.getText().toString().contains("A/C")){
                if(mImageView.getDrawable().getConstantState() == view.getResources().getDrawable(R.drawable.acnyala).getConstantState()){
                    mImageView.setImageResource(R.drawable.acmati);
                }else{
                    mImageView.setImageResource(R.drawable.acnyala);
                }
            }else{
                if(mImageView.getDrawable().getConstantState() == view.getResources().getDrawable(R.drawable.ic_lampu_mati).getConstantState()){
                    mImageView.setImageResource(R.drawable.ic_lampu_nyala);
                }else{
                    mImageView.setImageResource(R.drawable.ic_lampu_mati);
                }
            }

        }
    }
}

