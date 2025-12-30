package com.algaworks.algasensors.temperature.monitoring.api.controller;


import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorID;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {
    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput getSensorAlertRepository(@PathVariable @Nonnull TSID sensorId){
        return convertSensorAlertOutput(sensorAlertRepository.findById(new SensorID(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorAlertOutput createUpdateSensorAlert(@RequestBody SensorAlertInput sensorAlertInput,
                                                     @PathVariable @Nonnull TSID sensorId){
        SensorAlert result = sensorAlertRepository.findById(new SensorID(sensorId))
                .map(existing -> updateSensorAlert(existing, sensorAlertInput))
                .orElseGet(() -> convertSensorInputtoSensor(new SensorID(sensorId), sensorAlertInput));

       return convertSensorAlertOutput(sensorAlertRepository.saveAndFlush(result));
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensorAlert(@PathVariable @Nonnull TSID sensorId){
        SensorAlert result = sensorAlertRepository.findById(new SensorID(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        sensorAlertRepository.delete(result);
    }


    private SensorAlert updateSensorAlert(SensorAlert sensorAlert, SensorAlertInput sensorAlertInput){
        return  SensorAlert.builder()
                .id(sensorAlert.getId())
                .maxTemperature(sensorAlertInput.getMaxTemperature())
                .minTemperature(sensorAlertInput.getMinTemperature())
                .build();
    }

    private SensorAlert convertSensorInputtoSensor(SensorID sensorID, SensorAlertInput sensorAlertInput){
        return  SensorAlert.builder()
                .id(sensorID)
                .maxTemperature(sensorAlertInput.getMaxTemperature())
                .minTemperature(sensorAlertInput.getMinTemperature())
                .build();
    }

    private SensorAlertOutput convertSensorAlertOutput(SensorAlert sensor) {
        return SensorAlertOutput.builder()
                .id(sensor.getId().getValue())
                .maxTemperature(sensor.getMaxTemperature())
                .minTemperature(sensor.getMinTemperature())
                .build();
    }
}
