package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
@Tag(name = "Movies Recourse", description = "Movie Rest API")
public class MovieResource {
    public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getMovies",
            summary = "Get Movies",
            description = "Get all movies inside the list" )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "countMovies",
            summary = "Count Movies",
            description = "Size of the list movies" )
    @APIResponse(
            responseCode = "200",
            description = "Operation completed",
            content = @Content(mediaType = MediaType.TEXT_PLAIN))
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createMovies",
            summary = "Create Movies",
            description = "Create a new movie to add inside the list" )
    @APIResponse(
            responseCode = "201",
            description = "Movie Created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response createMovie(
            @RequestBody(
                    description = "Movie to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            Movie newMovie) {
        movies.add(newMovie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }


    @PUT
    @Path("/{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "updateMovies",
            summary = "Update Movies",
            description = "Update a movie inside the list" )
    @APIResponse(
            responseCode = "200",
            description = "Movie Updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response updateMovie(
            @Parameter(
                    description = "Movie id",
                    required = true
            )
        @PathParam("id") Long id,
        @Parameter(
                description = "Movie title",
                required = true
        )
        @PathParam("title") String title) {
        movies = movies.stream().map(movie -> {
            if (movie.getId().equals(id)) {
              movie.setTitle(title);
              return movie;
            }
            return movie;
          }).collect(Collectors.toList());
        return Response.ok(movies).build();
}


@DELETE
@Path("/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Operation(
        operationId = "deleteMovies",
        summary = "Delete Movies",
        description = "Delete a movie inside the list" )
@APIResponse(
        responseCode = "204",
        description = "Movie deleted",
        content = @Content(mediaType = MediaType.APPLICATION_JSON))
@APIResponse(
        responseCode = "400",
        description = "Movie not valid",
        content = @Content(mediaType = MediaType.APPLICATION_JSON))
public Response deleteMovie(
        @PathParam("id") Long id) {
       Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();
    boolean removed = false;
       if (movieToDelete.isPresent()) {
           removed = movies.remove(movieToDelete.get());
       }
       if(removed){
           return Response.noContent().build();
       }
       return Response.status(Response.Status.BAD_REQUEST).build();

    }



}


