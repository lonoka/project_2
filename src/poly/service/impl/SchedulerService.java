package poly.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import poly.service.ICommuService;
import poly.service.ISchedulerService;

@Component
@Service("SchedulerService")
public class SchedulerService implements ISchedulerService {

	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "CommuService")
	private ICommuService commuService;
	
	@Scheduled(cron = "0 0 0/1 * * ?")
	@Override
	public void sCrawlling() throws Exception {
		
		log.info(this.getClass().getName() + " test!!!");
		commuService.collectDcComData();
	}

}
