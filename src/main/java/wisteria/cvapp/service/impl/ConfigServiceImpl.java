package wisteria.cvapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wisteria.cvapp.model.Config;
import wisteria.cvapp.repository.ConfigRepository;
import wisteria.cvapp.service.ConfigService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    @Override
    public List<Config> getConfigByGroupAndAttribute(String groupValue, String attributeValue) {
        return this.configRepository.getConfigListByGroupAndAttribute(groupValue,attributeValue);
    }
}
