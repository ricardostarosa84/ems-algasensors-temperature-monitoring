package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorID;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public SensorMonitoringOutput getDetail(@PathVariable @Nonnull TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);

        return convertSensorOutput(sensor);
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable @Nonnull TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);
        if(Boolean.TRUE.equals(sensor.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        sensor.setEnabled(true);

        sensorMonitoringRepository.saveAndFlush(sensor);

    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SneakyThrows
    public void disable(@PathVariable @Nonnull TSID sensorId) {
        SensorMonitoring sensor = findByIdOrDefault(sensorId);
        if(Boolean.FALSE.equals(sensor.getEnabled())) {
            Thread.sleep(Duration.ofSeconds(10));
        }
        sensor.setEnabled(false);
        sensorMonitoringRepository.saveAndFlush(sensor);
    }



    private SensorMonitoring findByIdOrDefault(@Nonnull TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorID(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorID(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updatedAt(null)
                        .build());
    }

    private SensorMonitoringOutput convertSensorOutput(SensorMonitoring sensor) {
        return SensorMonitoringOutput.builder()
                .id(sensor.getId().getValue())
                .enabled(sensor.getEnabled())
                .lastTemperature(sensor.getLastTemperature())
                .updatedAt(sensor.getUpdatedAt())
                .build();
    }
}
