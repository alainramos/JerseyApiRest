package mine.rest.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid")
	private String id;
	private String username;
	private String password;
	private String link;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private Set<Message> messages = new HashSet<>();

	public User(String id, String username, String password, String link,
			Set<Message> messages) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.link = link;
		this.messages = messages;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@JsonManagedReference
	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	
	
	
}
