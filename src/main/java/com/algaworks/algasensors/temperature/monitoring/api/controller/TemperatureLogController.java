package com.algaworks.algasensors.temperature.monitoring.api.controller;


import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorID;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures")
@RequiredArgsConstructor
public class TemperatureLogController {

    private final TemperatureLogRepository temperatureLogRepository;


    @GetMapping
    public Page<TemperatureLogOutput> search(@PathVariable @Nonnull TSID sensorId,
                                             @PageableDefault Pageable page){
        Page<TemperatureLog> temperatureLogs = temperatureLogRepository
                .findAllBySensorId(new SensorID(sensorId), page);

        return temperatureLogs.map(this::convertSensorOutput);
    }


    private TemperatureLogOutput convertSensorOutput(TemperatureLog temperatureLog) {

        return TemperatureLogOutput.builder()
                .sensorId(temperatureLog.getSensorId().getValue())
                .value(temperatureLog.getValue())
                .registeredAt(temperatureLog.getRegisteredAt())
                .uuid(temperatureLog.getTemperatureLogId().getValue())
                .build();
    }
}
