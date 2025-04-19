package com.ntou.creditcard.transactions.transactionquery;

import com.ntou.connections.OkHttpServiceClient;
import com.ntou.db.billrecord.BillrecordVO;
import com.ntou.db.billrecord.DbApiSenderBillrecord;
import com.ntou.exceptions.TException;
import com.ntou.tool.Common;
import com.ntou.tool.DateTool;
import com.ntou.tool.ExecutionTimer;
import com.ntou.tool.ResTool;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.ntou.creditcard.transactions.transactionquery.TransactionQueryRC.*;

/** 消費紀錄區間查詢 */
@Log4j2
public class TransactionQuery {
    private final OkHttpServiceClient okHttpServiceClient = new OkHttpServiceClient();
    DbApiSenderBillrecord dbApiSenderBillrecord = new DbApiSenderBillrecord();

    public Response doAPI(TransactionQueryReq req) throws Exception {
        ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());

		log.info(Common.API_DIVIDER + Common.START_B + Common.API_DIVIDER);
        log.info(Common.REQ + req);
        TransactionQueryRes res = new TransactionQueryRes();

        if(!req.checkReq())
            ResTool.regularThrow(res, T161A.getCode(), T161A.getContent(), req.getErrMsg());
		
		ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        List<BillrecordVO> billList = dbApiSenderBillrecord.findCusBillAll(okHttpServiceClient, voBillrecordSelect(req), req.getStartDate(), req.getEndDate());
        if(billList == null || billList.isEmpty()) {
            ResTool.setRes(res, T161C.getCode(), T161C.getContent());
            throw new TException(res);
        }
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());

        ResTool.setRes(res, T1610.getCode(), T1610.getContent());
        res.setResult(billList);

        log.info(Common.RES + res);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());
        ExecutionTimer.exportTimings(this.getClass().getSimpleName() + "_" + DateTool.getYYYYmmDDhhMMss() + ".txt");
		return Response.status(Response.Status.OK).entity(res).build();
    }

    private BillrecordVO voBillrecordSelect(TransactionQueryReq req){
        BillrecordVO vo = new BillrecordVO();
        vo.setCid		(req.getCid());
        vo.setCardType	(req.getCardType());
        return vo;
    }
}
