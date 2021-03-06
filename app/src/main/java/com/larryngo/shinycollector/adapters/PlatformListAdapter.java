package com.larryngo.shinycollector.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.larryngo.shinycollector.R;
import com.larryngo.shinycollector.models.Platform;
import com.larryngo.shinycollector.util.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlatformListAdapter extends RecyclerView.Adapter<PlatformListAdapter.ViewHolder> implements Filterable {
    private final Context mContext;
    private List<Platform> data;
    private List<Platform> data_all;
    private final PlatformListListener listener;

    public PlatformListAdapter(Context context, ArrayList<Platform> data, PlatformListListener listener){
        this.mContext = context;
        this.data = data;
        this.data_all = new ArrayList<>(data);
        this.listener = listener;
    }

    public void addDataList(ArrayList<Platform> entry) {
        data = entry;
        data_all = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlatformListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.platform_list_entry, parent, false);
        return new PlatformListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Platform platform = data.get(position);

        if(Settings.isAnimModeOn()) {
            holder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.list_anim_grow_right));
        }
        holder.name.setText(platform.getName());
        Bitmap bitmap = BitmapFactory.decodeByteArray(platform.getImage(), 0, platform.getImage().length);

        holder.image.setImageBitmap(bitmap); //sets the picture
    }

    public List<Platform> getList() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LinearLayout container;
        private final ImageView image;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.platform_list_entry_card);
            name = view.findViewById(R.id.platform_list_entry_name);
            image = view.findViewById(R.id.platform_list_entry_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Platform> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()) {
                filteredList.addAll(data_all);
            } else {
                for (Platform platform : data_all) {
                    //System.out.println(game.getName() + constraint.toString().toLowerCase());
                    if (platform.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(platform);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends Platform>) results.values);
            notifyDataSetChanged();
        }
    };

    public interface PlatformListListener {
        void onClick(View v, int position);
    }
}
