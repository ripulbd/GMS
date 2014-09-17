package mir.gla.ac.uk.gms;

import java.io.File;
import java.util.ArrayList;

public class StatModule {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBUtils dbUtil = new DBUtils();
		long totalSize = dbUtil.totalSize();
		//System.out.println("Total Size in DB:" + totalSize + "Bytes, " + totalSize / 1024 + "KB, " + totalSize/(1024 * 1024) +"MB");
		System.out.printf("Total Size in DB: %d Bytes, %.2fKB, %.2fMB\n", totalSize, (double) totalSize / 1024, (double) totalSize / (1024 * 1024));
		
		long bbcSize = dbUtil.totalSize("http://www.bbc.co.uk");
		long dailyRecordSize = dbUtil.totalSize("http://www.dailyrecord.co.uk");
		long etSize = dbUtil.totalSize("Evening Times");
		long scotsmanSize = dbUtil.totalSize("http://www.scotsman.com");
		
		System.out.printf("Total Size of BBC in DB: %d Bytes, %.2fKB, %.2fMB\n", bbcSize, (double) bbcSize / 1024, (double) bbcSize / (1024 * 1024));
		System.out.printf("Total Size of Daily Record in DB: %d Bytes, %.2fKB, %.2fMB\n", dailyRecordSize, (double) dailyRecordSize / 1024, (double) dailyRecordSize / (1024 * 1024));
		System.out.printf("Total Size of ET in DB: %d Bytes, %.2fKB, %.2fMB\n", etSize, (double) etSize / 1024, (double) etSize / (1024 * 1024));
		System.out.printf("Total Size of Scotsman in DB: %d Bytes, %.2fKB, %.2fMB\n", scotsmanSize, (double) scotsmanSize / 1024, (double) scotsmanSize / (1024 * 1024));
		
		
		/*System.out.println("Total Size of BBC in DB:" + bbcSize + "Bytes, " + (double) bbcSize / 1024 + "KB, " + (double) bbcSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of Daily Record in DB:" + dailyRecordSize + "Bytes, " + dailyRecordSize / 1024 + "KB, " + dailyRecordSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of ET in DB:" + etSize + "Bytes, " + etSize / 1024 + "KB, " + etSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of Scotsman in DB:" + scotsmanSize + "Bytes, " + scotsmanSize / 1024 + "KB, " + scotsmanSize / (1024 * 1024) + "MB");*/
		System.out.println("--------------------------------------------------------------------------------");
		
		long bbcImageSize = folderSize(new File("/home/ripul/images/bbc/"));
		long dailyRecordImageSize = folderSize(new File("/home/ripul/images/dr/"));
		long etImageSize = folderSize(new File("/home/ripul/images/et/"));
		long scotsmanImageSize = folderSize(new File("/home/ripul/images/scotsman/"));
		
		bbcSize += bbcImageSize;
		dailyRecordSize += dailyRecordImageSize;
		etSize += etImageSize;
		scotsmanSize += scotsmanImageSize;
		
		System.out.printf("Total Size of BBC with images: %d Bytes, %.2fKB, %.2fMB\n", bbcSize, (double) bbcSize / 1024, (double) bbcSize / (1024 * 1024));
		System.out.printf("Total Size of Daily Record with images: %d Bytes, %.2fKB, %.2fMB\n", dailyRecordSize, (double) dailyRecordSize / 1024, (double) dailyRecordSize / (1024 * 1024));
		System.out.printf("Total Size of ET with images: %d Bytes, %.2fKB, %.2fMB\n", etSize, (double) etSize / 1024, (double) etSize / (1024 * 1024));
		System.out.printf("Total Size of Scotsman with images: %d Bytes, %.2fKB, %.2fMB\n", scotsmanSize, (double) scotsmanSize / 1024, (double) scotsmanSize / (1024 * 1024));
		
		/*System.out.println("Total Size of BBC with images:" + bbcSize + "Bytes, " + bbcSize / 1024 + "KB, " + bbcSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of Daily Record with images:" + dailyRecordSize + "Bytes, " + dailyRecordSize / 1024 + "KB, " + dailyRecordSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of ET with images:" + etSize + "Bytes, " + etSize / 1024 + "KB, " + etSize / (1024 * 1024) + "MB");
		System.out.println("Total Size of Scotsman with images:" + scotsmanSize + "Bytes, " + scotsmanSize / 1024 + "KB, " + scotsmanSize / (1024 * 1024) + "MB");*/
		
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("Total Number:" + dbUtil.totalNumber());
		System.out.println("Total Number BBC:" + dbUtil.totalNumber("http://www.bbc.co.uk"));
		System.out.println("Total Number Daily Record:" + dbUtil.totalNumber("http://www.dailyrecord.co.uk"));
		System.out.println("Total Number ET:" + dbUtil.totalNumber("Evening Times"));
		System.out.println("Total Number Scotsman:" + dbUtil.totalNumber("http://www.scotsman.com"));
		
		//String URL = "http://www.scotsman.com/news/scotland/top-stories/scottish-house-prices-put-homes-out-of-reach-1-3516207";
		String URL = "http://www.dailyrecord.co.uk/entertainment/celebrity/glasgow-sons-anarchy-star-tommy-4256082";
		long elementSize = dbUtil.totalSizeEachElement(URL);
		ArrayList<String> imageList = dbUtil.imageName(URL);
		
		long imageSize = imageSize(imageList);
		System.out.println("--------------------------------------------------------------------------------");
		System.out.printf("Total Size of the element, without images: %d Bytes, %.2fKB, %.2fMB, with images: %d Bytes, %.2fKB, %.2fMB\n", elementSize, (double) elementSize / 1024, (double) elementSize / (1024 * 1024), (elementSize + imageSize), (double) (elementSize + imageSize) / 1024, (double) (elementSize + imageSize) / (1024 * 1024));
		System.out.println("--------------------------------------------------------------------------------");
		String date = "16/09/2014";
		
		long numberPerDate = dbUtil.totalNumberEachDate(date);
		
		System.out.printf("Total Number of news on %s is: %d\n", date, numberPerDate);
		
		String scotsman = "http://www.scotsman.com";
		String et = "Evening Times";
		String bbc = "http://www.bbc.co.uk";
		String dr = "http://www.dailyrecord.co.uk";
		
		long scotDateNumber = dbUtil.totalNumberEachDate(date, scotsman);
		long etDateNumber = dbUtil.totalNumberEachDate(date, et);
		long bbcDateNumber = dbUtil.totalNumberEachDate(date, bbc);
		long drDateNumber = dbUtil.totalNumberEachDate(date, dr);
		
		System.out.printf("Total Number of news of %s on %s is: %d\n", scotsman, date, scotDateNumber);
		System.out.printf("Total Number of news of %s on %s is: %d\n", et, date, etDateNumber);
		System.out.printf("Total Number of news of %s on %s is: %d\n", bbc, date, bbcDateNumber);
		System.out.printf("Total Number of news of %s on %s is: %d\n", dr, date, drDateNumber);
		
		//http://www.scotsman.com/news/scotland/top-stories/scottish-house-prices-put-homes-out-of-reach-1-3516207
		
	}
	
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
	
	public static long imageSize(ArrayList<String> imageList) {
	    long length = 0;
	    for(String image : imageList){
	    	File file = new File("/home/ripul/resources/images/" + image);
	    	length += file.length();
	    }
	    return length;
	}

}
