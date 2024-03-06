package wisteria.cvapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "cv")
@Getter
@Setter
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Timestamp created_at;

    private Timestamp modified_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
