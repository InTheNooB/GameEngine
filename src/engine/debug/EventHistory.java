/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.debug;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author lione
 */
public class EventHistory {

    private List<String> events;

    public EventHistory() {
        events = new CopyOnWriteArrayList();
    }

    public void showEvents() {
        System.out.println("===== Event History =====");
        for (int i = 0; i < events.size(); i++) {
            System.out.println(events.get(i));
        }
        System.out.println("=========================");
    }

    public void addEvent(String event) {
        events.add("Event [" + (events.size() + 1) + "] : " + event);
    }

    public List<String> getEvents() {
        return events;
    }

    public String getStringEvents() {
        String result = "";
        for (String event : events) {
            result += event + "\n";
        }
        return result;
    }

}
