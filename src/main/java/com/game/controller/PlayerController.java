package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception_handling.NoSuchPlayerException;
import com.game.exception_handling.NotEnoughDataException;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public List<Player> showAllPlayers(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "race", required = false) Race race,
            @RequestParam(name = "profession", required = false) Profession profession,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "banned", required = false) Boolean banned,
            @RequestParam(name = "minExperience", required = false) Integer minExperience,
            @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(name = "minLevel", required = false) Integer minLevel,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(name = "order", defaultValue = "ID") PlayerOrder order,
            @RequestParam(name = "pageSize", defaultValue = "3") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber) {
        List<Player> allPlayers = playerService.getAllPlayers(
                name,
                title,
                race,
                profession,
                after,
                before,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel,
                order,
                pageSize,
                pageNumber);
        return allPlayers;
    }

    @GetMapping("/players/count")
    public int getPlayersCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "race", required = false) Race race,
            @RequestParam(name = "profession", required = false) Profession profession,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "banned", required = false) Boolean banned,
            @RequestParam(name = "minExperience", required = false) Integer minExperience,
            @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(name = "minLevel", required = false) Integer minLevel,
            @RequestParam(name = "maxLevel", required = false) Integer maxLevel) {
        int count = playerService.getPlayersCount(
                name,
                title,
                race,
                profession,
                after,
                before,
                banned,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel);
        return count;
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        checkPlayerProperties(player, true);
        player.calcLevelAndUntilNextLevel();
        return playerService.addOrUpdatePlayer(player);
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable long id) {
        checkID(id);
        return playerService.getPlayer(id);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable long id, @RequestBody Player player) {
        checkID(id);
        checkPlayerProperties(player, false);
        System.out.println(player.getId());
        player.setId(id);
        return playerService.addOrUpdatePlayer(player);
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable long id) {
        checkID(id);
        playerService.deletePlayer(id);
    }

    private void checkPlayerProperties(Player player, boolean isCreate) throws NotEnoughDataException {
        if (isCreate) {
            if (player.getName() == null ||
                    player.getRace() == null ||
                    player.getTitle() == null ||
                    player.getProfession() == null ||
                    player.getBanned() == null ||
                    player.getExperience() == null ||
                    player.getBirthday() == null) throw new NotEnoughDataException("Not enough data");
        }
        if (player.getName() != null && player.getName().length() > 12) throw new NotEnoughDataException("Too long Name (MAX 12)");
        if (player.getTitle() != null && player.getTitle().length() > 30) throw new NotEnoughDataException("Too long Title (MAX 30)");
        if (player.getExperience() != null && (player.getExperience() < 0 || player.getExperience() > 10_000_000))
            throw new NotEnoughDataException("Experience is incorrect (RANGE 0-10'000'000");
        if (player.getBirthday() != null && player.getBirthday().getTime() < 0) throw new NotEnoughDataException("Birthday is incorrect");
        if (player.getName() != null && player.getName().isEmpty()) throw new NotEnoughDataException("Name is empty");
    }

    private void checkID(Long id) throws NotEnoughDataException {
        if (id == 0) throw new NotEnoughDataException("ID can't be 0");
        if (playerService.getPlayer(id) == null)
            throw new NoSuchPlayerException("Player with ID=" + id + " isn't found");
    }
}
