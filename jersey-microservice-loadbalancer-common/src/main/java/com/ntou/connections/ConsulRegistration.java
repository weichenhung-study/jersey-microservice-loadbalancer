//package com.ntou.connections;
//
//import com.ntou.tool.Common;
//import com.orbitz.consul.Consul;
//import com.orbitz.consul.model.agent.ImmutableRegistration;
//import com.orbitz.consul.model.agent.Registration;
//import lombok.extern.log4j.Log4j2;
//
//import java.net.InetAddress;
//import java.util.UUID;
//
//@Log4j2
//public class ConsulRegistration {
//
//    public static void registerService(String serviceName, int port) {
//        try {
//            String host = InetAddress.getLocalHost().getHostAddress(); // 容器內部 IP
//            String serviceId = serviceName + "-" + UUID.randomUUID();  // 唯一 ID
//
//            Consul consul = Consul.builder().withUrl("http://host.docker.internal:8500").build(); // 如果 Consul 跑在宿主機
//            // 若你之後也把 Consul 放進 docker-compose，可改用 http://consul:8500
//
//            Registration service = ImmutableRegistration.builder()
//                    .id(serviceId)
//                    .name(serviceName) // 所有 instance 同樣的 serviceName
//                    .port(port)
//                    .address(host) // Consul 透過此 IP + port 存取你的服務
//                    .check(Registration.RegCheck.http("http://" + host + ":" + port + "/health", 10)) // 健康檢查
//                    .build();
//
//            consul.agentClient().register(service);
//
//            log.info("Registered {} on port {} to Consul as {}", serviceName, port, serviceId);
//        } catch (Exception e) {
//            log.error(Common.EXCEPTION,e);
//        }
//    }
//}
