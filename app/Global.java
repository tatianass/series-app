import models.Episode;
import models.Season;
import models.Series;
import models.dao.GenericDAO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gustavo on 06/12/14.
 */
public class Global extends GlobalSettings {
    private final GenericDAO dao = new GenericDAO();

    @Override
    public void onStart(Application app) {
        Logger.info("Using global.java");
        JPA.withTransaction(() -> populateDB());
    }

    @Override
    public void onStop(Application app){
        Logger.info("Removing Itens of DB");
        JPA.withTransaction(() -> {
            List<Series> seriesList = dao.findAllByClass(Series.class);
            for (Series series : seriesList) {
                dao.removeById(Series.class, series.getId());
            }
        });
    }

    private void populateDB() throws Exception {
        String file = Play.application().getFile("/conf/seriesFinalFile.csv").getAbsolutePath();
        BufferedReader br;
        String line = "";
        Logger.info("Populing DB");

        Series series = new Series("South Park");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Cartman Gets an Anal Probe", season);

        season.addEpisode(episode);
        series.addSeason(season);

        try {
            br = new BufferedReader(new FileReader(file));
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] lineData = line.split(",");

                for (int i = 0; i < lineData.length; i++) {
                    lineData[i] = lineData[i].substring(1, lineData[i].length() - 1);
                }

                String seriesName = lineData[0];
                int seasonNumber = Integer.parseInt(lineData[1]);
                int episodeNumber = Integer.parseInt(lineData[2]);
                String episodeName = (lineData.length == 4) ? lineData[3] : "Sem Título";

                episode = new Episode(episodeNumber, episodeName, season);

                if (seriesName.equals(series.getName())) {

                    if (seasonNumber == season.getNumber()) {
                        season.addEpisode(episode);

                    } else {
                        series.addSeason(season);

                        season = new Season(seasonNumber, series);
                        season.addEpisode(episode);
                    }
                } else {
                    Logger.debug("Persisted " + series.getName());
                    dao.persist(series);

                    series = new Series(seriesName);
                    season = new Season(seasonNumber, series);

                    season.addEpisode(episode);
                    series.addSeason(season);
                }
            }
            Logger.debug("Persisted " + series.getName());
            dao.persist(series);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
        dao.flush();
    }
}
