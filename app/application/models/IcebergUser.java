package application.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Danny on 9/25/2014.
 */
@Entity(name = "iceberg_user")
public class IcebergUser implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "id")
    private GlobalUser user;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;
    @Column(name = "gender", nullable = false, length = 100)
    private String gender;

    public void setUser(GlobalUser user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public GlobalUser getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
}
