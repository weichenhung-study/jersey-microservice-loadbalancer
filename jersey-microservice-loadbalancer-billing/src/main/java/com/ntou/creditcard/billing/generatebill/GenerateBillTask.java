package com.ntou.creditcard.billing.generatebill;

import com.ntou.connections.OkHttpServiceClient;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import javax.ws.rs.core.Response;

@Log4j2
public class GenerateBillTask implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        try {
            log.info("GenerateBillTask 執行中");
            try (Response response = new GenerateBill(new OkHttpServiceClient()).doAPI()) {}

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
