package models;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 05/12/14.
 */
@Entity(name = "Season")
public class Season {
	@Id
	@GeneratedValue
	@Column
	private long id;
	@Column
	private int number;
	@ManyToOne(cascade = CascadeType.ALL)
	private Series series;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<Episode> episodes;
	@Column
	private final int PRIMOHASHCODE = 31;

	@Column
	private final int BYEPISODE = 1;
	@Column
	private final int BYLASTWATCHED = 0;
	@Column
	private final int BYTHREE = -1;
	@Column
	private int nextEpisodeMode;

	public enum Status {
		FULL, INCOMPLETE, NONE
	}

	public Season() {
		episodes = new ArrayList<>();
	}

	public Season(int number, Series series) {
		this();
		this.number = number;
		this.series = series;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public void addEpisode(Episode episode) {
		episodes.add(episode);
	}

	public long getId() {
		return id;
	}

	public Status getStatus() {
		int cont = 0;
		for (Episode episode : episodes) {
			if (episode.isWatched()) {
				cont++;
			}
		}
		if (cont == 0) {
			return Status.NONE;
		} else if (cont == episodes.size()) {
			return Status.FULL;
		} else {
			return Status.INCOMPLETE;
		}
	}

	public Episode nextEpisode() {
		if (nextEpisodeMode == BYEPISODE) {
			return nextByEpisode();
		} else if (nextEpisodeMode == BYLASTWATCHED) {
			return nextByLastWatched();
		} else if (nextEpisodeMode == BYTHREE){
			return nextByThree();
		}

		return null;
	}
	
	private Episode nextByThree() {
		Episode ep = new Episode(0, "Três episódios foram assistidos fora de ordem", new Season());
		for (int i=0; i< episodes.size(); i++) {
			if (!episodes.get(i).isWatched()) {
				if(getStatus() == Status.INCOMPLETE) { //case episode is the last watched
					if(checkByThree(i+1)){ //case episode don't have three after him watched
						return episodes.get(i);
					}else{
						break; //left for and return ep
					}
				}
			}
		}
		return ep;
	}
	
	//check if others 3 are watched
	private boolean checkByThree(int indexEp){
		int count = 0;
		for(int i= indexEp; i<episodes.size(); i++){
			if (episodes.get(i).isWatched()) {
				count++;
			}
		}
		
		return count < 3;
	}

	private Episode nextByEpisode() {
		for (int i = episodes.size() - 1; i > -1; i--) {
			if (episodes.get(i).isWatched()) {
				if(getStatus() == Status.INCOMPLETE){ //case episode is the last watched
					return episodes.get(i+1);
				}
			}
		}
		return null;
	}

	private Episode nextByLastWatched() {
		for (Episode episode : episodes) {
			if (!episode.isWatched()) {
				return episode;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Season season = (Season) o;

		if (number != season.number) {
			return false;
		}
		if (!episodes.equals(season.episodes)) {
			return false;
		}
		if (!series.equals(season.series)) {
			return false;
		}

		return true;
	}

	public int getNextEpisodeMode() {
		return nextEpisodeMode;
	}

	public void setNextEpisodeMode(int nextEpisodeMode) {
		this.nextEpisodeMode = nextEpisodeMode;
	}

	@Override
	public int hashCode() {
		int result = number;
		result = PRIMOHASHCODE * result + series.hashCode();
		result = PRIMOHASHCODE * result + episodes.hashCode();
		return result;
	}
}
