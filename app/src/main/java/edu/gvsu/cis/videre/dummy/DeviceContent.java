package edu.gvsu.cis.videre.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.gvsu.cis.videre.Device;
import edu.gvsu.cis.videre.DeviceType;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeviceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Device> ITEMS = new ArrayList<Device>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Device> ITEM_MAP = new HashMap<String, Device>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDevice(String.valueOf(i)));
        }
    }

    /**
     * Adds an item to the list of items bound to the recycler view.
     * @param item
     *      The item to be added.
     * @return
     *      True if the item was added. False otherwise.
     */
    public static boolean addItem(Device item) {
        boolean itemNameNotInUse = true; // Flag that is set to false when the item name is in use.
        for(Device device : ITEMS) {
            if(device.id.equalsIgnoreCase(item.id)) {
                itemNameNotInUse = false;
                break;
            }
        }
        if(itemNameNotInUse) {
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }

        return itemNameNotInUse;
    }

    public static Device createDevice(String device_name) {
        // Initially set the return item to null.
        Device retItem = new Device(device_name, true, DeviceType.MOVE);
        return retItem;
    }
}
