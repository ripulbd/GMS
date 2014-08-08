package mir.gla.ac.uk.gms;
import java.util.ArrayList;

public class CommentDocument {
	private String userName, timeStamp, commentBody;
	private int upVote, downVote;
	private ArrayList<CommentDocument> replies;
	
	public CommentDocument(){
		userName = "";
		timeStamp = "";
		commentBody = "";
		replies = new ArrayList<CommentDocument>();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timestamp) {
		this.timeStamp = timestamp;
	}
	public String getCommentBody() {
		return commentBody;
	}
	public void setCommentBody(String commentBody) {
		this.commentBody = commentBody;
	}
	public int getUpVote() {
		return upVote;
	}
	public void setUpVote(int upVote) {
		this.upVote = upVote;
	}
	public int getDownVote() {
		return downVote;
	}
	public void setDownVote(int downVote) {
		this.downVote = downVote;
	}
	public ArrayList<CommentDocument> getReplies() {
		return replies;
	}
	public void setReplies(ArrayList<CommentDocument> replies) {
		this.replies = replies;
	}
	
	@Override
	public String toString(){
		String returnString = "[++======================================================================================\n";
		returnString = returnString + "++UserName:" + userName + ", TimeStamp:" + timeStamp +", UpVote:" + upVote + ", DownVote:" + downVote + "\n";
		returnString += "++Main Comment:\n";
		returnString += "++---------------------------------------\n";
		returnString += "++" + commentBody + "\n";
		returnString += "++---------------------------------------\n";
		if(replies.size() > 0){
			returnString += "++This comment has the following replies:\n";
			for(CommentDocument comment: replies){
				returnString += "++++++++++++++++++++++++++++++++++++++++\n";
				returnString += "UserName:" + comment.getUserName() + ", TimeStamp:" + comment.getTimeStamp() + ", UpVote:" + comment.getUpVote() + ", DownVote:" + comment.getDownVote() + "\n";
				returnString += "Reply Comment:\n";
				returnString += "++---------------------------------------\n";
				returnString += comment.getCommentBody() + "\n";
				returnString += "++---------------------------------------\n";
			}
		}
		returnString += "++======================================================================================]\n";
		return returnString;
	}
}
