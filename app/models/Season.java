package models;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 05/12/14.
 */
@Entity(name="Season")
public class Season {
    @Id
    @GeneratedValue
    @Column
    private long id;
    @Column
    private int number;
    @ManyToOne(cascade=CascadeType.ALL)
    private Series series;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn
    private List<Episode> episodes;
    @Column
    private final int PRIMOHASHCODE = 31;

    public enum Status {
        FULL,
        INCOMPLETE,
        NONE
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

    public Episode nextEpisode(){
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

    @Override
    public int hashCode() {
        int result = number;
        result = PRIMOHASHCODE * result + series.hashCode();
        result = PRIMOHASHCODE * result + episodes.hashCode();
        return result;
    }
}
