package com.zrosfjord.cs.schedule;

import com.zrosfjord.cs.utils.RandomStringUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

public class Schedule<T extends Scheduleable> {

    private LinkedList<ScheduleItem> items;
    private LinkedList<ScheduleObserver> observers;

    private ScheduleItem currentItem;

    /**
     * Constructor the Schedule class
     */
    public <T extends Scheduleable> Schedule() {
        items = new LinkedList<ScheduleItem>();
        observers = new LinkedList<ScheduleObserver>();
    }

    /**
     * Watcher issued method. Only invoked when the currentItem finishes.
     * Updates the currentItem and observers.
     *
     * @param now current time
     */
    public void update(LocalDateTime now) {
        ScheduleItem prevItem = currentItem;
        currentItem = getItemAt(now);

        observers.stream().forEach(e -> e.scheduleUpdated(prevItem, currentItem));
    }

    /**
     * Watcher issued method. Only invoked when the currenItem finishes.
     * Cleans out the schedule of previous events.
     *
     * @param now current time
     */
    public void clean(LocalDateTime now) {
        // Goes through the items list
        Iterator<ScheduleItem> itemsIt = items.iterator();
        while (itemsIt.hasNext()) {

            ScheduleItem nextItem = itemsIt.next();
            LocalDateTime startTime = nextItem.getStartTime();
            T item = nextItem.getScheduleable();

            // If the item isn't current playing and its start time has passed, it is removed
            if (!item.getTimeDuration().conflictsAt(startTime, now) && startTime.isBefore(now)) {
                itemsIt.remove();

                // Updates observers
                observers.stream().forEach(e -> e.scheduleItemRemoved(nextItem));
            }
        }
    }

    /**
     * Adds a scheduleable at the time given
     *
     * @param time the start time for this scheduleable
     * @param object the scheduleable
     * @throws ScheduleConflictException if there is something already scheduled for then
     */
    public ScheduleItem addScheduleable(LocalDateTime time, T object) throws ScheduleConflictException {
        Iterator<ScheduleItem> it = items.iterator();
        while (it.hasNext()) {
            ScheduleItem next = it.next();
            LocalDateTime startTime = next.getStartTime();
            T sch = next.getScheduleable();

            if (sch.getTimeDuration().conflictsAt(startTime, time))
                throw new ScheduleConflictException(next);
        }

        ScheduleItem item = new ScheduleItem(time, object);
        items.add(item);
        observers.stream().forEach(e -> e.scheduleItemAdded(item));
        return item;
    }

    /**
     * Returns the scheduledItem at a time
     *
     * @param time the time you are looking
     * @return the ScheduleItem
     */
    public ScheduleItem getItemAt(LocalDateTime time) {
        Iterator<ScheduleItem> it = items.iterator();
        while (it.hasNext()) {
            ScheduleItem next = it.next();
            LocalDateTime startTime = next.getStartTime();
            T item = next.getScheduleable();

            if (item.getTimeDuration().conflictsAt(startTime, time))
                return next;
        }

        return null;
    }

    /**
     * Get item with the given id
     *
     * @param id id of the item you are looking for
     * @return the ScheduleItem
     */
    public ScheduleItem getItem(String id) {
        Iterator<ScheduleItem> it = items.iterator();
        while (it.hasNext()) {
            ScheduleItem next = it.next();
            if(next.getId().equals(id))
                return next;
        }

        return null;
    }


    public ScheduleItem getCurrentItem() {
        return currentItem;
    }

    public LinkedList<ScheduleItem> getItems() {
        return items;
    }

    public LinkedList<ScheduleObserver> getObservers() {
        return observers;
    }

    public class ScheduleItem implements Comparable<ScheduleItem> {

        private LocalDateTime startTime;
        private T scheduleable;

        private String id;

        /**
         * Constructor for ScheduleItem
         *
         * @param startTime start time
         * @param item the item you are scheduling
         */
        private ScheduleItem(LocalDateTime startTime, T item) {
            this.startTime = startTime;
            this.scheduleable = item;

            id = RandomStringUtil.createRandomCode(11
                    , RandomStringUtil.ALPHA_NUM
                    , "^([a-zA-Z0-9]{3})([a-zA-Z0-9]{5})([a-zA-Z0-9]{3})$"
                    , "$1-$2-$3");
        }

        @Override
        public int compareTo(ScheduleItem o) {
            return startTime.compareTo(o.getStartTime());
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return "ScheduleItem@{ID: " + id
                    + "; StartTime: " + formatter.format(startTime)
                    + "; Item: " + scheduleable + ";}";
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public String getId() {
            return id;
        }

        public T getScheduleable() {
            return scheduleable;
        }

    }

}
