package mine.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import mine.rest.entity.Message;
import mine.rest.entity.User;

@Path("/users")
public class UsersResource {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getJSON() {

		try {

			entityManagerFactory = Persistence
					.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			List<User> users = entityManager.createQuery("from User s")
					.getResultList();

			String jsonResponse = new Gson().toJson(users);

			entityManager.getTransaction().commit();
			entityManager.close();
			return jsonResponse;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	@GET
	@Path("/otherU")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJacksonJSON() {

		try {

			entityManagerFactory = Persistence
					.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			List<User> messages = (List<User>) entityManager.createQuery(
					"select s from User s").getResultList();

			ObjectMapper mapper = new ObjectMapper();

			String jsonresponse = mapper.writeValueAsString(messages);

			entityManager.close();
			return Response.ok(jsonresponse).build();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(Response.Status.PRECONDITION_FAILED).build();

	}

	@POST
	public String addUser(@QueryParam("username") String username,
			@QueryParam("password") String password) {

		try {

			entityManagerFactory = Persistence
					.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			User person = new User();
			person.setUsername(username);
			person.setPassword(password);
			person.setLink("/users/" + username);

			entityManager.persist(person);
			entityManager.getTransaction().commit();
			entityManager.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("userAdd")
	public String addUserFormParam(@FormParam("username") String username,
			@FormParam("password") String password) {

		try {

			entityManagerFactory = Persistence
					.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			User person = new User();
			person.setUsername(username);
			person.setPassword(password);
			person.setLink("/users/" + username);

			entityManager.persist(person);
			entityManager.getTransaction().commit();
			entityManager.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

}
