package application.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Danny on 9/24/2014.
 */
@Entity(name = "app")
public class App implements Serializable {
    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name", nullable = false, length = 100)
    private String name;
    @Column(name="api_key", nullable = false, length = 100)
    private String apiKey;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
