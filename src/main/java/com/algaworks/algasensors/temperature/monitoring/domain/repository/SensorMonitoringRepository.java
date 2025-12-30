package com.algaworks.algasensors.temperature.monitoring.domain.repository;

import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorID;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorID> {
}
