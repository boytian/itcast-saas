package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryDao factoryDao;

    public void save(Factory factory) {

    }

    public void update(Factory factory) {

    }

    public void delete(String id) {

    }

    public Factory findById(String id) {
        return null;
    }

    public List<Factory> findAll(FactoryExample example) {
        return factoryDao.selectByExample(example);
    }
}