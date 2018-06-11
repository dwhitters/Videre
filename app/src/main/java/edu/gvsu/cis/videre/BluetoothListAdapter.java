package edu.gvsu.cis.videre;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.gvsu.cis.videre.BluetoothFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BluetoothItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

    private final List<BluetoothItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BluetoothListAdapter(List<BluetoothItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bluetooth, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBtName.setText(mValues.get(position).name);
        holder.mBtAddress.setText(mValues.get(position).address);

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
        public final TextView mBtName;
        public final TextView mBtAddress;
        public BluetoothItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBtName = (TextView) view.findViewById(R.id.bt_name);
            mBtAddress = (TextView) view.findViewById(R.id.bt_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBtName.getText() + "'";
        }
    }
}
