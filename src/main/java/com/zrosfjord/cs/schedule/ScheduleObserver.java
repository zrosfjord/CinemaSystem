package com.zrosfjord.cs.schedule;

public interface ScheduleObserver<T extends Scheduleable> {

    void scheduleUpdated(Schedule<T>.ScheduleItem prev, Schedule<T>.ScheduleItem current);
    void scheduleItemRemoved(Schedule<T>.ScheduleItem item);
    void scheduleItemAdded(Schedule<T>.ScheduleItem item);

}
