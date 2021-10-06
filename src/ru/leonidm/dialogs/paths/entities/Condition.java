package ru.leonidm.dialogs.paths.entities;

public enum Condition {

    BEFORE_DIALOGS("beforeDialogs"), AFTER_DIALOGS("afterDialogs"),
    BEFORE_QUESTS("beforeQuests"), AFTER_QUESTS("afterQuests"), WHEN_QUESTS("whenQuests");

    private final String toStringValue;

    @Override
    public String toString() {
        return toStringValue;
    }

    Condition(String toStringValue) {
        this.toStringValue = toStringValue;
    }
}
