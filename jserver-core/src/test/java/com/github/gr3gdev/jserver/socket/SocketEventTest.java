package com.github.gr3gdev.jserver.socket;

import com.github.gr3gdev.jserver.http.Request;
import com.github.gr3gdev.jserver.http.RequestMethod;
import com.github.gr3gdev.jserver.route.RouteListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SocketEventTest {

    @Test
    public void testPriority() {
        final SocketEvent socketAll = new SocketEvent("/api/persons", RequestMethod.GET, new RouteListener());
        final SocketEvent socketById = new SocketEvent("/api/persons/{id}", RequestMethod.GET, new RouteListener());

        final Request req = mock(Request.class);
        when(req.method()).thenReturn(RequestMethod.GET.name());
        when(req.path()).thenReturn("/api/persons/1");

        assertTrue("Request not match", socketById.match(req));
        assertFalse("Request match", socketAll.match(req));
    }

    @Test
    public void testNotMatch() {
        final SocketEvent socket = new SocketEvent("/api/test1", RequestMethod.GET, new RouteListener());

        final Request req1 = mock(Request.class);
        when(req1.method()).thenReturn(RequestMethod.POST.name());
        when(req1.path()).thenReturn("/api/test1");

        final Request req2 = mock(Request.class);
        when(req2.path()).thenReturn("/api/test2");

        final Request req3 = mock(Request.class);
        when(req3.path()).thenReturn("/api/test3");

        assertFalse("Request match", socket.match(req1));
        assertFalse("Request match", socket.match(req2));
        assertFalse("Request match", socket.match(req3));
    }

    @Test
    public void testMatchSimpleGET() {
        final SocketEvent socket = new SocketEvent("/api/users", RequestMethod.GET, new RouteListener());

        final Request req = mock(Request.class);
        when(req.method()).thenReturn(RequestMethod.GET.name());
        when(req.path()).thenReturn("/api/users");

        assertTrue("Request not match", socket.match(req));
    }

    @Test
    public void testMatchSimplePOST() {
        final SocketEvent socket = new SocketEvent("/api/groups", RequestMethod.POST, new RouteListener());

        final Request req = mock(Request.class);
        when(req.method()).thenReturn(RequestMethod.POST.name());
        when(req.path()).thenReturn("/api/groups");

        assertTrue("Request not match", socket.match(req));
    }

    @Test
    public void testMatchWithPathParameters() {
        final SocketEvent socket = new SocketEvent("/api/users/{userId}/groups/{groupId}", RequestMethod.GET, new RouteListener());

        final Map<String, String> parameters = new HashMap<>();

        final Request req = mock(Request.class);
        when(req.method()).thenReturn("GET");
        when(req.path()).thenReturn("/api/users/101/groups/8");
        doAnswer(invocation -> parameters.put(invocation.getArgument(0), invocation.getArgument(1)))
                .when(req).params(anyString(), anyString());

        assertTrue("Request not match", socket.match(req));
        assertEquals("Request parameters not found", 2, parameters.size());
        assertNotNull(parameters.get("userId"));
        assertEquals("Invalid parameter value for userId", "101", parameters.get("userId"));
        assertNotNull(parameters.get("groupId"));
        assertEquals("Invalid parameter value for groupId", "8", parameters.get("groupId"));
    }
}
