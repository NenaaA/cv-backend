package wisteria.cvapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cv_category_details")
@Setter
@Getter
public class CvCategoryDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String label;

    private String value;

    @ManyToOne
    @JoinColumn(name = "cv_category_id")
    private CvCategory cvCategory;
}
