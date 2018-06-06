package edu.gvsu.cis.videre;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import edu.gvsu.cis.videre.DeviceFragment.OnListFragmentInteractionListener;
import edu.gvsu.cis.videre.Device;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Device} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final List<Device> mValues;
    private final OnListFragmentInteractionListener mListener;

    public DeviceAdapter(List<Device> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDeviceNameView.setText(mValues.get(position).id);
        holder.mInUseView.setText(""); // No text needed.
        holder.mInUseView.setChecked(mValues.get(position).inUse);
        holder.mSettingView.setText(mValues.get(position).toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDeviceNameView;
        public final CheckBox mInUseView;
        public final TextView mSettingView;
        public Device mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDeviceNameView = (TextView) view.findViewById(R.id.item_number);
            mInUseView = (CheckBox) view.findViewById(R.id.in_use_check);
            mSettingView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDeviceNameView.getText() + "'";
        }
    }
}
