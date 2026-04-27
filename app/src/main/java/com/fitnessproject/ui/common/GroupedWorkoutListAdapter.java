package com.fitnessproject.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DatabaseHelper;

import java.util.List;

public class GroupedWorkoutListAdapter extends ArrayAdapter<String> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ENTRY = 1;

    private final LayoutInflater inflater;

    public GroupedWorkoutListAdapter(@NonNull Context context, @NonNull List<String> items) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String item = getItem(position);
        return isHeader(item) ? TYPE_HEADER : TYPE_ENTRY;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_HEADER;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = getItem(position);
        if (getItemViewType(position) == TYPE_HEADER) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_workout_group_header, parent, false);
            }
            TextView header = view.findViewById(R.id.txtGroupHeader);
            header.setText(stripHeaderPrefix(item));
            return view;
        }

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_workout_entry, parent, false);
        }

        TextView time = view.findViewById(R.id.txtWorkoutTime);
        TextView details = view.findViewById(R.id.txtWorkoutDetails);

        String[] parts = splitEntry(item);
        time.setText(parts[0]);
        details.setText(parts[1]);
        return view;
    }

    private boolean isHeader(@Nullable String item) {
        return item != null && item.startsWith(DatabaseHelper.GROUP_HEADER_PREFIX);
    }

    private String stripHeaderPrefix(@Nullable String item) {
        if (item == null) {
            return "";
        }
        return item.substring(DatabaseHelper.GROUP_HEADER_PREFIX.length());
    }

    private String[] splitEntry(@Nullable String item) {
        if (item == null) {
            return new String[]{"", ""};
        }
        int separatorIndex = item.indexOf("  ");
        if (separatorIndex < 0) {
            return new String[]{"", item};
        }
        String time = item.substring(0, separatorIndex).trim();
        String details = item.substring(separatorIndex + 2).trim();
        return new String[]{time, details};
    }
}
