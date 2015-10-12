package fr.genetic.server.web.controller;

import fr.genetic.server.game.Game;
import fr.genetic.server.game.RunBoard;
import fr.genetic.server.simulation.CarDefinition;
import fr.genetic.server.simulation.Simulation;
import fr.genetic.server.game.Team;
import fr.genetic.server.web.validator.CarViewListValidator;
import fr.genetic.server.web.view.CarScoreView;
import fr.genetic.server.web.view.CarView;
import fr.genetic.server.web.view.ChampionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SimulationController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CarViewListValidator carValidator;

    @RequestMapping(value="/simulation/evaluate/{team}", method = RequestMethod.POST)
    public List<CarScoreView> evaluatePopulation(@RequestBody List<CarView> carViews, @PathVariable("team") Team team) {
        carValidator.validate(carViews, team);

        List<CarDefinition> definitions = carViews.stream()
                .map(carDto -> carDto.toCarDefintion())
                .collect(Collectors.toList());

        RunBoard runBoard = Game.getRunBoard(team);

        synchronized (runBoard) {
            runBoard.runSimulation(definitions);
            sendChampion(team, runBoard);
            return runBoard.cars.stream()
                    .map(car -> CarScoreView.create(car))
                    .collect(Collectors.toList());
        }

    }

    private void sendChampion(Team team, RunBoard runBoard) {
        template.convertAndSend("/topic/champions", new ChampionView(team, runBoard));
    }

    @RequestMapping(value="/simulation/champions", method = RequestMethod.GET)
    public List<ChampionView> getChampions() {
        return Game.players().entrySet().stream()
                .map(entry -> new ChampionView(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/simulation/champions/{team}", method = RequestMethod.GET)
    public ChampionView getChampion(@PathVariable("team") Team team) {
        return new ChampionView(team, Game.getRunBoard(team));
    }

}
