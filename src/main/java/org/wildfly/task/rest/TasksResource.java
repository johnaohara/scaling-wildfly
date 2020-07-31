package org.wildfly.task.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.wildfly.task.model.Task;

@Path("tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TasksResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Transactional
    public List<Task> getAllTasks() {
        return em.createNamedQuery(Task.FIND_ALL).getResultList();
    }

    @POST
    @Transactional
    public Response saveTask(Task newTask, @Context UriInfo uriInfo){
        em.persist(newTask);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(newTask.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getSingleTask(@PathParam("id") String id){
        Task foundTask = em.find(Task.class, id);

        if (foundTask != null){
            return Response.ok(foundTask).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") String id){
        Task task = em.find(Task.class, 1);
        if (task != null){
            em.remove(task);

            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
