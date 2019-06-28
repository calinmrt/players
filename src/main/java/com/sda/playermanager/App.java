package com.sda.playermanager;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sda.playermanager.dao.IPlayerDao;
import com.sda.playermanager.dao.JdbcPlayerDao;
import com.sda.playermanager.domain.Player;

import lombok.ToString;


public class App 
{
	
	private static final Logger log = LogManager.getLogger(App.class);
	
    public static void main( String[] args )
    {
    	if (log.isDebugEnabled()) {
    		log.debug("main(): a intrat in metoda:");
    	}
    	        
        IPlayerDao playerDao = new JdbcPlayerDao();
        playerDao.getAllPlayers();
       
        Player player = playerDao.getPlayer(2);
        
        log.info("Player 2: " + player);
        
        player = playerDao.getPlayer(10);
 
        log.info("Player 10: " + player);
        
        String nameToSearch="art";
        List<Player> list=playerDao.getPlayersByName(nameToSearch);
        for (Player p : list) {
			log.info(p);
		}
        
        playerDao.deletePlayer(3);
        
        

        if (log.isDebugEnabled()) {
        	log.debug("main(): a iesit !!!:");
        }
    }
 
}
