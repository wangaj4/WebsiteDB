import java.util.ArrayList;

public class Film {

	private String title;

	private int year;

	private String id;

	private String director;

	private ArrayList<String> genres = new ArrayList<String>();

	public Film(){

	}

	public Film(String id, String title, int year, String director) {
		this.title = title;
		this.year = year;
		this.id  = id;
		this.director = director;
		
	}

	public void addGenre(String genre){
		genres.add(genre);
	}
	public ArrayList<String> getGenres(){return genres;}
	public String getID() {
		return this.id;
	}


	public String getTitle() {
		return title;
	}


	public int getYear() {
		return year;
	}

	public String getDirector() {return director;}

	public void setID(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDirector(String director) {
		this.director = director;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("Title:" + getTitle());
		sb.append(", ");
		sb.append("ID:" + getID());
		sb.append(", ");
		sb.append("Year:" + getYear());
		sb.append(", ");
		sb.append("Director:" + getDirector());
		sb.append(". ");
		sb.append("Genres: ");
		for(int i = 0; i < genres.size(); i++){
			sb.append(genres.get(i) + ", ");
		}
		
		return sb.toString();
	}
}
