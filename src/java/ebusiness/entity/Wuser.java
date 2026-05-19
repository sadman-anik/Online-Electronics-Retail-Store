package ebusiness.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "WUSER")
@NamedQueries({
    @NamedQuery(
        name = "Wuser.findByUsername",
        query = "SELECT w FROM Wuser w WHERE w.username = :username"
    ),
    @NamedQuery(
        name = "Wuser.findByEmail",
        query = "SELECT w FROM Wuser w WHERE w.email = :email"
    )
})
public class Wuser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 128)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date since;

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getSince() { return since; }
    public void setSince(Date since) { this.since = since; }
}
