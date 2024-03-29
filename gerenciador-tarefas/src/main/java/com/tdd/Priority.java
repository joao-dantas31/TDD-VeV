package com.tdd;

public enum Priority {
    LOW(0), MEDIUM(1), HIGH(2);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
