package io.github.jdocker.events;

import io.github.jdocker.ActionState;

import java.util.*;
import java.util.logging.Level;

/**
 * Basic event type, can be extended if necessary.
 */
public final class JDockerEvent {

    private JDockerEvent parent;
    private long timestamp = System.currentTimeMillis();
    private String id = UUID.randomUUID().toString();
    private CommandType type;
    private Level severity;
    private String description;
    private Map<Object,Object> payload = new HashMap<>();
    private ActionState state = ActionState.NEW;
    private List<String> messages = new ArrayList<>();

    private JDockerEvent(CommandType type, String description, Level severity){
        this.type = Objects.requireNonNull(type);
        this.payload = payload;
        this.severity = severity;
    }

    public static <T> JDockerEvent info(CommandType type, String description){
        return new JDockerEvent(type, description, Level.INFO);
    }

    public static <T> JDockerEvent warn(CommandType type, String description){
        return new JDockerEvent(type, description, Level.WARNING);
    }

    public static <T> JDockerEvent error(CommandType type, String description){
        return new JDockerEvent(type, description, Level.SEVERE);
    }

    public static <T> JDockerEvent debug(CommandType type, String description){
        return new JDockerEvent(type, description, Level.FINEST);
    }

    public static <T> JDockerEvent config(CommandType type, String description) {
        return new JDockerEvent(type, description, Level.CONFIG);
    }

    public long getTimestamp(){
        return timestamp;
    }

    public String getId(){
        return id;
    }

    public CommandType getCommandType(){
        return type;
    }

    public String getDescription(){
        return description;
    }

    public Level getSeverity(){
        return severity;
    }

    public JDockerEvent getParent(){
        return parent;
    }

    public Object getPayload(String key){
        return payload.get(key);
    }

    public List<String> getMessages(){
        return Collections.unmodifiableList(messages);
    }

    public String getMessageInfo(){
        StringBuilder b = new StringBuilder();
        b.append(severity.toString() + ": " + description + "\n" +
                " STATE:" + state + "\n");
        for(String msg:messages){
            b.append("   ").append(msg).append("\n");
        }
        return b.toString();
    }

    public void addMessage(String source, String message, ActionState newState){
        checkMutable();
        messages.add(source+": " + Objects.requireNonNull(message));
        if(newState!=null){
            messages.add(source+": STATE: "+state + " -> "+ newState);
            this.state = newState;
        }
    }

    public void setSeverity(String source, String message, Level severity) {
        checkMutable();
        messages.add(source+": " + Objects.requireNonNull(message));
        messages.add(source+": SEVERITY: "+this.severity + " -> "+ severity);
        this.severity = Objects.requireNonNull(severity);
    }

    public void setDescription(String source, String message, String description) {
        checkMutable();
        messages.add(source+": " + Objects.requireNonNull(message));
        messages.add(source+": DESCRIPTION: '"+this.description + "' -> '"+ description + "'");
        this.description = Objects.requireNonNull(description);
    }

    public void setPayload(String source, String message, String key, Object payload) {
        checkMutable();
        messages.add(source+": " + Objects.requireNonNull(message));
        messages.add(source+": PAYLOAD: '"+this.payload + "' -> '"+ payload + "'");
        this.payload.put(key, payload);
    }

    private void checkMutable() {
        switch(this.state){
            case ABORTED:
            case FAILED:
            case SUCCESS:
                throw new IllegalStateException("Action is finished and immutalbe.");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JDockerEvent)) return false;

        JDockerEvent action = (JDockerEvent) o;
        return id.equals(action.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", severity=" + severity +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}
