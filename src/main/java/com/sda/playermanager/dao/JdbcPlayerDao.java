package com.sda.playermanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sda.playermanager.domain.Player;

public class JdbcPlayerDao implements IPlayerDao {

	private static final Logger log = LogManager.getLogger(JdbcPlayerDao.class);

	private static final String dbUrl = "jdbc:mysql://localhost:3306/players?&serverTimezone=EST5EDT";
	private static final String dbUser = "root"; // "appuser";
	private static final String dbPass = "root"; // "n3wP4$$";

	private Connection con = null;

	private Connection getConnection() {
		if (log.isDebugEnabled()) {
			log.debug("getConnection(): IN");
		}

		if (null == con) {
			try {
				con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			} catch (SQLException e) {
				log.error("Exception while getting connection: " + e.getMessage(), e);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("getConnection(): OUT:" + con);
		}
		return con;
	}

	public void tearDown() {
		if (null != con) {
			try {
				con.close();
				log.debug("connection closed!!!" + con);
			} catch (SQLException e) {
				log.error("Exception while closing the connection: " + e.getMessage(), e);
			}
		}
	}

	public List<Player> getAllPlayers() {
		if (log.isDebugEnabled()) {
			log.debug("getAllPlayers(): IN:");
		}

		List<Player> result = new ArrayList<>();

		Connection c = getConnection();
		try {
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery("select * from players.players");
			while (rs.next()) {
				Player player = getPlayerFromRS(rs);
				result.add(player);

				log.info("Player: " + player);
			}
		} catch (SQLException e) {
			log.error("Exception while parsing players: " + e.getMessage(), e);
		}

		if (log.isDebugEnabled()) {
			log.debug("getAllPlayers(): OUT:");
		}
		return result;
	}

	public Player getPlayer(int id) {
		if (log.isDebugEnabled()) {
			log.debug("getPlayer(): IN:");
		}

		Player result = null;

		Connection c = getConnection();
		try {
			PreparedStatement ps = c.prepareStatement("select * from players.players where id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = getPlayerFromRS(rs);

				log.info("Player: " + result);
			}
		} catch (SQLException e) {
			log.error("Exception while parsing players: " + e.getMessage(), e);
		}

		if (log.isDebugEnabled()) {
			log.debug("getPlayer(): OUT:");
		}

		return result;
	}

	public List<Player> getPlayersByName(String likeName) {

		if (log.isDebugEnabled()) {
			log.debug("getPlayersByName(): IN:");
		}

		List<Player> result = new ArrayList<>();

		Connection c = getConnection();
		try {
			PreparedStatement ps = c.prepareStatement("select * from players.players where first_name = ?");
			ps.setString(1, likeName);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Player player = getPlayerFromRS(rs);
				result.add(player);

				log.info("Player: " + player);
			}

		} catch (SQLException e) {
			log.error("Exception while parsing players: " + e.getMessage(), e);
		}

		if (log.isDebugEnabled()) {
			log.debug("getPlayersByName(): OUT:");
		}

		return result;

	}

	public void deletePlayer(int id) {
		if (log.isDebugEnabled()) {
			log.debug("deletePlayer(): IN:");
		}

		//Player result = null;

		Connection c = getConnection();
		try {
			PreparedStatement ps = c.prepareStatement("delete from players.players where id = ?");
			ps.setInt(1, id);
			int row=ps.executeUpdate();
			log.info("deleted "+row+" rows");
			
		} catch (SQLException e) {
			log.error("Exception while deleting player: " + e.getMessage(), e);
		}

		if (log.isDebugEnabled()) {
			log.debug("deletePlayer(): OUT:");
		}
	}

	private Player getPlayerFromRS(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String accountName = rs.getString("account_name");
		String firstName = rs.getString("first_name");
		String lastName = rs.getString("last_name");
		Timestamp birthDate = rs.getTimestamp("birth_date");

		return new Player(id, accountName, firstName, lastName,
				null == birthDate ? null : new Date(birthDate.getTime()));

	}

}
