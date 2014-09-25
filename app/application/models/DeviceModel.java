package application.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Danny on 9/25/2014.
 */
@Entity(name = "device_model")
public class DeviceModel implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "manufacturer", nullable = false, length = 100)
    private String manufacturer;
    @Column(name = "model", nullable = false, length = 100)
    private String model;

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }
}
