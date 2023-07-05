package com.package1;

public class NumberResponse {
    private long oldValue;
    private long newValue;

    public NumberResponse(long oldValue, long newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public long getOldValue() {
        return oldValue;
    }

    public void setOldValue(int oldValue) {
        this.oldValue = oldValue;
    }

    public long getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }
}
