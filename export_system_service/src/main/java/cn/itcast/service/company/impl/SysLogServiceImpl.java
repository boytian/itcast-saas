package cn.itcast.service.company.impl;

import cn.itcast.dao.system.SysLogDao;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.company.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author: TianTian
 * @Date: 2020/3/20 20:16
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Resource
    private SysLogDao sysLogDao;


    public PageInfo findAll(String compayId, int page, int size) {
        PageHelper.startPage(page,size);
        List<SysLog> list = sysLogDao.findAll(compayId);
        return new PageInfo(list);
    }

    public void save(SysLog log) {
        log.setId(UUID.randomUUID().toString());
        sysLogDao.save(log);
    }
}
