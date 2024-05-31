package ro.kmagic.mcgolevels.data.mysql;

import ro.kmagic.mcgolevels.MCGOLevels;
import ro.kmagic.mcgolevels.data.objects.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MySQL {

    private final ConnectionPoolManager connectionPoolManager;

    public MySQL() {
        connectionPoolManager = new ConnectionPoolManager();
        setupTables();
    }

    private void setupTables() {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + MCGOLevels.getInstance().getConfig().getString("mysql.table") + "(name VARCHAR(32) NOT NULL, uuid VARCHAR(36) NOT NULL, exp INT, level INT, PRIMARY KEY(name))");
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + MCGOLevels.getInstance().getConfig().getString("mysql.table-claimed") + "(name VARCHAR(32), level INT)");
            statement.executeUpdate();
            statement.close();

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public void addPlayer(PlayerData player) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + MCGOLevels.getInstance().getConfig().getString("mysql.table") + "(name, uuid, exp, level) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE exp = ? , level = ?");

            statement.setString(1, player.getName());
            statement.setString(2, player.getUuid().toString());
            statement.setLong(3, player.getExp());
            statement.setLong(4, player.getLevel());

            statement.setLong(5, player.getExp());
            statement.setLong(6, player.getLevel());

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public PlayerData getPlayer(String player) {
        Connection connection = null;

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + MCGOLevels.getInstance().getConfig().getString("mysql.table") + " WHERE name=?");

            statement.setString(1, player);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String uuid = result.getString("uuid");
                int exp = result.getInt("exp");
                int level = result.getInt("level");

                return new PlayerData(player, UUID.fromString(uuid), exp, level);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }

        return null;
    }

    public PlayerData getPlayerByUUID(UUID player) {
        Connection connection = null;

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + MCGOLevels.getInstance().getConfig().getString("mysql.table") + " WHERE uuid=?");

            statement.setString(1, player.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                int exp = result.getInt("exp");
                int level = result.getInt("level");

                return new PlayerData(name, player, exp, level);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }

        return null;
    }

    public List<Integer> getClaimed(String player) {
        Connection connection = null;
        List<Integer> list = new LinkedList<>();

        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + MCGOLevels.getInstance().getConfig().getString("mysql.table-claimed") + " WHERE name=?");

            statement.setString(1, player);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int level = result.getInt("level");

                list.add(level);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }

        return list;
    }

    public void addClaimed(PlayerData player, int level) {
        Connection connection = null;
        try {
            connection = getConnectionPoolManager().getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + MCGOLevels.getInstance().getConfig().getString("mysql.table-claimed") + "(name, level) VALUES(?, ?)");

            statement.setString(1, player.getName());
            statement.setInt(2, level);

            statement.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (getConnectionPoolManager() != null) getConnectionPoolManager().close(connection);
        }
    }

    public ConnectionPoolManager getConnectionPoolManager() {
        return connectionPoolManager;
    }

}
