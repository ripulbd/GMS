package mir.gla.ac.uk.gms;
import java.util.ArrayList;

/**
 * This class represents a comment. Each comment should have a name of the commenter, the timestamp representing when it's been created 
 * and the body of the comment.
 * Many news sites allow users to up vote or down vote a comment represented by the upVote and downVote variables.
 * Each comment may have a list of replies, each reply is represented with a comment object. 
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
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
