package mir.gla.ac.uk.gms;

/**
 * This class implements an image web page
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class GMSImageDocument extends GMSDocument {
	
	private String owner, ownerURL;
	
	private String[] images, videos;
	
	public GMSImageDocument(String URL){
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
