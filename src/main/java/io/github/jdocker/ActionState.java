package io.github.jdocker;

/**
 * The state of an action.
 */
public enum ActionState {
    NEW,
    RUNNING,
    PAUSED,
    ABORTED,
    FAILED,
    SUCCESS
}
