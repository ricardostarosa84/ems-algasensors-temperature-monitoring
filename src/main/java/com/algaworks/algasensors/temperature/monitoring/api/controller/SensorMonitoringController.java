package com.algaworks.algasensors.temperature.monitoring.api.controller;


import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorID;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        sensor.setEnabled(true);
        sensorMonitoringRepository.saveAndFlush(sensor);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable @Nonnull TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);
        sensor.setEnabled(true);
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
