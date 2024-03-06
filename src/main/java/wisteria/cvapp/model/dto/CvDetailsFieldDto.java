package wisteria.cvapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CvDetailsFieldDto {
    private String category;
    private String label;
    private String value;
}
