package mir.gla.ac.uk.gms;

public class GMSVideoDocument extends GMSDocument {
	
	private String owner, ownerURL;
	
	private String[] images, videos;
	
	public GMSVideoDocument(String URL){
		super(URL);
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerURL() {
		return ownerURL;
	}

	public void setOwnerURL(String ownerURL) {
		this.ownerURL = ownerURL;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public String[] getVideos() {
		return videos;
	}

	public void setVideos(String[] videos) {
		this.videos = videos;
	}
}
