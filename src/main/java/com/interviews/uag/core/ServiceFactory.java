package com.interviews.uag.core;

import com.interviews.uag.api.GroupService;
import com.interviews.uag.api.MembershipService;
import com.interviews.uag.api.UserService;
import com.interviews.uag.memory.MemoryGroupService;
import com.interviews.uag.memory.MemoryMembershipService;
import com.interviews.uag.memory.MemoryUserService;

/**
 * Factory for building a new memory-based users-and-groups service.
 */
public class ServiceFactory implements Services {
    private final GroupService groupService;
    private final UserService userService;
    private final MembershipService membershipService;

    public static Services createServices() {
        return new ServiceFactory();
    }

    private ServiceFactory() {
        groupService = new MemoryGroupService(this);
        userService = new MemoryUserService(this);
        membershipService = new MemoryMembershipService(this);
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public UserService getUserService() {
        return userService;
    }

    public MembershipService getMembershipService() {
        return membershipService;
    }
}
