package edu.gvsu.cis.videre.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DeviceItem> ITEM_MAP = new HashMap<String, DeviceItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDevice(String.valueOf(i)));
        }
    }

    private static void addItem(DeviceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
        Constructor for the DeviceItem class.

        @param device_name
            The name of the device.

        @return
            A new device item if no other item has the same name. Otherwise null.
     */
    public static DeviceItem createDevice(String device_name) {
        // Initially set the return item to null.
        DeviceItem retItem = new DeviceItem(device_name, true, "Item " + device_name);
        for(DeviceItem device : ITEMS) {
            if(device.id.equalsIgnoreCase(device_name)) {
                retItem = null; // Don't return the new item if another item has the same name.
                break;
            }
        }
        return retItem;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DeviceItem {
        public final String id;
        public final boolean inUse;
        public final String content;

        public DeviceItem(String id, boolean inUse, String content) {
            this.id = id;
            this.inUse = inUse;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
