package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    public PlayerDAO playerDAO;

    @Override
    @Transactional
    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order, int pageSize, int pageNumber) {
        return playerDAO.getAllPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order, pageSize, pageNumber);
    }

    @Override
    @Transactional
    public int getPlayersCount(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        return playerDAO.getPlayersCount(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @Override
    @Transactional
    public Player addOrUpdatePlayer(Player player) {
        return playerDAO.addOrUpdatePlayer(player);
    }

    @Override
    @Transactional
    public Player getPlayer(long id) {
        return playerDAO.getPlayer(id);
    }

    @Override
    @Transactional
    public void deletePlayer(long id) {
        playerDAO.deletePlayer(id);
    }
}
