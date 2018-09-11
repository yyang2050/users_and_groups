package com.interviews.uag.api;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.Objects.requireNonNull;

/**
 * A user that may or may not belong to any groups.
 */
@ParametersAreNonnullByDefault
public class User implements Comparable<User> {
    private final String name;

    /**
     * Creates a new instance of a user.
     * This does not implicitly register the user with the {@link UserService}.
     *
     * @param name the unique name that identifies this user; must not be {@code null}.
     */
    public User(String name) {
        this.name = requireNonNull(name, "name");
    }

    /**
     * Returns the name that identifies this user.
     *
     * @return the name that identifies this user.
     */
    public String getName() {
        return name;
    }

    public int compareTo(@Nonnull User other) {
        return name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
