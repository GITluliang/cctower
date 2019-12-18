package com.nuoze.cctower.rest.mobile;

import com.nuoze.cctower.common.result.ResponseResult;
import com.nuoze.cctower.common.result.Result;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.component.MqSendComponent;
import com.nuoze.cctower.pojo.entity.Parking;
import com.nuoze.cctower.pojo.entity.Passageway;
import com.nuoze.cctower.pojo.vo.PassagewayVO;
import com.nuoze.cctower.service.ParkingService;
import com.nuoze.cctower.service.PassagewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.nuoze.cctower.common.util.ShiroUtils.getUserId;

/**
 * @Author luliang
 * @Date 2019-12-18 9:51
 */
@Controller
@RequestMapping("/mobile/passageway")
public class MobilePassagewayController {
    private String prefix = "h5/mobile/passageway/";

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private MqSendComponent mqSendComponent;
    @Autowired
    private PassagewayService passagewayService;
    @RequestMapping()
    public String passageway() {
        return prefix + "listPassageway";
    }

    @ResponseBody
    @RequestMapping("authz")
    public Result carLongAuthZ() {
        List<Parking> parkingByUser = parkingService.findParkingByUser(getUserId());
        return parkingByUser != null ? (parkingByUser.get(0).getPassagewayStatic() == 1 ? ResponseResult.success() : ResponseResult.fail(401, "没有权限")) : ResponseResult.fail(401, "没有权限");
    }

    /**
     * 删除
     */
    @PostMapping("/openPassageway")
    @ResponseBody
    public R remove(Long id) {
        Passageway byId = passagewayService.findById(id);
        if (byId != null) {
             mqSendComponent.sendOpenPassageway(byId.getParkingId(),new PassagewayVO().setIp(byId.getIp()).setType(byId.getType()).setParkingId(byId.getParkingId()));
        }
        return R.ok();
    }
}
