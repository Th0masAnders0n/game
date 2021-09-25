package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    @PersistenceContext
    protected EntityManager entityManager;


    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(Object o) throws IOException {
        entityManager.persist(o);
    }

    @Override
    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order, int pageSize, int pageNumber) {
        if (name == null) name = "";
        if (title == null) title = "";

        Session session = entityManager.unwrap(Session.class);

        String hql = "from Player where name like :name and title like :title";
        if (race != null) hql += " and race='" + race + "'";
        if (profession != null) hql += " and profession='" + profession + "'";
        if (after != null) hql += " and birthday>=:after";
        if (before != null) hql += " and birthday<=:before";
        if (banned != null) hql += " and banned=" + banned + "";
        if (minExperience != null) hql += " and experience>=" + minExperience;
        if (maxExperience != null) hql += " and  experience<=" + maxExperience;
        if (minLevel != null) hql += " and  level>=" + minLevel;
        if (maxLevel != null) hql += " and  level<=" + maxLevel;
        hql += " order by " + order.getFieldName();

        Query<Player> query = session.createQuery(hql, Player.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("title", "%" + title + "%");
        if (after != null) query.setParameter("after", new Date(after), TemporalType.DATE);
        if (before != null) query.setParameter("before", new Date(before), TemporalType.DATE);

        query.setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int getPlayersCount(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {

        if (name == null) name = "";
        if (title == null) title = "";

        Session session = entityManager.unwrap(Session.class);

        String hql = "from Player where name like :name and title like :title";
        if (race != null) hql += " and race='" + race + "'";
        if (profession != null) hql += " and profession='" + profession + "'";
        if (after != null) hql += " and birthday>=:after";
        if (before != null) hql += " and birthday<=:before";
        if (banned != null) hql += " and banned=" + banned + "";
        if (minExperience != null) hql += " and experience>=" + minExperience;
        if (maxExperience != null) hql += " and  experience<=" + maxExperience;
        if (minLevel != null) hql += " and  level>=" + minLevel;
        if (maxLevel != null) hql += " and  level<=" + maxLevel;

        Query<Player> query = session.createQuery(hql, Player.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("title", "%" + title + "%");
        if (after != null) query.setParameter("after", new Date(after), TemporalType.DATE);
        if (before != null) query.setParameter("before", new Date(before), TemporalType.DATE);

        return query.getResultList().size();
    }

    @Override
    public Player addOrUpdatePlayer(Player player) {
        Session session = entityManager.unwrap(Session.class);
        if (player.getId() == 0) {
            session.save(player);
            return player;
        } else {
            Player playerToUpdate = session.get(Player.class, player.getId());
            if (player.getName() != null) playerToUpdate.setName(player.getName());
            if (player.getTitle() != null) playerToUpdate.setTitle(player.getTitle());
            if (player.getRace() != null) playerToUpdate.setRace(player.getRace());
            if (player.getProfession() != null) playerToUpdate.setProfession(player.getProfession());
            if (player.getBirthday() != null) playerToUpdate.setBirthday(player.getBirthday());
            if (player.getExperience() != null) {
                playerToUpdate.setExperience(player.getExperience());
                playerToUpdate.calcLevelAndUntilNextLevel();
            }
            if (player.getBanned() != null) playerToUpdate.setBanned(player.getBanned());
            session.saveOrUpdate(playerToUpdate);
            return playerToUpdate;
        }
    }

    @Override
    public Player getPlayer(long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Player.class, id);
    }

    @Override
    public void deletePlayer(long id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Player> query = session.createQuery("delete from Player where id=:playerId");
        query.setParameter("playerId", id);
        query.executeUpdate();
    }
}
