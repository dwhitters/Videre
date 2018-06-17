package edu.gvsu.cis.videre;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import edu.gvsu.cis.videre.DeviceFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Device} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final List<Device> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;

    public DeviceAdapter(List<Device> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_device, parent, false);
            return new ViewHolder(view,viewType);
        } else if(viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_device_list_header, parent, false);
            return new ViewHolder(view,viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(holder.view_Type == TYPE_LIST) {
            holder.mItem = mValues.get(position-1);
            holder.mDeviceNameView.setText(mValues.get(position-1).id);
            holder.mInUseView.setText(""); // No text needed.
            holder.mInUseView.setChecked(mValues.get(position-1).inUse);
            holder.mSettingView.setText(mValues.get(position-1).toString());

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

        } else if(holder.view_Type == TYPE_HEAD) {
            holder.title_Device.setText("DEVICE");
            holder.title_Use.setText("USE");
            holder.title_Setting.setText("SETTING");
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int view_Type;
        public View mView = null;
        public TextView mDeviceNameView = null;
        public CheckBox mInUseView = null;
        public TextView mSettingView = null;
        public Device mItem;

        public TextView title_Device = null, title_Use = null, title_Setting = null;

        public ViewHolder(View view,int viewType) {
            super(view);

            if(viewType == TYPE_LIST) {
                mView = view;
                mDeviceNameView = (TextView) view.findViewById(R.id.item_number);
                mInUseView = (CheckBox) view.findViewById(R.id.in_use_check);
                mSettingView = (TextView) view.findViewById(R.id.content);
                view_Type = 1;
                //checkbox click event handling
                mInUseView.setOnCheckedChangeListener((k,v) -> {
                       for(Device d : mValues) {
                           if(d.id.equals(mDeviceNameView.getText())) {
                               d.inUse = mInUseView.isChecked();
                               try {
                                   CurrentSession.getInstance().getDatabaseRef().child("devices").child(d.key).setValue(d);
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   }
                );
                mView.setOnLongClickListener(v -> {
                    DeviceActivity.longClickOccurred = true;
                    return false; // Signal that the click event was not handled. It will be handled in the
                                  // normal onListFragmentInteraction handler.
                });
            } else if(viewType == TYPE_HEAD) {
                title_Device = (TextView) view.findViewById(R.id.h_device);
                title_Use = (TextView) view.findViewById(R.id.h_inUse);
                title_Setting = (TextView) view.findViewById(R.id.h_setting);
                view_Type = 0;
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDeviceNameView.getText() + "'";
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0) {
            return TYPE_HEAD;
        }
        return TYPE_LIST;
    }

}
