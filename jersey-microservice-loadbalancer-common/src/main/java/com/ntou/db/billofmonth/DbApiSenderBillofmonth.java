package com.ntou.db.billofmonth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntou.connections.OkHttpServiceClient;
import com.ntou.tool.Common;
import com.ntou.tool.JsonTool;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Log4j2
public class DbApiSenderBillofmonth {
    private String dbServiceUrl;

    public DbApiSenderBillofmonth() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("找不到 config.properties");
            }
            Properties prop = new Properties();
            prop.load(input);
            this.dbServiceUrl = prop.getProperty("jdbc.service.url.billofmonth");
        } catch (Exception e) {
            log.error(Common.EXCEPTION,e);
        }
    }

    public List<BillofmonthVO> findCusBill(OkHttpServiceClient svc, BillofmonthVO vo) throws JsonProcessingException {
        String str = svc.getService(dbServiceUrl
                + "FindCusBill?cid=" + vo.getCid() + "&cardType=" + vo.getCardType() + "&payDate=" + vo.getPayDate());
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        List<BillofmonthVO> output = new ObjectMapper().readValue(nodeResult.toString(), new TypeReference<>() {});
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String insertBill(OkHttpServiceClient svc, BillofmonthVO vo) throws JsonProcessingException {
        String str = svc.postService(dbServiceUrl + "InsertBill", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updatePayDate(OkHttpServiceClient svc, BillofmonthVO vo) throws JsonProcessingException {
        String str = svc.putService(dbServiceUrl + "UpdatePayDate", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
}
