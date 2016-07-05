package mine.rest;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mine.rest.entity.Message;
import mine.rest.entity.User;

@Path("/messages")
public class MessageResources {

	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJSON() {
		
		try{
			
			entityManagerFactory = Persistence.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			List<Message> messages = (List<Message>) entityManager.createQuery("select s from Message s").getResultList();
			
			JsonArray messagesJSon = new JsonArray();
			
			for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
				Message message = (Message) iterator.next();
				JsonObject messageJson  = new JsonObject();
				messageJson.addProperty("id", message.getId());
				messageJson.addProperty("username", message.getUser().getUsername());
				messageJson.addProperty("content", message.getContent());
				messageJson.addProperty("link", message.getLink());
				messagesJSon.add(messageJson);
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	       
		    
		    entityManager.close();
		    return Response.ok(gson.toJson(messagesJSon)).build();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		
		return Response.status(Response.Status.PRECONDITION_FAILED).build();

	 }
	
	@GET
	@Path("/other")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGenericJSON() {
		
		try{
			
			entityManagerFactory = Persistence.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			List<Message> messages = (List<Message>) entityManager.createQuery("select s from Message s").getResultList();
			
			Gson gson = new GsonBuilder()
		    .setExclusionStrategies(new ExclusionStrategy() {

		        public boolean shouldSkipClass(Class<?> clazz) {
		            return (clazz == User.class);
		        }

		        /**
		          * Custom field exclusion goes here
		          */
		        public boolean shouldSkipField(FieldAttributes f) {
		            return false;
		        }

		     })
		    .serializeNulls()
		    .create();
		    
		    entityManager.close();
		    return Response.ok(gson.toJson(messages)).build();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		
		return Response.status(Response.Status.PRECONDITION_FAILED).build();

	 }
	
	@GET
	@Path("/otherJ")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJacksonJSON() {
		
		try{
			
			entityManagerFactory = Persistence.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			List<Message> messages = (List<Message>) entityManager.createQuery("select s from Message s").getResultList();
			
			ObjectMapper mapper  = new ObjectMapper();
			
			String jsonresponse = mapper.writeValueAsString(messages);
		    
		    entityManager.close();
		    return Response.ok(jsonresponse).build();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		
		return Response.status(Response.Status.PRECONDITION_FAILED).build();

	 }
	
	
	@POST
	public String addMessage(@QueryParam("username") String username,@QueryParam("content") String content){
		
		try{
			
			entityManagerFactory = Persistence.createEntityManagerFactory("RestTest");
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			
			
			List<User> persons = (List<User> )entityManager.createQuery("from User s where s.username=:userName ").setParameter("userName", username).getResultList();
			
			if(persons==null || persons.size()==0){
				return "No se encuentra el usuario";
			}
			else {
				User person = persons.get(0);
				Message m = new Message();
				m.setContent(content);
				m.setUser(person);
				
				
				entityManager.persist( m);
			    entityManager.getTransaction().commit();
			    
			    entityManager.getTransaction().begin();
			    m.setLink(username+"/"+m.getId());
			    entityManager.persist( m);
			    entityManager.getTransaction().commit();
			    
			    
			    entityManager.close();
			}
			
			
			
			
			
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		
		return "";
	}
	
}
