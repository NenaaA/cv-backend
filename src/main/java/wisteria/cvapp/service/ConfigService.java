package wisteria.cvapp.service;

import jakarta.validation.constraints.NotNull;
import wisteria.cvapp.model.Config;

import java.util.List;

public interface ConfigService {
    List<Config> getConfigByGroupAndAttribute(@NotNull String groupValue, @NotNull String attributeValue);
}
