package com.interviews.uag.memory;

import com.interviews.uag.api.Group;
import com.interviews.uag.api.MembershipService;
import com.interviews.uag.api.User;
import com.interviews.uag.core.AbstractService;
import com.interviews.uag.core.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * An implementation of the membership service that stores user and group relationships in memory.
 */
@ParametersAreNonnullByDefault
public class MemoryMembershipService extends AbstractService implements MembershipService {
    private static final Logger LOG = LoggerFactory.getLogger(MemoryMembershipService.class);

    private final Map<Group, Set<Group>> childGroupsByParent = new HashMap<>();
    private final Map<Group, Set<User>> usersByGroup = new HashMap<>();

    public MemoryMembershipService(Services services) {
        super(services);
    }

    @Override
    public void addGroupToGroup(Group child, Group parent) {
        requireExists(parent);
        requireExists(child);

        Set<Group> children = childGroupsByParent.get(parent);
        if (children == null) {
            children = new HashSet<>();
            childGroupsByParent.put(parent, children);
        }
        children.add(child);

        LOG.debug("Added child group " + child + " to parent group " + parent);
    }

    public void addUserToGroup(User user, Group group) {
        requireExists(user);
        requireExists(group);

        Set<User> users = usersByGroup.get(group);
        if (users == null) {
            users = new HashSet<>();
            usersByGroup.put(group, users);
        }
        users.add(user);

        LOG.debug("Added user " + user + " to group " + group);
    }

    public boolean isUserInGroup(User user, Group group) {
        requireNonNull(user, "user");
        requireNonNull(group, "group");

        boolean isInDirectGroup = getUsersInGroup(group).contains(user);

        if (isInDirectGroup)
            return true;

        Collection<Group> children = getChildrenGroups(group);
        for (Group child : children) {
            if (isUserInGroup(user, child))
                return true;
        }
        return false;
    }

    public boolean isGroupInGroup(Group child, Group parent) {
        requireNonNull(child, "child");
        requireNonNull(parent, "parent");

        boolean isInDirectGroup =  getChildrenGroups(parent).contains(child);

        if (isInDirectGroup)
            return true;

        Collection<Group> children = getChildrenGroups(parent);
        for (Group childGroup : children) {
            if (isGroupInGroup(child, childGroup))
                return true;
        }
        return false;
    }

    public Collection<User> getUsersInGroup(Group group) {
        requireNonNull(group, "group");

        final Collection<User> users = usersByGroup.get(group);
        LOG.debug("Current users in group {}: {}", group.toString(), users == null ? "" : users.toString());
        return users == null ? Collections.EMPTY_SET : users;
    }

    @Override
    public void removeGroupFromGroup(Group child, Group parent) {
        requireNonNull(parent, "parent");
        requireNonNull(child, "child");

        getChildrenGroups(parent).remove(child);
    }

    public void removeUserFromGroup(User user, Group group) {
        requireNonNull(user, "user");
        requireNonNull(group, "group");

        getUsersInGroup(group).remove(user);
        LOG.debug(String.format("Removed user %s from group %s", user, group));
    }

    @Override
    public void removeGroup(Group group) {
        requireNonNull(group);

        childGroupsByParent.remove(group);
        usersByGroup.remove(group);
    }

    private void requireExists(User user) {
        requireNonNull(user, "user");
        if (services.getUserService().findByName(user.getName()) == null) {
            throw new IllegalArgumentException("User '" + user + "' does not exist!");
        }
    }

    private void requireExists(Group group) {
        requireNonNull(group, "group");
        if (services.getGroupService().findByName(group.getName()) == null) {
            throw new IllegalArgumentException("Group '" + group + "' does not exist!");
        }
    }

    private Collection<Group> getChildrenGroups(Group group) {
        requireNonNull(group, "group");

        final Collection<Group> children = childGroupsByParent.get(group);
        return children == null ? Collections.EMPTY_SET : children;
    }
}
