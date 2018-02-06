package school.management.admin.common.config.druid.dynamic.tabswitch.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import school.management.db.datasource.tabswitch.DynamicSwitchDataSourceGlobal;
import school.management.db.datasource.tabswitch.DynamicSwitchDataSourceHolder;
import school.management.db.datasource.tabswitch.annotation.DataSource;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(school.management.db.datasource.tabswitch.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource ds = method.getAnnotation(DataSource.class);
        if(ds == null){
            DynamicSwitchDataSourceHolder.putDataSource(DynamicSwitchDataSourceGlobal.MANAGEMENT);
            logger.debug("set datasource is " + DynamicSwitchDataSourceGlobal.MANAGEMENT.name());
        }else {
            DynamicSwitchDataSourceHolder.putDataSource(DynamicSwitchDataSourceGlobal.getByName(ds.name()));
            logger.debug("set datasource is " + ds.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicSwitchDataSourceHolder.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}