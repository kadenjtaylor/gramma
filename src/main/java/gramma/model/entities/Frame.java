package gramma.model.entities;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Frame {

    private final Map<String, String> labelToIdMap;

    public Frame(Map<String, String> labelToIdMap) {
        this.labelToIdMap = labelToIdMap;
    }

    public String getId(String label) {
        return this.labelToIdMap.get(label);
    }

    public boolean isEmpty() {
        return labelToIdMap.isEmpty();
    }

    public static Frame empty() {
        return new Frame(ImmutableMap.of());
    }

    @Override
    public String toString() {
        return labelToIdMap.toString();
    }
}
