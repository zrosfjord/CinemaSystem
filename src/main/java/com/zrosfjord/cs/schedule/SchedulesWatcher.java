package com.zrosfjord.cs.schedule;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

public class SchedulesWatcher implements Runnable, Serializable {

    private LinkedList<Schedule> schedules;

    private transient boolean running;
    private transient Thread thread;

    /**
     * Constructor for SchedulesWatcher
     */
    public SchedulesWatcher() {
        schedules = new LinkedList<Schedule>();
    }

    /**
     * Starts thread for watching
     */
    public void start() {
        if (running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (running) {
            LocalDateTime now = LocalDateTime.now();

            //System.out.println("\nUpdating at: " + formatter.format(now) + "\n");

            try {
                Iterator<Schedule> it = schedules.iterator();
                while (it.hasNext()) {
                    Schedule next = it.next();

                    if (next.getCurrentItem() != next.getItemAt(now)) {
                        //System.out.println();

                        next.clean(now);
                        next.update(now);
                    }
                }


                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Getters and Setters

    public LinkedList<Schedule> getSchedules() {
        return schedules;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
