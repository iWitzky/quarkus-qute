package org.acme.todo;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.quarkus.qute.CheckedTemplate;
import io.smallrye.common.annotation.Blocking;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.TemplateInstance;


@Path("/todo")
public class TodoResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance error(String message);
        public static native TemplateInstance todo(Todo todo, List<Integer> priorities, boolean update);
        public static native TemplateInstance todos(List<Todo> todos, long totalCount, List<Integer> priorities, String filter, boolean filtered);
    }

    final List<Integer> priorities = IntStream.range(1, 6).boxed().collect(Collectors.toList());

    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance listTodos(@QueryParam("filter") String filter) {
        return Templates.todos(find(filter), Todo.count(), priorities, filter, filter != null && !filter.isEmpty());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    public List<Todo> listTodosJson(@QueryParam("filter") String filter) {
        return find(filter);
    }

    private List<Todo> find(String filter) {
        Sort sort = Sort.ascending("completed")
            .and("priority", Direction.Descending)
            .and("title", Direction.Ascending);

        if (filter != null && !filter.isEmpty()) {
            return Todo.find("LOWER(title) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        }
        else {
            return Todo.findAll(sort).list();
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    @Path("/new")
    @Blocking
    public Response addTodo(TodoForm todoForm) {
        Todo todo = todoForm.convertIntoTodo();
        todo.persist();

        return Response.status(Status.SEE_OTHER)
            .location(URI.create("/todo"))
            .build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{id}/edit")
    @Blocking
    public TemplateInstance updateForm(@PathParam("id") long id) {
        Todo loaded = Todo.findById(id);

        if (loaded == null) {
            return Templates.error("Todo with id " + id + " does not exist.");
        }

        return Templates.todo(loaded, priorities, true);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    @Path("/{id}/edit")
    @Blocking
    public Object updateTodo(
        @PathParam("id") long id,
        TodoForm todoForm) {

        Todo loaded = Todo.findById(id);

        if (loaded == null) {
            return Templates.error("Todo with id " + id + " has been deleted after loading this form.");
        }

        todoForm.updateTodo(loaded);

        return Response.status(301)
            .location(URI.create("/todo"))
            .build();
    }

    @POST
    @Transactional
    @Path("/{id}/delete")
    @Blocking
    public Response deleteTodo(@PathParam("id") long id) {
        Todo.delete("id", id);

        return Response.status(301)
            .location(URI.create("/todo"))
            .build();
    }
}
