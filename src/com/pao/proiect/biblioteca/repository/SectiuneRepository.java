package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Sectiune;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectiuneRepository implements Repository<Sectiune, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Sectiune entity) {
        String sql = "INSERT INTO sectiuni (id, nume) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea sectiunii", e);
        }
    }

    @Override
    public Optional<Sectiune> findById(String id) {
        String sql = "SELECT id, nume FROM sectiuni WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea sectiunii", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Sectiune> findAll() {
        String sql = "SELECT id, nume FROM sectiuni";
        List<Sectiune> sectiuni = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                sectiuni.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea sectiunilor", e);
        }
        return sectiuni;
    }

    @Override
    public void update(Sectiune entity) {
        String sql = "UPDATE sectiuni SET nume = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea sectiunii", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM sectiuni WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea sectiunii", e);
        }
    }

    private Sectiune mapRow(ResultSet resultSet) throws SQLException {
        return new Sectiune(resultSet.getString("id"), resultSet.getString("nume"));
    }
}
