package com.lukaszbaran.starter.dao.impl;

import com.lukaszbaran.starter.api.Camera;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dao-context.xml" })
//@Transactional(propagation = Propagation.REQUIRED)
public class CameraDAOImplTest {

    @Resource(name = "cameraDAO")
    private CameraDAO cameraDAO;

    @Test
    public void shouldGetAll() {
        List<Camera> list = cameraDAO.getAll();

//        Campaign campaign = new Campaign();
//        campaign.setId("test");
//        campaign.setOptInId("testOptInId");
//        campaign.setDescription("test");
//        dao.add(campaign);
//        assertEquals("test",dao.getCampaignIdByOptInId("testOptInId"));
    }
}