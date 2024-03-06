package wisteria.cvapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cv_category")
@Getter
@Setter
public class CvCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String category;

    private String name;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private Cv cv;
}
