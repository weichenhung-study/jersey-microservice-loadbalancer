package com.ntou.creditcard.billing.feepayment;

import com.ntou.connections.OkHttpServiceClient;
import com.ntou.db.billofmonth.BillofmonthVO;
import com.ntou.db.billofmonth.DbApiSenderBillofmonth;
import com.ntou.db.cuscredit.CuscreditVO;
import com.ntou.db.cuscredit.DbApiSenderCuscredit;
import com.ntou.sysintegrat.mailserver.JavaMail;
import com.ntou.sysintegrat.mailserver.MailVO;
import com.ntou.tool.Common;
import com.ntou.tool.DateTool;
import com.ntou.tool.ExecutionTimer;
import com.ntou.tool.ResTool;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.core.Response;
import java.util.List;

/** 客戶繳交信用卡費 */
@Log4j2
public class FeePayment {
    private final OkHttpServiceClient okHttpServiceClient = new OkHttpServiceClient();
    DbApiSenderCuscredit dbApiSenderCuscredit = new DbApiSenderCuscredit();
    DbApiSenderBillofmonth dbApiSenderBillofmonth = new DbApiSenderBillofmonth();

    public Response doAPI(FeePaymentReq req) throws Exception {
        ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());

		log.info(Common.API_DIVIDER + Common.START_B + Common.API_DIVIDER);
        log.info(Common.REQ + req);
        FeePaymentRes res = new FeePaymentRes();

        if(!req.checkReq())
            ResTool.regularThrow(res, FeePaymentRC.T171A.getCode(), FeePaymentRC.T171A.getContent(), req.getErrMsg());
		
		ExecutionTimer.startStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        BillofmonthVO vo = setUpdatePayDate(req);
        List<BillofmonthVO> listBillofmonth = dbApiSenderBillofmonth.findCusBill(okHttpServiceClient, vo);
        if (listBillofmonth != null && listBillofmonth.size() == 1) {
            int notPaidAmount = Integer.parseInt(listBillofmonth.get(0).getAmt()) - Integer.parseInt(req.getPayAmt());
            vo.setNotPaidAmount(String.valueOf(notPaidAmount));

            String updateCount = dbApiSenderBillofmonth.updatePayDate(okHttpServiceClient, vo);
            if(!updateCount.equals("InsertBill00"))
                ResTool.commonThrow(res, FeePaymentRC.T171C.getCode(), FeePaymentRC.T171C.getContent());
            sendMail(req, listBillofmonth.get(0));
        } else
            ResTool.commonThrow(res, FeePaymentRC.T171D.getCode(), FeePaymentRC.T171D.getContent());
		
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.DATA_INTERFACE.getValue());
        ResTool.setRes(res, FeePaymentRC.T1710.getCode(), FeePaymentRC.T1710.getContent());

        log.info(Common.RES + res);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        
		ExecutionTimer.endStage(ExecutionTimer.ExecutionModule.APPLICATION.getValue());
        ExecutionTimer.exportTimings(this.getClass().getSimpleName() + "_" + DateTool.getYYYYmmDDhhMMss() + ".txt");
		return Response.status(Response.Status.CREATED).entity(res).build();
    }
    private void sendMail(FeePaymentReq req, BillofmonthVO key) throws Exception {
        MailVO vo = new MailVO();
//        CuscreditVO voCuscredit = cuscreditSvc.selectKey(req.getCid(), req.getCardType());
        CuscreditVO voCuscredit = dbApiSenderCuscredit.getCardHolder(okHttpServiceClient, req.getCid(), req.getCardType());
        vo.setEmailAddr(voCuscredit.getEmail());
        vo.setSubject("信用卡繳費通知");
        vo.setContent("<h1>收到您的信用卡費</h1>" +
                "帳單月份：" + key.getBillMonth() +"<br>" +
                "當月帳單應繳金額：" + key.getAmt() +"<br>" +
                "當月繳款金額：" + key.getPaidAmount() +"<br>" +
                "剩餘未繳金額：" + key.getNotPaidAmount() +"<br>" +
                "繳款時間：" + key.getPayDate() +"<br>"
        );
        new JavaMail().sendMail(vo);
    }
    private BillofmonthVO setUpdatePayDate(FeePaymentReq req){
        BillofmonthVO vo = new BillofmonthVO();
        vo.setCid(req.getCid());
        vo.setCardType(req.getCardType());
        vo.setPayDate(req.getPayDate());
        vo.setPayAmt(req.getPayAmt());
        vo.setBillMonth(req.getPayDate());
        return vo;
    }
}
