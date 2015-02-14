package models;

import models.dao.GenericDAO;

import org.junit.Test;

import test.AbstractTest;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class SeasonTest extends AbstractTest {
    GenericDAO dao = new GenericDAO();
    List<Season> seasons;

    @Test
    public void mustStartWithNoSeason() throws Exception {
        seasons = dao.findAllByClass(Season.class);
        assertThat(seasons).isEmpty();
    }

    @Test
    public void mustAddEpisodeToDB() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        seasons = dao.findAllByClass(Season.class);
        assertThat(seasons.size()).isEqualTo(1);
        assertThat(seasons.get(0)).isEqualTo(season);
    }
    
    @Test
    public void mustShowCorrectNextEpisode(){
    	Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode1 = new Episode(1, "Primeiro epi", season);
        Episode episode2 = new Episode(2, "Segundo epi", season);
        Episode episode3 = new Episode(3, "Terceiro epi", season);
        Episode episode4 = new Episode(4, "Quarto epi", season);
        Episode episode5 = new Episode(5, "Quinto epi", season);
        
        season.addEpisode(episode1);
        season.addEpisode(episode2);
        season.addEpisode(episode3);
        season.addEpisode(episode4);
        season.addEpisode(episode5);
        season.setNextEpisodeMode(0); //by last watched
        series.addSeason(season);
        dao.persist(series);

        seasons = dao.findAllByClass(Season.class);
        assertThat(seasons.get(0).nextEpisode()).isEqualTo(episode1);
        assertThat(seasons.get(0).getNextEpisodeMode()).isEqualTo(0);
 
        seasons.get(0).getEpisodes().get(0).setWatched();
        assertThat(seasons.get(0).getEpisodes().get(0).getName()).isEqualTo("Primeiro epi");
        
        assertThat(seasons.get(0).nextEpisode().getName()).isEqualTo(episode2.getName());
        
        season.setNextEpisodeMode(1); //by episode
        seasons.get(0).getEpisodes().get(2).setWatched();
        assertThat(seasons.get(0).nextEpisode()).isEqualTo(episode4);
        
        season.setNextEpisodeMode(-1); //by three
        seasons.get(0).getEpisodes().get(3).setWatched();
        seasons.get(0).getEpisodes().get(4).setWatched();
        assertThat(seasons.get(0).nextEpisode().getName()).isEqualTo("Três episódios foram assistidos fora de ordem");
   }
}