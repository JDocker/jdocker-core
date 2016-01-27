package io.github.jdocker;

import java.util.Objects;

/**
 * Created by atsticks on 25.01.16.
 */
public final class CommandDescriptor {
    String commandName;
    String parameters;
    String result;
    String description;

    public static CommandDescriptor of(String commandName,
                                       String description) {
        return of(commandName, "-", "-", description);
    }

    public static CommandDescriptor of(String commandName,
                                       String parameters,
                                       String description) {
        return of(commandName, parameters, "-", description);
    }

    public static CommandDescriptor of(String commandName,
                                       String parameters,
                                       String result,
                                       String description) {
        CommandDescriptor desc = new CommandDescriptor();
        desc.commandName = Objects.requireNonNull(commandName);
        desc.parameters = Objects.requireNonNull(parameters);
        desc.result = Objects.requireNonNull(result);;
        desc.description = description;
        return desc;
    }

    public String getName() {
        return commandName;
    }

    public String getParameters() {
        return parameters;
    }

    public String getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandDescriptor)) return false;

        CommandDescriptor that = (CommandDescriptor) o;

        return commandName.equals(that.commandName);

    }

    @Override
    public int hashCode() {
        return commandName.hashCode();
    }

    @Override
    public String toString() {
        return "CommandDescriptor{" +
                "commandName='" + commandName + '\'' +
                " " + parameters +
                ", -> " + result +
                '}';
    }
}
