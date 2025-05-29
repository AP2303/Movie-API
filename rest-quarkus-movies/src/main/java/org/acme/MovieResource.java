package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    public static List<Movie> movies = new ArrayList<>();

    @GET
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Path("/size")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    public Response createMovie(Movie newMovie) {
        movies.add(newMovie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }

    @PUT
    @Path("/{id}/{title}")
    public Response updateMovie(@PathParam("id") Long id,
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
    public Response deleteMovie(@PathParam("id") Long id) {
        Optional<Movie> movieToDelete = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();

        boolean removed = movieToDelete.map(movies::remove).orElse(false);

        if (removed) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
