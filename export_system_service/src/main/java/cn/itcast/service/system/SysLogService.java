package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * @Author: TianTian
 * @Date: 2020/3/20 20:16
 */

public interface SysLogService {

    PageInfo findAll(String compayId, int page, int size);

    void save(SysLog log);
}
