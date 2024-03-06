package wisteria.cvapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="config")
@Getter
@Setter
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String group;

    private String attribute;

    private String value;

    private String additionalInfo;

    private String description;
}
