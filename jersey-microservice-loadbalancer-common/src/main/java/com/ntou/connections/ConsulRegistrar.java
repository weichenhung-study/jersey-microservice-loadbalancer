package com.ntou.connections;

import com.ntou.tool.Common;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.util.UUID;

@Log4j2
public class ConsulRegistrar {

    public static void registerWithConsul(String serviceName, String sys) {
        try {
            Consul consul = Consul.builder()
                    .withHostAndPort(com.google.common.net.HostAndPort.fromParts("jersey-consul", 8500))
                    .build();

            String serviceId = serviceName + "-" + UUID.randomUUID();
            log.info("serviceId:{}", serviceId);

            String address = System.getenv("HOSTNAME");
            int port = 8080;

            String urlHealth = "http://" + address + ":" + port + "/jersey-microservice-loadbalancer" + sys + "/health";
            log.info("urlHealth:{}", urlHealth);
            Registration.RegCheck check = Registration.RegCheck.http(
                    urlHealth,10);

            Registration service = ImmutableRegistration.builder()
                    .id(serviceId)
                    .name(serviceName)
                    .address(address)
                    .port(port)
                    .check(check)
                    .build();

            consul.agentClient().register(service);
            log.info("Registered service with Consul: {}", serviceId);
        } catch (Exception e) {
            log.error(Common.EXCEPTION,e);
        }
    }
}

