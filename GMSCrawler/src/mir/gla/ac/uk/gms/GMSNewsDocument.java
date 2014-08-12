package mir.gla.ac.uk.gms;

import java.util.ArrayList;
import java.util.HashMap;

public class GMSNewsDocument extends GMSDocument {
	
	private String mainStory;	
	private ArrayList<HashMap<String, String>> imageNameCaption, videoNameCaption;	
	private ArrayList<HashMap<String, String>> relatedStories;
	
	public GMSNewsDocument(String URL){
		super(URL);
	}
	
	public ArrayList<HashMap<String, String>> getRelatedStories() {
		return relatedStories;
	}

	public void setRelatedStories(ArrayList<HashMap<String, String>> relatedStories) {
		this.relatedStories = relatedStories;
	}
	
	public String getMainStory() {
		return mainStory;
	}

	public void setMainStory(String mainStory) {
		this.mainStory = mainStory;
	}

	public ArrayList<HashMap<String, String>> getImageNameCaption() {
		return imageNameCaption;
	}

	public void setImageNameCaption(
			ArrayList<HashMap<String, String>> imageNameCaption) {
		this.imageNameCaption = imageNameCaption;
	}

	public ArrayList<HashMap<String, String>> getVideoNameCaption() {
		return videoNameCaption;
	}

	public void setVideoNameCaption(
			ArrayList<HashMap<String, String>> videoNameCaption) {
		this.videoNameCaption = videoNameCaption;
	}
	
	@Override
	public String toString(){
		String returnString = "";
		returnString += "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";
		returnString += "++Title:" + title + "\n";
		returnString += "++TimeStamp:" + timeStamp + "\n";
		returnString += "++Description:" + description + "\n";
		returnString += "++Main Story:\n++---------------\n" + mainStory + "\n++-----------------\n";
		returnString += "++Title:" + title + "\n";
		if(imageNameCaption.size()> 0){
			for(HashMap<String, String> tmpMap : imageNameCaption){
				returnString += "++Image Name:" + tmpMap.get("name") + "\n";
				returnString += "++Caption:" + tmpMap.get("caption") + "\n";
			}
		}
		if(videoNameCaption != null && videoNameCaption.size()> 0){
			for(HashMap<String, String> tmpMap : imageNameCaption){
				returnString += "++Image Name:" + tmpMap.get("name") + "\n";
				returnString += "++Caption:" + tmpMap.get("caption") + "\n";
			}
		}
		if(videoNameCaption != null && relatedStories.size()> 0){
			for(HashMap<String, String> tmpMap : imageNameCaption){
				returnString += "++URL:" + tmpMap.get("url") + "\n";
				returnString += "++Title:" + tmpMap.get("title") + "\n";
			}
		}
		if(comments != null && comments.size()> 0){
			for(CommentDocument commentDoc : comments){
				returnString += commentDoc.toString();
			}
		}
		
		returnString += "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";
		
		return returnString;
	}

}
