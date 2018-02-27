package school.management.admin.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import school.management.admin.modules.business.businesshelp.service.BusinessHelpAdminServiceImpl;
import school.management.business.businesshelp.entity.BusinessHeadlines;
import school.management.business.visa.entity.VisaCombo;
import school.management.business.visa.service.VisaComboServiceImpl;
import school.management.db.utils.PageUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestYou {

    @Autowired
    BusinessHelpAdminServiceImpl businessHelpAdminServiceImpl;

    @Autowired
    VisaComboServiceImpl visaComboServiceImpl;


    @Test
    public void businessHelp() {
        Map map = new HashMap();
        PageUtils<BusinessHeadlines> pageUtils = businessHelpAdminServiceImpl.headlinesQueryPageMap(map);
        System.out.println(pageUtils);
        for(BusinessHeadlines bl : pageUtils.getList()){
            System.out.println(bl);
        }
    }

    // 注意 测试时需要再 CrudService 上注解 @DataSource注解，切换数据源
    @Test
    public void visaCombo() {
        VisaCombo visaCombo = new VisaCombo();
        visaCombo.setVisaId(123);
        visaCombo.setComboNum(123);
        visaCombo.setName("333");
        visaCombo.setDescription("666");
        visaCombo.setMarketPrice(new BigDecimal(100));
        visaCombo.setPrice(new BigDecimal(100));
        System.out.println(visaComboServiceImpl.save(visaCombo));
    }



}
