package com.ntou.db.cuscredit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ntou.connections.OkHttpServiceClient;
import com.ntou.tool.Common;
import com.ntou.tool.JsonTool;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class DbApiSenderCuscredit {
    private String dbServiceUrl;

    public DbApiSenderCuscredit() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("找不到 config.properties");
            }
            Properties prop = new Properties();
            prop.load(input);
            this.dbServiceUrl = prop.getProperty("jdbc.service.url.cuscredit");
        } catch (Exception e) {
            log.error(Common.EXCEPTION,e);
        }
    }

    public CuscreditVO getCardHolder(OkHttpServiceClient cuscreditSvc,String cid, String cardType) throws JsonProcessingException {
        String str = cuscreditSvc.getService(dbServiceUrl + "GetCardHolder?cid=" + cid + "&cardType=" + cardType);
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        CuscreditVO output = JsonTool.readValue(nodeResult.toString(),CuscreditVO.class);
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public CuscreditVO getActivatedCardHolder(OkHttpServiceClient cuscreditSvc,String cid, String cardType, String cardNum, String securityCode) throws JsonProcessingException {
        String str = cuscreditSvc.getService(dbServiceUrl + "GetActivatedCardHolder?cid=" + cid + "&cardType=" + cardType + "&cardNum=" + cardNum + "&securityCode=" + securityCode);
        log.info(Common.RESULT + str);
        JsonNode nodeReadTree = JsonTool.readTree(str);
        JsonNode nodeResult = nodeReadTree.get("result");
        if(nodeResult == null)
            return null;
        CuscreditVO output = JsonTool.readValue(nodeResult.toString(),CuscreditVO.class);
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updateActivationRecord(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.putService(dbServiceUrl + "UpdateActivationRecord", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String updateCardApprovalStatus(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.putService(dbServiceUrl + "UpdateCardApprovalStatus", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
    public String createCuscredit(OkHttpServiceClient cuscreditSvc, CuscreditVO vo) throws JsonProcessingException {
        String str = cuscreditSvc.postService(dbServiceUrl + "CreateCuscredit", vo);
        log.info(Common.RESULT + str);
        JsonNode nodeUpdateResult = JsonTool.readTree(str);
        String output = nodeUpdateResult.get("resCode").asText();
        log.info(Common.NODE_RESULT + output);
        return output;
    }
}
