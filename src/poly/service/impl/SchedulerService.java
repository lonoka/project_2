package poly.service.impl;

import java.util.ArrayList;
import java.util.List;

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
		commuService.collectDcComData();
		commuService.collectSlrData();
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		sList.add("Slr_");
		for(int i = 0; i <sList.size();i++) {
			commuService.AnalysisData(sList.get(i));
		}
	}

}
