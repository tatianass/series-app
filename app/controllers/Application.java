package controllers;

import static play.data.Form.form;
import models.Episode;
import models.Season;
import models.Series;
import models.dao.GenericDAO;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;
import views.html.index;

import java.util.List;

public class Application extends Controller {
	static final GenericDAO DAO = new GenericDAO();
	private static Form<Series> serieForm = form(Series.class);

	@Transactional
	public static Result index() {
		return redirect(routes.Application.series());
	}

	@Transactional
	public static Result series() {
		List<Series> seriesList = DAO.findByAttributeName("Series", "watched",
				"false");
		List<Series> watchedSeriesList = DAO.findByAttributeName("Series",
				"watched", "true");
		return ok(index.render(seriesList, watchedSeriesList));
	}

	@Transactional
	public static Result watchSeries(long id) {
		Series series = DAO.findByEntityId(Series.class, id);
		series.setWatched();
		DAO.merge(series);
		return redirect(routes.Application.series());
	}

	@Transactional
	public static Result watchEpisode(long idEpisode) {
		Episode episode = DAO.findByEntityId(Episode.class, idEpisode);
		episode.setWatched();

		DAO.merge(episode);
		return redirect(routes.Application.series());
	}
	
	@Transactional
	public static Result chageNextEpisode(long idSeason) {

		Form<Series> filledForm = serieForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			List<Series> seriesList = DAO.findByAttributeName("Series", "watched",
					"false");
			List<Series> watchedSeriesList = DAO.findByAttributeName("Series",
					"watched", "true");
			return badRequest(index.render(seriesList, watchedSeriesList));
		} else {
			Season season = DAO.findByEntityId(Season.class, idSeason);
			
			season.setNextEpisodeMode(Integer.parseInt(filledForm.field("nextEpisode").value()));
			
			Logger.debug(filledForm.field("nextEpisode").value() + season.getNextEpisodeMode());
			
			DAO.merge(season);
			DAO.flush();
			return redirect(routes.Application.index());
		}

	}

}
