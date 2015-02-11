package models;

import javax.persistence.*;

/**
 * Created by gustavo on 05/12/14.
 */
@Entity(name="Episode")
public class Episode {
    @Id
    @GeneratedValue
    @Column
    private long id;
    @ManyToOne(cascade=CascadeType.ALL)
    private Season season;
    @Column
    private int number;
    @Column
    private String name;
    @Column
    private boolean watched;
    @Column
    private final int PRIMO_HASHCODE = 31;

    public Episode() {
        this.watched = false;
    }

    public Episode(int number, String name, Season season) {
        this();
        this.number = number;
        this.name = name;
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched() {
        this.watched = true;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Episode episode = (Episode) o;

        if (number != episode.number) {
        	return false;
        }
        if (watched != episode.watched) {
        	return false;
        }
        if (!name.equals(episode.name)) {
        	return false;
        }
        if (!season.equals(episode.season)) {
        	return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = season.hashCode();
        result = PRIMO_HASHCODE * result + number;
        result = PRIMO_HASHCODE * result + name.hashCode();
        result = PRIMO_HASHCODE * result + (watched ? 1 : 0);
        return result;
    }
}
