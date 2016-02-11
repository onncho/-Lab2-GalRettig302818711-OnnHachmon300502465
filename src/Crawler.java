import java.util.LinkedList;

public class Crawler {

	ThreadPoolV1 m_threadPool;
	SynchronizedQueueLL m_UrlsQueueToDownload;
	SynchronizedQueueLL m_HtmlsQueueToAnalyze;
	int m_NumOfDownloaders;
	int m_NumOfAnalyzers;
	String m_DomainToCrawl;
	Downloader m_Downloader;
	
	LinkedList<LinkReport> m_Reports;
	CrawlerReport m_FinalReport;

	// TODO: Need to receive parameters 
	public Crawler(String i_DomainToCrawl) {
		// TODO Auto-generated constructor stub
		m_Reports = new LinkedList<>();
		m_NumOfDownloaders = Integer.parseInt(ConfigurationObject.getMaxDownloaders());
		m_NumOfAnalyzers = Integer.parseInt(ConfigurationObject.getMaxAnalyzers());
		m_UrlsQueueToDownload = new SynchronizedQueueLL();
		m_HtmlsQueueToAnalyze = new SynchronizedQueueLL();
		m_threadPool = new ThreadPoolV1(m_UrlsQueueToDownload, m_NumOfDownloaders, 
				m_HtmlsQueueToAnalyze, m_NumOfAnalyzers);
		
		startCrawling();
		
	}

	private void startCrawling() {
		// TODO Auto-generated method stub
		
		// if port scan V -> DO First
		
		// if robots -> do something 
		
		m_Downloader = new Downloader(m_threadPool, m_DomainToCrawl);
		m_threadPool.putTaskInDownloaderQueue((Runnable) m_Downloader);
		
		// wait for the analyzers to finish creating reports
		try {
			m_Reports.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// analysis ends 
		createFinalReport();
	}

	private void createFinalReport() {
		// reports before process
		m_FinalReport = new CrawlerReport(false, m_DomainToCrawl);
		m_FinalReport.reportsToProcess(m_Reports);
		
		// Show Results on Html
	}

}
