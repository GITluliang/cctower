package com.nuoze.cctower.service.applet;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.nuoze.cctower.pojo.dto.WalletDTO;
import com.nuoze.cctower.pojo.vo.TopUpRecordVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-01-19 00:49
 */
public interface WalletService {
    /**
     * 钱包充值
     * @param dto
     * @param request
     * @return
     */
    WxPayMpOrderResult walletPrepay(WalletDTO dto, HttpServletRequest request);

    /**
     * 消费记录
     *
     * @param map
     * @return
     */
    List<TopUpRecordVO> recordList(Map<String, Object> map);
}
