package com.zrosfjord.cs.schedule;

public class ScheduleConflictException extends Exception {

    public ScheduleConflictException(Schedule.ScheduleItem schItem) {
        super("Conflict with " + schItem.getId() + " at " + schItem.getStartTime());
    }

}
