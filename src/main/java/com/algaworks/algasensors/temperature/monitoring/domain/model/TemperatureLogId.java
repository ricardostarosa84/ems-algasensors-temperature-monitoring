package com.algaworks.algasensors.temperature.monitoring.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class TemperatureLogId implements Serializable {
    private UUID value;
    
    public TemperatureLogId(UUID uuid) {
        this.value = uuid;
    }

    public TemperatureLogId(String uuid) {
        this.value = UUID.fromString(uuid);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
