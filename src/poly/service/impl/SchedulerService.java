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
	
	

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sDcComCrawlling() throws Exception {
		commuService.collectDcComData();
		commuService.AnalysisData("DcCom_");
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sSlrCrawlling() throws Exception {
		commuService.collectSlrData();
		commuService.AnalysisData("Slr_");
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sPpomCrawlling() throws Exception {
		commuService.collectPpomData();
		commuService.AnalysisData("Ppom_");
		
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void s82CookCrawlling() throws Exception {
		commuService.collect82CookData();
		commuService.AnalysisData("82Cook_");
		
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sMlbCrawlling() throws Exception {
		commuService.collectMPData();
		commuService.AnalysisData("Mlb_");
		
	}

}
