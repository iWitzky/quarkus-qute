package org.acme.todo;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;


public class TodoForm {

    public @FormParam("title") @PartType(MediaType.TEXT_PLAIN) String title;
    public @FormParam("completed") @PartType(MediaType.TEXT_PLAIN) String completed;
    public @FormParam("priority") @PartType(MediaType.TEXT_PLAIN) int priority;

    public Todo convertIntoTodo() {
        Todo todo = new Todo();
        todo.title = title;
        todo.completed = "on".equals(completed);
        todo.priority = priority;
        return todo;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Todo updateTodo(Todo toUpdate) {
        toUpdate.title = title;
        toUpdate.completed = "on".equals(completed);
        toUpdate.priority = priority;
        return toUpdate;
    }
}
