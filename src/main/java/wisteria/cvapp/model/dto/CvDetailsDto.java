package wisteria.cvapp.model.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CvDetailsDto {
    @NotNull(message = "userId is required")
    private Integer userId;

    private Map<String, List<Map<String,String>>> categoryMap= new HashMap<>();
}
